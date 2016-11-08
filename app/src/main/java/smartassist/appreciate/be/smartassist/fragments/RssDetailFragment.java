package smartassist.appreciate.be.smartassist.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.BaseActivity;
import smartassist.appreciate.be.smartassist.contentproviders.RssContentProvider;
import smartassist.appreciate.be.smartassist.database.RssTable;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 25/02/2015.
 */
public class RssDetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private ImageView imageViewRss;
    private TextView textViewTitle;
    private TextView textViewIntro;
    private TextView textViewDescription;
    private TextView textViewDate;

    public static final String KEY_RSS_ID = "rss_id";
    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("d MMMM", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_rss_detail, container, false);

        this.imageViewRss = (ImageView) view.findViewById(R.id.imageView_rss);
        this.textViewTitle = (TextView) view.findViewById(R.id.textView_title);
        this.textViewIntro = (TextView) view.findViewById(R.id.textView_intro);
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
        this.getLoaderManager().restartLoader(Constants.LOADER_RSS_ITEM, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        int selectedRssId = this.getArguments().getInt(KEY_RSS_ID);
        String whereClause = RssTable.COLUMN_RSS_ID + "='" + selectedRssId + "'";
        return new CursorLoader(this.getView().getContext(), RssContentProvider.CONTENT_URI, null, whereClause, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if(data.moveToFirst())
        {
            this.textViewTitle.setText(data.getString(data.getColumnIndex(RssTable.COLUMN_TITLE)));
            this.textViewIntro.setText(data.getString(data.getColumnIndex(RssTable.COLUMN_INTRO)));

            String body = data.getString(data.getColumnIndex(RssTable.COLUMN_BODY));
            this.textViewDescription.setText(Html.fromHtml(body));

            String image = data.getString(data.getColumnIndex(RssTable.COLUMN_IMAGE));
            if (this.getActivity() != null && !this.getActivity().isFinishing() && !TextUtils.isEmpty(image))
                Picasso.with(this.getActivity())
                        .load(image)
                        .into(this.imageViewRss);

            long start = data.getLong(data.getColumnIndex(RssTable.COLUMN_DATE));
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