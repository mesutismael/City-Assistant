package smartassist.appreciate.be.smartassist.adapters;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.api.CalendarEvent;
import smartassist.appreciate.be.smartassist.model.CalendarEventClean;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 17/02/2015.
 */
public class CalendarMonthEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<CalendarEventClean> events;
    private MonthDisplayHelper monthDisplayHelper;
    private int selectedDay;
    private Calendar today;
    private int rowHeight;
    private OnDayClickListener listener;
    private int currentCount;
    private int maxCount;

    private static final int TYPE_ENTRY = 1;
    private static final int TYPE_HEADER = 2;

    public CalendarMonthEntryAdapter(Calendar calendar, int rowHeight)
    {
        this.setCalendar(calendar);
        this.today = Calendar.getInstance();
        this.rowHeight = rowHeight;
    }

    public void setListener(OnDayClickListener listener)
    {
        this.listener = listener;
    }

    public void setCalendar(Calendar calendar)
    {
        if(calendar == null)
            calendar = Calendar.getInstance();
        this.monthDisplayHelper = new MonthDisplayHelper(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Calendar.MONDAY);
        this.selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int lastRow = this.monthDisplayHelper.getRowOf(this.monthDisplayHelper.getNumberOfDaysInMonth());
        this.maxCount = (lastRow + 1) * 7 + 7;
        this.currentCount = 1;
    }

    public void setSelectedDay(int day)
    {
        int oldSelected = (this.monthDisplayHelper.getRowOf(this.selectedDay) + 1) * 7 + this.monthDisplayHelper.getColumnOf(this.selectedDay);
        this.selectedDay = day;
        int newSelected = (this.monthDisplayHelper.getRowOf(this.selectedDay) + 1) * 7 + this.monthDisplayHelper.getColumnOf(this.selectedDay);
        this.notifyItemChanged(oldSelected);
        this.notifyItemChanged(newSelected);
    }

    public void setEvents(List<CalendarEventClean> events)
    {
        this.events = events;
        if(this.events != null && this.events.size() > 1)
            Collections.sort(this.events);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position < 7 ? TYPE_HEADER : TYPE_ENTRY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        switch (viewType)
        {
            case TYPE_HEADER:
                View viewHeader = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_calendar_month_day, viewGroup, false);
                return new DayOfWeekViewHolder(viewHeader);
            case TYPE_ENTRY:
                View viewEntry = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_calendar_month_entry, viewGroup, false);
                return new CalendarMonthEntryViewHolder(viewEntry);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        if(viewHolder instanceof CalendarMonthEntryViewHolder)
        {
            ((CalendarMonthEntryViewHolder) viewHolder).bind(i - 7, this.monthDisplayHelper, this.events, this.selectedDay, this.today);
        }
        else if(viewHolder instanceof DayOfWeekViewHolder)
        {
            ((DayOfWeekViewHolder) viewHolder).bind(i);
        }

        new Handler().post(new Runnable()
        {
            @Override
            public void run()
            {
                CalendarMonthEntryAdapter.this.inflateNextItem();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return this.currentCount;
    }

    private void inflateNextItem()
    {
        if(this.currentCount < this.maxCount)
        {
            this.currentCount++;
            this.notifyItemInserted(this.currentCount - 1);
        }
    }

    public class CalendarMonthEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private View viewHabitant;
        private TextView textViewDay;
        private View viewSelectedDay;
        private int day;

        public CalendarMonthEntryViewHolder(View itemView)
        {
            super(itemView);

            this.viewHabitant = itemView.findViewById(R.id.view_habitant);
            this.textViewDay = (TextView)itemView.findViewById(R.id.textView_day);
            this.viewSelectedDay = itemView.findViewById(R.id.view_selectedDay);

            itemView.getLayoutParams().height = CalendarMonthEntryAdapter.this.rowHeight;

            itemView.setOnClickListener(this);
        }

        public void bind(int position, MonthDisplayHelper monthDisplayHelper, List<CalendarEventClean> events, int selectedDay, Calendar now)
        {
            int row = position / 7;
            int col = position % 7;
            int day = monthDisplayHelper.getDayAt(row, col);
            boolean inCurrentMonth = monthDisplayHelper.isWithinCurrentMonth(row, col);
            this.day = inCurrentMonth ? day : -1;
            boolean selectDay = day == selectedDay && inCurrentMonth;
            boolean today = monthDisplayHelper.getYear() == now.get(Calendar.YEAR)
                    && monthDisplayHelper.getMonth() == now.get(Calendar.MONTH)
                    && day == now.get(Calendar.DAY_OF_MONTH);

            this.textViewDay.setText(today ? Html.fromHtml("<u>" + day + "</u>") : String.valueOf(day));
            this.textViewDay.setTextColor(ContextCompat.getColor(this.textViewDay.getContext(), selectDay
                    ? R.color.general_text_white
                    : inCurrentMonth
                    ? R.color.general_text
                    : R.color.calendar_day_different_month));
            this.viewSelectedDay.setVisibility(selectDay ? View.VISIBLE : View.GONE);
            this.viewHabitant.setVisibility(inCurrentMonth && !selectDay ? View.VISIBLE : View.GONE);

            boolean hab1 = false;
            boolean hab2 = false;
            boolean flat = false;

            if(inCurrentMonth && events != null)
            {
                for(CalendarEventClean event : events)
                {
                    long start = event.getStart();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(start);
                    if(calendar.get(Calendar.DAY_OF_MONTH) == day)
                    {
                        int level = event.getLevel();
                        if(level == CalendarEvent.LEVEL_HABITANT_1)
                            hab1 = true;
                        if(level == CalendarEvent.LEVEL_HABITANT_2)
                            hab2 = true;
                        if(level == CalendarEvent.LEVEL_FLAT)
                            flat = true;
                    }
                }
            }

            this.viewHabitant.setBackgroundResource(flat || (hab1 && hab2)
                    ? R.drawable.shape_habitant_flat
                    : hab1
                    ? R.drawable.shape_habitant_1
                    : hab2
                    ? R.drawable.shape_habitant_2
                    : 0);
        }

        @Override
        public void onClick(View v)
        {
            if(CalendarMonthEntryAdapter.this.listener != null)
                CalendarMonthEntryAdapter.this.listener.onDayClick(v, this.day);
        }
    }

    public class DayOfWeekViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewDay;

        public DayOfWeekViewHolder(View itemView)
        {
            super(itemView);
            this.textViewDay = (TextView) itemView.findViewById(R.id.textView_day);
            itemView.getLayoutParams().height = CalendarMonthEntryAdapter.this.rowHeight;
        }

        public void bind(int position)
        {
            switch (position)
            {
                case 0: this.textViewDay.setText(R.string.calendar_monday_short); break;
                case 1: this.textViewDay.setText(R.string.calendar_tuesday_short); break;
                case 2: this.textViewDay.setText(R.string.calendar_wednesday_short); break;
                case 3: this.textViewDay.setText(R.string.calendar_thursday_short); break;
                case 4: this.textViewDay.setText(R.string.calendar_friday_short); break;
                case 5: this.textViewDay.setText(R.string.calendar_saturday_short); break;
                case 6: this.textViewDay.setText(R.string.calendar_sunday_short); break;
            }
        }
    }

    public interface OnDayClickListener
    {
        void onDayClick(View caller, int day);
    }
}
