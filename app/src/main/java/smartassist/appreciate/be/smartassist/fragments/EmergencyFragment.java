package smartassist.appreciate.be.smartassist.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sinch.android.rtc.calling.Call;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.BaseActivity;
import smartassist.appreciate.be.smartassist.activities.CallActivity;
import smartassist.appreciate.be.smartassist.adapters.CategoryAdapter;
import smartassist.appreciate.be.smartassist.adapters.EmergencyAdapter;
import smartassist.appreciate.be.smartassist.asynctasks.QrCodeAsyncTask;
import smartassist.appreciate.be.smartassist.contentproviders.EmergencyCategoryContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.EmergencyContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.PoiContentProvider;
import smartassist.appreciate.be.smartassist.database.EmergencyCategoryTable;
import smartassist.appreciate.be.smartassist.database.EmergencyTable;
import smartassist.appreciate.be.smartassist.database.PoiTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.Category;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.model.Poi;
import smartassist.appreciate.be.smartassist.services.SinchService;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke on 28/01/2015.
 */
public class EmergencyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener,
        CategoryAdapter.OnCategoryClickListener, EmergencyAdapter.OnEmergencyClickListener, QrCodeAsyncTask.QrCodeAsyncTaskListener
{
    private EmergencyAdapter emergencyAdapter;
    private CategoryAdapter categoryAdapter;
    private GridLayoutManager layoutManager;
    private RoundedImageView imageViewQr;
    private MaterialDialog dialogQr;

    private static final String KEY_WHERE_CLAUSE = "where_clause";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_emergency, container, false);

        RecyclerView recyclerViewEmergency = (RecyclerView) view.findViewById(R.id.recyclerView_emergency);
        RecyclerView recyclerViewCategories = (RecyclerView) view.findViewById(R.id.recyclerView_categories);

        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewEmergency.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        decoration.setAddPaddingToTop(false);
        recyclerViewEmergency.addItemDecoration(decoration);
        this.emergencyAdapter = new EmergencyAdapter();
        this.emergencyAdapter.setListener(this);
        recyclerViewEmergency.setAdapter(this.emergencyAdapter);

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
        this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY_DOCTOR, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY_PHARMACY, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY_CATEGORIES, null, this);

        PreferencesHelper.getPreferences(view.getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView()
    {
        PreferencesHelper.getPreferences(this.getView().getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onPause()
    {
        if(this.dialogQr != null)
            this.dialogQr.dismiss();

        super.onPause();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_EMERGENCY:
                String sortOrder = EmergencyTable.COLUMN_NAME_FULL + " ASC";
                return new CursorLoader(this.getView().getContext(), EmergencyContentProvider.CONTENT_URI, null, null, null, sortOrder);

            case Constants.LOADER_EMERGENCY_FILTERED:
                String whereClause = args != null && args.containsKey(KEY_WHERE_CLAUSE) ? args.getString(KEY_WHERE_CLAUSE) : null;
                String sortOrderFilter = EmergencyTable.COLUMN_NAME_FULL + " ASC";
                return new CursorLoader(this.getView().getContext(), EmergencyContentProvider.CONTENT_URI, null, whereClause, null, sortOrderFilter);

            case Constants.LOADER_EMERGENCY_DOCTOR:
                int emergencyDoctorId = PreferencesHelper.getEmergencyDoctorId(this.getView().getContext());
                String whereClauseDoctor = PoiTable.COLUMN_POI_ID + "='" + emergencyDoctorId + "'";
                return new CursorLoader(this.getView().getContext(), PoiContentProvider.CONTENT_URI, null, whereClauseDoctor, null, null);

            case Constants.LOADER_EMERGENCY_PHARMACY:
                int emergencyPharmacyId = PreferencesHelper.getEmergencyPharmacyId(this.getView().getContext());
                String whereClausePharmacy = PoiTable.COLUMN_POI_ID + "='" + emergencyPharmacyId + "'";
                return new CursorLoader(this.getView().getContext(), PoiContentProvider.CONTENT_URI, null, whereClausePharmacy, null, null);

            case Constants.LOADER_EMERGENCY_CATEGORIES:
                String whereClauseCategories = EmergencyCategoryTable.COLUMN_SHOW_TAB + "='1'";
                return new CursorLoader(this.getView().getContext(), EmergencyCategoryContentProvider.CONTENT_URI, null, whereClauseCategories, null, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_EMERGENCY:
                this.emergencyAdapter.setEmergencyContacts(EmergencyContact.constructListFromCursor(data), true);
                break;

            case Constants.LOADER_EMERGENCY_FILTERED:
                this.emergencyAdapter.setEmergencyContacts(EmergencyContact.constructListFromCursor(data), false);
                break;

            case Constants.LOADER_EMERGENCY_DOCTOR:
                this.emergencyAdapter.setDoctors(Poi.constructListFromCursor(data));
                break;

            case Constants.LOADER_EMERGENCY_PHARMACY:
                this.emergencyAdapter.setPharmacies(Poi.constructListFromCursor(data));
                break;

            case Constants.LOADER_EMERGENCY_CATEGORIES:
                this.categoryAdapter.setCategories(Category.constructListFromCursor(data, Category.TYPE_EMERGENCY));
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
        this.emergencyAdapter.notifyDataSetChanged();
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(PreferencesHelper.PREFERENCE_EMERGENCY_DOCTOR_ID.equals(key) && this.getView() != null)
        {
            this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY_DOCTOR, null, this);
        }
        else if(PreferencesHelper.PREFERENCE_EMERGENCY_PHARMACY_ID.equals(key) && this.getView() != null)
        {
            this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY_PHARMACY, null, this);
        }
    }

    @Override
    public void onCategoryClick(View caller, Category category)
    {
        if(category != null)
        {
            Bundle bundle = new Bundle();
            String whereClause = EmergencyTable.TABLE_NAME + "." + EmergencyTable.COLUMN_CATEGORY_ID + "='" + category.getId() + "'";
            bundle.putString(KEY_WHERE_CLAUSE, whereClause);
            this.getLoaderManager().destroyLoader(Constants.LOADER_EMERGENCY);
            this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY_FILTERED, bundle, this);
        }
        else
        {
            this.getLoaderManager().destroyLoader(Constants.LOADER_EMERGENCY_FILTERED);
            this.getLoaderManager().restartLoader(Constants.LOADER_EMERGENCY, null, this);
        }
    }

    @Override
    public void onEmergencyClick(View caller, EmergencyContact contact)
    {
        switch (caller.getId())
        {
            case R.id.imageView_qr:
                this.showQrCode(caller.getContext(), contact.getHash());
                break;

            case R.id.imageView_call:
                if(this.getActivity() != null && this.getActivity() instanceof BaseActivity)
                {
                    SinchService.SinchServiceInterface sinch = ((BaseActivity) this.getActivity()).getSinchServiceInterface();
                    if(sinch != null)
                    {
                        Call call = sinch.callUserAudio(contact.getHash());

                        if(call != null)
                        {
                            Intent intent = new Intent(caller.getContext(), CallActivity.class);
                            intent.putExtra(CallActivity.KEY_CALL_ID, call.getCallId());
                            intent.putExtra(CallActivity.KEY_CALL_NAME, contact.getName());
                            intent.putExtra(CallActivity.KEY_CALL_PHOTO, CategoryHelper.getEmergencyContactPhoto(contact, false));
                            intent.putExtra(CallActivity.KEY_OUTGOING_CALL, true);
                            this.startActivity(intent);
                        }
                    }
                }
                break;

            case R.id.imageView_videocall:
                if(this.getActivity() != null && this.getActivity() instanceof BaseActivity)
                {
                    SinchService.SinchServiceInterface sinch = ((BaseActivity) this.getActivity()).getSinchServiceInterface();
                    if(sinch != null)
                    {
                        Call call = sinch.callUserVideo(contact.getHash());

                        if(call != null)
                        {
                            Intent intent = new Intent(caller.getContext(), CallActivity.class);
                            intent.putExtra(CallActivity.KEY_CALL_ID, call.getCallId());
                            intent.putExtra(CallActivity.KEY_CALL_NAME, contact.getName());
                            intent.putExtra(CallActivity.KEY_CALL_PHOTO, CategoryHelper.getEmergencyContactPhoto(contact, false));
                            intent.putExtra(CallActivity.KEY_OUTGOING_CALL, true);
                            this.startActivity(intent);
                        }
                    }
                }
                break;
        }
    }

    private void showQrCode(Context context, String hash)
    {
        this.imageViewQr = new RoundedImageView(context);
        this.imageViewQr.setCornerRadiusDimen(R.dimen.main_tile_corners);

        this.dialogQr = new MaterialDialog.Builder(context)
                .title(R.string.emergency_qr_title)
                .customView(this.imageViewQr, true)
                .positiveText(R.string.emergency_qr_positive)
                .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf")
                .show();

        QrCodeAsyncTask task = new QrCodeAsyncTask(hash, context.getResources().getDimensionPixelSize(R.dimen.contacts_qr));
        task.setListener(this);
        task.execute();
    }

    @Override
    public void onQrCodeCreated(Bitmap qrCode)
    {
        if(qrCode != null && this.imageViewQr != null)
        {
            this.imageViewQr.setImageBitmap(qrCode);
        }
    }
}
