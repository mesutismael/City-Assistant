package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.RssDetailFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;

/**
 * Created by Inneke on 25/02/2015.
 */
public class RssDetailActivity extends BaseActivity
{
    public static final String KEY_RSS_ID = RssDetailFragment.KEY_RSS_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_rss_detail);

        RssDetailFragment fragment = new RssDetailFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_RSS_DETAIL;
    }
}
