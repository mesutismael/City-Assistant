package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inneke De Clippel on 20/04/2016.
 */
public class MarkAsReadRequest
{
    @SerializedName("conversation_id")
    private int conversationId;

    public MarkAsReadRequest(int conversationId)
    {
        this.conversationId = conversationId;
    }
}
