package smartassist.appreciate.be.smartassist.adapters;

import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.model.Poi;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 27/02/2015.
 */
public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder>
{
    private List<EmergencyContact> emergencyContacts;
    private List<Poi> doctors;
    private List<Poi> pharmacies;
    private boolean showDoctorAndPharmacy;
    private OnEmergencyClickListener listener;

    public EmergencyAdapter()
    {
    }

    public void setEmergencyContacts(List<EmergencyContact> emergencyContacts, boolean showDoctorAndPharmacy)
    {
        this.emergencyContacts = emergencyContacts;
        this.showDoctorAndPharmacy = showDoctorAndPharmacy;
        this.notifyDataSetChanged();
    }

    public void setDoctors(List<Poi> doctors)
    {
        this.doctors = doctors;
        this.notifyDataSetChanged();
    }

    public void setPharmacies(List<Poi> pharmacies)
    {
        this.pharmacies = pharmacies;
        this.notifyDataSetChanged();
    }

    public void setListener(OnEmergencyClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public EmergencyViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_emergency, viewGroup, false);
        return new EmergencyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EmergencyViewHolder viewHolder, int i)
    {
        if(i < this.getDoctorCount())
        {
            Poi doctor = this.doctors.get(i);

            viewHolder.bind(doctor, i, true);
        }
        else if(i < this.getDoctorCount() + this.getPharmacyCount())
        {
            Poi pharmacy = this.pharmacies.get(i - this.getDoctorCount());

            viewHolder.bind(pharmacy, i, false);
        }
        else
        {
            EmergencyContact contact = this.emergencyContacts.get(i - this.getDoctorCount() - this.getPharmacyCount());

            viewHolder.bind(contact, i);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.getEmergencyCount() + this.getDoctorCount() + this.getPharmacyCount();
    }

    private int getEmergencyCount()
    {
        return this.emergencyContacts != null ? this.emergencyContacts.size() : 0;
    }

    private int getDoctorCount()
    {
        return this.doctors != null && this.showDoctorAndPharmacy ? this.doctors.size() : 0;
    }

    private int getPharmacyCount()
    {
        return this.pharmacies != null && this.showDoctorAndPharmacy ? this.pharmacies.size() : 0;
    }

    public class EmergencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imageViewLarge;
        private ImageView imageViewSmall;
        private TextView textViewTitle;
        private TextView textViewPhone;
        private ImageView imageViewQr;
        private ImageView imageViewCall;
        private ImageView imageViewVideocall;
        private EmergencyContact contact;

        public EmergencyViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewLarge = (ImageView) itemView.findViewById(R.id.imageView_large);
            this.imageViewSmall = (ImageView) itemView.findViewById(R.id.imageView_small);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textView_name);
            this.textViewPhone = (TextView) itemView.findViewById(R.id.textView_phone);
            this.imageViewQr = (ImageView) itemView.findViewById(R.id.imageView_qr);
            this.imageViewCall = (ImageView) itemView.findViewById(R.id.imageView_call);
            this.imageViewVideocall = (ImageView) itemView.findViewById(R.id.imageView_videocall);

            this.imageViewQr.setOnClickListener(this);
            this.imageViewCall.setOnClickListener(this);
            this.imageViewVideocall.setOnClickListener(this);
        }

        public void bind(EmergencyContact contact, int position)
        {
            this.contact = contact;

            boolean landscape = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean large = (landscape && (position % 10 == 4 || position % 10 == 8)) || (!landscape && position % 3 == 2);
            String image = CategoryHelper.getEmergencyContactPhoto(contact, large);

            if(large)
            {
                this.imageViewLarge.setVisibility(View.VISIBLE);
                this.imageViewSmall.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(image))
                    Picasso.with(this.imageViewLarge.getContext())
                            .load(image)
                            .into(this.imageViewLarge);
            }
            else
            {
                this.imageViewLarge.setVisibility(View.GONE);
                this.imageViewSmall.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(image))
                    Picasso.with(this.imageViewSmall.getContext())
                            .load(image)
                            .into(this.imageViewSmall);
            }

            this.textViewTitle.setText(contact.getName());
            this.textViewPhone.setText(contact.getNumber());
            this.imageViewQr.setVisibility(contact.hasQr() ? View.VISIBLE : View.GONE);
            this.imageViewCall.setVisibility(TextUtils.isEmpty(contact.getHash()) ? View.GONE : View.VISIBLE);
            this.imageViewVideocall.setVisibility(TextUtils.isEmpty(contact.getHash()) ? View.GONE : View.VISIBLE);
        }

        public void bind(Poi poi, int position, boolean doctor)
        {
            this.contact = null;

            boolean landscape = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean large = (landscape && (position % 10 == 4 || position % 10 == 8)) || (!landscape && position % 3 == 2);
            String image = CategoryHelper.getPoiPhoto(poi, large);
            String title = (doctor
                    ? this.itemView.getContext().getString(R.string.emergency_doctor)
                    : this.itemView.getContext().getString(R.string.emergency_pharmacist))
                    + "\n" + poi.getName();

            if(large)
            {
                this.imageViewLarge.setVisibility(View.VISIBLE);
                this.imageViewSmall.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(image))
                    Picasso.with(this.imageViewLarge.getContext())
                            .load(image)
                            .into(this.imageViewLarge);
            }
            else
            {
                this.imageViewLarge.setVisibility(View.GONE);
                this.imageViewSmall.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(image))
                    Picasso.with(this.imageViewSmall.getContext())
                            .load(image)
                            .into(this.imageViewSmall);
            }

            this.textViewTitle.setText(title);
            this.textViewPhone.setText(poi.getPhone());
            this.imageViewQr.setVisibility(View.GONE);
            this.imageViewCall.setVisibility(View.GONE);
            this.imageViewVideocall.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v)
        {
            if(EmergencyAdapter.this.listener != null && this.contact != null)
                EmergencyAdapter.this.listener.onEmergencyClick(v, this.contact);
        }
    }

    public interface OnEmergencyClickListener
    {
        void onEmergencyClick(View caller, EmergencyContact contact);
    }
}
