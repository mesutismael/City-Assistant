package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by i24sm on 07/10/2016.
 */

public class NearByData {

    @SerializedName("poi")
    private PoiData poiData;

    @SerializedName("activities")
    private Activities activities;

    public PoiData getPoiData() {
        return poiData;
    }

    public Activities getActivity() {
        return activities;
    }
}
