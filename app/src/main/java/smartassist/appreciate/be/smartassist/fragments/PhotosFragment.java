package smartassist.appreciate.be.smartassist.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.PhotoDetailActivity;
import smartassist.appreciate.be.smartassist.adapters.PhotoThumbnailAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.PhotoContentProvider;
import smartassist.appreciate.be.smartassist.database.PhotoTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.Photo;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.DisplayUtils;

/**
 * Created by Inneke on 28/01/2015.
 */
public class PhotosFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, PhotoThumbnailAdapter.OnThumbnailClickListener
{
    private GridLayoutManager layoutManager;
    private PhotoThumbnailAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        RecyclerView recyclerViewPhotos = (RecyclerView) view.findViewById(R.id.recyclerView_photos);

        this.layoutManager = new GridLayoutManager(view.getContext(), DisplayUtils.getSpanCountPhotos(view.getContext()));
        recyclerViewPhotos.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        recyclerViewPhotos.addItemDecoration(decoration);
        this.adapter = new PhotoThumbnailAdapter();
        this.adapter.setListener(this);
        recyclerViewPhotos.setAdapter(this.adapter);

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
        this.adapter.setPhotos(Photo.constructListFromCursor(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        int spanCount;
        if (this.getActivity() != null && !this.getActivity().isFinishing())
            spanCount = DisplayUtils.getSpanCountPhotos(this.getActivity());
        else
            spanCount = 5;
        this.layoutManager.setSpanCount(spanCount);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onThumbnailClick(View caller, int position)
    {
        Intent intent = new Intent(caller.getContext(), PhotoDetailActivity.class);
        intent.putExtra(PhotoDetailActivity.KEY_PHOTO_POSITION, position);
        this.startActivity(intent);
    }


}
