package smartassist.appreciate.be.smartassist.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.NearByActivitiesActivity;
import smartassist.appreciate.be.smartassist.activities.PoiActivity;
import smartassist.appreciate.be.smartassist.model.Category;

/**
 * Created by banada ismael on 21/10/2016.
 */
public class PoiSelectionFragment extends Fragment implements View.OnClickListener
{
    private CardView cardview_nearby;
    private CardView cardview_activities;
    private ImageView imageView_nearby;
    private ImageView imageView_activities;
    private String nearby_image;
    private String activities_image;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_poi_selection, container, false);
        this.imageView_nearby= (ImageView)view.findViewById(R.id.imageview_nearby) ;
        this.imageView_activities= (ImageView)view.findViewById(R.id.imageview_activities) ;
        this.cardview_nearby= (CardView)view.findViewById(R.id.cardview_nearby);
        this.cardview_activities= (CardView)view.findViewById(R.id.cardview_activities);
        this.cardview_nearby.setOnClickListener(this);
        this.cardview_activities.setOnClickListener(this);

  /*      if (!TextUtils.isEmpty(PreferencesHelper.getResidentInfoPhoto(getContext())))
            this.nearby_image= PreferencesHelper.getResidentInfoPhoto(getContext()).replaceAll("\\s", "%20");

        if(!TextUtils.isEmpty(PreferencesHelper.getResidentNewsPhoto(getContext())))
            this.activities_image=PreferencesHelper.getResidentNewsPhoto(getContext()).replaceAll("\\s", "%20");
        */

        //TODO: remove demo images
        Picasso.with(this.imageView_nearby.getContext()).load(Category.DEMO_IMAGE).into(this.imageView_nearby);
        Picasso.with(this.imageView_activities.getContext()).load(Category.DEMO_IMAGE_2).into(this.imageView_activities);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.cardview_nearby:
                startActivity(new Intent(getContext(), PoiActivity.class));
                break;

            case R.id.cardview_activities:
                startActivity(new Intent(getContext(), NearByActivitiesActivity.class));
                break;
        }
    }


}
