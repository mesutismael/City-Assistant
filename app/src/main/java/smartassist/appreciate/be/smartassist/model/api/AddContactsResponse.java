package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke De Clippel on 15/09/2016.
 */
public class AddContactsResponse
{
    @SerializedName("conversation_id")
    private int conversationId;
    @SerializedName("participants")
    private List<ChatSender> senders;
    @SerializedName("error")
    private String error;

    public String getError()
    {
        return error;
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
