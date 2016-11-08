package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.adapters.CareBookAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.CareBookContentProvider;
import smartassist.appreciate.be.smartassist.database.CareBookTable;
import smartassist.appreciate.be.smartassist.model.CarebookItem;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke De Clippel on 12/02/2016.
 */
public class CareBookFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private CareBookAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_care_book, container, false);

        RecyclerView recyclerViewCareBook = (RecyclerView) view.findViewById(R.id.recyclerView_careBook);

        this.adapter = new CareBookAdapter();
        recyclerViewCareBook.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewCareBook.setAdapter(this.adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_CARE_BOOK, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_CARE_BOOK:
                String sortOrder = CareBookTable.COLUMN_CREATED_AT + " DESC";
                return new CursorLoader(this.getView().getContext(), CareBookContentProvider.CONTENT_URI, null, null, null, sortOrder);

            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_CARE_BOOK:
                this.adapter.setCarebookItems(CarebookItem.constructListFromCursor(data));
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }
}
