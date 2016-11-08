package smartassist.appreciate.be.smartassist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import smartassist.appreciate.be.smartassist.fragments.MainFragment;
import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.services.ChatService;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;

/**
 * Created by Inneke on 26/01/2015.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, new MainFragment(), MainFragment.TAG)
                    .commit();
        }

        ImageView buttonSettings = (ImageView) this.findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(this);

        Intent chatServiceIntent = new Intent(this, ChatService.class);
        this.startService(chatServiceIntent);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_settings:
                Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_MAIN;
    }
}
