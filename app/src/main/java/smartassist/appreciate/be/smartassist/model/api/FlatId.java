package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inneke on 9/02/2015.
 */
public class FlatId
{
    @SerializedName("flat_id")
    private int flatId;
    private String hash;

    public FlatId(int flatId, String hash)
    {
        this.flatId = flatId;
        this.hash = hash;
    }

    public int getFlatId()
    {
        return flatId;
    }

    public String getHash()
    {
        return hash;
    }
}
