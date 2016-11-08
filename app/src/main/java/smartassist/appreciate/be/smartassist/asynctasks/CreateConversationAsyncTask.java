package smartassist.appreciate.be.smartassist.asynctasks;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.RetrofitError;
import smartassist.appreciate.be.smartassist.api.ApiHelper;
import smartassist.appreciate.be.smartassist.contentproviders.ChatConversationContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatSenderContentProvider;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.CreateConversationRequest;
import smartassist.appreciate.be.smartassist.model.api.CreateConversationResponse;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 13/09/2016.
 */
public class CreateConversationAsyncTask extends AsyncTask<Void, Void, Integer>
{
    private List<EmergencyContact> selectedContacts;
    private WeakReference<Context> context;
    private WeakReference<CreateConversationAsyncTaskListener> listener;

    public CreateConversationAsyncTask(List<EmergencyContact> selectedContacts, Context context)
    {
        this.selectedContacts = selectedContacts;
        this.context = new WeakReference<>(context.getApplicationContext());
    }

    public void setListener(CreateConversationAsyncTaskListener listener)
    {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected Integer doInBackground(Void... params)
    {
        CreateConversationResponse response = this.createConversation(true);

        if(response != null)
        {
            boolean successfullySaved = this.insertConversation(response);

            if(successfullySaved)
            {
                return response.getConversationId();
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }

    private CreateConversationResponse createConversation(boolean retry)
    {
        try
        {
            CreateConversationRequest request = new CreateConversationRequest(this.selectedContacts);
            CreateConversationResponse response = ApiHelper.getService().createConversation(Constants.APP_SECRET, String.valueOf(this.getFlatId()), this.getAccessToken(), request.getContacts());

            if(response != null && response.getConversationId() > 0)
            {
                return response;
            }
            else if(retry)
            {
                boolean tokenSuccess = this.refreshToken();
                return tokenSuccess ? this.createConversation(false) : null;
            }
            else
            {
                return null;
            }
        }
        catch (RetrofitError e)
        {
            return null;
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

    private boolean insertConversation(CreateConversationResponse conversation)
    {
        if(this.context != null && this.context.get() != null)
        {
            ContentResolver contentResolver = this.context.get().getContentResolver();
            contentResolver.insert(ChatConversationContentProvider.CONTENT_URI, conversation.getContentValues());
            contentResolver.bulkInsert(ChatSenderContentProvider.CONTENT_URI, conversation.getSenderContentValues());

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Integer integer)
    {
        super.onPostExecute(integer);

        if (this.listener != null && this.listener.get() != null)
        {
            if (integer > 0)
            {
                this.listener.get().onConversationCreated(integer);
            }
            else
            {
                this.listener.get().onError();
            }
        }
    }

    public interface CreateConversationAsyncTaskListener
    {
        void onConversationCreated(int conversationId);
        void onError();
    }
}
