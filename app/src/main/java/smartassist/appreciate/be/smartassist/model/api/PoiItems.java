package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 4/03/2015.
 */
public class PoiItems
{
    @SerializedName("items")
    private List<PoiItem> poiItems;
    private List<RemovedDataItem> removed;

    public List<PoiItem> getPoiItems()
    {
        return poiItems;
    }

    public List<RemovedDataItem> getRemoved()
    {
        return removed;
    }



}
