package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by banada ismael on 10/31/2016.
 */

public class ActivityTypeItems {
    @SerializedName("items")
    private List<ActivitytypeItem> activityTypesItems;
    private List<RemovedDataItem> removed;

    public List<ActivitytypeItem> getActivityTypesItems() {return activityTypesItems;}

    public List<RemovedDataItem> getRemoved() {return removed;}



}
