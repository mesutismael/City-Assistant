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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.NearByActivitiesDetailsActivity;
import smartassist.appreciate.be.smartassist.adapters.CategoryAdapter;
import smartassist.appreciate.be.smartassist.adapters.NearByActivitiesAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.ActivitiesContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ActivityTypesContentProvider;
import smartassist.appreciate.be.smartassist.database.ActivityTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.ActivityItem;
import smartassist.appreciate.be.smartassist.model.Category;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by banada ismael on 01/11/2016.
 */
public class NearByActivitiesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, NearByActivitiesAdapter.OnActivityClickListener, CategoryAdapter.OnCategoryClickListener
{
    private static final String KEY_WHERE_CLAUSE = "where_clause";
    private NearByActivitiesAdapter nearByActivitiesAdapter;
    private CategoryAdapter categoryAdapter;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        RecyclerView recyclerViewActivities = (RecyclerView) view.findViewById(R.id.recyclerView_activities);
        RecyclerView recyclerViewTypes = (RecyclerView) view.findViewById(R.id.recyclerView_activity_types);
        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewActivities.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        decoration.setAddPaddingToTop(false);
        recyclerViewActivities.addItemDecoration(decoration);
        this.nearByActivitiesAdapter = new NearByActivitiesAdapter();
        this.nearByActivitiesAdapter.setOnActivityClickListener(this);
        recyclerViewActivities.setAdapter(this.nearByActivitiesAdapter);

        PaddingDecoration decorationCategories = new PaddingDecoration(view.getContext());
        recyclerViewTypes.addItemDecoration(decorationCategories);
        this.categoryAdapter = new CategoryAdapter();
        this.categoryAdapter.setListener(this);
        recyclerViewTypes.setAdapter(this.categoryAdapter);
        recyclerViewTypes.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_ACTIVITIES, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_ACTIVITY_TYPES, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_ACTIVITIES:
                String whereClause = null;
                if(args != null && args.containsKey(KEY_WHERE_CLAUSE))
                    whereClause = args.getString(KEY_WHERE_CLAUSE);
                String sortOrderActivities = ActivityTable.TABLE_NAME + "." + ActivityTable.COLUMN_CREATED_AT + " ASC";
                return new CursorLoader(this.getView().getContext(), ActivitiesContentProvider.CONTENT_URI, null, whereClause, null, sortOrderActivities);

            case Constants.LOADER_ACTIVITY_TYPES:
                whereClause=ActivityTable.TABLE_NAME + "." + ActivityTable.COLUMN_NAME + " != '" + null + "'";
                return new CursorLoader(this.getView().getContext(), ActivityTypesContentProvider.CONTENT_URI, null, whereClause, null, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_ACTIVITIES:
                this.nearByActivitiesAdapter.setActivityItems(ActivityItem.constructListFromCursor(data));
                break;

            case Constants.LOADER_ACTIVITY_TYPES:
                this.categoryAdapter.setCategories(Category.constructListFromCursor(data, Category.TYPE_ACTIVITY));
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
        this.nearByActivitiesAdapter.notifyDataSetChanged();
    }

    private void adjustLayoutManager(int orientation)
    {
        //This layout shows more columns
        /*if(orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            this.layoutManager.setSpanCount(4);
            this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    return (position % 6 == 4 || position % 6 == 5) ? 2 : 1;
                }
            });
        }
        else
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
        }*/

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
    public void onActivityClick(View caller, ActivityItem activityItem)
    {
        switch (caller.getId())
        {

            case R.id.button_more:
            default:
                Intent intent = new Intent(caller.getContext(), NearByActivitiesDetailsActivity.class);
                intent.putExtra(NearByActivitiesDetailsActivity.KEY_ACTIVITY_ID, activityItem.getId());
                this.startActivity(intent);




        }
    }

    @Override
    public void onCategoryClick(View caller, Category category)
    {
        Bundle bundle = null;

        if(category != null)
        {
            bundle = new Bundle();
            String whereClause = ActivityTable.TABLE_NAME + "." + ActivityTable.COLUMN_ACTIVITY_TYPE_ID + " = '" + category.getId() + "'";
            bundle.putString(KEY_WHERE_CLAUSE, whereClause);
        }

        this.getLoaderManager().restartLoader(Constants.LOADER_ACTIVITIES, bundle, this);
    }



}
