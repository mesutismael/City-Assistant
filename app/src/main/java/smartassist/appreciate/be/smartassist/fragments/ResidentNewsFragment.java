package smartassist.appreciate.be.smartassist.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import smartassist.appreciate.be.smartassist.activities.OpenFileActivity;
import smartassist.appreciate.be.smartassist.activities.ResidenceNewsDetailsActivity;
import smartassist.appreciate.be.smartassist.adapters.ResidentNewsAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.ResidentNewsContentProvider;
import smartassist.appreciate.be.smartassist.database.ResidentNewsTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.ResidentNewsItem;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Banada ismael on 25/10/2016.
 */
public class ResidentNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ResidentNewsAdapter.OnNewsClickListener, SharedPreferences.OnSharedPreferenceChangeListener
{
    private ResidentNewsAdapter newsAdapter;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView recyclerViewNews = (RecyclerView) view.findViewById(R.id.recyclerView_news);

        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewNews.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        recyclerViewNews.addItemDecoration(decoration);
        this.newsAdapter = new ResidentNewsAdapter();
        this.newsAdapter.setListener(this);
        recyclerViewNews.setAdapter(this.newsAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_NEWS, null, this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (this.getActivity() != null && !this.getActivity().isFinishing())
        {
            PreferencesHelper.saveUnreadNews(this.getActivity(), 0);
            PreferencesHelper.getPreferences(this.getActivity()).registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onPause()
    {
        if (this.getActivity() != null && !this.getActivity().isFinishing())
        {
            PreferencesHelper.getPreferences(this.getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        }
        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_NEWS:
                String sortOrderNews = ResidentNewsTable.COLUMN_CREATION_DATE + " DESC";
                return new CursorLoader(this.getView().getContext(), ResidentNewsContentProvider.CONTENT_URI, null, null, null, sortOrderNews);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_NEWS:
                this.newsAdapter.setNews(ResidentNewsItem.constructListFromCursor(data));
                break;

        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        this.adjustLayoutManager(newConfig.orientation);
        this.newsAdapter.notifyDataSetChanged();
    }

    private void adjustLayoutManager(int orientation)
    {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            this.layoutManager.setSpanCount(3);
            this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return (position % 10 == 4 || position % 10 == 8) ? 2 : 1;
                }
            });
        } else
        {
            this.layoutManager.setSpanCount(2);
            this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return (position % 3 == 2) ? 2 : 1;
                }
            });
        }
    }

    @Override
    public void onNewsClick(View caller, ResidentNewsItem residentNewsItem)
    {
        switch (caller.getId())
        {
            case R.id.button_fileDownload:
                    Intent i = new Intent(caller.getContext(), OpenFileActivity.class);
                    i.putExtra(OpenFileActivity.KEY_FILE_ATTACHMENT, residentNewsItem.getFile());
                    this.startActivity(i);
                break;

            case R.id.button_more:
            default:
                Intent intent = new Intent(caller.getContext(), ResidenceNewsDetailsActivity.class);
                intent.putExtra(ResidenceNewsDetailsActivity.KEY_RESIDENCE_ID, residentNewsItem.getResidence_id());
                this.startActivity(intent);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (PreferencesHelper.PREFERENCE_UNREAD_NEWS.equals(key) && this.getActivity() != null && !this.getActivity().isFinishing())
        {
            if (sharedPreferences.getInt(key, 0) != 0)
                PreferencesHelper.saveUnreadNews(this.getActivity(), 0);
        }
    }
}
