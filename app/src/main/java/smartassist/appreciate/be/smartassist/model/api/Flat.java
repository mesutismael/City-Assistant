package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 9/02/2015.
 */
public class Flat
{
    private int id;
    @SerializedName("residence_id")
    private int residenceId;
    private String number;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String hash;
    private String name;
    private String photo;
    private Residence residence;
    private List<Habitant> habitants;
    @SerializedName("modules")
    private ModuleData moduleData;

    public int getId()
    {
        return id;
    }

    public String getNumber()
    {
        return number;
    }

    public String getHash()
    {
        return hash;
    }

    public String getName()
    {
        return name;
    }

    public String getPhoto()
    {
        return this.photo != null ? this.photo.replace(" ", "%20") : null;
    }

    public Residence getResidence()
    {
        return residence;
    }

    public List<Habitant> getHabitants()
    {
        return habitants;
    }

    public ModuleData getModuleData()
    {
        return moduleData;
    }
}
