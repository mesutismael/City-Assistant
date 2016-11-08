package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 9/02/2015.
 */
public class Habitant
{
    private int id;
    @SerializedName("flat_id")
    private int flatId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("color_code")
    private int colorCode;
    @SerializedName("medication")
    private List<MedicationItem> medicationItems;

    public int getId()
    {
        return id;
    }

    public List<MedicationItem> getMedicationItems()
    {
        return medicationItems;
    }
}
