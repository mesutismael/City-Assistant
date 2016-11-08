package smartassist.appreciate.be.smartassist.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.BaseActivity;
import smartassist.appreciate.be.smartassist.adapters.OpeningTimesAdapter;
import smartassist.appreciate.be.smartassist.adapters.PoiAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.OpeningTimesContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.PoiContentProvider;
import smartassist.appreciate.be.smartassist.database.OpeningTimesTable;
import smartassist.appreciate.be.smartassist.database.PoiTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.OpeningTime;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.TextView;
import smartassist.appreciate.be.smartassist.views.ToggleButton;

/**
 * Created by Inneke on 11/02/2015.
 */
public class PoiDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener
{
    private ImageView imageViewCategory;
    private TextView textViewTitle;
    private TextView textViewDescriptionTitle;
    private String image;
    private TextView TextViewDescription;
    private TextView textViewAddress;
    private TextView textViewPhoneTitle;
    private TextView textViewPhone;
    private TextView textViewOpeningtimesTitle;
    private ImageView imageViewFavaourite;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private IconFactory iconFactory;
    private CameraUpdate cameraUpdate;
    private MarkerOptions markerOptions;
    private Cursor cursorPoi;
    private OpeningTimesAdapter adapter;
    private int poiId;
    private int favorite;

