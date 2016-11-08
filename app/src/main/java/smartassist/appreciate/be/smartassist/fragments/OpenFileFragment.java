package smartassist.appreciate.be.smartassist.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.squareup.picasso.Picasso;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Banada ismael on 25/10/2016.
 */
public class OpenFileFragment extends Fragment
{
    public static final String KEY_FILE_ATTACHMENT = "file_attachment";
    private GridLayoutManager layoutManager;
    private PDFView pdfViewAttachment;
    private ImageView imageViewAttachment;
    private String file;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_open_file, container, false);

        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        this.pdfViewAttachment= (PDFView)view.findViewById(R.id.pdfView_attachment);
        this.imageViewAttachment= (ImageView)view.findViewById(R.id.imageView_attachment);
        if (this.getArguments().getString(KEY_FILE_ATTACHMENT)!=null)
        {
            file=this.getArguments().getString(KEY_FILE_ATTACHMENT);
            if (file.endsWith(".pdf"))
            {
                this.pdfViewAttachment.setVisibility(View.VISIBLE);
                this.imageViewAttachment.setVisibility(View.GONE);
                this.pdfViewAttachment.fromUri(Uri.parse(file)).enableSwipe(true).load();
            }
            else
            {
                this.pdfViewAttachment.setVisibility(View.GONE);
                this.imageViewAttachment.setVisibility(View.VISIBLE);
                Picasso.with(this.getActivity()).load(file).into(this.imageViewAttachment);
            }

        }
        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        this.adjustLayoutManager(newConfig.orientation);
    }

    private void adjustLayoutManager(int orientation)
    {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
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
        } else
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


}
