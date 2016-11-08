package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.api.CalendarEvent;
import smartassist.appreciate.be.smartassist.model.CalendarEventClean;
import smartassist.appreciate.be.smartassist.utils.DisplayUtils;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;

/**
 * Created by Inneke on 18/02/2015.
 */
public class CalendarWeekView extends FrameLayout implements View.OnClickListener
{
    private int dayWidth;
    private int eventMarginHorizontal;
    private int eventPadding;
    private int eventLinesPadding;
    private int hourHeight;
    private int hourLineHeight;
    private int habitantCircleSize;
    private int hourLineColor;
    private int textColor;
    private float hourTextSize;
    private Drawable habitant1;
    private Drawable habitant2;
    private Drawable habitantFlat;
    private Calendar calendarStartDate;
    private boolean scrollLayoutFinished;
    private int requestedScroll;
    private String textEntireDay;
    private String textEntireDayLine1;
    private String textEntireDayLine2;

    private ScrollView scrollViewCalendar;
    private RelativeLayout layoutEvents;
    private TextView textViewMonday;
    private TextView textViewTueday;
    private TextView textViewWednesday;
    private TextView textViewThursday;
    private TextView textViewFriday;
    private TextView textViewSaturday;
    private TextView textViewSunday;
    private View currentTimeView;

    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("EE d", Locale.getDefault());

    public CalendarWeekView(Context context)
    {
        this(context, null);
    }

