package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.Medication;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 12/04/2016.
 */
public class MedicationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Medication> medications;

    private static final int TYPE_ITEM = 1;

    public MedicationAdapter()
    {
    }

    public void setMedications(List<Medication> medications)
    {
        this.medications = medications;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        switch (viewType)
        {
            case TYPE_ITEM:
                View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_medication, viewGroup, false);
                return new ItemViewHolder(viewItem);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        if(viewHolder instanceof ItemViewHolder)
        {
            Medication medication = this.medications.get(i);

            ((ItemViewHolder) viewHolder).bind(medication);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.medications != null ? this.medications.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewName;
        TextView textViewDose;
        ImageView imageViewTime1;
        ImageView imageViewTime2;
        ImageView imageViewTime3;
        ImageView imageViewTime4;
        ImageView imageViewTime5;
        ImageView imageViewTime6;
        ImageView imageViewTime7;

        public ItemViewHolder(View itemView)
        {
            super(itemView);

            this.textViewName = (TextView) itemView.findViewById(R.id.textView_name);
            this.textViewDose = (TextView) itemView.findViewById(R.id.textView_dose);
            this.imageViewTime1 = (ImageView) itemView.findViewById(R.id.imageView_time_1);
            this.imageViewTime2 = (ImageView) itemView.findViewById(R.id.imageView_time_2);
            this.imageViewTime3 = (ImageView) itemView.findViewById(R.id.imageView_time_3);
            this.imageViewTime4 = (ImageView) itemView.findViewById(R.id.imageView_time_4);
            this.imageViewTime5 = (ImageView) itemView.findViewById(R.id.imageView_time_5);
            this.imageViewTime6 = (ImageView) itemView.findViewById(R.id.imageView_time_6);
            this.imageViewTime7 = (ImageView) itemView.findViewById(R.id.imageView_time_7);
        }

        public void bind(Medication medication)
        {
            this.textViewName.setText(medication.getName());
            this.textViewDose.setText(medication.getDose());
            this.imageViewTime1.setImageResource(Medication.getImageForTime(medication.getTime1()));
            this.imageViewTime2.setImageResource(Medication.getImageForTime(medication.getTime2()));
            this.imageViewTime3.setImageResource(Medication.getImageForTime(medication.getTime3()));
            this.imageViewTime4.setImageResource(Medication.getImageForTime(medication.getTime4()));
            this.imageViewTime5.setImageResource(Medication.getImageForTime(medication.getTime5()));
            this.imageViewTime6.setImageResource(Medication.getImageForTime(medication.getTime6()));
            this.imageViewTime7.setImageResource(Medication.getImageForTime(medication.getTime7()));
        }
    }
}
