package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inneke De Clippel on 18/04/2016.
 */
public class SendMessageRequest
{
    @SerializedName("conversation_id")
    private int conversationId;
    @SerializedName("message")
    private String message;
    @SerializedName("timestamp")
    private long timestamp;

    public SendMessageRequest(int conversationId, String message, long timestamp)
    {
        this.conversationId = conversationId;
        this.message = message;
        this.timestamp = timestamp;
    }
}
