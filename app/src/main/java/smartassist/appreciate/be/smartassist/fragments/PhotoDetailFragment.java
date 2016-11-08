package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.adapters.PhotoPagerAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.PhotoContentProvider;
import smartassist.appreciate.be.smartassist.database.PhotoTable;
import smartassist.appreciate.be.smartassist.model.Photo;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke on 3/03/2015.
 */
public class PhotoDetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private PhotoPagerAdapter adapter;
    private ViewPager viewPager;
    private boolean movedToPosition;

    public static final String KEY_PHOTO_POSITION = "photo_id";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        this.viewPager = (ViewPager) view.findViewById(R.id.viewPager_photos);
        final ImageView imageViewPrevious = (ImageView) view.findViewById(R.id.imageView_previous);
        final ImageView imageViewNext = (ImageView) view.findViewById(R.id.imageView_next);

        this.adapter = new PhotoPagerAdapter();
        this.viewPager.setAdapter(this.adapter);
        this.viewPager.setOffscreenPageLimit(2);

        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i2)
            {
            }

            @Override
            public void onPageSelected(int i)
            {
                imageViewPrevious.setVisibility(i == 0 ? View.GONE : View.VISIBLE);
                imageViewNext.setVisibility(i == PhotoDetailFragment.this.adapter.getCount() - 1 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {
            }
        });

        imageViewPrevious.setOnClickListener(this);
        imageViewNext.setOnClickListener(this);

        this.movedToPosition = false;

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_PHOTOS, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String whereClause = PhotoTable.COLUMN_SCREEN_SAVER + "=0";
        return new CursorLoader(this.getView().getContext(), PhotoContentProvider.CONTENT_URI, null, whereClause, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        List<Photo> photos = Photo.constructListFromCursor(data);
        this.adapter.setPhotos(photos);

        if(!this.movedToPosition && photos != null && photos.size() > 0)
        {
            this.viewPager.setCurrentItem(this.getArguments().getInt(KEY_PHOTO_POSITION, 0), false);
            this.movedToPosition = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onClick(View v)
    {
        int currentPage = this.viewPager.getCurrentItem();

        switch (v.getId())
        {
            case R.id.imageView_previous:
                if(currentPage > 0)
                    this.viewPager.setCurrentItem(currentPage - 1, true);
                break;

            case R.id.imageView_next:
                if(currentPage < this.adapter.getCount() - 1)
                    this.viewPager.setCurrentItem(currentPage + 1, true);
                break;
        }
    }
}
