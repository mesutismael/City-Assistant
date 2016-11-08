package smartassist.appreciate.be.smartassist.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URI;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.HabitantsActivity;
import smartassist.appreciate.be.smartassist.activities.ResidentNewsActivity;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by banada ismael on 21/10/2016.
 */
public class ResidenceInfoSelectionFragment extends Fragment implements View.OnClickListener
{
    private CardView cardview_residentInfo;
    private CardView cardview_residentNews;
    private ImageView imageView_residentInfo;
    private ImageView imageView_residentNews;
    private String resident_news_image;
    private String resident_info_image;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user_selection, container, false);
        this.imageView_residentInfo= (ImageView)view.findViewById(R.id.imageview_resident_info) ;
        this.imageView_residentNews= (ImageView)view.findViewById(R.id.imageview_resident_news) ;
        this.cardview_residentInfo= (CardView)view.findViewById(R.id.cardview_resident_info);
        this.cardview_residentNews= (CardView)view.findViewById(R.id.cardview_resident_news);
        this.cardview_residentNews.setOnClickListener(this);
        this.cardview_residentInfo.setOnClickListener(this);

        if (!TextUtils.isEmpty(PreferencesHelper.getResidentInfoPhoto(getContext())))
        this.resident_info_image= PreferencesHelper.getResidentInfoPhoto(getContext()).replaceAll("\\s", "%20");

        if(!TextUtils.isEmpty(PreferencesHelper.getResidentNewsPhoto(getContext())))
        this.resident_news_image=PreferencesHelper.getResidentNewsPhoto(getContext()).replaceAll("\\s", "%20");
        Picasso.with(this.imageView_residentInfo.getContext()).load(resident_info_image).into(this.imageView_residentInfo);
        Picasso.with(this.imageView_residentNews.getContext()).load(resident_news_image).into(this.imageView_residentNews);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.cardview_resident_info:
                startActivity(new Intent(getContext(), HabitantsActivity.class));
                break;

            case R.id.cardview_resident_news:
                startActivity(new Intent(getContext(), ResidentNewsActivity.class));
                break;
        }
    }


}