    public CalendarWeekView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CalendarWeekView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.view_calendar_week, this, true);
        this.scrollViewCalendar = (ScrollView) view.findViewById(R.id.scrollView_calendar);
        this.layoutEvents = (RelativeLayout) view.findViewById(R.id.layout_events);
        this.textViewMonday = (TextView) view.findViewById(R.id.textView_monday);
        this.textViewTueday = (TextView) view.findViewById(R.id.textView_tuesday);
        this.textViewWednesday = (TextView) view.findViewById(R.id.textView_wednesday);
        this.textViewThursday = (TextView) view.findViewById(R.id.textView_thursday);
        this.textViewFriday = (TextView) view.findViewById(R.id.textView_friday);
        this.textViewSaturday = (TextView) view.findViewById(R.id.textView_saturday);
        this.textViewSunday = (TextView) view.findViewById(R.id.textView_sunday);
        RelativeLayout layoutBackground = (RelativeLayout) view.findViewById(R.id.layout_eventsBackground);
        RelativeLayout layoutHours = (RelativeLayout) view.findViewById(R.id.layout_hours);
        View viewMonday = view.findViewById(R.id.view_monday);
        View viewTuesday = view.findViewById(R.id.view_tuesday);
        View viewWednesday = view.findViewById(R.id.view_wednesday);
        View viewThursday = view.findViewById(R.id.view_thursday);
        View viewFriday = view.findViewById(R.id.view_friday);
        View viewSaturday = view.findViewById(R.id.view_saturday);
        View viewSunday = view.findViewById(R.id.view_sunday);

        viewMonday.setOnClickListener(this);
        viewTuesday.setOnClickListener(this);
        viewWednesday.setOnClickListener(this);
        viewThursday.setOnClickListener(this);
        viewFriday.setOnClickListener(this);
        viewSaturday.setOnClickListener(this);
        viewSunday.setOnClickListener(this);

        this.dayWidth = DisplayUtils.getApproximateCalendarWeekDayWidth(context);
        this.eventMarginHorizontal = (int) context.getResources().getDimension(R.dimen.week_view_event_margin_horizontal);
        this.eventPadding = (int) context.getResources().getDimension(R.dimen.week_view_event_padding);
        this.eventLinesPadding = (int) context.getResources().getDimension(R.dimen.week_view_event_lines_padding);
        this.hourHeight = (int) context.getResources().getDimension(R.dimen.week_view_height) / 24;
        this.hourLineHeight = (int) context.getResources().getDimension(R.dimen.list_divider_height);
        this.habitantCircleSize = (int) this.getContext().getResources().getDimension(R.dimen.calendar_habitant_circle);
        this.hourLineColor = ContextCompat.getColor(context, R.color.calendar_day_list_divider);
        this.textColor = ContextCompat.getColor(context, R.color.general_text_white);
        this.hourTextSize = context.getResources().getDimensionPixelSize(R.dimen.week_view_event_hour_text);
        this.habitant1 = ContextCompat.getDrawable(context, R.drawable.shape_habitant_1);
        this.habitant1.setBounds(0, 0, this.habitantCircleSize, this.habitantCircleSize);
        this.habitant2 = ContextCompat.getDrawable(context, R.drawable.shape_habitant_2);
        this.habitant2.setBounds(0, 0, this.habitantCircleSize, this.habitantCircleSize);
        this.habitantFlat = ContextCompat.getDrawable(context, R.drawable.shape_habitant_flat);
        this.habitantFlat.setBounds(0, 0, this.habitantCircleSize, this.habitantCircleSize);
        this.textEntireDay = context.getString(R.string.calendar_entire_day);
        this.textEntireDayLine1 = context.getString(R.string.calendar_entire_day_line_1);
        this.textEntireDayLine2 = context.getString(R.string.calendar_entire_day_line_2);

        this.calendarStartDate = this.getStartOfWeek(null);

        this.currentTimeView = new View(context);
        int timeWidth = this.dayWidth - 2 * this.eventMarginHorizontal;
        int timeHeight = (int) context.getResources().getDimension(R.dimen.week_view_current_time_height);
        RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(timeWidth, timeHeight);
        this.currentTimeView.setLayoutParams(timeParams);
        this.currentTimeView.setBackgroundColor(ContextCompat.getColor(context, R.color.calendar_week_current_time));
        this.currentTimeView.setVisibility(INVISIBLE);
        this.layoutEvents.addView(this.currentTimeView);

        this.addHours(layoutHours);
        this.addLines(layoutBackground);

        this.scrollLayoutFinished = false;
        this.requestedScroll = 0;
        this.scrollViewCalendar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                CalendarWeekView.this.scrollLayoutFinished = true;
                CalendarWeekView.this.scrollViewCalendar.scrollTo(0, CalendarWeekView.this.requestedScroll);
                CalendarWeekView.this.scrollViewCalendar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private Calendar getStartOfWeek(Calendar calendar)
    {
        if(calendar == null)
            calendar = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(calendar.getTimeInMillis());
        MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), Calendar.MONDAY);
        int row = monthDisplayHelper.getRowOf(startDate.get(Calendar.DAY_OF_MONTH));
        int firstDay = monthDisplayHelper.getDayAt(row, 0);
        startDate.set(Calendar.DAY_OF_MONTH, firstDay);
        if(!monthDisplayHelper.isWithinCurrentMonth(row, 0))
            startDate.add(Calendar.MONTH, row == 0 ? -1 : 1);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        return startDate;
    }

    @Override
    public void onClick(View v)
    {
        int daysToAdd = -1;

        switch (v.getId())
        {
            case R.id.view_monday: daysToAdd = 0; break;
            case R.id.view_tuesday: daysToAdd = 1; break;
            case R.id.view_wednesday: daysToAdd = 2; break;
            case R.id.view_thursday: daysToAdd = 3; break;
            case R.id.view_friday: daysToAdd = 4; break;
            case R.id.view_saturday: daysToAdd = 5; break;
            case R.id.view_sunday: daysToAdd = 6; break;
        }

        if(daysToAdd != -1)
        {
            Calendar clickedDate = Calendar.getInstance();
            clickedDate.setTimeInMillis(this.calendarStartDate.getTimeInMillis());
            clickedDate.add(Calendar.DAY_OF_MONTH, daysToAdd);
            //TODO go to day view for this date
        }
    }

    public void setData(List<CalendarEventClean> events, Calendar calendar)
    {
        this.calendarStartDate = this.getStartOfWeek(calendar);
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(this.calendarStartDate.getTimeInMillis());

        this.textViewMonday.setText(SDF_DATE.format(new Date(startDate.getTimeInMillis())).toUpperCase());
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        this.textViewTueday.setText(SDF_DATE.format(new Date(startDate.getTimeInMillis())).toUpperCase());
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        this.textViewWednesday.setText(SDF_DATE.format(new Date(startDate.getTimeInMillis())).toUpperCase());
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        this.textViewThursday.setText(SDF_DATE.format(new Date(startDate.getTimeInMillis())).toUpperCase());
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        this.textViewFriday.setText(SDF_DATE.format(new Date(startDate.getTimeInMillis())).toUpperCase());
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        this.textViewSaturday.setText(SDF_DATE.format(new Date(startDate.getTimeInMillis())).toUpperCase());
        startDate.add(Calendar.DAY_OF_MONTH, 1);
        this.textViewSunday.setText(SDF_DATE.format(new Date(startDate.getTimeInMillis())).toUpperCase());

        this.layoutEvents.removeAllViews();

        List<Event> weekEvents = new ArrayList<>();

        if(events != null)
        {
            for(CalendarEventClean event : events)
            {
                String description = event.getDescription();
                int level = event.getLevel();
                long startTime = event.getStart();
                long stopTime = event.getStop();
                Calendar calendarEvent = Calendar.getInstance();
                calendarEvent.setTimeInMillis(startTime);
                int dayOfWeek = (calendarEvent.get(Calendar.DAY_OF_WEEK) + 5) % 7;
                int startHour = calendarEvent.get(Calendar.HOUR_OF_DAY);
                int startMinute = calendarEvent.get(Calendar.MINUTE);
                calendarEvent.setTimeInMillis(stopTime);
                int stopHour = calendarEvent.get(Calendar.HOUR_OF_DAY);
                int stopMinute = calendarEvent.get(Calendar.MINUTE);
                weekEvents.add(new Event(startTime, stopTime, dayOfWeek, startHour, startMinute, stopHour, stopMinute, description, level));
            }
        }

        List<List<Event>> eventGroups = this.getEventGroups(weekEvents);
        if(eventGroups != null)
            for(List<Event> eventGroup : eventGroups)
                if(eventGroup != null)
                    for(int i = 0; i < eventGroup.size(); i++)
                        this.addEvent(eventGroup.get(i), i, eventGroup.size());

        this.layoutEvents.addView(this.currentTimeView);
        this.updateCurrentTimeView();
    }

    public void updateCurrentTimeView()
    {
        Calendar now = Calendar.getInstance();
        long nowMillis = System.currentTimeMillis();
        long startMillis = this.calendarStartDate.getTimeInMillis();
        long stopMillis = startMillis + (7 * 24 * 60 * 60 * 1000);

        if(nowMillis >= startMillis && nowMillis < stopMillis)
        {
            this.currentTimeView.setVisibility(VISIBLE);
            int dayOfWeek = (now.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);
            int topMargin = (hour * this.hourHeight) + (minute * this.hourHeight / 60);
            int leftMargin = dayOfWeek * this.dayWidth + this.eventMarginHorizontal;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.currentTimeView.getLayoutParams();
            params.topMargin = topMargin;
            params.leftMargin = leftMargin;
            this.currentTimeView.bringToFront();
        }
        else
        {
            this.currentTimeView.setVisibility(INVISIBLE);
        }
    }

    public void scrollToHour(int hour)
    {
        int scrollY = hour * this.hourHeight;
        this.requestedScroll = scrollY;
        if(this.scrollLayoutFinished)
            this.scrollViewCalendar.scrollTo(0, scrollY);
    }

    private void addHours(RelativeLayout layout)
    {
        for(int i = 0; i < 24; i++)
        {
            TextView textViewHour = new TextView(this.getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, this.hourHeight);
            params.topMargin = i * this.hourHeight;
            textViewHour.setText(String.valueOf(i)+":00");
            textViewHour.setGravity(Gravity.CENTER_HORIZONTAL);
            textViewHour.setTypeface(this.getContext(), TypefaceHelper.MONTSERRAT_LIGHT);
            layout.addView(textViewHour, params);
        }
    }

    private void addLines(RelativeLayout layout)
    {
        for(int i = 1; i < 24; i++)
        {
            View viewLine = new View(this.getContext());
            viewLine.setBackgroundColor(this.hourLineColor);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, this.hourLineHeight);
            params.topMargin = i * this.hourHeight;
            layout.addView(viewLine, params);
        }
    }

    private void addEvent(Event event, int column, int columnCount)
    {
        int top = event.startHour * this.hourHeight + (this.hourHeight * event.startMinute / 60);
        int bottom = event.stopHour * this.hourHeight + (this.hourHeight * event.stopMinute / 60);
        int height = bottom - top;
        int columnWidth = this.dayWidth / columnCount;
        int width = columnWidth - 2 * this.eventMarginHorizontal;
        int left = this.dayWidth * event.day + columnWidth * column + this.eventMarginHorizontal;

        RelativeLayout.LayoutParams paramsLayout = new RelativeLayout.LayoutParams(width, height);
        paramsLayout.topMargin = top;
        paramsLayout.leftMargin = left;
        LinearLayout.LayoutParams paramsTimeText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramsDescriptionText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layoutEvent = new LinearLayout(this.getContext());
        layoutEvent.setBackgroundResource(R.drawable.shape_week_event);
        layoutEvent.setPadding(this.eventPadding, this.eventPadding, this.eventPadding, this.eventPadding);
        layoutEvent.setOrientation(LinearLayout.VERTICAL);

        TextView textViewDescription = new TextView(this.getContext());
        textViewDescription.setPadding(0, this.eventLinesPadding, 0, 0);
        textViewDescription.setTextColor(this.textColor);
        textViewDescription.setTypeface(this.getContext(), TypefaceHelper.MONTSERRAT_LIGHT);

        TextView textViewTime = new TextView(this.getContext());
        textViewTime.setTextColor(this.textColor);
        textViewTime.setTypeface(this.getContext(), TypefaceHelper.MONTSERRAT_LIGHT);
        textViewTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, hourTextSize);

        String timeText = this.getTimeText(event, width - 2 * this.eventPadding, height - 2 * this.eventPadding, textViewTime.getLineHeight(), textViewTime.getPaint());
        if(timeText != null)
        {
            textViewTime.setText(timeText);
            layoutEvent.addView(textViewTime, paramsTimeText);

            int timeLines = timeText.contains("\n") ? 2 : 1;
            int timeHeight = timeLines * textViewTime.getLineHeight();
            int availableHeight = height - 2 * this.eventPadding - timeHeight - this.eventLinesPadding;

            if(availableHeight >= textViewDescription.getLineHeight())
            {
                Drawable habitantDrawable;
                if(event.level == CalendarEvent.LEVEL_HABITANT_1)
                    habitantDrawable = this.habitant1;
                else if(event.level == CalendarEvent.LEVEL_HABITANT_2)
                    habitantDrawable = this.habitant2;
                else
                    habitantDrawable = this.habitantFlat;

                SpannableString spannableString = new SpannableString(": " + event.description);
                spannableString.setSpan(new ImageSpan(habitantDrawable, ImageSpan.ALIGN_BASELINE), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                textViewDescription.setText(spannableString, TextView.BufferType.SPANNABLE);
                layoutEvent.addView(textViewDescription, paramsDescriptionText);
                //TODO fix bug where text is cut off at the bottom
            }
        }

        this.layoutEvents.addView(layoutEvent, paramsLayout);
    }

    /**
     * Helper method to get the text displaying the time. If there is not enough space, the text
     * will be shown in two lines or not at all.
     * @param event the event containing the time
     * @param width the available width for the text
     * @param height the available height for the text
     * @param lineHeight the line height of the TextView
     * @param textPaint the paint of the TextView
     * @return The text to be displayed in the TextView, can be null if there is not enough space.
     */
    private String getTimeText(Event event, int width, int height, int lineHeight, Paint textPaint)
    {
        String startMinute = event.startMinute < 10 ? "0" + event.startMinute : String.valueOf(event.startMinute);
        String stopMinute = event.stopMinute < 10 ? "0" + event.stopMinute : String.valueOf(event.stopMinute);
        String startHour = String.valueOf(event.startHour) + ":" + startMinute;
        String stopHour = String.valueOf(event.stopHour) + ":" + stopMinute;

        if(event.startHour == 0 && event.startMinute == 0 && event.stopHour == 23 && event.stopMinute == 59)
        {
            int multilineWidth = (int) Math.max(textPaint.measureText(this.textEntireDayLine1), textPaint.measureText(this.textEntireDayLine2));
            int multilineHeight = lineHeight * 2;
            int oneLineWidth = (int) textPaint.measureText(this.textEntireDay);

            if(width >= oneLineWidth && height >= lineHeight)
                return this.textEntireDay;
            if(width >= multilineWidth && height >= multilineHeight)
                return this.textEntireDayLine1 + "\n" + textEntireDayLine2;
        }
        else
        {
            int multilineWidth = (int) Math.max(textPaint.measureText(startHour), textPaint.measureText(stopHour));
            int multilineHeight = lineHeight * 2;
            int oneLineWidth = (int) textPaint.measureText(startHour + " - " + stopHour);

            if(width >= oneLineWidth && height >= lineHeight)
                return startHour + " - " + stopHour;
            if(width >= multilineWidth && height >= multilineHeight)
                return startHour + "\n" + stopHour;
        }

        return null;
    }

    /**
     * Helper method to make groups of the events where each group contains 1 or more events
     * for a certain time period. The events in a group should be made smaller so they aren't
     * drawn on top of each other.
     * @param events The entire list of events in a week
     * @return A list with groups of events
     */
    private List<List<Event>> getEventGroups(List<Event> events)
    {
        //TODO improve this method
        if(events == null)
            return null;

        List<List<Event>> eventGroups = new ArrayList<>();

        while(events.size() > 0)
        {
            List<Event> eventGroup = this.makeEventGroup(events, events.get(0).start, events.get(0).stop);
            eventGroups.add(eventGroup);
            events.removeAll(eventGroup);
        }

        return eventGroups;
    }

    /**
     * Helper method to create a subset of the events. The subset will contain all events in
     * a certain time period.
     * @param events The list of events in a week, or a subset of it
     * @param start The start time for the subset
     * @param stop The stop time for the subset
     * @return A group of events
     */
    private List<Event> makeEventGroup(List<Event> events, long start, long stop)
    {
        List<Event> eventGroup = new ArrayList<>();

        if(events != null && events.size() > 0)
        {
            eventGroup.add(events.get(0));

            for (int i = 1; i < events.size(); i++)
            {
                Event event = events.get(i);
                if(event.start >= stop || event.stop <= start)
                    continue;

                if(event.start >= start && event.stop <= stop)
                    eventGroup.add(event);
                else
                    return this.makeEventGroup(events, Math.min(start, event.start), Math.max(stop, event.stop));
            }
        }

        return eventGroup;
    }

    private class Event
    {
        long start;
        long stop;
        int day;
        int startHour;
        int startMinute;
        int stopHour;
        int stopMinute;
        String description;
        int level;

        private Event(long start, long stop, int day, int startHour, int startMinute, int stopHour, int stopMinute, String description, int level)
        {
            this.start = start;
            this.stop = stop;
            this.day = day;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.stopHour = stopHour;
            this.stopMinute = stopMinute;
            this.description = description;
            this.level = level;
        }
    }
}
