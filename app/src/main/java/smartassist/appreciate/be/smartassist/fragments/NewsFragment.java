package smartassist.appreciate.be.smartassist.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.CalendarActivity;
import smartassist.appreciate.be.smartassist.activities.NewsDetailActivity;
import smartassist.appreciate.be.smartassist.adapters.NewsAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarEventContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.NewsContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.WeatherContentProvider;
import smartassist.appreciate.be.smartassist.database.CalendarEventTable;
import smartassist.appreciate.be.smartassist.database.NewsTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.News;
import smartassist.appreciate.be.smartassist.model.Weather;
import smartassist.appreciate.be.smartassist.utils.CalendarUtils;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke on 27/01/2015.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NewsAdapter.OnNewsClickListener, SharedPreferences.OnSharedPreferenceChangeListener
{
    private NewsAdapter newsAdapter;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView recyclerViewNews = (RecyclerView) view.findViewById(R.id.recyclerView_news);

        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewNews.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        recyclerViewNews.addItemDecoration(decoration);
        this.newsAdapter = new NewsAdapter();
        this.newsAdapter.setListener(this);
        recyclerViewNews.setAdapter(this.newsAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_NEWS, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_WEATHER, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_CALENDAR_EVENTS, null, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (this.getActivity() != null && !this.getActivity().isFinishing())
        {
            PreferencesHelper.saveUnreadNews(this.getActivity(), 0);
            PreferencesHelper.getPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onPause()
    {
        if (this.getActivity() != null && !this.getActivity().isFinishing())
        {
            PreferencesHelper.getPreferences(this.getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        }
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_NEWS:
                String sortOrderNews = NewsTable.COLUMN_CREATION_DATE + " DESC";
                return new CursorLoader(this.getView().getContext(), NewsContentProvider.CONTENT_URI, null, null, null, sortOrderNews);

            case Constants.LOADER_WEATHER:
                return new CursorLoader(this.getView().getContext(), WeatherContentProvider.CONTENT_URI, null, null, null, null);

            case Constants.LOADER_CALENDAR_EVENTS:
                Calendar calendar = Calendar.getInstance();
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
                String sortOrder = CalendarEventTable.COLUMN_START + " ASC";
                return new CursorLoader(this.getView().getContext(), CalendarEventContentProvider.CONTENT_URI, null, whereClause, null, sortOrder);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_NEWS:
                this.newsAdapter.setNews(News.constructListFromCursor(data));
                break;

            case Constants.LOADER_WEATHER:
                this.newsAdapter.setWeatherItems(Weather.constructListFromCursor(data));
                break;

            case Constants.LOADER_CALENDAR_EVENTS:
                Calendar calendar = Calendar.getInstance();
                long start = calendar.getTimeInMillis();
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                long stop = calendar.getTimeInMillis();
                this.newsAdapter.setEvents(CalendarUtils.constructEventsFromCursor(data, start, stop));
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        this.adjustLayoutManager(newConfig.orientation);
        this.newsAdapter.notifyDataSetChanged();
    }

    private void adjustLayoutManager(int orientation)
    {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            this.layoutManager.setSpanCount(3);
            this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return (position % 10 == 4 || position % 10 == 8) ? 2 : 1;
                }
            });
        } else
        {
            this.layoutManager.setSpanCount(2);
            this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return (position % 3 == 2) ? 2 : 1;
                }
            });
        }
    }

    @Override
    public void onNewsClick(View caller, News news)
    {
        switch (caller.getId())
        {
            case R.id.button_more:
            default:
                Intent intent = new Intent(caller.getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.KEY_NEWS_ID, news.getId());
                this.startActivity(intent);
        }
    }

    @Override
    public void onCalendarClick(View caller)
    {
        Intent intent = new Intent(caller.getContext(), CalendarActivity.class);
        intent.putExtra(CalendarActivity.KEY_VIEW_MODE, CalendarActivity.VIEW_MODE_DAY);
        this.startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (PreferencesHelper.PREFERENCE_UNREAD_NEWS.equals(key) && this.getActivity() != null && !this.getActivity().isFinishing())
        {
            if (sharedPreferences.getInt(key, 0) != 0)
                PreferencesHelper.saveUnreadNews(this.getActivity(), 0);
        }
    }
}
