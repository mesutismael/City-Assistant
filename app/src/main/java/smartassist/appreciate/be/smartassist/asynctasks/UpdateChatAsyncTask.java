package smartassist.appreciate.be.smartassist.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import retrofit.RetrofitError;
import smartassist.appreciate.be.smartassist.api.ApiHelper;
import smartassist.appreciate.be.smartassist.contentproviders.ChatMessageContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.NotificationContentProvider;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.database.NotificationTable;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.model.api.MarkAsReadRequest;
import smartassist.appreciate.be.smartassist.model.api.MarkAsReadResponse;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 20/04/2016.
 */
public class UpdateChatAsyncTask extends AsyncTask<Void, Void, Void>
{
    private int conversationId;
    private WeakReference<Context> context;
    private MarkAsReadRequest request;

    public UpdateChatAsyncTask(Context context, int conversationId)
    {
        this.conversationId = conversationId;
        this.context = new WeakReference<>(context);
        this.request = new MarkAsReadRequest(conversationId);
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        this.updateMessages();
        this.removeNotifications();
        this.postReadStatus(true);

        return null;
    }

    private void updateMessages()
    {
        ContentValues cv = new ContentValues();
        cv.put(ChatMessageTable.COLUMN_READ, 1);

        String selection = ChatMessageTable.COLUMN_CONVERSATION_ID + " = " + this.conversationId;

        if(this.context != null && this.context.get() != null)
        {
            this.context.get().getContentResolver().update(ChatMessageContentProvider.CONTENT_URI, cv, selection, null);
        }
    }

    private void removeNotifications()
    {
        String selection = NotificationTable.COLUMN_EXTRA_ID + " = " + this.conversationId;

        if(this.context != null && this.context.get() != null)
        {
            this.context.get().getContentResolver().delete(NotificationContentProvider.CONTENT_URI, selection, null);
        }
    }

    private void postReadStatus(boolean retry)
    {
        try
        {
            MarkAsReadResponse response = ApiHelper.getService().markAsRead(Constants.APP_SECRET, String.valueOf(this.getFlatId()), this.getAccessToken(), this.request);

            if((response == null || response.getError() == null) && retry)
            {
                boolean tokenSuccess = this.refreshToken();
                if(tokenSuccess)
                {
                    this.postReadStatus(false);
                }
            }
        }
        catch (RetrofitError e)
        {
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
