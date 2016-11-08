package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.NewsFragment;
import smartassist.appreciate.be.smartassist.fragments.OpenFileFragment;
import smartassist.appreciate.be.smartassist.fragments.ResidenceNewsDetailFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by banada ismael on 26/10/2016.
 */
public class OpenFileActivity extends BaseActivity implements View.OnClickListener
{
    public static final String KEY_FILE_ATTACHMENT = OpenFileFragment.KEY_FILE_ATTACHMENT;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_news);

        TextView textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        textViewTitle.setText(R.string.module_open_file);
        OpenFileFragment fragment = new OpenFileFragment();
        fragment.setArguments(this.getIntent().getExtras());

        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.layout_back:
                this.startParentActivity();
                break;
        }
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_NEWS;
    }
}
