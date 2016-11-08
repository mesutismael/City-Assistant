package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.OpeningTime;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 12/02/2015.
 */
public class OpeningTimesAdapter extends RecyclerView.Adapter<OpeningTimesAdapter.OpeningTimesViewHolder>
{
    private List<OpeningTime> openingTimes;

    public OpeningTimesAdapter()
    {
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes)
    {
        this.openingTimes = openingTimes;
        this.notifyDataSetChanged();
    }

    @Override
    public OpeningTimesViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_opening_times, viewGroup, false);
        return new OpeningTimesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OpeningTimesViewHolder viewHolder, int i)
    {
        OpeningTime openingTime = this.openingTimes.get(i);

        viewHolder.bind(openingTime);
    }

    @Override
    public int getItemCount()
    {
        return this.openingTimes != null ? this.openingTimes.size() : 0;
    }

    public class OpeningTimesViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewDay;
        TextView textViewMorning;
        TextView textViewMidday;

        public OpeningTimesViewHolder(View itemView)
        {
            super(itemView);

            this.textViewDay = (TextView)itemView.findViewById(R.id.textView_day);
            this.textViewMorning = (TextView)itemView.findViewById(R.id.textView_morning);
            this.textViewMidday = (TextView)itemView.findViewById(R.id.textView_midday);
        }

        public void bind(OpeningTime openingTime)
        {

            this.textViewDay.setText(openingTime.getDayResource());

            if(!TextUtils.isEmpty(openingTime.getStartMorning()) && !TextUtils.isEmpty(openingTime.getStopMorning()))
            {
                String morningStart = DateUtils.formatOpeningTime(DateUtils.parseApiOpeningTime(openingTime.getStartMorning()));
                String morningStop = DateUtils.formatOpeningTime(DateUtils.parseApiOpeningTime(openingTime.getStopMorning()));
                this.textViewMorning.setText(this.textViewMorning.getContext().getString(R.string.poi_opening_time_period, morningStart, morningStop));
            }
            else
            {
                this.textViewMorning.setText(R.string.poi_closed);
            }

                 if(!TextUtils.isEmpty(openingTime.getStartMidday()) && !TextUtils.isEmpty(openingTime.getStopMidday())&&!TextUtils.isEmpty(openingTime.getStopMorning()))
                 {
                     if(!openingTime.getStopMorning().equalsIgnoreCase(openingTime.getStopMidday()) )
                     {
                     String middayStart = DateUtils.formatOpeningTime(DateUtils.parseApiOpeningTime(openingTime.getStartMidday()));
                     String middayStop = DateUtils.formatOpeningTime(DateUtils.parseApiOpeningTime(openingTime.getStopMidday()));
                     this.textViewMidday.setText(this.textViewMorning.getContext().getString(R.string.poi_opening_time_period, middayStart, middayStop));
                     }else
                     {
                         this.textViewMidday.setVisibility(View.GONE);
                     }
                 }
                 else
                 {
                     this.textViewMidday.setVisibility(View.GONE);
                 }


        }
    }
}
