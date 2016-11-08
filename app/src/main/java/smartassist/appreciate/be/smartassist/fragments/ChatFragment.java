package smartassist.appreciate.be.smartassist.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.BaseActivity;
import smartassist.appreciate.be.smartassist.activities.ChatActivity;
import smartassist.appreciate.be.smartassist.adapters.ChatAdapter;
import smartassist.appreciate.be.smartassist.asynctasks.AddContactsAsyncTask;
import smartassist.appreciate.be.smartassist.asynctasks.LeaveConversationAsyncTask;
import smartassist.appreciate.be.smartassist.asynctasks.SendMessageAsyncTask;
import smartassist.appreciate.be.smartassist.asynctasks.UpdateChatAsyncTask;
import smartassist.appreciate.be.smartassist.contentproviders.ChatMessageContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatSenderContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.EmergencyContentProvider;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.database.ChatSenderTable;
import smartassist.appreciate.be.smartassist.database.EmergencyTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.ChatMessage;
import smartassist.appreciate.be.smartassist.model.ChatSender;
import smartassist.appreciate.be.smartassist.model.EmergencyCategory;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;

/**
 * Created by Inneke De Clippel on 13/04/2016.
 */
public class ChatFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>,
        ChatAdapter.OnChatClickListener, PopupMenu.OnMenuItemClickListener, LeaveConversationAsyncTask.LeaveConversationAsyncTaskListener,
        AddContactsAsyncTask.AddContactsAsyncTaskListener
{
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private ChatAdapter chatAdapter;
    private int conversationId;
    private int ownId;
    private MaterialDialog dialogProgress;
    private List<EmergencyContact> contacts;
    private List<EmergencyCategory> contactCategories;

    public static final String KEY_CONVERSATION_ID = "conversation_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        this.recyclerViewChat = (RecyclerView) view.findViewById(R.id.recyclerView_chat);
        ImageView imageViewSend = (ImageView) view.findViewById(R.id.imageView_send);
        ImageView imageViewOptions = (ImageView) view.findViewById(R.id.imageView_options);
        this.editTextMessage = (EditText) view.findViewById(R.id.editText_message);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setStackFromEnd(true);
        this.recyclerViewChat.setLayoutManager(layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        this.recyclerViewChat.addItemDecoration(decoration);
        this.chatAdapter = new ChatAdapter(view.getContext());
        this.chatAdapter.setListener(this);
        this.recyclerViewChat.setAdapter(this.chatAdapter);

        imageViewSend.setOnClickListener(this);
        imageViewOptions.setOnClickListener(this);

        this.conversationId = this.getArguments() != null ? this.getArguments().getInt(KEY_CONVERSATION_ID) : 0;
        this.ownId = ContactHelper.getOwnContactId(view.getContext());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().initLoader(Constants.LOADER_CHAT, null, this);
        this.getLoaderManager().initLoader(Constants.LOADER_CHAT_SENDERS, null, this);
        this.getLoaderManager().initLoader(Constants.LOADER_EMERGENCY, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_CHAT:
                String selection = ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_CONVERSATION_ID + " = " + this.conversationId;
                String sortOrder = ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_SENT_AT + " ASC";
                return new CursorLoader(this.getView().getContext(), ChatMessageContentProvider.CONTENT_URI, null, selection, null, sortOrder);

            case Constants.LOADER_CHAT_SENDERS:
                String selectionSenders = ChatSenderTable.TABLE_NAME + "." + ChatSenderTable.COLUMN_CONVERSATION_ID + " = " + this.conversationId
                        + " AND " + ChatSenderTable.TABLE_NAME + "." + ChatSenderTable.COLUMN_JOIN_DATE + " > " + ChatSenderTable.TABLE_NAME + "." + ChatSenderTable.COLUMN_LEAVE_DATE;
                String sortOrderSenders = ChatSenderTable.TABLE_NAME + "." + ChatSenderTable.COLUMN_CONTACT_NAME;
                return new CursorLoader(this.getView().getContext(), ChatSenderContentProvider.CONTENT_URI, null, selectionSenders, null, sortOrderSenders);

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
            case Constants.LOADER_CHAT:
                List<ChatMessage> messages = ChatMessage.constructListFromCursor(data);
                int countBefore = this.chatAdapter.getItemCount();
                this.chatAdapter.setMessages(messages);
                int countAfter = this.chatAdapter.getItemCount();
                if(countAfter > countBefore)
                {
                    this.recyclerViewChat.smoothScrollToPosition(countAfter - 1);
                }
                this.updateRead(messages);
                break;

            case Constants.LOADER_CHAT_SENDERS:
                if(this.getActivity() != null && this.getActivity() instanceof ChatActivity)
                {
                    List<ChatSender> senders = ChatSender.constructListFromCursor(data);
                    StringBuilder sb = new StringBuilder();

                    for(ChatSender sender : senders)
                    {
                        if(sender.getContactId() != this.ownId)
                        {
                            if(sb.length() > 0)
                            {
                                sb.append(", ");
                            }
                            sb.append(sender.getName());
                        }
                    }

                    ((ChatActivity) this.getActivity()).setTopBarTitle(sb.toString());
                }
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageView_send:
                String message = this.editTextMessage.getText().toString();

                if(!TextUtils.isEmpty(message))
                {
                    SendMessageAsyncTask task = new SendMessageAsyncTask(v.getContext().getApplicationContext(), this.conversationId, message, System.currentTimeMillis());
                    task.execute();

                    this.editTextMessage.setText(null);

                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                break;

            case R.id.imageView_options:
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.menu_chat);
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                this.showContactPicker();
                return true;

            case R.id.action_leave:
                if(this.getContext() != null)
                {
                    this.dialogProgress = new MaterialDialog.Builder(this.getContext())
                            .content(R.string.chat_leave_progress)
                            .progress(true, 0)
                            .accentColorRes(R.color.dialog_text)
                            .cancelable(false)
                            .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                            .show();

                    LeaveConversationAsyncTask task = new LeaveConversationAsyncTask(this.getContext(), this.conversationId);
                    task.setListener(this);
                    task.execute();
                }
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onChatClick(View caller, ChatMessage message)
    {
        SendMessageAsyncTask task = new SendMessageAsyncTask(caller.getContext().getApplicationContext(), this.conversationId, message.getMessage(), message.getSentAt());
        task.execute();
    }

    private void updateRead(List<ChatMessage> messages)
    {
        boolean unreadMessages = false;

        for(ChatMessage message : messages)
        {
            if(!message.isRead() && message.getContactId() != this.ownId)
            {
                unreadMessages = true;
                break;
            }
        }

        if(unreadMessages && this.getActivity() != null)
        {
            UpdateChatAsyncTask task = new UpdateChatAsyncTask(this.getActivity(), this.conversationId);
            task.execute();
        }
    }

    @Override
    public void onConversationLeft()
    {
        if(this.dialogProgress != null)
        {
            this.dialogProgress.dismiss();
        }

        if(this.getActivity() != null && this.getActivity() instanceof BaseActivity)
        {
            ((BaseActivity) this.getActivity()).startParentActivity();
        }
    }

    @Override
    public void onLeaveError()
    {
        if(this.dialogProgress != null)
        {
            this.dialogProgress.dismiss();
        }

        if(this.getContext() != null)
        {
            new MaterialDialog.Builder(this.getContext())
                    .title(R.string.chat_leave_error_title)
                    .content(R.string.chat_leave_error_message)
                    .positiveText(R.string.dialog_positive)
                    .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                    .show();
        }
    }

    @Override
    public void onContactsAdded()
    {
        if(this.dialogProgress != null)
        {
            this.dialogProgress.dismiss();
        }
    }

    @Override
    public void onContactsError()
    {
        if(this.dialogProgress != null)
        {
            this.dialogProgress.dismiss();
        }

        if(this.getContext() != null)
        {
            new MaterialDialog.Builder(this.getContext())
                    .title(R.string.chat_add_error_title)
                    .content(R.string.chat_add_error_message)
                    .positiveText(R.string.dialog_positive)
                    .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                    .show();
        }
    }

    private void showContactPicker()
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

        if(this.getContext() != null)
        {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getContext())
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
                                if (ChatFragment.this.getContext() != null)
                                {
                                    ChatFragment.this.dialogProgress = new MaterialDialog.Builder(ChatFragment.this.getContext())
                                            .content(R.string.chat_add_progress)
                                            .progress(true, 0)
                                            .accentColorRes(R.color.dialog_text)
                                            .cancelable(false)
                                            .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                                            .show();

                                    List<EmergencyContact> selectedContacts = ContactHelper.createListOfSelectedContacts(which, ChatFragment.this.contacts, ChatFragment.this.contactCategories);
                                    AddContactsAsyncTask task = new AddContactsAsyncTask(ChatFragment.this.conversationId, selectedContacts, ChatFragment.this.getContext());
                                    task.setListener(ChatFragment.this);
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
    }
}
