package smartassist.appreciate.be.smartassist.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.views.TextClock;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 4/03/2015.
 */
public class ScreenSaverTimeFragment extends Fragment
{
    private TextClock textClock;
    private float clockTextPortrait;
    private float clockTextLandscape;

    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("EEEE dd MMMM", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_screen_saver_time, container, false);

        this.textClock = (TextClock) view.findViewById(R.id.textClock);
        TextView textViewDate = (TextView) view.findViewById(R.id.textView_date);

        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        this.clockTextPortrait = this.getPreferredTextSizeClock(Math.min(size.x, size.y));
        this.clockTextLandscape = this.getPreferredTextSizeClock(Math.max(size.x, size.y));
        boolean landscape = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        this.textClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, landscape ? this.clockTextLandscape : this.clockTextPortrait);
        textViewDate.setText(SDF_DATE.format(new Date(System.currentTimeMillis())));

        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        boolean landscape = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        this.textClock.setTextSize(TypedValue.COMPLEX_UNIT_PX, landscape ? this.clockTextLandscape : this.clockTextPortrait);
    }

    private float getPreferredTextSizeClock(int screenWidth)
    {
        float textSize = this.getResources().getDimension(R.dimen.screen_saver_time_large_text);
        float padding = this.getResources().getDimension(R.dimen.screen_saver_time_padding);
        float totalExtraSpace = padding * 2;

        Paint paint = this.textClock.getPaint();
        String text = "10:00 AM";

        return this.measureText(text, paint, totalExtraSpace, screenWidth, textSize);
    }

    private float measureText(String text, Paint paint, float extraSpace, int screenWidth, float textSize)
    {
        paint.setTextSize(textSize);
        float titleSize = paint.measureText(text, 0, text.length());
        float totalLength = titleSize + extraSpace;

        if(totalLength < screenWidth)
            return textSize;
        else
            return this.measureText(text, paint, extraSpace, screenWidth, textSize - 8);
    }
}
