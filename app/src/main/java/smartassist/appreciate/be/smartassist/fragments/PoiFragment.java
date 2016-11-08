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
import smartassist.appreciate.be.smartassist.activities.PoiDetailActivity;
import smartassist.appreciate.be.smartassist.adapters.CategoryAdapter;
import smartassist.appreciate.be.smartassist.adapters.PoiAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.PoiCategoryContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.PoiContentProvider;
import smartassist.appreciate.be.smartassist.database.PoiCategoryTable;
import smartassist.appreciate.be.smartassist.database.PoiTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.Category;
import smartassist.appreciate.be.smartassist.model.Poi;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke on 28/01/2015.
 */
public class PoiFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, PoiAdapter.OnPoiClickListener, CategoryAdapter.OnCategoryClickListener
{
    public static final String KEY_POI_ID = "poi_id";
    private static final String KEY_WHERE_CLAUSE = "where_clause";
    private PoiAdapter poiAdapter;
    private CategoryAdapter categoryAdapter;
    private GridLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);

        RecyclerView recyclerViewPoi = (RecyclerView) view.findViewById(R.id.recyclerView_poi);
        RecyclerView recyclerViewCategories = (RecyclerView) view.findViewById(R.id.recyclerView_categories);
        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewPoi.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        decoration.setAddPaddingToTop(false);
        recyclerViewPoi.addItemDecoration(decoration);
        this.poiAdapter = new PoiAdapter();
        this.poiAdapter.setOnPoiClickListener(this);
        recyclerViewPoi.setAdapter(this.poiAdapter);

        PaddingDecoration decorationCategories = new PaddingDecoration(view.getContext());
        recyclerViewCategories.addItemDecoration(decorationCategories);
        this.categoryAdapter = new CategoryAdapter();
        this.categoryAdapter.setListener(this);
        recyclerViewCategories.setAdapter(this.categoryAdapter);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_POI_ITEMS, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_POI_CATEGORIES, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_POI_ITEMS:
                String whereClause = null;
                if(args != null && args.containsKey(KEY_WHERE_CLAUSE))
                    whereClause = args.getString(KEY_WHERE_CLAUSE);
                String sortOrderPoi = PoiTable.TABLE_NAME + "." + PoiTable.COLUMN_FAVORITE + " DESC ,"+PoiTable.TABLE_NAME + "." + PoiTable.COLUMN_DISTANCE + " ASC";
                return new CursorLoader(this.getView().getContext(), PoiContentProvider.CONTENT_URI, null, whereClause, null, sortOrderPoi);

            case Constants.LOADER_POI_CATEGORIES:
                String sortOrderCategory = PoiCategoryTable.COLUMN_IS_MAIN + " DESC";
                whereClause=PoiTable.TABLE_NAME + "." + PoiTable.COLUMN_NAME + "!='" + null + "'";
                return new CursorLoader(this.getView().getContext(), PoiCategoryContentProvider.CONTENT_URI, null, whereClause, null, sortOrderCategory);

            /*case Constants.LOADER_POI_ITEM:
                String whereClausePoi=null;
                if(args != null && args.containsKey(KEY_POI_ID))
                 whereClausePoi = PoiTable.COLUMN_POI_ID + "='" + args.getInt(KEY_POI_ID) + "'";
                return new CursorLoader(this.getView().getContext(), PoiContentProvider.CONTENT_URI, null, whereClausePoi, null, null);*/

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_POI_ITEMS:
                this.poiAdapter.setPoiItems(Poi.constructListFromCursor(data));
                break;

            case Constants.LOADER_POI_CATEGORIES:
                this.categoryAdapter.setCategories(Category.constructListFromCursor(data, Category.TYPE_POI));
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
        this.poiAdapter.notifyDataSetChanged();
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
    public void onPoiClick(View caller, Poi poi)
    {
        switch (caller.getId())
        {

            case R.id.button_more:
            default:
                Intent intent = new Intent(caller.getContext(), PoiDetailActivity.class);
                intent.putExtra(PoiDetailActivity.KEY_POI_ID, poi.getId());
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
            String whereClause = PoiTable.TABLE_NAME + "." + PoiTable.COLUMN_CATEGORY_ID + "='" + category.getId() + "'";
            bundle.putString(KEY_WHERE_CLAUSE, whereClause);
        }

        this.getLoaderManager().restartLoader(Constants.LOADER_POI_ITEMS, bundle, this);
    }



}
