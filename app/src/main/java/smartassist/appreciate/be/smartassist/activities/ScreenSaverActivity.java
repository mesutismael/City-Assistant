package smartassist.appreciate.be.smartassist.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.ScreenSaverPhotoFragment;
import smartassist.appreciate.be.smartassist.fragments.ScreenSaverTimeFragment;
import smartassist.appreciate.be.smartassist.fragments.ScreenSaverWeatherFragment;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
import smartassist.appreciate.be.smartassist.views.TextClock;

/**
 * Created by Inneke on 4/03/2015.
 */
public class ScreenSaverActivity extends BaseScreenSaverActivity implements View.OnClickListener
{
    private ImageView imageViewTime;
    private ImageView imageViewWeather;
    private ImageView imageViewPhotos;
    private TextClock textClock;

    private int currentType;

    public static final int TYPE_TIME = 0;
    public static final int TYPE_WEATHER = 1;
    public static final int TYPE_PHOTOS = 2;

    private static final int REQUEST_PIN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_screen_saver);

        this.imageViewTime = (ImageView) this.findViewById(R.id.imageView_time);
        this.imageViewWeather = (ImageView) this.findViewById(R.id.imageView_weather);
        this.imageViewPhotos = (ImageView) this.findViewById(R.id.imageView_photos);
        this.textClock = (TextClock) this.findViewById(R.id.textClock);
        View viewTouch = this.findViewById(R.id.view_screenSaver);

        this.imageViewTime.setOnClickListener(this);
        this.imageViewWeather.setOnClickListener(this);
        this.imageViewPhotos.setOnClickListener(this);
        viewTouch.setOnClickListener(this);

        this.currentType = PreferencesHelper.getScreenSaverMode(this);
        this.switchFragment(this.currentType);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageView_time:
                if(this.currentType != TYPE_TIME)
                {
                    this.currentType = TYPE_TIME;
                    PreferencesHelper.saveScreenSaverMode(this, this.currentType);
                    this.switchFragment(this.currentType);
                }
                break;

            case R.id.imageView_weather:
                if(this.currentType != TYPE_WEATHER)
                {
                    this.currentType = TYPE_WEATHER;
                    PreferencesHelper.saveScreenSaverMode(this, this.currentType);
                    this.switchFragment(this.currentType);
                }
                break;

            case R.id.imageView_photos:
                if(this.currentType != TYPE_PHOTOS)
                {
                    this.currentType = TYPE_PHOTOS;
                    PreferencesHelper.saveScreenSaverMode(this, this.currentType);
                    this.switchFragment(this.currentType);
                }
                break;

            case R.id.view_screenSaver:
                if(PreferencesHelper.isPinEnabled(this))
                {
                    Intent intent = new Intent(this, PinUnlockActivity.class);
                    this.startActivityForResult(intent, REQUEST_PIN);
                }
                else
                {
                    this.exitScreenSaver();
                }
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        if(PreferencesHelper.isPinEnabled(this))
        {
            Intent intent = new Intent(this, PinUnlockActivity.class);
            this.startActivityForResult(intent, REQUEST_PIN);
        }
        else
        {
            this.exitScreenSaver();
        }
    }

    private void switchFragment(int type)
    {
        Fragment fragment;

        switch (type)
        {
            case TYPE_TIME:
                fragment = new ScreenSaverTimeFragment();
                break;
            case TYPE_WEATHER:
                fragment = new ScreenSaverWeatherFragment();
                break;
            case TYPE_PHOTOS:
                fragment = new ScreenSaverPhotoFragment();
                break;
            default:
                fragment = new ScreenSaverTimeFragment();
        }

        this.imageViewTime.setImageAlpha(type == TYPE_TIME ? 0xFF : 0x33);//20% transparency
        this.imageViewWeather.setImageAlpha(type == TYPE_WEATHER ? 0xFF : 0x33);//20% transparency
        this.imageViewPhotos.setImageAlpha(type == TYPE_PHOTOS ? 0xFF : 0x33);//20% transparency
        this.textClock.setVisibility(type == TYPE_TIME ? View.GONE : View.VISIBLE);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_PIN && resultCode == Activity.RESULT_OK && data != null && data.getBooleanExtra(PinUnlockActivity.KEY_PIN_SUCCESS, false))
        {
            int newsId = data.getIntExtra(PinUnlockActivity.KEY_NEWS_ID, 0);
            if(newsId == 0)
                this.exitScreenSaver();
            else
                this.startNewsActivity(newsId);
        }
    }

    private void exitScreenSaver()
    {
        if(this.isTaskRoot())
        {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
        this.finish();
    }

    private void startNewsActivity(int newsId)
    {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.KEY_NEWS_ID, newsId);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void goToNewsDetail(int newsId)
    {
        if(PreferencesHelper.isPinEnabled(this))
        {
            Intent intent = new Intent(this, PinUnlockActivity.class);
            intent.putExtra(PinUnlockActivity.KEY_NEWS_ID, newsId);
            this.startActivityForResult(intent, REQUEST_PIN);
        }
        else
        {
            this.startNewsActivity(newsId);
        }
    }
}
