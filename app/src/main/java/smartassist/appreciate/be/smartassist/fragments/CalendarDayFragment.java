package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.CalendarActivity;
import smartassist.appreciate.be.smartassist.adapters.CalendarDayEntryAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarBirthdayContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarEventContentProvider;
import smartassist.appreciate.be.smartassist.database.CalendarBirthdayTable;
import smartassist.appreciate.be.smartassist.database.CalendarEventTable;
import smartassist.appreciate.be.smartassist.model.Birthday;
import smartassist.appreciate.be.smartassist.utils.CalendarUtils;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 16/02/2015.
 */
public class CalendarDayFragment extends AbstractCalendarFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private TextView textViewDay;
    private TextView textViewMonth;
    private TextView textViewDayOfWeek;
    private CalendarDayEntryAdapter adapter;
    private Calendar selectedDate;

    private static final SimpleDateFormat SDF_MONTH = new SimpleDateFormat("MMMM", Locale.getDefault());
    private static final SimpleDateFormat SDF_DAY_OF_WEEK = new SimpleDateFormat("EEEE", Locale.getDefault());
    private static final String KEY_MILLISECONDS = "milliseconds";
    private static final String KEY_WHERE_CLAUSE = "where_clause";

    public static CalendarDayFragment newInstance(Calendar calendar)
    {
        CalendarDayFragment fragment = new CalendarDayFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_MILLISECONDS, calendar.getTimeInMillis());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar_day, container, false);

        this.textViewDay = (TextView) view.findViewById(R.id.textView_day);
        this.textViewMonth = (TextView) view.findViewById(R.id.textView_month);
        this.textViewDayOfWeek = (TextView) view.findViewById(R.id.textView_dayOfWeek);
        ImageView imageViewPrevious = (ImageView) view.findViewById(R.id.imageView_previousDay);
        ImageView imageViewNext = (ImageView) view.findViewById(R.id.imageView_nextDay);
        RecyclerView recyclerViewDayEntries = (RecyclerView) view.findViewById(R.id.recyclerView_dayEntries);

        imageViewPrevious.setOnClickListener(this);
        imageViewNext.setOnClickListener(this);

        long millis = this.getArguments().getLong(KEY_MILLISECONDS);
        this.selectedDate = Calendar.getInstance();
        this.selectedDate.setTimeInMillis(millis);
        this.textViewDay.setText(String.valueOf(this.selectedDate.get(Calendar.DAY_OF_MONTH)));
        this.textViewMonth.setText(SDF_MONTH.format(new Date(millis)));
        this.textViewDayOfWeek.setText(SDF_DAY_OF_WEEK.format(new Date(millis)));

        recyclerViewDayEntries.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.adapter = new CalendarDayEntryAdapter();
        this.adapter.setSelectedDate(this.selectedDate);
        recyclerViewDayEntries.setAdapter(this.adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        long millis = this.getArguments().getLong(KEY_MILLISECONDS, System.currentTimeMillis());
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, this.createEventsWhereClause(millis), this);
        this.getLoaderManager().restartLoader(Constants.LOADER_BIRTHDAYS, this.createBirthdaysWhereClause(millis), this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_CALENDAR_EVENTS:
                String eventsWhere = args.getString(KEY_WHERE_CLAUSE);
                String sortOrder = CalendarEventTable.COLUMN_START + " ASC";
                return new CursorLoader(this.getView().getContext(), CalendarEventContentProvider.CONTENT_URI, null, eventsWhere, null, sortOrder);

            case Constants.LOADER_BIRTHDAYS:
                String birthdaysWhere = args.getString(KEY_WHERE_CLAUSE);
                return new CursorLoader(this.getView().getContext(), CalendarBirthdayContentProvider.CONTENT_URI, null, birthdaysWhere, null, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_CALENDAR_EVENTS:
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(this.selectedDate.getTimeInMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                long start = calendar.getTimeInMillis();
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                long stop = calendar.getTimeInMillis();
                this.adapter.setEvents(CalendarUtils.constructEventsFromCursor(data, start, stop));
                break;

            case Constants.LOADER_BIRTHDAYS:
                this.adapter.setBirthdays(Birthday.constructListFromCursor(data));
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageView_previousDay:
                ((CalendarActivity) this.getActivity()).addDaysToCalendar(-1);
                break;

            case R.id.imageView_nextDay:
                ((CalendarActivity) this.getActivity()).addDaysToCalendar(1);
                break;
        }
    }

    @Override
    public void onCalendarChanged(Calendar calendar)
    {
        this.selectedDate = calendar;
        this.adapter.setSelectedDate(this.selectedDate);
        long millis = this.selectedDate.getTimeInMillis();
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, this.createEventsWhereClause(millis), this);
        this.getLoaderManager().restartLoader(Constants.LOADER_BIRTHDAYS, this.createBirthdaysWhereClause(millis), this);

        this.textViewDay.setText(String.valueOf(this.selectedDate.get(Calendar.DAY_OF_MONTH)));
        this.textViewMonth.setText(SDF_MONTH.format(new Date(millis)));
        this.textViewDayOfWeek.setText(SDF_DAY_OF_WEEK.format(new Date(millis)));
    }

    private Bundle createEventsWhereClause(long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long stop = calendar.getTimeInMillis();

        String whereClauseNoRepeat =
                CalendarEventTable.COLUMN_REPEAT_CODE + "='" + CalendarUtils.REPEAT_NEVER + "' AND " +
                CalendarEventTable.COLUMN_START + " < " + stop + " AND " +
                CalendarEventTable.COLUMN_STOP + " > " + start;

        String whereClauseRepeat =
                CalendarEventTable.COLUMN_REPEAT_CODE + "!='" + CalendarUtils.REPEAT_NEVER + "' AND " +
                CalendarEventTable.COLUMN_START + " < " + stop + " AND " +
                CalendarEventTable.COLUMN_REPEAT_UNTIL + " > " + start;

        String whereClause = "(" + whereClauseNoRepeat + ") OR (" + whereClauseRepeat + ")";
        Bundle bundle = new Bundle();
        bundle.putString(KEY_WHERE_CLAUSE, whereClause);
        return bundle;
    }

    private Bundle createBirthdaysWhereClause(long milliseconds)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String whereClause = CalendarBirthdayTable.COLUMN_MONTH + "=" + month + " AND " + CalendarBirthdayTable.COLUMN_DAY + "=" + day;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_WHERE_CLAUSE, whereClause);
        return bundle;
    }
}
