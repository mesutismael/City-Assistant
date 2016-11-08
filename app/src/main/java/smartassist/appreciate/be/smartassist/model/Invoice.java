package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.InvoiceTable;

/**
 * Created by Inneke De Clippel on 4/08/2016.
 */
public class Invoice
{
    private int id;
    private String number;
    private String date;
    private String url;

    public int getId()
    {
        return id;
    }

    public String getNumber()
    {
        return number;
    }

    public String getDate()
    {
        return date;
    }

    public String getUrl()
    {
        return url;
    }

    public static List<Invoice> constructListFromCursor(Cursor cursor)
    {
        List<Invoice> invoices = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                invoices.add(Invoice.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return invoices;
    }

    public static Invoice constructFromCursor(Cursor cursor)
    {
        Invoice invoice = new Invoice();

        invoice.id = cursor.getInt(cursor.getColumnIndex(InvoiceTable.COLUMN_INVOICE_ID_FULL));
        invoice.number = cursor.getString(cursor.getColumnIndex(InvoiceTable.COLUMN_NUMBER_FULL));
        invoice.date = cursor.getString(cursor.getColumnIndex(InvoiceTable.COLUMN_DATE_FULL));
        invoice.url = cursor.getString(cursor.getColumnIndex(InvoiceTable.COLUMN_URL_FULL));

        return invoice;
    }
}
