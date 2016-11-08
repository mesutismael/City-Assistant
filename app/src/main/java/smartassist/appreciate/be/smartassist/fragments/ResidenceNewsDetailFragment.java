package smartassist.appreciate.be.smartassist.fragments;

import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.BaseActivity;
import smartassist.appreciate.be.smartassist.activities.OpenFileActivity;
import smartassist.appreciate.be.smartassist.contentproviders.NewsContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ResidentNewsContentProvider;
import smartassist.appreciate.be.smartassist.database.NewsTable;
import smartassist.appreciate.be.smartassist.database.ResidentNewsTable;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.Button;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by banada ismael on 25/10/2016.
 */
public class ResidenceNewsDetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private ImageView imageViewCategory;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewDate;
    private String file;

    public static final String KEY_RESIDENCE_ID = "residence_id";
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("d MMMM", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        this.imageViewCategory = (ImageView) view.findViewById(R.id.imageView_news);
        this.textViewTitle = (TextView) view.findViewById(R.id.textView_title);
        this.textViewDescription = (TextView) view.findViewById(R.id.textView_description);
        this.textViewDate = (TextView) view.findViewById(R.id.textView_date);
        LinearLayout layoutBack = (LinearLayout) view.findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_NEWS_ITEM, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        int selectedNewsId = this.getArguments().getInt(KEY_RESIDENCE_ID);
        String whereClause = ResidentNewsTable.COLUMN_RESIDENCE_ID + "='" + selectedNewsId + "'";
        return new CursorLoader(this.getView().getContext(), ResidentNewsContentProvider.CONTENT_URI, null, whereClause, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if(data.moveToFirst())
        {
            this.textViewTitle.setText(data.getString(data.getColumnIndex(ResidentNewsTable.COLUMN_TITLE)));
            this.textViewDescription.setText(data.getString(data.getColumnIndex(ResidentNewsTable.COLUMN_BODY)));
             file = data.getString(data.getColumnIndex(ResidentNewsTable.COLUMN_FILE));
            if (this.getActivity() != null && !this.getActivity().isFinishing() && !TextUtils.isEmpty(file))
                Picasso.with(this.getActivity())
                        .load(file)
                        .into(this.imageViewCategory);

            long start = data.getLong(data.getColumnIndex(ResidentNewsTable.COLUMN_CREATION_DATE));

                this.textViewDate.setText(SDF_DATE.format(new Date(start)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
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
