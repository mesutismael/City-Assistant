package smartassist.appreciate.be.smartassist.utils;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke on 24/02/2015.
 */
public class WeatherHelper
{
    public static final int COLOR_DARK = 0;
    public static final int COLOR_GRAY = 1;
    public static final int COLOR_WHITE = 2;

    private static final int CODE_STORM = 1;
    private static final int CODE_RAIN_CLOUD = 2;
    private static final int CODE_PARTLY_RAINY = 3;
    private static final int CODE_HAIL = 4;
    private static final int CODE_SNOW = 5;
    private static final int CODE_FOG = 6;
    private static final int CODE_TORNADO = 7;
    private static final int CODE_SUN = 8;
    private static final int CODE_CLOUD = 9;
    private static final int CODE_LOW_TEMP = 10;
    private static final int CODE_HIGH_TEMP = 11;
    private static final int CODE_WIND = 12;
    private static final int CODE_PARTLY_SUNNY = 13;

    public static int getWeatherIcon(int code, int color)
    {
        switch (code)
        {
            case CODE_STORM:
                return color == COLOR_DARK ? R.drawable.storm_dark : color == COLOR_GRAY ? R.drawable.storm_grey : R.drawable.storm_white;

            case CODE_RAIN_CLOUD:
                return color == COLOR_DARK ? R.drawable.rain_cloud_dark : color == COLOR_GRAY ? R.drawable.rain_cloud_grey : R.drawable.rain_cloud_white;

            case CODE_PARTLY_RAINY:
                return color == COLOR_DARK ? R.drawable.partly_rainy_dark : color == COLOR_GRAY ? R.drawable.partly_rainy_grey : R.drawable.partly_rainy_white;

            case CODE_HAIL:
                return color == COLOR_DARK ? R.drawable.hail_dark : color == COLOR_GRAY ? R.drawable.hail_grey : R.drawable.hail_white;

            case CODE_SNOW:
                return color == COLOR_DARK ? R.drawable.snow_dark : color == COLOR_GRAY ? R.drawable.snow_grey : R.drawable.snow_white;

            case CODE_FOG:
                return color == COLOR_DARK ? R.drawable.fog_dark : color == COLOR_GRAY ? R.drawable.fog_grey : R.drawable.fog_white;

            case CODE_TORNADO:
                return color == COLOR_DARK ? R.drawable.tornado_dark : color == COLOR_GRAY ? R.drawable.tornado_grey : R.drawable.tornado_white;

            case CODE_SUN:
                return color == COLOR_DARK ? R.drawable.sun_dark : color == COLOR_GRAY ? R.drawable.sun_grey : R.drawable.sun_white;

            case CODE_CLOUD:
                return color == COLOR_DARK ? R.drawable.cloud_dark : color == COLOR_GRAY ? R.drawable.cloud_grey : R.drawable.cloud_white;

            case CODE_LOW_TEMP:
                return color == COLOR_DARK ? R.drawable.lowtemp_dark : color == COLOR_GRAY ? R.drawable.lowtemp_grey : R.drawable.lowtemp_white;

            case CODE_HIGH_TEMP:
                return color == COLOR_DARK ? R.drawable.highttemp_dark : color == COLOR_GRAY ? R.drawable.highttemp_grey : R.drawable.highttemp_white;

            case CODE_WIND:
                return color == COLOR_DARK ? R.drawable.wind_dark : color == COLOR_GRAY ? R.drawable.wind_grey : R.drawable.wind_white;

            case CODE_PARTLY_SUNNY:
                return color == COLOR_DARK ? R.drawable.partly_sunny_dark : color == COLOR_GRAY ? R.drawable.partly_sunny_grey : R.drawable.partly_sunny_white;

            default:
                return color == COLOR_DARK ? R.drawable.partly_sunny_dark : color == COLOR_GRAY ? R.drawable.partly_sunny_grey : R.drawable.partly_sunny_white;
        }
    }

    public static int convertWeatherCode(int code)
    {
        if(code >= 200 && code <= 232)
        {
            return CODE_STORM;
        }
        else if((code >= 300 && code <= 321) || (code >= 520 && code <= 531))
        {
            return CODE_RAIN_CLOUD;
        }
        else if(code >= 500 && code <= 504)
        {
            return CODE_PARTLY_RAINY;
        }
        else if(code == 511 || code == 906)
        {
            return CODE_HAIL;
        }
        else if(code >= 600 && code <= 622)
        {
            return CODE_SNOW;
        }
        else if(code >= 701 && code <= 771)
        {
            return CODE_FOG;
        }
        else if(code == 781 || (code >= 900 && code <= 902) || (code >= 960 && code <= 962))
        {
            return CODE_TORNADO;
        }
        else if(code == 800 || code == 951)
        {
            return CODE_SUN;
        }
        else if(code == 801)
        {
            return CODE_PARTLY_SUNNY;
        }
        else if(code >= 802 && code <= 804)
        {
            return CODE_CLOUD;
        }
        else if(code == 903)
        {
            return CODE_LOW_TEMP;
        }
        else if(code == 904)
        {
            return CODE_HIGH_TEMP;
        }
        else if(code == 905 || (code >= 952 && code <= 959))
        {
            return CODE_WIND;
        }
        else
        {
            return CODE_PARTLY_SUNNY;
        }
    }
}
