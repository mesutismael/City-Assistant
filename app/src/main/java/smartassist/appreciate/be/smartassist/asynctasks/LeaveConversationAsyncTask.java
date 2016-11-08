package smartassist.appreciate.be.smartassist.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import retrofit.RetrofitError;
import smartassist.appreciate.be.smartassist.api.ApiHelper;
import smartassist.appreciate.be.smartassist.contentproviders.ChatConversationContentProvider;
import smartassist.appreciate.be.smartassist.database.ChatConversationTable;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.model.api.LeaveConversationRequest;
import smartassist.appreciate.be.smartassist.model.api.LeaveConversationResponse;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 15/09/2016.
 */
public class LeaveConversationAsyncTask extends AsyncTask<Void, Void, Boolean>
{
    private WeakReference<Context> context;
    private int conversationId;
    private WeakReference<LeaveConversationAsyncTaskListener> listener;

    public LeaveConversationAsyncTask(Context context, int conversationId)
    {
        this.context = new WeakReference<>(context.getApplicationContext());
        this.conversationId = conversationId;
    }

    public void setListener(LeaveConversationAsyncTaskListener listener)
    {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        boolean success = this.doApiCall(true);

        if(success)
        {
            this.updateDatabase();
        }

        return success;
    }

    @Override
    protected void onPostExecute(Boolean success)
    {
        if(this.listener != null && this.listener.get() != null)
        {
            if(success)
            {
                this.listener.get().onConversationLeft();
            }
            else
            {
                this.listener.get().onLeaveError();
            }
        }
    }

    private boolean doApiCall(boolean retry)
    {
        try
        {
            LeaveConversationRequest request = new LeaveConversationRequest(this.conversationId);
            LeaveConversationResponse response = ApiHelper.getService().leaveConversation(Constants.APP_SECRET, String.valueOf(this.getFlatId()), this.getAccessToken(), request);

            if(response != null && response.getError() == null)
            {
                return true;
            }
            else if(retry)
            {
                boolean tokenSuccess = this.refreshToken();
                return tokenSuccess ? this.doApiCall(false) : false;
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

    private void updateDatabase()
    {
        ContentValues cv = new ContentValues();
        cv.put(ChatConversationTable.COLUMN_LEAVE_DATE, System.currentTimeMillis());

        String selection = ChatConversationTable.COLUMN_CONVERSATION_ID + " = " + this.conversationId;

        if(this.context.get() != null)
        {
            this.context.get().getContentResolver().update(ChatConversationContentProvider.CONTENT_URI, cv, selection, null);
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

    public interface LeaveConversationAsyncTaskListener
    {
        void onConversationLeft();
        void onLeaveError();
    }
}
