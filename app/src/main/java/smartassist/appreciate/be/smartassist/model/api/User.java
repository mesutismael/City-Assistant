package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inneke De Clippel on 15/02/2016.
 */
public class User
{
    @SerializedName("full_name")
    private String name;

    public String getName()
    {
        return name;
    }
}
