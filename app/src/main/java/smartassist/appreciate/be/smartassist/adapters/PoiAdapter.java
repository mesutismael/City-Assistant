package smartassist.appreciate.be.smartassist.adapters;

import android.content.Context;
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
import smartassist.appreciate.be.smartassist.contentproviders.OpeningTimesContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.PoiContentProvider;
import smartassist.appreciate.be.smartassist.model.Poi;
import smartassist.appreciate.be.smartassist.utils.CategoryHelper;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.views.TextView;
import smartassist.appreciate.be.smartassist.views.ToggleButton;

/**
 * Created by Inneke on 10/02/2015.
 */
public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.PoiViewHolder>
{
    private List<Poi> poiItems;
    private OnPoiClickListener listener;

    public PoiAdapter()
    {
    }

    public void setPoiItems(List<Poi> poiItems)
    {
        this.poiItems = poiItems;
        this.notifyDataSetChanged();
    }

    public void setOnPoiClickListener(OnPoiClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public PoiViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_poi, viewGroup, false);
        return new PoiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PoiViewHolder viewHolder, int i)
    {
        Poi poi = this.poiItems.get(i);

        viewHolder.bind(poi, i);
    }

    @Override
    public int getItemCount()
    {
        return this.poiItems != null ? this.poiItems.size() : 0;
    }

    public class PoiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imageViewLarge;
        private ImageView imageViewSmall;
        private TextView textViewTitle;
        private ImageView imageViewOpeningTime;
        private TextView textViewAddress;
        private TextView textViewPhoneTitle;
        private TextView textViewPhone;
        private Button buttonMore;
        private ImageView ImageViewFavorite;

        private Poi poi;

        public PoiViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewLarge = (ImageView) itemView.findViewById(R.id.imageView_large);
            this.imageViewSmall = (ImageView)itemView.findViewById(R.id.imageView_small);
            this.imageViewOpeningTime=(ImageView)itemView.findViewById(R.id.imageView_openingTimeIcon);
            this.textViewTitle = (TextView)itemView.findViewById(R.id.textView_title);
            this.textViewAddress = (TextView)itemView.findViewById(R.id.textView_address);
            this.textViewPhoneTitle = (TextView)itemView.findViewById(R.id.textView_phone_title);
            this.textViewPhone = (TextView)itemView.findViewById(R.id.textView_phone);
            this.buttonMore = (Button) itemView.findViewById(R.id.button_more);
            this.ImageViewFavorite=(ImageView)itemView.findViewById(R.id.imageView_favorites) ;

            this.buttonMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Poi poi, int position)
        {
            this.poi = poi;

            boolean large = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && (position % 10 == 4 || position % 10 == 8)
                    || this.itemView.getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE && position % 3 == 2;

            String image = !TextUtils.isEmpty(poi.getImage()) ? poi.getImage(): CategoryHelper.getPoiPhoto(poi, large);


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
            this.textViewTitle.setText(poi.getName());
            this.textViewAddress.setText(poi.getFullAddress());
            this.textViewPhone.setText(poi.getPhone());
            this.textViewPhone.setVisibility(TextUtils.isEmpty(poi.getPhone()) ? View.GONE : View.VISIBLE);
            this.textViewPhoneTitle.setVisibility(TextUtils.isEmpty(poi.getPhone()) ? View.GONE : View.VISIBLE);
            this.imageViewOpeningTime.setVisibility(poi.getOpeningTimeTimes()==Constants.TRUE ? View.VISIBLE : View.GONE);
            this.ImageViewFavorite.setVisibility(poi.getFavorite()==Constants.TRUE? View.VISIBLE:View.GONE);

        }

        @Override
        public void onClick(View v)
        {
            if(PoiAdapter.this.listener != null)
                PoiAdapter.this.listener.onPoiClick(v, this.poi);
        }
    }

    public interface OnPoiClickListener
    {
        void onPoiClick(View caller, Poi poi);
    }
}
