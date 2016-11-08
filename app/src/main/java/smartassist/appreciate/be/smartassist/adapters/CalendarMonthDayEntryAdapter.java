package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.Birthday;
import smartassist.appreciate.be.smartassist.model.api.CalendarEvent;
import smartassist.appreciate.be.smartassist.model.CalendarEventClean;
import smartassist.appreciate.be.smartassist.utils.CalendarUtils;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 18/02/2015.
 */
public class CalendarMonthDayEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<CalendarEventClean> events;
    private List<Birthday> birthdays;
    private Calendar selectedDate;

    private static final int TYPE_ENTRY = 1;
    private static final int TYPE_BIRTHDAY = 2;

    public CalendarMonthDayEntryAdapter()
    {
        this.selectedDate = Calendar.getInstance();
    }

    public void setSelectedDate(Calendar selectedDate)
    {
        this.selectedDate = selectedDate;
    }

    public void setEvents(List<CalendarEventClean> events)
    {
        this.events = events;
        if(this.events != null && this.events.size() > 1)
            Collections.sort(this.events);
        this.notifyDataSetChanged();
    }

    public void setBirthdays(List<Birthday> birthdays)
    {
        this.birthdays = birthdays;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position < this.getBirthdayCount() ? TYPE_BIRTHDAY : TYPE_ENTRY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        switch (viewType)
        {
            case TYPE_BIRTHDAY:
                View viewBirthday = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_calendar_month_birthday, viewGroup, false);
                return new BirthdayViewHolder(viewBirthday);
            case TYPE_ENTRY:
                View viewEntry = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_calendar_month_day_entry, viewGroup, false);
                return new CalendarMonthDayEntryViewHolder(viewEntry);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        if(viewHolder instanceof BirthdayViewHolder)
        {
            Birthday birthday = this.birthdays.get(i);
            int selectedYear = this.selectedDate.get(Calendar.YEAR);

            ((BirthdayViewHolder) viewHolder).bind(birthday, selectedYear);
        }
        else if(viewHolder instanceof CalendarMonthDayEntryViewHolder)
        {
            int position = i - this.getBirthdayCount();

            CalendarEventClean event = this.events.get(position);

            ((CalendarMonthDayEntryViewHolder) viewHolder).bind(event);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.getEventCount() + this.getBirthdayCount();
    }

    private int getBirthdayCount()
    {
        return this.birthdays != null ? this.birthdays.size() : 0;
    }

    private int getEventCount()
    {
        return this.events != null ? this.events.size() : 0;
    }

    public class CalendarMonthDayEntryViewHolder extends RecyclerView.ViewHolder
    {
        private View viewHabitant;
        private TextView textViewTime;
        private TextView textViewDescription;

        public CalendarMonthDayEntryViewHolder(View itemView)
        {
            super(itemView);

            this.viewHabitant = itemView.findViewById(R.id.view_habitant);
            this.textViewTime = (TextView)itemView.findViewById(R.id.textView_time);
            this.textViewDescription = (TextView)itemView.findViewById(R.id.textView_description);
        }

        public void bind(CalendarEventClean event)
        {
            this.viewHabitant.setBackgroundResource(event.getLevel() == CalendarEvent.LEVEL_HABITANT_1
                    ? R.drawable.shape_habitant_1
                    : event.getLevel() == CalendarEvent.LEVEL_HABITANT_2
                    ? R.drawable.shape_habitant_2
                    : R.drawable.shape_habitant_flat);

            this.textViewTime.setText(CalendarUtils.getEventTimeText(this.textViewTime.getContext(), event.getStart(), event.getStop()));
            this.textViewDescription.setText(event.getDescription());
        }
    }

    public class BirthdayViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewDescription;

        public BirthdayViewHolder(View itemView)
        {
            super(itemView);

            this.textViewDescription = (TextView)itemView.findViewById(R.id.textView_description);
        }

        public void bind(Birthday birthday, int selectedYear)
        {
            int age = selectedYear - birthday.getYear();
            String description = birthday.getFirstName() + " " + birthday.getLastName() + " " + age;

            this.textViewDescription.setText(description);
        }
    }
}
