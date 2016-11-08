package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 20/04/2015.
 */
public class CalendarEventItems
{
    @SerializedName("items")
    private List<CalendarEvent> eventItems;
    private List<RemovedDataItem> removed;

    public List<CalendarEvent> getEventItems()
    {
        return eventItems;
    }

    public List<RemovedDataItem> getRemoved()
    {
        return removed;
    }
}
