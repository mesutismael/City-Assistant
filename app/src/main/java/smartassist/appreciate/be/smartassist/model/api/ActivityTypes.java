package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by i24sm on 07/10/2016.
 */

public class ActivityTypes {

    @SerializedName("data")
    private ActivityTypeItems ActivityTypeItems;

    public ActivityTypeItems getActivityTypeItems() {return ActivityTypeItems;}
}
