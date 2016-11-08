package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 10/02/2015.
 */
public class PoiData
{
    @SerializedName("data")
    private PoiItems poiItems;
    @SerializedName("categories")
    private Categories categories;

    public List<PoiItem> getPoiItems()
    {
        if(this.poiItems == null)
            return null;
        return this.poiItems.getPoiItems();
    }

    public List<RemovedDataItem> getRemoved()
    {
        if(this.poiItems == null)
            return null;
        return this.poiItems.getRemoved();
    }

    public Categories getCategories()
    {
        return categories;
    }
}
