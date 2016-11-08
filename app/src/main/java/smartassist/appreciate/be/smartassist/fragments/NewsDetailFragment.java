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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.BaseActivity;
import smartassist.appreciate.be.smartassist.contentproviders.NewsContentProvider;
import smartassist.appreciate.be.smartassist.database.NewsTable;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 24/02/2015.
 */
public class NewsDetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private ImageView imageViewCategory;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewDate;

    public static final String KEY_NEWS_ID = "news_id";
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
        int selectedNewsId = this.getArguments().getInt(KEY_NEWS_ID);
        String whereClause = NewsTable.COLUMN_NEWS_ID + "='" + selectedNewsId + "'";
        return new CursorLoader(this.getView().getContext(), NewsContentProvider.CONTENT_URI, null, whereClause, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if(data.moveToFirst())
        {
            this.textViewTitle.setText(data.getString(data.getColumnIndex(NewsTable.COLUMN_TITLE_FULL)));
            this.textViewDescription.setText(data.getString(data.getColumnIndex(NewsTable.COLUMN_BODY_FULL)));

            boolean landscape = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            String image = CategoryHelper.getNewsPhoto(data, !landscape);

            if (this.getActivity() != null && !this.getActivity().isFinishing() && !TextUtils.isEmpty(image))
                Picasso.with(this.getActivity())
                        .load(image)
                        .into(this.imageViewCategory);

            long start = data.getLong(data.getColumnIndex(NewsTable.COLUMN_START_DATE_FULL));
            long stop = data.getLong(data.getColumnIndex(NewsTable.COLUMN_END_DATE_FULL));
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTimeInMillis(start);
            Calendar calendarStop = Calendar.getInstance();
            calendarStop.setTimeInMillis(stop);
            if (calendarStart.get(Calendar.DAY_OF_YEAR) == calendarStop.get(Calendar.DAY_OF_YEAR) && calendarStart.get(Calendar.YEAR) == calendarStop.get(Calendar.YEAR))
                this.textViewDate.setText(SDF_DATE.format(new Date(start)));
            else
                this.textViewDate.setText(SDF_DATE.format(new Date(start)) + " - " + SDF_DATE.format(new Date(stop)));
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
