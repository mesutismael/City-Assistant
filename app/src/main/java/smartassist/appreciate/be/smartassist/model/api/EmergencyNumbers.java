package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke De Clippel on 8/02/2016.
 */
public class EmergencyNumbers
{
    @SerializedName("items")
    private List<EmergencyNumber> emergencyNumbers;
    private List<RemovedEmergencyItem> removed;

    public List<EmergencyNumber> getEmergencyNumbers()
    {
        return emergencyNumbers;
    }

    public List<RemovedEmergencyItem> getRemoved()
    {
        return removed;
    }
}
