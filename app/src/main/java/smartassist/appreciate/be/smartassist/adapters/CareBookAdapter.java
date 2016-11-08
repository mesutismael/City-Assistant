package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.CarebookItem;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 12/02/2016.
 */
public class CareBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<CarebookItem> carebookItems;

    private static final int TYPE_ITEM = 1;

    public CareBookAdapter()
    {
    }

    public void setCarebookItems(List<CarebookItem> carebookItems)
    {
        this.carebookItems = carebookItems;
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
                View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_care_book, viewGroup, false);
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
            CarebookItem item = this.carebookItems.get(i);
            boolean first = i == 0;
            boolean last = i == this.getItemCount() - 1;

            ((ItemViewHolder) viewHolder).bind(item, first, last);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.carebookItems != null ? this.carebookItems.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageViewSmiley;
        View viewIndicatorTop;
        View viewIndicatorBottom;
        TextView textViewDate;
        TextView textViewMessage;
        TextView textViewName;

        public ItemViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewSmiley = (ImageView) itemView.findViewById(R.id.imageView_smiley);
            this.viewIndicatorTop = itemView.findViewById(R.id.view_indicatorTop);
            this.viewIndicatorBottom = itemView.findViewById(R.id.view_indicatorBottom);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textView_date);
            this.textViewMessage = (TextView) itemView.findViewById(R.id.textView_message);
            this.textViewName = (TextView) itemView.findViewById(R.id.textView_name);
        }

        public void bind(CarebookItem carebookItem, boolean first, boolean last)
        {
            int smileyResId;

            switch (carebookItem.getSmiley())
            {
                case 1: smileyResId = R.drawable.health_smiley_5; break;
                case 2: smileyResId = R.drawable.health_smiley_4; break;
                case 3: smileyResId = R.drawable.health_smiley_3; break;
                case 4: smileyResId = R.drawable.health_smiley_2; break;
                case 5: smileyResId = R.drawable.health_smiley_1; break;
                default: smileyResId = R.drawable.health_smiley_3; break;
            }

            this.textViewDate.setText(DateUtils.formatCarebookDate(carebookItem.getDate(), this.itemView.getContext()));
            this.textViewMessage.setText(carebookItem.getMessage());
            this.textViewName.setText(carebookItem.getAuthor());
            this.imageViewSmiley.setImageResource(smileyResId);
            this.viewIndicatorTop.setVisibility(first ? View.INVISIBLE : View.VISIBLE);
            this.viewIndicatorBottom.setVisibility(last ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
