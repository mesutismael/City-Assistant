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

import java.util.Calendar;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.adapters.MedicationAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.MedicationContentProvider;
import smartassist.appreciate.be.smartassist.database.MedicationTable;
import smartassist.appreciate.be.smartassist.decorations.DividerDecoration;
import smartassist.appreciate.be.smartassist.model.Medication;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke De Clippel on 12/04/2016.
 */
public class MedicationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private MedicationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_medication, container, false);

        RecyclerView recyclerViewMedication = (RecyclerView) view.findViewById(R.id.recyclerView_medication);

        this.adapter = new MedicationAdapter();
        recyclerViewMedication.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewMedication.setAdapter(this.adapter);
        DividerDecoration decoration = new DividerDecoration(view.getContext());
        decoration.setColorRes(R.color.medication_divider);
        decoration.setBottomDivider(true);
        recyclerViewMedication.addItemDecoration(decoration);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_MEDICATION, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_MEDICATION:
                String sortOrder = MedicationTable.COLUMN_NAME;
                return new CursorLoader(this.getView().getContext(), MedicationContentProvider.CONTENT_URI, null, this.getMedicationSelection(), null, sortOrder);

            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_MEDICATION:
                this.adapter.setMedications(Medication.constructListFromCursor(data));
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    private String getMedicationSelection()
    {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = ((today.get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1;
        String afterStart = MedicationTable.COLUMN_START_DATE + " <= " + today.getTimeInMillis();
        String beforeEnd = MedicationTable.COLUMN_END_DATE + " = 0 OR " + MedicationTable.COLUMN_END_DATE + " >= " + today.getTimeInMillis();
        String forToday = MedicationTable.COLUMN_DAYS + " LIKE '%" + String.valueOf(dayOfWeek) + "%'";
        return forToday + " AND " + afterStart + " AND (" + beforeEnd + ")";
    }
}
