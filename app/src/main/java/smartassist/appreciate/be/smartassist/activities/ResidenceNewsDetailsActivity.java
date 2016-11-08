package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.ResidenceNewsDetailFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;

/**
 * Created by Banada ismael on 24/10/2016.
 */
public class ResidenceNewsDetailsActivity extends BaseActivity
{
    public static final String KEY_RESIDENCE_ID = ResidenceNewsDetailFragment.KEY_RESIDENCE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_resident_news_detail);

        ResidenceNewsDetailFragment fragment = new ResidenceNewsDetailFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_RESIDENT_INFO_DETAIL;
    }
}
