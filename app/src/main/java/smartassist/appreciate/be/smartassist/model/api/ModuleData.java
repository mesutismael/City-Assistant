package smartassist.appreciate.be.smartassist.model.api;
import com.google.gson.annotations.SerializedName;
import java.util.List;

import smartassist.appreciate.be.smartassist.model.ResidenceInfo;

/**
 * Created by Inneke on 9/02/2015.
 */
public class ModuleData
{
    @SerializedName("1")
    private NearByData nearByData;
    @SerializedName("2")
    private NewsData newsData;
    @SerializedName("3")
    private List<WeatherItem> weatherItems;
    @SerializedName("4")
    private CalendarData calendarData;
    @SerializedName("5")
    private EmergencyData emergencyData;
    @SerializedName("6")
    private PhotoData photoData;
    @SerializedName("7")
    private RssData rssData;
    @SerializedName("8")
    private List<HealthCareBookItem> careBookItems;
    @SerializedName("12")
    private List<Invoice> invoices;
    @SerializedName("13")
    private ResidenceInfo residenceInfo;



    public NearByData getNearByData() {return nearByData;}
    public NewsData getNewsData() {return newsData;}
    public List<WeatherItem> getWeatherItems()
    {
        return weatherItems;
    }
    public CalendarData getCalendarData()
    {
        return calendarData;
    }
    public EmergencyData getEmergencyData()
    {
        return emergencyData;
    }
    public PhotoData getPhotoData()
    {
        return photoData;
    }
    public RssData getRssData()
    {
        return rssData;
    }
    public List<HealthCareBookItem> getCareBookItems()
    {
        return careBookItems;
    }
    public List<Invoice> getInvoices()
    {
        return invoices;
    }
    public ResidenceInfo getResidenceInfo() {return residenceInfo;}
}