package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
 * Created by Inneke on 16/02/2015.
 */
public class CalendarDayEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<CalendarEventClean> events;
    private List<Birthday> birthdays;
    private Calendar selectedDate;
    private boolean[] selectedIndicators;

    private static final int TYPE_ENTRY = 1;
    private static final int TYPE_BIRTHDAY = 2;

    public CalendarDayEntryAdapter()
    {
        this.selectedDate = Calendar.getInstance();
        this.setSelectedIndicatorPositions();
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
        this.setSelectedIndicatorPositions();
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
                View viewBirthday = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_calendar_day_birthday, viewGroup, false);
                return new BirthdayViewHolder(viewBirthday);
            case TYPE_ENTRY:
                View viewEntry = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_calendar_day_entry, viewGroup, false);
                return new CalendarDayEntryViewHolder(viewEntry);
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
            boolean showDivider = i != this.getItemCount() - 1;

            ((BirthdayViewHolder) viewHolder).bind(birthday, selectedYear, showDivider);
        }
        else if(viewHolder instanceof CalendarDayEntryViewHolder)
        {
            int position = i - this.getBirthdayCount();

            CalendarEventClean event = this.events.get(position);
            boolean selected = this.selectedIndicators[position];
            boolean first = position == 0;
            boolean last = position == this.getEventCount() - 1;

            ((CalendarDayEntryViewHolder) viewHolder).bind(event, selected, first, last);
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

    private void setSelectedIndicatorPositions()
    {
        int eventCount = this.getEventCount();
        boolean[] indicators = new boolean[eventCount];
        Calendar now = Calendar.getInstance();
        long nowMillis = now.getTimeInMillis();
        boolean sameDay = now.get(Calendar.YEAR) == this.selectedDate.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == this.selectedDate.get(Calendar.DAY_OF_YEAR);

        if(sameDay && eventCount != 0)
        {
            boolean firstChoiceFound = false;
            int secondChoicePosition = -1;
            long secondChoiceTime = nowMillis + (2 * 60 * 60 * 1000);

            for(int i = 0; i < eventCount; i++)
            {
                long start = this.events.get(i).getStart();
                long stop = this.events.get(i).getStop();

                if(nowMillis >= start && nowMillis < stop)
                {
                    indicators[i] = true;
                    firstChoiceFound = true;
                }
                else if(nowMillis < start && start < secondChoiceTime)
                {
                    secondChoicePosition = i;
                    secondChoiceTime = start;
                }
            }

            if(!firstChoiceFound && secondChoicePosition >= 0)
            {
                indicators[secondChoicePosition] = true;
            }
        }

        this.selectedIndicators = indicators;
    }

    public class CalendarDayEntryViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageViewIndicator;
        private View viewIndicatorTop;
        private View viewIndicatorBottom;
        private View viewHabitant;
        private TextView textViewTime;
        private TextView textViewDescription;
        private View viewDivider;

        public CalendarDayEntryViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewIndicator = (ImageView)itemView.findViewById(R.id.imageView_indicator);
            this.viewIndicatorTop = itemView.findViewById(R.id.view_indicatorTop);
            this.viewIndicatorBottom = itemView.findViewById(R.id.view_indicatorBottom);
            this.viewHabitant = itemView.findViewById(R.id.view_habitant);
            this.textViewTime = (TextView)itemView.findViewById(R.id.textView_time);
            this.textViewDescription = (TextView)itemView.findViewById(R.id.textView_description);
            this.viewDivider = itemView.findViewById(R.id.view_divider);
        }

        public void bind(CalendarEventClean event, boolean selected, boolean first, boolean last)
        {
            this.viewHabitant.setBackgroundResource(event.getLevel() == CalendarEvent.LEVEL_HABITANT_1
                    ? R.drawable.shape_habitant_1
                    : event.getLevel() == CalendarEvent.LEVEL_HABITANT_2
                    ? R.drawable.shape_habitant_2
                    : R.drawable.shape_habitant_flat);

            this.textViewTime.setText(CalendarUtils.getEventTimeText(this.textViewTime.getContext(), event.getStart(), event.getStop()));
            this.textViewDescription.setText(event.getDescription());
            this.imageViewIndicator.setImageResource(selected ? R.drawable.timeline_selected : R.drawable.timeline_default);
            this.viewIndicatorTop.setVisibility(first ? View.INVISIBLE : View.VISIBLE);
            this.viewIndicatorBottom.setVisibility(last ? View.INVISIBLE : View.VISIBLE);
            this.viewDivider.setVisibility(last ? View.GONE : View.VISIBLE);
        }
    }

    public class BirthdayViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewDescription;
        private View viewDivider;

        public BirthdayViewHolder(View itemView)
        {
            super(itemView);

            this.textViewDescription = (TextView)itemView.findViewById(R.id.textView_description);
            this.viewDivider = itemView.findViewById(R.id.view_divider);
        }

        public void bind(Birthday birthday, int selectedYear, boolean showDivider)
        {
            int age = selectedYear - birthday.getYear();
            String description = birthday.getFirstName() + " " + birthday.getLastName() + " " + age;

            this.textViewDescription.setText(description);
            this.viewDivider.setVisibility(showDivider ? View.VISIBLE : View.GONE);
        }
    }
}
