package smartassist.appreciate.be.smartassist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.PinUnlockFragment;

/**
 * Created by Inneke on 19/03/2015.
 */
public class PinUnlockActivity extends AppCompatActivity
{
    public static final String KEY_PIN_SUCCESS = PinUnlockFragment.KEY_PIN_SUCCESS;
    public static final String KEY_NEWS_ID = PinUnlockFragment.KEY_NEWS_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_pin_unlock);

        PinUnlockFragment fragment = new PinUnlockFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void sendResult(boolean success, int newsId)
    {
        Intent intent = new Intent();
        intent.putExtra(KEY_PIN_SUCCESS, success);
        intent.putExtra(KEY_NEWS_ID, newsId);
        this.setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    @Override
    public void onBackPressed()
    {
        this.sendResult(false, 0);
    }

}
