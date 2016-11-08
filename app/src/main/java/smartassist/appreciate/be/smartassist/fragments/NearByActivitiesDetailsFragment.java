package smartassist.appreciate.be.smartassist.fragments;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.BaseActivity;
import smartassist.appreciate.be.smartassist.contentproviders.ActivitiesContentProvider;
import smartassist.appreciate.be.smartassist.database.ActivityTable;
import smartassist.appreciate.be.smartassist.database.ActivityTypesTable;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.KeyValueView;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by banada ismael on 11/02/2015.
 */
public class NearByActivitiesDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener
{
    public static final String KEY_ACTIVITY_ID = "activity_id";
    private ImageView imageViewCategory;
    private TextView textViewTitle;
    private TextView textViewActivityType;
    private String image;
    private KeyValueView keyValueViewLocation;
    private KeyValueView keyValueViewStartDateTime;
    private KeyValueView keyValueViewInvoicePrice;
    private KeyValueView keyValueViewDescription;
    private Cursor cursorActivity;
    private int activityId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_activities_details, container, false);

        this.imageViewCategory = (ImageView) view.findViewById(R.id.imageView_category);
        this.textViewTitle = (TextView) view.findViewById(R.id.textView_title);
        this.textViewActivityType = (TextView) view.findViewById(R.id.textView_activityType);
        this.keyValueViewLocation= (KeyValueView) view.findViewById(R.id.keyValueView_location);
        this.keyValueViewStartDateTime = (KeyValueView) view.findViewById(R.id.keyValueView_startDateTime);
        this.keyValueViewInvoicePrice = (KeyValueView) view.findViewById(R.id.keyValueView_invoice_price);
        this.keyValueViewDescription = (KeyValueView) view.findViewById(R.id.keyValueView_description);
        LinearLayout layoutBack = (LinearLayout) view.findViewById(R.id.layout_back);

        layoutBack.setOnClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_ACTIVITIES, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        int selectedPoiId = this.getArguments().getInt(KEY_ACTIVITY_ID);

        switch (id)
        {
            case Constants.LOADER_ACTIVITIES:
                String whereClausePoi = ActivityTable.TABLE_NAME + "." + ActivityTable.COLUMN_ID + "='" + selectedPoiId + "'";
                return new CursorLoader(this.getView().getContext(), ActivitiesContentProvider.CONTENT_URI, null, whereClausePoi, null, null);
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
                this.cursorActivity = data;
                this.setImage(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

                if(data.moveToFirst())
                {
                    this.activityId = data.getInt(data.getColumnIndex(ActivityTable.COLUMN_ID_FULL));
                    String title = data.getString(data.getColumnIndex(ActivityTable.COLUMN_NAME_FULL));
                    String location= data.getString(data.getColumnIndex(ActivityTable.COLUMN_LOCATION_FULL));
                    String startDate= data.getString(data.getColumnIndex(ActivityTable.COLUMN_START_DATE_FULL));
                    String startTime= data.getString(data.getColumnIndex(ActivityTable.COLUMN_START_TIME_FULL));
                    int invoicePrice= data.getInt(data.getColumnIndex(ActivityTable.COLUMN_INVOICE_PRICE_FULL));
                    String description= data.getString(data.getColumnIndex(ActivityTable.COLUMN_DESCRIPTION_FULL));
                    String endTime= data.getString(data.getColumnIndex(ActivityTable.COLUMN_END_TIME_FULL));
                    String activity_type= data.getString(data.getColumnIndex(ActivityTypesTable.COLUMN_NAME_FULL));
                    image=data.getString(data.getColumnIndex(ActivityTable.COLUMN_PHOTO_FULL));

                    this.textViewTitle.setText(title);
                    this.keyValueViewLocation.setValue(location);
                    this.keyValueViewInvoicePrice.setValue(Integer.toString(invoicePrice));
                    this.keyValueViewStartDateTime.setValue(startDate+"\n"+startTime+" - "+ endTime);
                    this.keyValueViewDescription.setValue(description);
                    this.textViewActivityType.setText(activity_type);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    private void setImage(boolean landscape)
    {
        if(this.cursorActivity != null && this.cursorActivity.moveToFirst() && this.getActivity() != null && !this.getActivity().isFinishing())
        {
            String loadedImage = !TextUtils.isEmpty(this.image)? this.image:CategoryHelper.getActivityPhoto(this.cursorActivity, !landscape);
            System.out.println("loadedImage: "+loadedImage);
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
