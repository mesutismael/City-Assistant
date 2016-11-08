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
import smartassist.appreciate.be.smartassist.model.Rss;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.views.Button;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 25/02/2015.
 */
public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssViewHolder>
{
    private List<Rss> rss;
    private OnRssClickListener listener;

    public RssAdapter()
    {
    }

    public void setRss(List<Rss> rss)
    {
        this.rss = rss;
        this.notifyDataSetChanged();
    }

    public void setListener(OnRssClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public RssViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_rss, viewGroup, false);
        return new RssViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RssViewHolder viewHolder, int i)
    {
        Rss rssItem = this.rss.get(i);

        viewHolder.bind(rssItem, i);
    }

    @Override
    public int getItemCount()
    {
        return this.rss != null ? this.rss.size() : 0;
    }

    public class RssViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imageViewLarge;
        private ImageView imageViewSmall;
        private TextView textViewDate;
        private TextView textViewTitle;
        private TextView textViewDescription;
        private Button buttonMore;

        private Rss rss;

        public RssViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewLarge = (ImageView) itemView.findViewById(R.id.imageView_large);
            this.imageViewSmall = (ImageView) itemView.findViewById(R.id.imageView_small);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textView_date);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textView_title);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.textView_description);
            this.buttonMore = (Button) itemView.findViewById(R.id.button_more);

            this.buttonMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Rss rss, int position)
        {
            this.rss = rss;

            boolean landscape = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean large = (landscape && (position % 10 == 4 || position % 10 == 8)) || (!landscape && position % 3 == 2);

            if(large)
            {
                this.imageViewLarge.setVisibility(!TextUtils.isEmpty(rss.getImage()) ? View.VISIBLE : View.GONE);
                this.imageViewSmall.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(rss.getImage()))
                    Picasso.with(this.imageViewLarge.getContext())
                            .load(rss.getImage())
                            .into(this.imageViewLarge);
            }
            else
            {
                this.imageViewLarge.setVisibility(View.GONE);
                this.imageViewSmall.setVisibility(!TextUtils.isEmpty(rss.getImage()) ? View.VISIBLE : View.GONE);
                if(!TextUtils.isEmpty(rss.getImage()))
                    Picasso.with(this.imageViewSmall.getContext())
                            .load(rss.getImage())
                            .into(this.imageViewSmall);
            }

            if(large || TextUtils.isEmpty(rss.getImage()))
            {
                this.textViewTitle.setVisibility(View.VISIBLE);
                this.textViewTitle.setText(rss.getTitle());
                this.textViewDescription.setText(rss.getIntro());
            }
            else
            {
                this.textViewTitle.setVisibility(View.GONE);
                this.textViewDescription.setText(rss.getTitle());
            }

            this.textViewDate.setText(DateUtils.formatRssDate(rss.getDate()));
        }

        @Override
        public void onClick(View v)
        {
            if(RssAdapter.this.listener != null)
                RssAdapter.this.listener.onRssClick(v, this.rss);
        }
    }

    public interface OnRssClickListener
    {
        void onRssClick(View caller, Rss rss);
    }
}
