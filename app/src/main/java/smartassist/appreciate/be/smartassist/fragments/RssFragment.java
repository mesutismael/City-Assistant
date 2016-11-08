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
import smartassist.appreciate.be.smartassist.activities.RssDetailActivity;
import smartassist.appreciate.be.smartassist.adapters.RssAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.RssContentProvider;
import smartassist.appreciate.be.smartassist.database.RssTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.Rss;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke on 25/02/2015.
 */
public class RssFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, RssAdapter.OnRssClickListener
{
    private RssAdapter rssAdapter;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        RecyclerView recyclerViewRss = (RecyclerView) view.findViewById(R.id.recyclerView_rss);

        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewRss.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        recyclerViewRss.addItemDecoration(decoration);
        this.rssAdapter = new RssAdapter();
        this.rssAdapter.setListener(this);
        recyclerViewRss.setAdapter(this.rssAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_RSS, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String sortOrder = RssTable.COLUMN_DATE + " DESC";
        return new CursorLoader(this.getView().getContext(), RssContentProvider.CONTENT_URI, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        this.rssAdapter.setRss(Rss.constructListFromCursor(data));
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
        this.rssAdapter.notifyDataSetChanged();
    }

    private void adjustLayoutManager(int orientation)
    {
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)
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
        }
        else
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
    public void onRssClick(View caller, Rss rss)
    {
        switch (caller.getId())
        {
            case R.id.button_more:
            default:
                Intent intent = new Intent(caller.getContext(), RssDetailActivity.class);
                intent.putExtra(RssDetailActivity.KEY_RSS_ID, rss.getId());
                this.startActivity(intent);
        }
    }

}
