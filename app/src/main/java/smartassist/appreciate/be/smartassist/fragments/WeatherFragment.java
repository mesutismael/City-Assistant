package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.contentproviders.WeatherContentProvider;
import smartassist.appreciate.be.smartassist.database.WeatherTable;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.WeatherHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 28/01/2015.
 */
public class WeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private ImageView imageViewBackground;
    private TextView textViewCurrentTemp;
    private TextView textViewTemp1;
    private TextView textViewTemp2;
    private TextView textViewTemp3;
    private TextView textViewTemp4;
    private TextView textViewDay1;
    private TextView textViewDay2;
    private TextView textViewDay3;
    private TextView textViewDay4;
    private ImageView imageViewWeather1;
    private ImageView imageViewWeather2;
    private ImageView imageViewWeather3;
    private ImageView imageViewWeather4;

    private static final SimpleDateFormat SDF_DAY_OF_WEEK = new SimpleDateFormat("EE", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        this.imageViewBackground = (ImageView) view.findViewById(R.id.imageView_weather);
        this.textViewCurrentTemp = (TextView) view.findViewById(R.id.textView_currentTemp);
        this.textViewTemp1 = (TextView) view.findViewById(R.id.textView_temp1);
        this.textViewTemp2 = (TextView) view.findViewById(R.id.textView_temp2);
        this.textViewTemp3 = (TextView) view.findViewById(R.id.textView_temp3);
        this.textViewTemp4 = (TextView) view.findViewById(R.id.textView_temp4);
        this.textViewDay1 = (TextView) view.findViewById(R.id.textView_day1);
        this.textViewDay2 = (TextView) view.findViewById(R.id.textView_day2);
        this.textViewDay3 = (TextView) view.findViewById(R.id.textView_day3);
        this.textViewDay4 = (TextView) view.findViewById(R.id.textView_day4);
        this.imageViewWeather1 = (ImageView) view.findViewById(R.id.imageView_icon1);
        this.imageViewWeather2 = (ImageView) view.findViewById(R.id.imageView_icon2);
        this.imageViewWeather3 = (ImageView) view.findViewById(R.id.imageView_icon3);
        this.imageViewWeather4 = (ImageView) view.findViewById(R.id.imageView_icon4);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_WEATHER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this.getView().getContext(), WeatherContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        Calendar day = Calendar.getInstance();
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        day2.add(Calendar.DAY_OF_YEAR, 1);
        Calendar day3 = Calendar.getInstance();
        day3.add(Calendar.DAY_OF_YEAR, 2);
        Calendar day4 = Calendar.getInstance();
        day4.add(Calendar.DAY_OF_YEAR, 3);

        this.textViewDay1.setText(SDF_DAY_OF_WEEK.format(new Date(day1.getTimeInMillis())).toUpperCase());
        this.textViewDay2.setText(SDF_DAY_OF_WEEK.format(new Date(day2.getTimeInMillis())).toUpperCase());
        this.textViewDay3.setText(SDF_DAY_OF_WEEK.format(new Date(day3.getTimeInMillis())).toUpperCase());
        this.textViewDay4.setText(SDF_DAY_OF_WEEK.format(new Date(day4.getTimeInMillis())).toUpperCase());

        if(data != null)
        {
            data.moveToFirst();
            while (!data.isAfterLast())
            {
                day.setTimeInMillis(data.getLong(data.getColumnIndex(WeatherTable.COLUMN_DATE)));
                if (day.get(Calendar.DAY_OF_YEAR) == day1.get(Calendar.DAY_OF_YEAR) && day.get(Calendar.YEAR) == day1.get(Calendar.YEAR))
                {
                    String currentTemp = data.getString(data.getColumnIndex(WeatherTable.COLUMN_CURRENT_TEMP));
                    if(!TextUtils.isEmpty(currentTemp))
                        this.textViewCurrentTemp.setText(currentTemp + "°C");
                    this.imageViewWeather1.setImageResource(WeatherHelper.getWeatherIcon(data.getInt(data.getColumnIndex(WeatherTable.COLUMN_CODE)), WeatherHelper.COLOR_WHITE));
                    this.textViewTemp1.setText(data.getString(data.getColumnIndex(WeatherTable.COLUMN_HIGH)) + "°C");
                } else if (day.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR) && day.get(Calendar.YEAR) == day2.get(Calendar.YEAR))
                {
                    this.imageViewWeather2.setImageResource(WeatherHelper.getWeatherIcon(data.getInt(data.getColumnIndex(WeatherTable.COLUMN_CODE)), WeatherHelper.COLOR_GRAY));
                    this.textViewTemp2.setText(data.getString(data.getColumnIndex(WeatherTable.COLUMN_HIGH)) + "°C");
                } else if (day.get(Calendar.DAY_OF_YEAR) == day3.get(Calendar.DAY_OF_YEAR) && day.get(Calendar.YEAR) == day3.get(Calendar.YEAR))
                {
                    this.imageViewWeather3.setImageResource(WeatherHelper.getWeatherIcon(data.getInt(data.getColumnIndex(WeatherTable.COLUMN_CODE)), WeatherHelper.COLOR_GRAY));
                    this.textViewTemp3.setText(data.getString(data.getColumnIndex(WeatherTable.COLUMN_HIGH)) + "°C");
                } else if (day.get(Calendar.DAY_OF_YEAR) == day4.get(Calendar.DAY_OF_YEAR) && day.get(Calendar.YEAR) == day4.get(Calendar.YEAR))
                {
                    this.imageViewWeather4.setImageResource(WeatherHelper.getWeatherIcon(data.getInt(data.getColumnIndex(WeatherTable.COLUMN_CODE)), WeatherHelper.COLOR_GRAY));
                    this.textViewTemp4.setText(data.getString(data.getColumnIndex(WeatherTable.COLUMN_HIGH)) + "°C");
                }
                data.moveToNext();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }
}
