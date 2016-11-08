package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.utils.ChatHelper;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;

/**
 * Created by Inneke De Clippel on 15/09/2016.
 */
public class ChatMessage
{
    @SerializedName("id")
    private int id;
    @SerializedName("sender_id")
    private int contactId;
    @SerializedName("sender_type")
    private String contactType;
    @SerializedName("message")
    private String message;
    @SerializedName("send_at")
    private long sentAt;
    @SerializedName("message_read")
    private int read;
    private int conversationId;

    public int getId()
    {
        return id;
    }

    public int getContactId()
    {
        return ContactHelper.alterId(this.contactId, this.contactType);
    }

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
        return read == 1;
    }

    public int getConversationId()
    {
        return conversationId;
    }

    public void setConversationId(int conversationId)
    {
        this.conversationId = conversationId;
    }

    public ContentValues getContentValues(int conversationId)
    {
        final ContentValues cv = new ContentValues();

        cv.put(ChatMessageTable.COLUMN_MESSAGE_ID, this.id);
        cv.put(ChatMessageTable.COLUMN_CONTACT_ID, this.getContactId());
        cv.put(ChatMessageTable.COLUMN_CONVERSATION_ID, conversationId);
        cv.put(ChatMessageTable.COLUMN_MESSAGE, this.message);
        cv.put(ChatMessageTable.COLUMN_SENT_AT, this.sentAt);
        cv.put(ChatMessageTable.COLUMN_READ, this.read);
        cv.put(ChatMessageTable.COLUMN_STATE, ChatHelper.STATE_SENT);

        return cv;
    }
}
