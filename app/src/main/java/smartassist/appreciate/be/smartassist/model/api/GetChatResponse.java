package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke De Clippel on 16/09/2016.
 */
public class GetChatResponse
{
    @SerializedName("conversations")
    private List<ChatConversation> conversations;
    @SerializedName("error")
    private String error;

    public List<ChatConversation> getConversations()
    {
        return conversations;
    }

    public String getError()
    {
        return error;
    }
}
