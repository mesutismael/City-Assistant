package smartassist.appreciate.be.smartassist.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import retrofit.RetrofitError;
import smartassist.appreciate.be.smartassist.api.ApiHelper;
import smartassist.appreciate.be.smartassist.contentproviders.ChatMessageContentProvider;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.model.api.SendMessageRequest;
import smartassist.appreciate.be.smartassist.model.api.SendMessageResponse;
import smartassist.appreciate.be.smartassist.utils.ChatHelper;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 18/04/2016.
 */
public class SendMessageAsyncTask extends AsyncTask<Void, Void, Void>
{
    private WeakReference<Context> context;
    private int senderId;
    private int conversationId;
    private String message;
    private long date;
    private SendMessageRequest request;

    public SendMessageAsyncTask(Context context, int conversationId, String message, long date)
    {
        this.context = new WeakReference<>(context.getApplicationContext());
        this.senderId = ContactHelper.getOwnContactId(context);
        this.conversationId = conversationId;
        this.message = message;
        this.date = date;
        this.request = new SendMessageRequest(conversationId, message, date);
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        this.insertMessage();
        boolean success = this.postMessage(true);
        this.updateMessage(success);

        return null;
    }

    private void insertMessage()
    {
        ContentValues cv = new ContentValues();
        cv.put(ChatMessageTable.COLUMN_MESSAGE_ID, 0);
        cv.put(ChatMessageTable.COLUMN_CONTACT_ID, this.senderId);
        cv.put(ChatMessageTable.COLUMN_CONVERSATION_ID, this.conversationId);
        cv.put(ChatMessageTable.COLUMN_MESSAGE, this.message);
        cv.put(ChatMessageTable.COLUMN_SENT_AT, this.date);
        cv.put(ChatMessageTable.COLUMN_READ, 1);
        cv.put(ChatMessageTable.COLUMN_STATE, ChatHelper.STATE_SENDING);

        String selection = ChatMessageTable.COLUMN_CONVERSATION_ID + " = " + this.conversationId
                + " AND " + ChatMessageTable.COLUMN_SENT_AT + " = " + this.date;

        if(this.context != null && this.context.get() != null)
        {
            int updatedRows = this.context.get().getContentResolver().update(ChatMessageContentProvider.CONTENT_URI, cv, selection, null);
            if(updatedRows == 0)
            {
                this.context.get().getContentResolver().insert(ChatMessageContentProvider.CONTENT_URI, cv);
            }
        }
    }

    private void updateMessage(boolean success)
    {
        ContentValues cv = new ContentValues();
        cv.put(ChatMessageTable.COLUMN_STATE, success ? ChatHelper.STATE_SENT : ChatHelper.STATE_ERROR);

        String selection = ChatMessageTable.COLUMN_CONVERSATION_ID + " = " + this.conversationId
                + " AND " + ChatMessageTable.COLUMN_SENT_AT + " = " + this.date;

        if(this.context != null && this.context.get() != null)
        {
            this.context.get().getContentResolver().update(ChatMessageContentProvider.CONTENT_URI, cv, selection, null);
        }
    }

    private boolean postMessage(boolean retry)
    {
        try
        {
            SendMessageResponse response = ApiHelper.getService().sendMessage(Constants.APP_SECRET, String.valueOf(this.getFlatId()), this.getAccessToken(), this.request);

            if(response != null && response.getError() == null)
            {
                return true;
            }
            else if(retry)
            {
                boolean tokenSuccess = this.refreshToken();
                return tokenSuccess ? this.postMessage(false) : false;
            }
            else
            {
                return false;
            }
        }
        catch (RetrofitError e)
        {
            return false;
        }
    }

    private boolean refreshToken()
    {
        try
        {
            ApiAccessToken apiAccessToken = ApiHelper.getService().getAccessToken(new FlatId(this.getFlatId(), this.getHash()), Constants.APP_SECRET);

            if (apiAccessToken != null && apiAccessToken.getAccessToken() != null && this.context != null && this.context.get() != null)
            {
                PreferencesHelper.saveAccessToken(this.context.get(), apiAccessToken.getAccessToken());
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (RetrofitError e)
        {
            return false;
        }
    }

    private String getAccessToken()
    {
        Context context = this.context != null ? this.context.get() : null;
        return context != null ? PreferencesHelper.getAccessToken(context) : null;
    }

    private int getFlatId()
    {
        Context context = this.context != null ? this.context.get() : null;
        return context != null ? PreferencesHelper.getFlatId(context) : 0;
    }

    private String getHash()
    {
        Context context = this.context != null ? this.context.get() : null;
        return context != null ? PreferencesHelper.getHash(context) : null;
    }
}
