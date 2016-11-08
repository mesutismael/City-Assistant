package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarEventContentProvider;
import smartassist.appreciate.be.smartassist.database.CalendarEventTable;
import smartassist.appreciate.be.smartassist.utils.CalendarUtils;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.CalendarWeekView;

/**
 * Created by Inneke on 16/02/2015.
 */
public class CalendarWeekFragment extends AbstractCalendarFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private CalendarWeekView weekView;
    private Calendar selectedDate;

    private static final String KEY_MILLIS = "millis";
    private static final String KEY_WHERE_CLAUSE = "where_clause";

    public static CalendarWeekFragment newInstance(Calendar calendar)
    {
        CalendarWeekFragment fragment = new CalendarWeekFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_MILLIS, calendar.getTimeInMillis());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calendar_week, container, false);

        this.weekView = (CalendarWeekView) view.findViewById(R.id.weekView);

        this.selectedDate = Calendar.getInstance();
        int currentHour = this.selectedDate.get(Calendar.HOUR_OF_DAY);
        this.selectedDate.setTimeInMillis(this.getArguments().getLong(KEY_MILLIS, System.currentTimeMillis()));
        this.weekView.setData(null, this.selectedDate);
        this.weekView.scrollToHour(currentHour);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        long millis = this.getArguments().getLong(KEY_MILLIS, System.currentTimeMillis());
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, this.createEventsWhereClause(millis), this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.weekView.updateCurrentTimeView();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String where = args.getString(KEY_WHERE_CLAUSE);
        String sortOrder = CalendarEventTable.COLUMN_START + " ASC";
        return new CursorLoader(this.getView().getContext(), CalendarEventContentProvider.CONTENT_URI, null, where, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(this.selectedDate.getTimeInMillis());
        MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), Calendar.MONDAY);
        int row = monthDisplayHelper.getRowOf(startDate.get(Calendar.DAY_OF_MONTH));
        int firstDay = monthDisplayHelper.getDayAt(row, 0);
        startDate.set(Calendar.DAY_OF_MONTH, firstDay);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        if(!monthDisplayHelper.isWithinCurrentMonth(row, 0))
            startDate.add(Calendar.MONTH, row == 0 ? -1 : 1);
        long startTime = startDate.getTimeInMillis();
        startDate.add(Calendar.DAY_OF_YEAR, 6);
        startDate.set(Calendar.HOUR_OF_DAY, 23);
        startDate.set(Calendar.MINUTE, 59);
        startDate.set(Calendar.SECOND, 59);
        long stopTime = startDate.getTimeInMillis();
        this.weekView.setData(CalendarUtils.constructEventsFromCursor(data, startTime, stopTime), this.selectedDate);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        this.weekView.setData(null, this.selectedDate);
    }

    @Override
    public void onCalendarChanged(Calendar calendar)
    {
        this.selectedDate = calendar;
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, this.createEventsWhereClause(this.selectedDate.getTimeInMillis()), this);
    }

    private Bundle createEventsWhereClause(long millis)
    {
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(millis);
        MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), Calendar.MONDAY);
        int row = monthDisplayHelper.getRowOf(startDate.get(Calendar.DAY_OF_MONTH));
        int firstDay = monthDisplayHelper.getDayAt(row, 0);
        startDate.set(Calendar.DAY_OF_MONTH, firstDay);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        if(!monthDisplayHelper.isWithinCurrentMonth(row, 0))
            startDate.add(Calendar.MONTH, row == 0 ? -1 : 1);
        long startTime = startDate.getTimeInMillis();
        startDate.add(Calendar.DAY_OF_YEAR, 6);
        startDate.set(Calendar.HOUR_OF_DAY, 23);
        startDate.set(Calendar.MINUTE, 59);
        startDate.set(Calendar.SECOND, 59);
        long stopTime = startDate.getTimeInMillis();

        String whereClauseNoRepeat =
                CalendarEventTable.COLUMN_REPEAT_CODE + "='" + CalendarUtils.REPEAT_NEVER + "' AND " +
                        CalendarEventTable.COLUMN_START + " < " + stopTime + " AND " +
                        CalendarEventTable.COLUMN_STOP + " > " + startTime;

        String whereClauseRepeat =
                CalendarEventTable.COLUMN_REPEAT_CODE + "!='" + CalendarUtils.REPEAT_NEVER + "' AND " +
                        CalendarEventTable.COLUMN_START + " < " + stopTime + " AND " +
                        CalendarEventTable.COLUMN_REPEAT_UNTIL + " > " + startTime;

        String whereClause = "(" + whereClauseNoRepeat + ") OR (" + whereClauseRepeat + ")";
        Bundle bundle = new Bundle();
        bundle.putString(KEY_WHERE_CLAUSE, whereClause);
        return bundle;
    }

    /*private Bundle createBirthdaysWhereClause(long millis)
    {
        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(millis);
        MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), Calendar.MONDAY);
        int row = monthDisplayHelper.getRowOf(startDate.get(Calendar.DAY_OF_MONTH));
        int firstDay = monthDisplayHelper.getDayAt(row, 0);
        startDate.set(Calendar.DAY_OF_MONTH, firstDay);
        if(!monthDisplayHelper.isWithinCurrentMonth(row, 0))
            startDate.add(Calendar.MONTH, row == 0 ? -1 : 1);
        int startMonth = startDate.get(Calendar.MONTH);
        int startDay = startDate.get(Calendar.DAY_OF_MONTH);
        startDate.add(Calendar.DAY_OF_YEAR, 6);
        int stopMonth = startDate.get(Calendar.MONTH);
        int stopDay = startDate.get(Calendar.DAY_OF_MONTH);

        String whereClause;
        if(startMonth == stopMonth)
            whereClause = CalendarBirthdayTable.COLUMN_MONTH + "=" + startMonth + " AND " + CalendarBirthdayTable.COLUMN_DAY + " BETWEEN " + startDay + " AND " + stopDay;
        else
            whereClause = "(" + CalendarBirthdayTable.COLUMN_MONTH + "=" + startMonth + " AND " + CalendarBirthdayTable.COLUMN_DAY + " BETWEEN " + startDay + " AND 31)"
                    + " OR (" + CalendarBirthdayTable.COLUMN_MONTH + "=" + stopMonth + " AND " + CalendarBirthdayTable.COLUMN_DAY + " BETWEEN 1 AND " + stopDay + ")";

        Bundle bundle = new Bundle();
        bundle.putString(KEY_WHERE_CLAUSE, whereClause);
        return bundle;
    }*/
}
