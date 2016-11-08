package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.database.ChatSenderTable;

/**
 * Created by Inneke De Clippel on 9/09/2016.
 */
public class ChatMessage
{
    private String message;
    private long sentAt;
    private boolean read;
    private int state;
    private int contactId;
    private String contactName;
    private String contactPhoto;

    public String getMessage()
    {
        return message;
    }

    public long getSentAt()
    {
        return sentAt;
    }

    public boolean isRead()
    {
        return read;
    }

    public int getState()
    {
        return state;
    }

    public int getContactId()
    {
        return contactId;
    }

    public String getContactName()
    {
        return contactName;
    }

    public String getContactPhoto()
    {
        return contactPhoto;
    }

    public static List<ChatMessage> constructListFromCursor(Cursor cursor)
    {
        List<ChatMessage> messages = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                messages.add(ChatMessage.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return messages;
    }

    public static ChatMessage constructFromCursor(Cursor cursor)
    {
        ChatMessage message = new ChatMessage();

        message.message = cursor.getString(cursor.getColumnIndex(ChatMessageTable.COLUMN_MESSAGE_FULL));
        message.sentAt = cursor.getLong(cursor.getColumnIndex(ChatMessageTable.COLUMN_SENT_AT_FULL));
        message.read = cursor.getInt(cursor.getColumnIndex(ChatMessageTable.COLUMN_READ_FULL)) == 1;
        message.state = cursor.getInt(cursor.getColumnIndex(ChatMessageTable.COLUMN_STATE_FULL));
        message.contactId = cursor.getInt(cursor.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_ID_FULL));
        message.contactName = cursor.getString(cursor.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_NAME_FULL));
        message.contactPhoto = cursor.getString(cursor.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_PHOTO_FULL));

        return message;
    }
}
