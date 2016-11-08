package smartassist.appreciate.be.smartassist.asynctasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import smartassist.appreciate.be.smartassist.contentproviders.ChatConversationContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatMessageContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatSenderContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.NotificationContentProvider;
import smartassist.appreciate.be.smartassist.database.ChatConversationTable;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.database.ChatSenderTable;
import smartassist.appreciate.be.smartassist.database.NotificationTable;
import smartassist.appreciate.be.smartassist.model.api.ChatConversation;
import smartassist.appreciate.be.smartassist.model.api.ChatMessage;
import smartassist.appreciate.be.smartassist.model.api.ChatSender;
import smartassist.appreciate.be.smartassist.utils.NotificationHelper;

/**
 * Created by Inneke De Clippel on 15/04/2016.
 */
public class InsertMessagesAsyncTask extends AsyncTask<Void, Void, Void>
{
    private WeakReference<Context> context;
    private List<ChatConversation> conversations;

    public InsertMessagesAsyncTask(Context context, List<ChatConversation> conversations)
    {
        this.context = new WeakReference<>(context);
        this.conversations = conversations;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        ContentResolver contentResolver = this.context.get() != null ? this.context.get().getContentResolver() : null;

        if(this.conversations != null && contentResolver != null)
        {
            for(ChatConversation conversation : this.conversations)
            {
                if(conversation.isDeleted())
                {
                    String selectionConversations = ChatConversationTable.COLUMN_CONVERSATION_ID + " = " + conversation.getConversationId();
                    contentResolver.delete(ChatConversationContentProvider.CONTENT_URI, selectionConversations, null);

                    String selectionMessages = ChatMessageTable.COLUMN_CONVERSATION_ID + " = " + conversation.getConversationId();
                    contentResolver.delete(ChatMessageContentProvider.CONTENT_URI, selectionMessages, null);

                    String selectionSenders = ChatSenderTable.COLUMN_CONVERSATION_ID + " = " + conversation.getConversationId();
                    contentResolver.delete(ChatSenderContentProvider.CONTENT_URI, selectionSenders, null);

                    String selectionNotifications = NotificationTable.COLUMN_TYPE + " = " + NotificationHelper.TYPE_CHAT
                            + " AND " + NotificationTable.COLUMN_EXTRA_ID + " = " + conversation.getConversationId();
                    contentResolver.delete(NotificationContentProvider.CONTENT_URI, selectionNotifications, null);
                }
                else
                {
                    if(conversation.getContacts() != null)
                    {
                        for(ChatSender sender : conversation.getContacts())
                        {
                            ContentValues cv = sender.getContentValues(conversation.getConversationId());
                            String selection = ChatSenderTable.COLUMN_CONTACT_ID + " = " + sender.getContactId()
                                    + " AND " + ChatSenderTable.COLUMN_CONVERSATION_ID + " = " + conversation.getConversationId();

                            int updatedRows = contentResolver.update(ChatSenderContentProvider.CONTENT_URI, cv, selection, null);
                            if(updatedRows == 0)
                            {
                                contentResolver.insert(ChatSenderContentProvider.CONTENT_URI, cv);
                            }
                        }
                    }

                    if(conversation.getMessages() != null)
                    {
                        for(ChatMessage message : conversation.getMessages())
                        {
                            ContentValues cv = message.getContentValues(conversation.getConversationId());
                            String selection = ChatMessageTable.COLUMN_SENT_AT + " = " + message.getSentAt()
                                    + " AND " + ChatMessageTable.COLUMN_CONVERSATION_ID + " = " + conversation.getConversationId();

                            int updatedRows = contentResolver.update(ChatMessageContentProvider.CONTENT_URI, cv, selection, null);
                            if(updatedRows == 0)
                            {
                                contentResolver.insert(ChatMessageContentProvider.CONTENT_URI, cv);
                            }
                        }
                    }

                    ContentValues cv = new ContentValues();
                    cv.put(ChatConversationTable.COLUMN_CONVERSATION_ID, conversation.getConversationId());

                    ChatSender ownContact = this.context.get() != null ? conversation.getOwnContact(this.context.get()) : null;
                    if(ownContact != null)
                    {
                        cv.put(ChatConversationTable.COLUMN_JOIN_DATE, ownContact.getJoinDate());
                        cv.put(ChatConversationTable.COLUMN_LEAVE_DATE, ownContact.getLeaveDate());
                    }

                    String selection = ChatConversationTable.COLUMN_CONVERSATION_ID + " = " + conversation.getConversationId();

                    int updatedRows = contentResolver.update(ChatConversationContentProvider.CONTENT_URI, cv, selection, null);
                    if(updatedRows == 0)
                    {
                        contentResolver.insert(ChatConversationContentProvider.CONTENT_URI, cv);
                    }
                }
            }
        }

        return null;
    }
}
