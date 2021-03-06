package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 1/08/2016.
 */
public class VitalsActivity extends BaseActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_vitals);

        TextView textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        textViewTitle.setText(R.string.module_vitals);

        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        //TODO add the VitalsFragment
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
        return TrackHelper.SCREEN_VITALS;
    }
}
