package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.PoiDetailFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;

/**
 * Created by Inneke on 11/02/2015.
 */
public class PoiDetailActivity extends BaseActivity
{
    public static final String KEY_POI_ID = PoiDetailFragment.KEY_POI_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_poi_detail);

        PoiDetailFragment fragment = new PoiDetailFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_POI_DETAIL;
    }
}
