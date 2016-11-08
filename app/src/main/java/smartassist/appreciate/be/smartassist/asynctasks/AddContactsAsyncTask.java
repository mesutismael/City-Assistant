package smartassist.appreciate.be.smartassist.asynctasks;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import smartassist.appreciate.be.smartassist.api.ApiHelper;
import smartassist.appreciate.be.smartassist.contentproviders.ChatSenderContentProvider;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.model.api.AddContactsResponse;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.CreateConversationContact;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 15/09/2016.
 */
public class AddContactsAsyncTask extends AsyncTask<Void, Void, Boolean>
{
    private int conversationId;
    private List<CreateConversationContact> createConversationContacts;
    private WeakReference<Context> context;
    private WeakReference<AddContactsAsyncTaskListener> listener;

    public AddContactsAsyncTask(int conversationId, List<EmergencyContact> selectedContacts, Context context)
    {
        this.conversationId = conversationId;
        this.context = new WeakReference<>(context.getApplicationContext());

        this.createConversationContacts = new ArrayList<>();
        if(selectedContacts != null)
        {
            for(EmergencyContact contact : selectedContacts)
            {
                if(contact != null)
                {
                    this.createConversationContacts.add(new CreateConversationContact(contact));
                }
            }
        }
    }

    public void setListener(AddContactsAsyncTaskListener listener)
    {
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        AddContactsResponse response = this.doApiCall(true);

        if(response != null)
        {
            boolean successfullySaved = this.insertContacts(response);
            return successfullySaved;
        }
        else
        {
            return false;
        }
    }

    private AddContactsResponse doApiCall(boolean retry)
    {
        try
        {
            AddContactsResponse response = ApiHelper.getService().addContacts(Constants.APP_SECRET, String.valueOf(this.getFlatId()), this.getAccessToken(), this.conversationId, this.createConversationContacts);

            if(response != null && response.getError() == null)
            {
                return response;
            }
            else if(retry)
            {
                boolean tokenSuccess = this.refreshToken();
                return tokenSuccess ? this.doApiCall(false) : null;
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

    private boolean insertContacts(AddContactsResponse conversation)
    {
        if(this.context != null && this.context.get() != null)
        {
            ContentResolver contentResolver = this.context.get().getContentResolver();
            contentResolver.bulkInsert(ChatSenderContentProvider.CONTENT_URI, conversation.getSenderContentValues());

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success)
    {
        if (this.listener != null && this.listener.get() != null)
        {
            if (success)
            {
                this.listener.get().onContactsAdded();
            }
            else
            {
                this.listener.get().onContactsError();
            }
        }
    }

    public interface AddContactsAsyncTaskListener
    {
        void onContactsAdded();
        void onContactsError();
    }
}
