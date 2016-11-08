package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 3/03/2015.
 */
public class PhotoData
{
    @SerializedName("screensaver")
    private List<PhotoItem> screenSavers;
    @SerializedName("photostream")
    private List<PhotoItem> photoStream;

    public List<PhotoItem> getScreenSavers()
    {
        return screenSavers;
    }

    public List<PhotoItem> getPhotoStream()
    {
        return photoStream;
    }
}
