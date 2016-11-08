package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Banada ismael on 10/31/2016.
 */

public class ActivityItems {

    @SerializedName("items")
    private List<ActivityItem> activityItems;
    private List<RemovedDataItem> removed;


    public List<ActivityItem> getActivityItems() {
        return activityItems;
    }

    public List<RemovedDataItem> getRemoved() {
        return removed;
    }
}
