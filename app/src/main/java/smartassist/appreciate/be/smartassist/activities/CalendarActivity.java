package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.AbstractCalendarFragment;
import smartassist.appreciate.be.smartassist.fragments.CalendarDayFragment;
import smartassist.appreciate.be.smartassist.fragments.CalendarMonthFragment;
import smartassist.appreciate.be.smartassist.fragments.CalendarWeekFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;
import smartassist.appreciate.be.smartassist.views.ToggleButton;

/**
 * Created by Inneke on 28/01/2015.
 */
public class CalendarActivity extends BaseActivity implements View.OnClickListener
{
    private ImageView imageViewNextMonth;
    private ImageView imageViewPreviousMonth;
    private TextView textViewMonth;
    private TextView textViewYear;
    private ToggleButton toggleButtonDay;
    private ToggleButton toggleButtonWeek;
    private ToggleButton toggleButtonMonth;

    private AbstractCalendarFragment currentFragment;
    private Calendar calendar;
    private int viewMode;

    private static final SimpleDateFormat SDF_MONTH = new SimpleDateFormat("MMMM", Locale.getDefault());
    public static final int VIEW_MODE_DAY = 1;
    public static final int VIEW_MODE_WEEK = 2;
    public static final int VIEW_MODE_MONTH = 3;
    public static final String KEY_VIEW_MODE = "view_mode";
    private static final String KEY_MILLIS = "time_millis";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_calendar);

        this.imageViewNextMonth = (ImageView) this.findViewById(R.id.imageView_nextMonth);
        this.imageViewPreviousMonth = (ImageView) this.findViewById(R.id.imageView_previousMonth);
        this.textViewMonth = (TextView) this.findViewById(R.id.textView_month);
        this.textViewYear = (TextView) this.findViewById(R.id.textView_year);
        this.toggleButtonDay = (ToggleButton) this.findViewById(R.id.button_day);
        this.toggleButtonWeek = (ToggleButton) this.findViewById(R.id.button_week);
        this.toggleButtonMonth = (ToggleButton) this.findViewById(R.id.button_month);
        TextView textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);

        textViewTitle.setText(R.string.module_calendar);

        this.calendar = Calendar.getInstance();
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_MILLIS))
            this.calendar.setTimeInMillis(savedInstanceState.getLong(KEY_MILLIS));
        this.textViewYear.setText(String.valueOf(this.calendar.get(Calendar.YEAR)));
        this.textViewMonth.setText(SDF_MONTH.format(new Date(this.calendar.getTimeInMillis())));

        this.imageViewNextMonth.setOnClickListener(this);
        this.imageViewPreviousMonth.setOnClickListener(this);
        this.toggleButtonDay.setOnClickListener(this);
        this.toggleButtonWeek.setOnClickListener(this);
        this.toggleButtonMonth.setOnClickListener(this);
        layoutBack.setOnClickListener(this);

        int viewMode;
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_VIEW_MODE))
            viewMode = savedInstanceState.getInt(KEY_VIEW_MODE, VIEW_MODE_MONTH);
        else if(this.getIntent().getExtras() != null)
            viewMode = this.getIntent().getExtras().getInt(KEY_VIEW_MODE, VIEW_MODE_MONTH);
        else
            viewMode = VIEW_MODE_MONTH;
        this.setViewMode(viewMode);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(KEY_VIEW_MODE, this.viewMode);
        outState.putLong(KEY_MILLIS, this.calendar.getTimeInMillis());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageView_nextMonth:
                if(this.viewMode == VIEW_MODE_MONTH)
                    this.calendar.add(Calendar.MONTH, 1);
                else if(this.viewMode == VIEW_MODE_WEEK)
                    this.calendar.add(Calendar.WEEK_OF_YEAR, 1);
                this.textViewYear.setText(String.valueOf(this.calendar.get(Calendar.YEAR)));
                this.textViewMonth.setText(SDF_MONTH.format(new Date(this.calendar.getTimeInMillis())));
                if(this.currentFragment != null)
                    this.currentFragment.onCalendarChanged(this.calendar);
                break;

            case R.id.imageView_previousMonth:
                if(this.viewMode == VIEW_MODE_MONTH)
                    this.calendar.add(Calendar.MONTH, -1);
                else if(this.viewMode == VIEW_MODE_WEEK)
                    this.calendar.add(Calendar.WEEK_OF_YEAR, -1);
                this.textViewYear.setText(String.valueOf(this.calendar.get(Calendar.YEAR)));
                this.textViewMonth.setText(SDF_MONTH.format(new Date(this.calendar.getTimeInMillis())));
                if(this.currentFragment != null)
                    this.currentFragment.onCalendarChanged(this.calendar);
                break;

            case R.id.button_day:
                this.toggleButtonDay.setChecked(true);
                if(this.viewMode != VIEW_MODE_DAY)
                    this.setViewMode(VIEW_MODE_DAY);
                break;

            case R.id.button_week:
                this.toggleButtonWeek.setChecked(true);
                if(this.viewMode != VIEW_MODE_WEEK)
                    this.setViewMode(VIEW_MODE_WEEK);
                break;

            case R.id.button_month:
                this.toggleButtonMonth.setChecked(true);
                if(this.viewMode != VIEW_MODE_MONTH)
                    this.setViewMode(VIEW_MODE_MONTH);
                break;

            case R.id.layout_back:
                this.startParentActivity();
                break;
        }
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_CALENDAR;
    }

    private void setViewMode(int viewMode)
    {
        this.viewMode = viewMode;

        this.toggleButtonDay.setChecked(viewMode == VIEW_MODE_DAY);
        this.toggleButtonWeek.setChecked(viewMode == VIEW_MODE_WEEK);
        this.toggleButtonMonth.setChecked(viewMode == VIEW_MODE_MONTH);
        this.imageViewNextMonth.setVisibility(viewMode == VIEW_MODE_DAY ? View.GONE : View.VISIBLE);
        this.imageViewPreviousMonth.setVisibility(viewMode == VIEW_MODE_DAY ? View.GONE : View.VISIBLE);
        this.textViewMonth.setVisibility(viewMode == VIEW_MODE_DAY ? View.GONE : View.VISIBLE);
        this.textViewYear.setVisibility(viewMode == VIEW_MODE_DAY ? View.GONE : View.VISIBLE);

        switch (viewMode)
        {
            case VIEW_MODE_DAY: this.currentFragment = CalendarDayFragment.newInstance(this.calendar); break;
            case VIEW_MODE_WEEK: this.currentFragment = CalendarWeekFragment.newInstance(this.calendar); break;
            case VIEW_MODE_MONTH: this.currentFragment = CalendarMonthFragment.newInstance(this.calendar); break;
        }
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, this.currentFragment)
                .commit();
    }

    //Called by the month view when a day is selected
    public Calendar setDayOnCalendar(int day)
    {
        this.calendar.set(Calendar.DAY_OF_MONTH, day);
        return this.calendar;
    }

    //Called by the day view when clicking the arrows
    public void addDaysToCalendar(int days)
    {
        this.calendar.add(Calendar.DAY_OF_YEAR, days);
        this.textViewYear.setText(String.valueOf(this.calendar.get(Calendar.YEAR)));
        this.textViewMonth.setText(SDF_MONTH.format(new Date(this.calendar.getTimeInMillis())));
        if(this.currentFragment != null)
            this.currentFragment.onCalendarChanged(this.calendar);
    }

    public Calendar addWeeksToCalendar(int weeks)
    {
        this.calendar.add(Calendar.WEEK_OF_YEAR, weeks);
        this.textViewYear.setText(String.valueOf(this.calendar.get(Calendar.YEAR)));
        this.textViewMonth.setText(SDF_MONTH.format(new Date(this.calendar.getTimeInMillis())));
        return this.calendar;
    }
}
