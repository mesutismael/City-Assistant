package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.ChatSenderTable;

/**
 * Created by Inneke De Clippel on 8/09/2016.
 */
public class ChatSender
{
    private int conversationId;
    private int contactId;
    private String name;
    private String photo;
    private long joinDate;
    private long leaveDate;

    public int getConversationId()
    {
        return conversationId;
    }

    public int getContactId()
    {
        return contactId;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoto()
    {
        return photo;
    }

    public boolean isActive()
    {
        return this.joinDate > this.leaveDate;
    }

    public static List<ChatSender> constructListFromCursor(Cursor cursor)
    {
        List<ChatSender> senders = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                senders.add(ChatSender.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return senders;
    }

    public static ChatSender constructFromCursor(Cursor cursor)
    {
        ChatSender sender = new ChatSender();

        sender.conversationId = cursor.getInt(cursor.getColumnIndex(ChatSenderTable.COLUMN_CONVERSATION_ID_FULL));
        sender.contactId = cursor.getInt(cursor.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_ID_FULL));
        sender.name = cursor.getString(cursor.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_NAME_FULL));
        sender.photo = cursor.getString(cursor.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_PHOTO_FULL));
        sender.joinDate = cursor.getLong(cursor.getColumnIndex(ChatSenderTable.COLUMN_JOIN_DATE_FULL));
        sender.leaveDate = cursor.getLong(cursor.getColumnIndex(ChatSenderTable.COLUMN_LEAVE_DATE_FULL));

        return sender;
    }
}
