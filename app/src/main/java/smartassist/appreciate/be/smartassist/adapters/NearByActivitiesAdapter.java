package smartassist.appreciate.be.smartassist.adapters;

import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.ActivityItem;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by banada ismael on 02/11/2016.
 */
public class NearByActivitiesAdapter extends RecyclerView.Adapter<NearByActivitiesAdapter.ActivityViewHolder>
{
    private List<ActivityItem> activityItems;
    private OnActivityClickListener listener;

    public NearByActivitiesAdapter()
    {
    }

    public void setActivityItems(List<ActivityItem> activityItems)
    {
        this.activityItems = activityItems;
        this.notifyDataSetChanged();
    }

    public void setOnActivityClickListener(OnActivityClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_activity, viewGroup, false);
        return new ActivityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NearByActivitiesAdapter.ActivityViewHolder viewHolder, int i)
    {
        ActivityItem activityItem = this.activityItems.get(i);

        viewHolder.bind(activityItem, i);
    }

    @Override
    public int getItemCount()
    {
        return this.activityItems != null ? this.activityItems.size() : 0;
    }

    public interface OnActivityClickListener
    {
        void onActivityClick(View caller, ActivityItem activityItem);
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imageViewLarge;
        private ImageView imageViewSmall;
        private TextView textViewTitle;
        private TextView textViewAddress;
        private Button buttonMore;

        private ActivityItem activityItem;

        public ActivityViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewLarge = (ImageView) itemView.findViewById(R.id.imageView_large);
            this.imageViewSmall = (ImageView)itemView.findViewById(R.id.imageView_small);
            this.textViewTitle = (TextView)itemView.findViewById(R.id.textView_title);
            this.textViewAddress = (TextView)itemView.findViewById(R.id.textView_address);
            this.buttonMore = (Button) itemView.findViewById(R.id.button_more);

            this.buttonMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(ActivityItem activityItem, int position)
        {
            this.activityItem = activityItem;

            boolean large = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && (position % 10 == 4 || position % 10 == 8)
                    || this.itemView.getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE && position % 3 == 2;
            String image = !TextUtils.isEmpty(activityItem.getPhoto())? activityItem.getPhoto(): activityItem.getActivityTypePhoto();


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
            this.textViewTitle.setText(activityItem.getName());
            this.textViewAddress.setText(activityItem.getLocation());

        }

        @Override
        public void onClick(View v)
        {
            if(NearByActivitiesAdapter.this.listener != null)
                NearByActivitiesAdapter.this.listener.onActivityClick(v, this.activityItem);
        }
    }
}
