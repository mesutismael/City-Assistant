package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inneke on 9/02/2015.
 */
public class ApiAccessToken
{
    @SerializedName("flat_id")
    private int flatId;
    @SerializedName("access_token")
    private String accessToken;
    private String error;

    public String getAccessToken()
    {
        return accessToken;
    }
}
