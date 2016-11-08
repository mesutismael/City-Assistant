package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by i24sm on 11/10/2016.
 */

public class Activities {
    @SerializedName("data")
    private ActivityItems activityItems;
    @SerializedName("activities_types")
    private ActivityTypes activityTypes;

    public ActivityItems getActivityItems() {return activityItems;}

    public ActivityTypes getActivityTypes() {
        return activityTypes;
    }
}
