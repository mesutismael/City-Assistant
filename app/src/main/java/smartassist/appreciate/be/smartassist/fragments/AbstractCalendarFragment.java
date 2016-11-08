package smartassist.appreciate.be.smartassist.fragments;

import android.support.v4.app.Fragment;

import java.util.Calendar;

/**
 * Created by Inneke on 18/02/2015.
 */
public abstract class AbstractCalendarFragment extends Fragment
{
    public abstract void onCalendarChanged(Calendar calendar);
}
