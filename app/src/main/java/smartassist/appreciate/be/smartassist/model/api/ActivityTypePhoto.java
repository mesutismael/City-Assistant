package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by banada ismael on 11/3/2016.
 */

public class ActivityTypePhoto {
    @SerializedName("id")
    private String photo_id;
    @SerializedName("portrait_img")
    private String portrait_img;
    @SerializedName("landscape_img")
    private String landscape_img;


    public String getPhoto_id() {return photo_id;}

    public String getPortrait_img() {return portrait_img;}

    public String getLandscape_img() {return landscape_img;}
}
