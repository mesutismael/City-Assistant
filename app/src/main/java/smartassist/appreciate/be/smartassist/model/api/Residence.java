package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 9/02/2015.
 */
public class Residence
{
    private List<ApiModule> layout;
    @SerializedName("modules")
    private ModuleData moduleData;
    private int id;
    @SerializedName("photo")
    private String photo;
    private String name;
    private String street;
    private String number;
    @SerializedName("postal_code")
    private String postalCode;
    private String locality;
    private float latitude;
    private float longitude;
    @SerializedName("sync_method")
    private int syncMethod;
    @SerializedName("sync_minutes")
    private int syncMinutes;

    public List<ApiModule> getLayout()
    {
        return layout;
    }

    public ModuleData getModuleData()
    {
        return moduleData;
    }

    public String getName()
    {
        return name;
    }

    public String getLocality()
    {
        return locality;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public String getPhoto() {return photo;}
}
