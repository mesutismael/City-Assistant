package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Inneke on 15/04/2015.
 */
public class Image
{
    @SerializedName("portrait_img")
    private String portraitImage;
    @SerializedName("landscape_img")
    private String landscapeImage;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;

    public String getPortraitImage()
    {
        return portraitImage;
    }

    public String getLandscapeImage()
    {
        return landscapeImage;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public String getDeletedAt()
    {
        return deletedAt;
    }
}
