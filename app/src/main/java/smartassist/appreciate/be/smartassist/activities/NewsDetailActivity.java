package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.NewsDetailFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;

/**
 * Created by Inneke on 24/02/2015.
 */
public class NewsDetailActivity extends BaseActivity
{
    public static final String KEY_NEWS_ID = NewsDetailFragment.KEY_NEWS_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_news_detail);

        NewsDetailFragment fragment = new NewsDetailFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_NEWS_DETAIL;
    }
}
