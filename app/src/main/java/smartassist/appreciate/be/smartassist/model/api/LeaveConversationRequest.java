package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inneke De Clippel on 15/09/2016.
 */
public class LeaveConversationRequest
{
    @SerializedName("conversation_id")
    private int conversationId;

    public LeaveConversationRequest(int conversationId)
    {
        this.conversationId = conversationId;
    }
}
