package smartassist.appreciate.be.smartassist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartassist.appreciate.be.smartassist.model.api.HabitantInfo;
import smartassist.appreciate.be.smartassist.model.api.ResidentNews;

/**
 * Created by banada ismael on 10/24/2016.
 */

public class ResidenceInfo {
    @SerializedName("infofiche")
    private List<HabitantInfo> habitants;

    @SerializedName("residence_info")
    private List<ResidentNews> residentNews;


    public List<HabitantInfo> getHabitants() {
        return habitants;
    }
    public List<ResidentNews> getResidentNews() {
        return residentNews;
    }
}
