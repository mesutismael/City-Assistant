package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.WeatherTable;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.utils.WeatherHelper;

/**
 * Created by Inneke on 23/02/2015.
 */
public class WeatherItem
{
    @SerializedName("current_temp")
    private String currentTemp;
    private String low;
    private String high;
    private int code;
    private String date;

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();

        cv.put(WeatherTable.COLUMN_CURRENT_TEMP, this.currentTemp);
        cv.put(WeatherTable.COLUMN_HIGH, this.high);
        cv.put(WeatherTable.COLUMN_LOW, this.low);
        cv.put(WeatherTable.COLUMN_DATE, DateUtils.parseApiDateWeather(this.date));
        cv.put(WeatherTable.COLUMN_CODE, WeatherHelper.convertWeatherCode(this.code));
        cv.put(WeatherTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
