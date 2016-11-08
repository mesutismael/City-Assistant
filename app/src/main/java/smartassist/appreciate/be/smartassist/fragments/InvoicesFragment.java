package smartassist.appreciate.be.smartassist.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.InvoiceDetailActivity;
import smartassist.appreciate.be.smartassist.adapters.InvoiceAdapter;
import smartassist.appreciate.be.smartassist.contentproviders.InvoiceContentProvider;
import smartassist.appreciate.be.smartassist.database.InvoiceTable;
import smartassist.appreciate.be.smartassist.decorations.PaddingDecoration;
import smartassist.appreciate.be.smartassist.model.Invoice;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke De Clippel on 1/08/2016.
 */
public class InvoicesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, InvoiceAdapter.OnInvoiceClickListener
{
    private GridLayoutManager layoutManager;
    private InvoiceAdapter invoiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_invoices, container, false);

        RecyclerView recyclerViewInvoice = (RecyclerView) view.findViewById(R.id.recyclerView_invoice);

        this.layoutManager = new GridLayoutManager(view.getContext(), 3);
        this.adjustLayoutManager(view.getContext().getResources().getConfiguration().orientation);
        recyclerViewInvoice.setLayoutManager(this.layoutManager);
        PaddingDecoration decoration = new PaddingDecoration(view.getContext());
        recyclerViewInvoice.addItemDecoration(decoration);
        this.invoiceAdapter = new InvoiceAdapter();
        this.invoiceAdapter.setListener(this);
        recyclerViewInvoice.setAdapter(this.invoiceAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().restartLoader(Constants.LOADER_INVOICES, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_INVOICES:
                String sortOrder = InvoiceTable.COLUMN_NUMBER + " ASC";
                return new CursorLoader(this.getView().getContext(), InvoiceContentProvider.CONTENT_URI, null, null, null, sortOrder);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_INVOICES:
                this.invoiceAdapter.setInvoices(Invoice.constructListFromCursor(data));
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
        this.invoiceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInvoiceClick(View caller, Invoice invoice)
    {
        Intent intent = new Intent(caller.getContext(), InvoiceDetailActivity.class);
        intent.putExtra(InvoiceDetailActivity.KEY_INVOICE_ID, invoice.getId());
        this.startActivity(intent);
    }

    private void adjustLayoutManager(int orientation)
    {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            this.layoutManager.setSpanCount(3);
        } else
        {
            this.layoutManager.setSpanCount(2);
        }
    }
}
