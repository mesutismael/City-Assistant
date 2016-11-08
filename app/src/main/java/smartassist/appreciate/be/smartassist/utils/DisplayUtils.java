package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke on 17/02/2015.
 */
public class DisplayUtils
{
    public static int getDisplayWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getDisplayHeight(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private static int getApproximateCalendarMonthHeightLandscape(Context context)
    {
        int displayHeight = getDisplayHeight(context);
        int topBarHeight = (int) context.getResources().getDimension(R.dimen.top_bar_height);
        int toggleMinHeight = (int) context.getResources().getDimension(R.dimen.calendar_toggle_min_height);
        int togglePadding = (int) context.getResources().getDimension(R.dimen.calendar_toggle_padding);
        int calendarBarPadding = (int) context.getResources().getDimension(R.dimen.fragment_padding_top_bottom);
        int calendarLayoutDivider = (int) context.getResources().getDimension(R.dimen.calendar_layout_divider_height);
        int calendarMonthPadding = (int) context.getResources().getDimension(R.dimen.calendar_month_padding);

        return displayHeight - topBarHeight - toggleMinHeight - (2 * togglePadding) - (2 * calendarBarPadding) - calendarLayoutDivider - (2 * calendarMonthPadding);
    }

    private static int getApproximateCalendarMonthHeightPortrait(Context context)
    {
        int displayHeight = getDisplayHeight(context);
        int topBarHeight = (int) context.getResources().getDimension(R.dimen.top_bar_height);
        int toggleMinHeight = (int) context.getResources().getDimension(R.dimen.calendar_toggle_min_height);
        int togglePadding = (int) context.getResources().getDimension(R.dimen.calendar_toggle_padding);
        int calendarBarPadding = (int) context.getResources().getDimension(R.dimen.fragment_padding_top_bottom);
        int calendarLayoutDivider = (int) context.getResources().getDimension(R.dimen.calendar_layout_divider_height);

        int contentHeight = displayHeight - topBarHeight - toggleMinHeight - (2 * togglePadding) - (2 * calendarBarPadding) - calendarLayoutDivider;
        int layoutHeight = contentHeight * 3 / 5;
        int calendarMonthPadding = (int) context.getResources().getDimension(R.dimen.calendar_month_padding);

        return layoutHeight - (2 * calendarMonthPadding);
    }

    public static int getRecommendedCalendarMonthRowHeight(Context context, int orientation, int dividerHeightResId)
    {
        int availableSpace = orientation == Configuration.ORIENTATION_LANDSCAPE ? getApproximateCalendarMonthHeightLandscape(context) : getApproximateCalendarMonthHeightPortrait(context);
        int dividerHeight = dividerHeightResId != 0 ? context.getResources().getDimensionPixelOffset(dividerHeightResId) : 0;
        int totalDividerHeight = dividerHeight * 6; //7 rows means 6 dividers
        int minRowHeight = (int) context.getResources().getDimension(R.dimen.calendar_month_entry_min_height);
        int maxRowHeight = (int) context.getResources().getDimension(R.dimen.calendar_month_entry_max_height);
        int calculatedRowHeight = (availableSpace - totalDividerHeight) / 7; //1 row for the header and 6 rows for the days
        return Math.min(Math.max(minRowHeight, calculatedRowHeight), maxRowHeight);
    }

    public static int getApproximateCalendarWeekDayWidth(Context context)
    {
        int displayWidth = getDisplayWidth(context);
        int hourWidth = (int) context.getResources().getDimension(R.dimen.week_view_hours_width);
        int padding = (int) context.getResources().getDimension(R.dimen.fragment_padding_left_right);
        int numberOfDays = 7;
        return (displayWidth - hourWidth - 2 * padding) / numberOfDays;
    }

    public static int getSpanCountPhotos(Context context)
    {
        int displayWidth = getDisplayWidth(context);
        int paddingFragmentHorizontal = (int) context.getResources().getDimension(R.dimen.list_padding_horizontal_outer);
        int itemWidth = (int) context.getResources().getDimension(R.dimen.photos_item);
        return (displayWidth - 2 * paddingFragmentHorizontal) / itemWidth;
    }
}
