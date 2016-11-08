package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.Invoice;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 1/08/2016.
 */
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>
{
    private OnInvoiceClickListener listener;
    private List<Invoice> invoices;

    public InvoiceAdapter()
    {
    }

    public void setInvoices(List<Invoice> invoices)
    {
        this.invoices = invoices;
        this.notifyDataSetChanged();
    }

    public void setListener(OnInvoiceClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_invoice, parent, false);
        return new InvoiceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InvoiceViewHolder holder, int position)
    {
        Invoice invoice = this.invoices.get(position);

        holder.bind(invoice);
    }

    @Override
    public int getItemCount()
    {
        return this.invoices != null ? this.invoices.size() : 0;
    }

    public class InvoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewTitle;
        private TextView textViewDate;
        private Invoice invoice;

        public InvoiceViewHolder(View itemView)
        {
            super(itemView);

            this.textViewTitle = (TextView) itemView.findViewById(R.id.textView_name);
            this.textViewDate = (TextView)itemView.findViewById(R.id.textView_date);

            itemView.setOnClickListener(this);
        }

        public void bind(Invoice invoice)
        {
            this.invoice = invoice;

            this.textViewTitle.setText(this.textViewTitle.getContext().getString(R.string.invoice_title, invoice.getNumber()));
            this.textViewDate.setText(invoice.getDate());
        }

        @Override
        public void onClick(View v)
        {
            if(InvoiceAdapter.this.listener != null)
                InvoiceAdapter.this.listener.onInvoiceClick(v, this.invoice);
        }
    }

    public interface OnInvoiceClickListener
    {
        void onInvoiceClick(View caller, Invoice invoice);
    }
}
