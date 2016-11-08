package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.CalendarActivity;
import smartassist.appreciate.be.smartassist.adapters.CalendarMonthDayEntryAdapter;
import smartassist.appreciate.be.smartassist.adapters.CalendarMonthEntryAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarBirthdayContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarEventContentProvider;
import smartassist.appreciate.be.smartassist.database.CalendarBirthdayTable;
import smartassist.appreciate.be.smartassist.database.CalendarEventTable;
import smartassist.appreciate.be.smartassist.decorations.DividerDecoration;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.Birthday;
import smartassist.appreciate.be.smartassist.utils.CalendarUtils;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.DisplayUtils;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 28/01/2015.
 */
public class CalendarMonthFragment extends AbstractCalendarFragment implements CalendarMonthEntryAdapter.OnDayClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private TextView textViewDay;
    private TextView textViewDayOfWeek;
    private CalendarMonthEntryAdapter monthEntryAdapter;
    private CalendarMonthDayEntryAdapter monthDayEntryAdapter;
    private Calendar selectedDate;

    private static final SimpleDateFormat SDF_DAY_OF_WEEK = new SimpleDateFormat("EEEE", Locale.getDefault());
    private static final String KEY_MILLIS = "millis";
    private static final String KEY_WHERE_CLAUSE = "where_clause";

    public static CalendarMonthFragment newInstance(Calendar calendar)
    {
        CalendarMonthFragment fragment = new CalendarMonthFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_MILLIS, calendar.getTimeInMillis());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar_month, container, false);

        this.textViewDay = (TextView) view.findViewById(R.id.textView_day);
        this.textViewDayOfWeek = (TextView) view.findViewById(R.id.textView_dayOfWeek);
        RecyclerView recyclerViewMonthEntries = (RecyclerView) view.findViewById(R.id.recyclerView_monthEntries);
        RecyclerView recyclerViewDayEntries = (RecyclerView) view.findViewById(R.id.recyclerView_montDayEntries);

        DividerDecoration monthDividerDecoration = new DividerDecoration(view.getContext());
        monthDividerDecoration.setColorRes(R.color.calendar_day_list_divider);
        recyclerViewMonthEntries.addItemDecoration(monthDividerDecoration);
        int recommendedRowHeight = DisplayUtils.getRecommendedCalendarMonthRowHeight(view.getContext(), view.getContext().getResources().getConfiguration().orientation, R.dimen.list_divider_height);
        this.selectedDate = Calendar.getInstance();
        this.selectedDate.setTimeInMillis(this.getArguments().getLong(KEY_MILLIS, System.currentTimeMillis()));
        this.monthEntryAdapter = new CalendarMonthEntryAdapter(this.selectedDate, recommendedRowHeight);
        this.monthEntryAdapter.setListener(this);
        recyclerViewMonthEntries.setLayoutManager(new GridLayoutManager(view.getContext(), 7));
        recyclerViewMonthEntries.setAdapter(this.monthEntryAdapter);
        recyclerViewMonthEntries.setItemAnimator(new DefaultItemAnimator());

        PaddingDecoration paddingDecoration = new PaddingDecoration(view.getContext());
        paddingDecoration.setPaddingRes(R.dimen.calendar_day_item_padding, 0);
        paddingDecoration.setDoubleSpaceBetweenItems(true);
        recyclerViewDayEntries.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DividerDecoration dividerDecoration = new DividerDecoration(view.getContext());
        dividerDecoration.setColorRes(R.color.calendar_month_day_list_divider);
        recyclerViewDayEntries.addItemDecoration(paddingDecoration);
        recyclerViewDayEntries.addItemDecoration(dividerDecoration);
        this.monthDayEntryAdapter = new CalendarMonthDayEntryAdapter();
        this.monthDayEntryAdapter.setSelectedDate(this.selectedDate);
        recyclerViewDayEntries.setAdapter(this.monthDayEntryAdapter);
        recyclerViewDayEntries.setItemAnimator(new DefaultItemAnimator());

        this.textViewDay.setText(String.valueOf(this.selectedDate.get(Calendar.DAY_OF_MONTH)));
        this.textViewDayOfWeek.setText(SDF_DAY_OF_WEEK.format(new Date(this.selectedDate.getTimeInMillis())));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        long millis = this.getArguments().getLong(KEY_MILLIS, System.currentTimeMillis());
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS_MONTH, this.createMonthEventsWhereClause(millis), this);
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, this.createEventsWhereClause(millis), this);
        this.getLoaderManager().restartLoader(Constants.LOADER_BIRTHDAYS, this.createBirthdaysWhereClause(millis), this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_CALENDAR_EVENTS_MONTH:
                String eventsMonthWhere = args.getString(KEY_WHERE_CLAUSE);
                String eventsMonthSortOrder = CalendarEventTable.COLUMN_START + " ASC";
                return new CursorLoader(this.getView().getContext(), CalendarEventContentProvider.CONTENT_URI, null, eventsMonthWhere, null, eventsMonthSortOrder);

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
            case Constants.LOADER_CALENDAR_EVENTS_MONTH:
                Calendar calendarMonth = Calendar.getInstance();
                calendarMonth.setTimeInMillis(this.selectedDate.getTimeInMillis());
                calendarMonth.set(Calendar.DAY_OF_MONTH, 1);
                calendarMonth.set(Calendar.HOUR_OF_DAY, 0);
                calendarMonth.set(Calendar.MINUTE, 0);
                calendarMonth.set(Calendar.SECOND, 0);
                long startMonth = calendarMonth.getTimeInMillis();
                calendarMonth.set(Calendar.DAY_OF_MONTH, calendarMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendarMonth.set(Calendar.HOUR_OF_DAY, 23);
                calendarMonth.set(Calendar.MINUTE, 59);
                calendarMonth.set(Calendar.SECOND, 59);
                long stopMonth = calendarMonth.getTimeInMillis();
                this.monthEntryAdapter.setEvents(CalendarUtils.constructEventsFromCursor(data, startMonth, stopMonth));
                break;

            case Constants.LOADER_CALENDAR_EVENTS:
                Calendar calendarDay = Calendar.getInstance();
                calendarDay.setTimeInMillis(this.selectedDate.getTimeInMillis());
                calendarDay.set(Calendar.HOUR_OF_DAY, 0);
                calendarDay.set(Calendar.MINUTE, 0);
                calendarDay.set(Calendar.SECOND, 0);
                long startDay = calendarDay.getTimeInMillis();
                calendarDay.set(Calendar.HOUR_OF_DAY, 23);
                calendarDay.set(Calendar.MINUTE, 59);
                calendarDay.set(Calendar.SECOND, 59);
                long stopDay = calendarDay.getTimeInMillis();
                this.monthDayEntryAdapter.setEvents(CalendarUtils.constructEventsFromCursor(data, startDay, stopDay));
                break;

            case Constants.LOADER_BIRTHDAYS:
                this.monthDayEntryAdapter.setBirthdays(Birthday.constructListFromCursor(data));
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onDayClick(View caller, int day)
    {
        if(day != -1)
        {
            this.monthEntryAdapter.setSelectedDay(day);
            Calendar calendar = ((CalendarActivity) this.getActivity()).setDayOnCalendar(day);
            this.selectedDate.setTimeInMillis(calendar.getTimeInMillis());
            this.monthDayEntryAdapter.setSelectedDate(this.selectedDate);
            this.textViewDay.setText(String.valueOf(this.selectedDate.get(Calendar.DAY_OF_MONTH)));
            this.textViewDayOfWeek.setText(SDF_DAY_OF_WEEK.format(new Date(this.selectedDate.getTimeInMillis())));
            this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, this.createEventsWhereClause(this.selectedDate.getTimeInMillis()), this);
            this.getLoaderManager().restartLoader(Constants.LOADER_BIRTHDAYS, this.createBirthdaysWhereClause(this.selectedDate.getTimeInMillis()), this);
        }
    }

    @Override
    public void onCalendarChanged(Calendar calendar)
    {
        this.selectedDate.setTimeInMillis(calendar.getTimeInMillis());
        this.monthDayEntryAdapter.setSelectedDate(this.selectedDate);
        this.monthEntryAdapter.setCalendar(this.selectedDate);
        this.monthEntryAdapter.notifyDataSetChanged();
        this.textViewDay.setText(String.valueOf(this.selectedDate.get(Calendar.DAY_OF_MONTH)));
        this.textViewDayOfWeek.setText(SDF_DAY_OF_WEEK.format(new Date(this.selectedDate.getTimeInMillis())));
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS_MONTH, this.createMonthEventsWhereClause(this.selectedDate.getTimeInMillis()), this);
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, this.createEventsWhereClause(this.selectedDate.getTimeInMillis()), this);
        this.getLoaderManager().restartLoader(Constants.LOADER_BIRTHDAYS, this.createBirthdaysWhereClause(this.selectedDate.getTimeInMillis()), this);
    }

    private Bundle createMonthEventsWhereClause(long millis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long start = calendar.getTimeInMillis();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
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
