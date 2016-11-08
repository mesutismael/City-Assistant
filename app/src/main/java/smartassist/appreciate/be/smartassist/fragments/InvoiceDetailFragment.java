package smartassist.appreciate.be.smartassist.fragments;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Priority;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.contentproviders.InvoiceContentProvider;
import smartassist.appreciate.be.smartassist.database.InvoiceTable;
import smartassist.appreciate.be.smartassist.utils.ClosableOkHttpDownloader;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke De Clippel on 1/08/2016.
 */
public class InvoiceDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private PDFView pdfView;
    private ProgressBar progressBar;
    private TextView textViewError;

    public static final String KEY_INVOICE_ID = "invoice_id";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_invoice_detail, container, false);

        this.pdfView = (PDFView) view.findViewById(R.id.pdfView);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.textViewError = (TextView) view.findViewById(R.id.textView_error);

        this.progressBar.setVisibility(View.GONE);
        this.textViewError.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_INVOICE_ITEM, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        int selectedInvoiceId = this.getArguments().getInt(KEY_INVOICE_ID);
        String whereClause = InvoiceTable.COLUMN_INVOICE_ID + " = " + selectedInvoiceId;
        return new CursorLoader(this.getView().getContext(), InvoiceContentProvider.CONTENT_URI, null, whereClause, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if(data != null && data.moveToFirst())
        {
            String url = data.getString(data.getColumnIndex(InvoiceTable.COLUMN_URL_FULL));

            if(this.getContext() != null && !TextUtils.isEmpty(url))
            {
                String fileName = String.valueOf(url.hashCode()) + ".pdf";
                File pdfFile = new File(this.getContext().getFilesDir(), fileName);

                if(pdfFile.exists())
                {
                    this.showPdf(pdfFile);
                }
                else
                {
                    this.downloadPdf(url, pdfFile);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    private void downloadPdf(String url, File destination)
    {
        DownloadManager manager = new DownloadManager.Builder().context(this.getContext())
                .downloader(ClosableOkHttpDownloader.create())
                .threadPoolSize(2)
                .build();

        DownloadRequest request = new DownloadRequest.Builder()
                .url(url)
                .retryTime(0)
                .progressInterval(200, TimeUnit.MILLISECONDS)
                .priority(Priority.HIGH)
                .destinationFilePath(destination.getAbsolutePath())
                .downloadCallback(new DownloadCallback()
                {
                    @Override public void onStart(int downloadId, long totalBytes)
                    {
                        InvoiceDetailFragment.this.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onProgress(int downloadId, long bytesWritten, long totalBytes)
                    {
                        if(totalBytes > 0)
                        {
                            InvoiceDetailFragment.this.progressBar.setProgress((int) (bytesWritten * 100 / totalBytes));
                        }
                    }

                    @Override public void onSuccess(int downloadId, String filePath)
                    {
                        InvoiceDetailFragment.this.progressBar.setVisibility(View.GONE);
                        File pdfFile = new File(filePath);
                        InvoiceDetailFragment.this.showPdf(pdfFile);
                    }

                    @Override public void onFailure(int downloadId, int statusCode, String errMsg)
                    {
                        InvoiceDetailFragment.this.progressBar.setVisibility(View.GONE);
                        InvoiceDetailFragment.this.textViewError.setVisibility(View.VISIBLE);
                        InvoiceDetailFragment.this.textViewError.setText(R.string.invoice_error_download);
                    }
                })
                .build();

        manager.add(request);
    }

    private void showPdf(File pdfFile)
    {
        if(pdfFile.exists())
        {
            this.pdfView.fromFile(pdfFile)
                    .onError(new OnErrorListener()
                    {
                        @Override
                        public void onError(Throwable t)
                        {
                            InvoiceDetailFragment.this.textViewError.setVisibility(View.VISIBLE);
                            InvoiceDetailFragment.this.textViewError.setText(R.string.invoice_error_load);
                        }
                    })
                    .load();
        }
    }
}
