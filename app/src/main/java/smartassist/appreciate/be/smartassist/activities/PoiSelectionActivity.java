package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.PoiSelectionFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by banada ismael on 31/10/2016.
 */
public class PoiSelectionActivity extends BaseActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_poi_selection);

        TextView textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        textViewTitle.setText(R.string.module_poi);

        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new PoiSelectionFragment())
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
        return TrackHelper.SCREEN_POI_SELECTION;
    }
}