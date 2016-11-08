package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.NearByActivitiesDetailsFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;

/**
 * Created by banada ismael on 04/11/2016.
 */
public class NearByActivitiesDetailsActivity extends BaseActivity
{
    public static final String KEY_ACTIVITY_ID = NearByActivitiesDetailsFragment.KEY_ACTIVITY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_activities_details);

        NearByActivitiesDetailsFragment fragment = new NearByActivitiesDetailsFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_NEARBY_ACTIVITIES_DETAIL;
    }
}
