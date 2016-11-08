package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.InvoiceDetailFragment;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 1/08/2016.
 */
public class InvoiceDetailActivity extends BaseActivity implements View.OnClickListener
{
    public static final String KEY_INVOICE_ID = InvoiceDetailFragment.KEY_INVOICE_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_invoice_detail);

        TextView textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        textViewTitle.setText(R.string.module_invoice);

        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        InvoiceDetailFragment fragment = new InvoiceDetailFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.layout_back:
                this.startParentActivity();
                break;
        }
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_INVOICE_DETAIL;
    }
}