    public static final String KEY_POI_ID = "poi_id";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_poi_detail, container, false);

        this.imageViewCategory = (ImageView) view.findViewById(R.id.imageView_category);
        this.textViewTitle = (TextView) view.findViewById(R.id.textView_title);
        this.textViewDescriptionTitle = (TextView) view.findViewById(R.id.textView_description_title);
        this.TextViewDescription= (TextView) view.findViewById(R.id.textView_description);
        this.textViewAddress = (TextView) view.findViewById(R.id.textView_address);
        this.textViewPhoneTitle = (TextView) view.findViewById(R.id.textView_phone_title);
        this.textViewPhone = (TextView) view.findViewById(R.id.textView_phone);
        this.textViewOpeningtimesTitle = (TextView) view.findViewById(R.id.textView_openingTimesTitle);
        this.mapView = (MapView) view.findViewById(R.id.mapView_poi);
        this.imageViewFavaourite=(ImageView)view.findViewById(R.id.imageView_favorites);
        LinearLayout layoutBack = (LinearLayout) view.findViewById(R.id.layout_back);
        RecyclerView recyclerViewOpeningTimes = (RecyclerView) view.findViewById(R.id.recyclerView_openingTimes);

        layoutBack.setOnClickListener(this);

        this.textViewOpeningtimesTitle.setVisibility(View.GONE);

        recyclerViewOpeningTimes.setLayoutManager(new LinearLayoutManager(view.getContext()));
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        decoration.setPaddingRes(R.dimen.poi_detail_opening_times_item_padding, 0);
        recyclerViewOpeningTimes.addItemDecoration(decoration);
        this.adapter = new OpeningTimesAdapter();
        recyclerViewOpeningTimes.setAdapter(adapter);
        recyclerViewOpeningTimes.setItemAnimator(new DefaultItemAnimator());

        this.cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(51.2159197, 4.4281639), 17);
        this.iconFactory = IconFactory.getInstance(view.getContext());
        this.mapView.setStyleUrl(Style.MAPBOX_STREETS);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap)
            {
                UiSettings uiSettings = mapboxMap.getUiSettings();
                uiSettings.setAttributionEnabled(false);
                uiSettings.setCompassGravity(Gravity.BOTTOM | Gravity.RIGHT);

                PoiDetailFragment.this.mapboxMap = mapboxMap;
                PoiDetailFragment.this.updateMap();
            }
        });

        //TODO add marker for home location

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_POI_ITEM, null, this);
        this.getLoaderManager().restartLoader(Constants.LOADER_OPENING_TIMES, null, this);
    }

    @Override
    public void onDestroyView()
    {
        this.mapView.onDestroy();

        super.onDestroyView();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        this.mapView.onStart();
    }

    @Override
    public void onStop()
    {
        this.mapView.onStop();

        super.onStop();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        this.mapView.onResume();
    }

    @Override
    public void onPause()
    {
        this.mapView.onPause();

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        this.mapView.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        int selectedPoiId = this.getArguments().getInt(KEY_POI_ID);

        switch (id)
        {
            case Constants.LOADER_POI_ITEM:
                String whereClausePoi = PoiTable.COLUMN_POI_ID + "='" + selectedPoiId + "'";
                return new CursorLoader(this.getView().getContext(), PoiContentProvider.CONTENT_URI, null, whereClausePoi, null, null);

            case Constants.LOADER_OPENING_TIMES:
                String whereClauseTimes = OpeningTimesTable.COLUMN_POI_ID + "='" + selectedPoiId + "'";
                String sortOrder = OpeningTimesTable.COLUMN_DAY + " ASC";
                return new CursorLoader(this.getView().getContext(), OpeningTimesContentProvider.CONTENT_URI, null, whereClauseTimes, null, sortOrder);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_POI_ITEM:
                this.cursorPoi = data;
                this.setImage(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

                if(data.moveToFirst())
                {
                    StringBuilder address = new StringBuilder("");
                    this.poiId = data.getInt(data.getColumnIndex(PoiTable.COLUMN_POI_ID_FULL));
                    this.favorite=data.getInt(data.getColumnIndex(PoiTable.COLUMN_FAVORITE_FULL));
                    String street = data.getString(data.getColumnIndex(PoiTable.COLUMN_STREET_FULL));
                    String number = data.getString(data.getColumnIndex(PoiTable.COLUMN_NUMBER_FULL));
                    String postalCode = data.getString(data.getColumnIndex(PoiTable.COLUMN_POSTAL_CODE_FULL));
                    String locality = data.getString(data.getColumnIndex(PoiTable.COLUMN_LOCALITY_FULL));
                    String phone = data.getString(data.getColumnIndex(PoiTable.COLUMN_PHONE_FULL));
                    String description = data.getString(data.getColumnIndex(PoiTable.COLUMN_DESCRIPTION_FULL));
                    image=data.getString(data.getColumnIndex(PoiTable.COLUMN_IMAGE_FULL));
                    if(street != null)
                        address.append(street);
                    if(number != null)
                        address.append(" ").append(number);
                    if(postalCode != null)
                        address.append("\n").append(postalCode);
                    if(locality != null)
                        address.append(" ").append(locality);

                    this.textViewTitle.setText(data.getString(data.getColumnIndex(PoiTable.COLUMN_NAME_FULL)));
                    this.textViewAddress.setText(address.toString());
                    this.textViewPhone.setText(phone);
                    this.textViewPhone.setVisibility(TextUtils.isEmpty(phone) ? View.GONE : View.VISIBLE);
                    this.textViewPhoneTitle.setVisibility(TextUtils.isEmpty(phone) ? View.GONE : View.VISIBLE);
                    this.textViewDescriptionTitle.setVisibility(TextUtils.isEmpty(description)  ? View.GONE : View.VISIBLE);
                    this.TextViewDescription.setVisibility(TextUtils.isEmpty(description)  ? View.GONE : View.VISIBLE);
                    this.imageViewFavaourite.setVisibility(this.favorite==1? View.VISIBLE:View.GONE);
                    if(!TextUtils.isEmpty(description))
                        this.TextViewDescription.setText(description);

                    double latitude = data.getDouble(data.getColumnIndex(PoiTable.COLUMN_LATITUDE_FULL));
                    double longitude = data.getDouble(data.getColumnIndex(PoiTable.COLUMN_LONGITUDE_FULL));

                    this.cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17);
                    this.markerOptions = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .icon(this.iconFactory.fromResource(R.drawable.marker_mapbox));

                    this.updateMap();
                }
                break;

            case Constants.LOADER_OPENING_TIMES:
                List<OpeningTime> openingTimes = OpeningTime.constructListFromCursor(data);
                this.textViewOpeningtimesTitle.setVisibility(openingTimes != null && openingTimes.size() > 0 ? View.VISIBLE : View.GONE);
                this.adapter.setOpeningTimes(openingTimes);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    private void updateMap()
    {
        if(this.mapboxMap != null)
        {
            if(this.cameraUpdate != null)
            {
                this.mapboxMap.moveCamera(this.cameraUpdate);
            }
            this.mapboxMap.removeAnnotations();
            if(this.markerOptions != null)
            {
                this.mapboxMap.addMarker(this.markerOptions);
            }
        }
    }

    private void setImage(boolean landscape)
    {
        if(this.cursorPoi != null && this.cursorPoi.moveToFirst() && this.getActivity() != null && !this.getActivity().isFinishing())
        {
            String loadedImage;
            if(this.image!=null)
                loadedImage=this.image;
                else
                loadedImage = CategoryHelper.getPoiPhoto(this.cursorPoi, !landscape);

            if(!TextUtils.isEmpty(loadedImage))
                Picasso.with(this.getActivity())
                        .load(loadedImage)
                        .into(this.imageViewCategory);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.layout_back:
                if(this.getActivity() != null && !this.getActivity().isFinishing())
                {
                    if(this.getActivity() instanceof BaseActivity)
                        ((BaseActivity) this.getActivity()).startParentActivity();
                    else
                        this.getActivity().onNavigateUp();
                }
                break;

        }
    }

}
