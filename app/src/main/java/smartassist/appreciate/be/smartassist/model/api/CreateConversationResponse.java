package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartassist.appreciate.be.smartassist.database.ChatConversationTable;

/**
 * Created by Inneke De Clippel on 13/09/2016.
 */
public class CreateConversationResponse
{
    @SerializedName("conversation_id")
    private int conversationId;
    @SerializedName("participants")
    private List<ChatSender> senders;

    public int getConversationId()
    {
        return conversationId;
    }

    public List<ChatSender> getSenders()
    {
        return senders;
    }

    public ContentValues getContentValues()
    {
        final ContentValues cv = new ContentValues();

        cv.put(ChatConversationTable.COLUMN_CONVERSATION_ID, this.conversationId);
        cv.put(ChatConversationTable.COLUMN_JOIN_DATE, System.currentTimeMillis());
        cv.put(ChatConversationTable.COLUMN_LEAVE_DATE, 0);

        return cv;
    }

    public ContentValues[] getSenderContentValues()
    {
        ContentValues[] cvArray = new ContentValues[this.senders != null ? this.senders.size() : 0];

        if(this.senders != null)
        {
            for(int i = 0; i < this.senders.size(); i++)
            {
                cvArray[i] = this.senders.get(i).getContentValues(this.conversationId);
            }
        }

        return cvArray;
    }
}
