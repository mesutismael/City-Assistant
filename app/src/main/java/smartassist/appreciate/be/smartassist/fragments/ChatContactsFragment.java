package smartassist.appreciate.be.smartassist.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.ChatActivity;
import smartassist.appreciate.be.smartassist.adapters.ChatContactAdapter;
import smartassist.appreciate.be.smartassist.asynctasks.CreateConversationAsyncTask;
import smartassist.appreciate.be.smartassist.contentproviders.ChatConversationContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatSenderContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.EmergencyContentProvider;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.database.ChatSenderTable;
import smartassist.appreciate.be.smartassist.database.EmergencyTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.ChatConversation;
import smartassist.appreciate.be.smartassist.model.ChatSender;
import smartassist.appreciate.be.smartassist.model.EmergencyCategory;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;

/**
 * Created by Inneke De Clippel on 18/04/2016.
 */
public class ChatContactsFragment extends Fragment implements ChatContactAdapter.OnContactClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener, CreateConversationAsyncTask.CreateConversationAsyncTaskListener
{
    private ChatContactAdapter contactAdapter;
    private GridLayoutManager layoutManager;
    private List<ChatConversation> conversations;
    private List<ChatSender> senders;
    private List<EmergencyContact> contacts;
    private List<EmergencyCategory> contactCategories;
    private MaterialDialog dialogProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat_contacts, container, false);

        RecyclerView recyclerViewContacts = (RecyclerView) view.findViewById(R.id.recyclerView_chatContacts);
        FloatingActionButton buttonNewMessage = (FloatingActionButton) view.findViewById(R.id.button_newMessage);

        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewContacts.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        recyclerViewContacts.addItemDecoration(decoration);
        this.contactAdapter = new ChatContactAdapter();
        this.contactAdapter.setListener(this);
        recyclerViewContacts.setAdapter(this.contactAdapter);

        buttonNewMessage.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_CHAT_CONVERSATIONS, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_CHAT_SENDERS, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY, null, this);
    }

    @Override
    public void onContactClick(View caller, ChatConversation conversation)
    {
        this.openConversation(conversation.getConversationId());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_newMessage:
                this.showContactPicker(v.getContext());
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_CHAT_CONVERSATIONS:
                String sortOrder = ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_SENT_AT + " DESC";
                return new CursorLoader(this.getView().getContext(), ChatConversationContentProvider.CONTENT_URI, null, null, null, sortOrder);

            case Constants.LOADER_CHAT_SENDERS:
                String sortOrderSenders = ChatSenderTable.TABLE_NAME + "." + ChatSenderTable.COLUMN_CONTACT_NAME;
                return new CursorLoader(this.getView().getContext(), ChatSenderContentProvider.CONTENT_URI, null, null, null, sortOrderSenders);

            case Constants.LOADER_EMERGENCY:
                String selectionContacts = EmergencyTable.TABLE_NAME + "." + EmergencyTable.COLUMN_CAN_CHAT + " = 1";
                String sortOrderContacts = EmergencyTable.TABLE_NAME + "." + EmergencyTable.COLUMN_NAME;
                return new CursorLoader(this.getView().getContext(), EmergencyContentProvider.CONTENT_URI, null, selectionContacts, null, sortOrderContacts);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_CHAT_CONVERSATIONS:
                this.conversations = ChatConversation.constructListFromCursor(data);
                this.mergeConversationsWithSenders();
                break;

            case Constants.LOADER_CHAT_SENDERS:
                this.senders = ChatSender.constructListFromCursor(data);
                this.mergeConversationsWithSenders();
                break;

            case Constants.LOADER_EMERGENCY:
                this.contacts = EmergencyContact.constructListFromCursor(data);
                this.contactCategories = new ArrayList<>();

                for(EmergencyContact contact : this.contacts)
                {
                    if(!this.contactCategories.contains(contact.getCategory()))
                    {
                        this.contactCategories.add(contact.getCategory());
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        this.adjustLayoutManager(newConfig.orientation);
        this.contactAdapter.notifyDataSetChanged();
    }

    private void adjustLayoutManager(int orientation)
    {
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            this.layoutManager.setSpanCount(3);
            this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return (position % 10 == 4 || position % 10 == 8) ? 2 : 1;
                }
            });
        }
        else
        {
            this.layoutManager.setSpanCount(2);
            this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return (position % 3 == 2) ? 2 : 1;
                }
            });
        }
    }

    private void showContactPicker(Context context)
    {
        List<String> contactNames = new ArrayList<>();

        if(this.contactCategories != null)
        {
            for(EmergencyCategory category : this.contactCategories)
            {
                contactNames.add(category.getName());
            }
        }
        if(this.contacts != null)
        {
            for(EmergencyContact contact : this.contacts)
            {
                contactNames.add(contact.getName());
            }
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(R.string.chat_contact_picker_title)
                .positiveText(R.string.chat_contact_picker_positive)
                .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf");

        if(contactNames.size() > 0)
        {
            builder.negativeText(R.string.chat_contact_picker_cancel).items(contactNames.toArray(new String[contactNames.size()]))
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMulti()
                    {
                        @Override
                        public void onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text)
                        {
                            if (ChatContactsFragment.this.getContext() != null)
                            {
                                ChatContactsFragment.this.showProgressDialog(ChatContactsFragment.this.getContext());
                                List<EmergencyContact> selectedContacts = ContactHelper.createListOfSelectedContacts(which, ChatContactsFragment.this.contacts, ChatContactsFragment.this.contactCategories);
                                CreateConversationAsyncTask task = new CreateConversationAsyncTask(selectedContacts, ChatContactsFragment.this.getContext());
                                task.setListener(ChatContactsFragment.this);
                                task.execute();
                            }
                        }
                    });
        }
        else
        {
            builder.content(R.string.chat_contact_picker_empty);
        }

        builder.show();
    }

    @Override
    public void onConversationCreated(int conversationId)
    {
        if(this.dialogProgress != null)
        {
            this.dialogProgress.dismiss();
        }

        this.openConversation(conversationId);
    }

    @Override
    public void onError()
    {
        if(this.dialogProgress != null)
        {
            this.dialogProgress.dismiss();
        }

        if(this.getContext() != null)
        {
            this.showErrorDialog(this.getContext());
        }
    }

    private void openConversation(int conversationId)
    {
        if(this.getActivity() != null)
        {
            Intent intent = new Intent(this.getActivity(), ChatActivity.class);
            intent.putExtra(ChatActivity.KEY_CONVERSATION_ID, conversationId);
            this.startActivity(intent);
        }
    }

    private void showProgressDialog(Context context)
    {
        this.dialogProgress = new MaterialDialog.Builder(context)
                .content(R.string.chat_new_conversation_progress)
                .progress(true, 0)
                .accentColorRes(R.color.dialog_text)
                .cancelable(false)
                .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                .show();
    }

    private void showErrorDialog(Context context)
    {
        new MaterialDialog.Builder(context)
                .title(R.string.chat_new_conversation_error_title)
                .content(R.string.chat_new_conversation_error_message)
                .positiveText(R.string.dialog_positive)
                .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                .show();
    }

    private void mergeConversationsWithSenders()
    {
        if(this.conversations != null && this.senders != null)
        {
            for(ChatConversation conversation : this.conversations)
            {
                conversation.clearSenders();
            }

            for(ChatSender sender : this.senders)
            {
                if(sender.isActive())
                {
                    int conversationId = sender.getConversationId();

                    for (ChatConversation conversation : this.conversations)
                    {
                        if (conversation.getConversationId() == conversationId)
                        {
                            conversation.addSender(sender);
                            break;
                        }
                    }
                }
            }

            this.contactAdapter.setConversations(this.conversations);
        }
    }
}
