package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by i24sm on 11/10/2016.
 */

public class Categories {

    @SerializedName("data")
    private CategoryItem poiItems;

    public CategoryItem getPoiItems() {
        return poiItems;
    }
}
