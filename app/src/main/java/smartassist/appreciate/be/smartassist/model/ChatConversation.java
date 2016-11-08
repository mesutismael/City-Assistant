package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.ChatConversationTable;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;

/**
 * Created by Inneke De Clippel on 8/09/2016.
 */
public class ChatConversation
{
    private int conversationId;
    private String lastMessage;
    private long lastMessageSentAt;
    private boolean lastMessageRead;
    private List<ChatSender> senders;

    public int getConversationId()
    {
        return conversationId;
    }

    public String getLastMessage()
    {
        return lastMessage;
    }

    public long getLastMessageSentAt()
    {
        return lastMessageSentAt;
    }

    public boolean isLastMessageRead()
    {
        return lastMessageRead;
    }

    public List<ChatSender> getSenders()
    {
        return senders;
    }

    public String getParticipantNames(int ownContactId)
    {
        StringBuilder sb = new StringBuilder();

        if(this.senders != null)
        {
            for(ChatSender sender : this.senders)
            {
                if(sender.getContactId() != ownContactId)
                {
                    if(sb.length() > 0)
                    {
                        sb.append(", ");
                    }
                    sb.append(sender.getName());
                }
            }
        }

        //TODO show a default value 'You are the only one left' when sb.length() == 0

        return sb.toString();
    }

    public boolean isGroupChat()
    {
        return this.senders != null && this.senders.size() > 2;
    }

    public String getSenderPhoto(int ownContactId)
    {
        if(this.senders != null)
        {
            for(ChatSender sender : this.senders)
            {
                if(sender.getContactId() != ownContactId)
                {
                    return sender.getPhoto();
                }
            }
        }

        return null;
    }

    public void addSender(ChatSender sender)
    {
        if(this.senders == null)
        {
            this.senders = new ArrayList<>();
        }

        this.senders.add(sender);
    }

    public void clearSenders()
    {
        if(this.senders != null)
        {
            this.senders.clear();
        }
    }

    public static List<ChatConversation> constructListFromCursor(Cursor cursor)
    {
        List<ChatConversation> conversations = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                conversations.add(ChatConversation.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return conversations;
    }

    public static ChatConversation constructFromCursor(Cursor cursor)
    {
        ChatConversation conversation = new ChatConversation();

        conversation.conversationId = cursor.getInt(cursor.getColumnIndex(ChatConversationTable.COLUMN_CONVERSATION_ID_FULL));
        conversation.lastMessage = cursor.getString(cursor.getColumnIndex(ChatMessageTable.COLUMN_MESSAGE_FULL));
        conversation.lastMessageSentAt = cursor.getLong(cursor.getColumnIndex(ChatMessageTable.COLUMN_SENT_AT_FULL));
        conversation.lastMessageRead = cursor.getInt(cursor.getColumnIndex(ChatMessageTable.COLUMN_READ_FULL)) == 1;

        return conversation;
    }
}
