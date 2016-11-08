package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by i24sm on 11/10/2016.
 */

public class CategoryItem {

    @SerializedName("items")
    private List<PoiCategory> poiCategoryList;
    @SerializedName("removed")
    private List<RemovedDataItem> removed;

    public List<PoiCategory> getPoiCategoryList() {
        return poiCategoryList;
    }

    public List<RemovedDataItem> getRemoved() {
        return removed;
    }
}
