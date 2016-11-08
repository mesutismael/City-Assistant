package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 20/02/2015.
 */
public class CalendarData
{
    @SerializedName("agenda")
    private CalendarEventItems events;
    private List<CalendarBirthday> birthdays;

    public List<CalendarEvent> getItems()
    {
        if(this.events == null)
            return null;
        return this.events.getEventItems();
    }

    public List<RemovedDataItem> getRemoved()
    {
        if(this.events == null)
            return null;
        return this.events.getRemoved();
    }

    public List<CalendarBirthday> getBirthdays()
    {
        return birthdays;
    }
}
