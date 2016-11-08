package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.contentproviders.PhotoContentProvider;
import smartassist.appreciate.be.smartassist.database.PhotoTable;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke on 17/03/2015.
 */
public class ScreenSaverPhotoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private ImageView imageViewPhoto1;
    private ImageView imageViewPhoto2;
    private int currentIndex;
    private Cursor cursor;
    private Animation fadeOut;
    private Animation fadeIn;
    private Handler handler;
    private boolean resumed;
    private boolean view1Visible;

    private static final int ANIMATION_DURATION = 1000;
    private static final int PHOTO_DURATION = 20000;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.currentIndex = -1;
        this.resumed = false;
        this.view1Visible = true;

        this.fadeOut = new AlphaAnimation(1, 0);
        this.fadeOut.setDuration(ANIMATION_DURATION);
        this.fadeOut.setFillAfter(true);
        this.fadeOut.setInterpolator(new LinearInterpolator());
        this.fadeOut.setAnimationListener(this.animationListener);

        this.fadeIn = new AlphaAnimation(0, 1);
        this.fadeIn.setDuration(ANIMATION_DURATION);
        this.fadeIn.setFillAfter(true);
        this.fadeIn.setInterpolator(new LinearInterpolator());
        this.fadeIn.setAnimationListener(this.animationListener);

        this.handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_screen_saver_photo, container, false);

        this.imageViewPhoto1 = (ImageView) view.findViewById(R.id.imageView_photo1);
        this.imageViewPhoto2 = (ImageView) view.findViewById(R.id.imageView_photo2);

        this.imageViewPhoto2.setVisibility(this.view1Visible ? View.INVISIBLE : View.VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_PHOTOS, null, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        this.resumed = true;
        this.startCrossFadeAnimation();
    }

    @Override
    public void onPause()
    {
        this.resumed = false;
        this.handler.removeCallbacks(this.photoWaitCallback);
        this.imageViewPhoto2.clearAnimation();
        this.imageViewPhoto2.setVisibility(this.view1Visible ? View.GONE : View.VISIBLE);
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new CursorLoader(this.getView().getContext(), PhotoContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if(this.currentIndex == -1 && data != null && data.getCount() > 0)
            this.currentIndex = (int) (Math.random() * data.getCount());
        this.handler.removeCallbacks(this.photoWaitCallback);
        this.imageViewPhoto2.clearAnimation();
        this.imageViewPhoto2.setVisibility(View.GONE);
        this.cursor = data;
        this.startCrossFadeAnimation();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        this.cursor = null;
    }

    private void startCrossFadeAnimation()
    {
        if(this.resumed && this.cursor != null && this.cursor.getCount() > 0 && this.currentIndex >= 0)
        {
            int cursorCount = this.cursor.getCount();
            if(this.currentIndex >= cursorCount)
                this.currentIndex = this.currentIndex % cursorCount;

            if(this.cursor.moveToPosition(this.currentIndex))
            {
                String photoUrl = this.cursor.getString(this.cursor.getColumnIndex(PhotoTable.COLUMN_IMAGE));

                if(!TextUtils.isEmpty(photoUrl) && this.getActivity() != null && !this.getActivity().isFinishing())
                    Picasso.with(this.getActivity())
                            .load(photoUrl)
                            .noFade()
                            .into(this.view1Visible ? this.imageViewPhoto2 : this.imageViewPhoto1, this.picassoCallback);
            }
        }
    }

    private void startTimer()
    {
        if(this.resumed)
            this.handler.postDelayed(this.photoWaitCallback, PHOTO_DURATION);
    }

    private Callback picassoCallback = new Callback()
    {
        @Override
        public void onSuccess()
        {
            if(ScreenSaverPhotoFragment.this.resumed)
                ScreenSaverPhotoFragment.this.imageViewPhoto2.startAnimation(ScreenSaverPhotoFragment.this.view1Visible ? ScreenSaverPhotoFragment.this.fadeIn : ScreenSaverPhotoFragment.this.fadeOut);
        }

        @Override
        public void onError()
        {
            ScreenSaverPhotoFragment.this.currentIndex++;
            ScreenSaverPhotoFragment.this.startCrossFadeAnimation();
        }
    };

    private Animation.AnimationListener animationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation)
        {
            ScreenSaverPhotoFragment.this.imageViewPhoto2.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            if(ScreenSaverPhotoFragment.this.resumed)
            {
                ScreenSaverPhotoFragment.this.currentIndex++;
                ScreenSaverPhotoFragment.this.view1Visible = !ScreenSaverPhotoFragment.this.view1Visible;
                if(ScreenSaverPhotoFragment.this.view1Visible)
                    ScreenSaverPhotoFragment.this.imageViewPhoto2.setVisibility(ScreenSaverPhotoFragment.this.view1Visible ? View.GONE : View.VISIBLE);
                ScreenSaverPhotoFragment.this.startTimer();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation)
        {
        }
    };

    private Runnable photoWaitCallback = new Runnable()
    {
        @Override
        public void run()
        {
            ScreenSaverPhotoFragment.this.startCrossFadeAnimation();
        }
    };
}
