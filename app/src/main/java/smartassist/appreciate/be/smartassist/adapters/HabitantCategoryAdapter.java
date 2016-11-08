package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.Habitant;

/**
 * Created by Inneke De Clippel on 3/08/2016.
 */
public class HabitantCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Habitant> habitants;
    private int selectedIndex;
    private OnHabitantCategoryClickListener listener;

    private static final int TYPE_ITEM = 1;

    public HabitantCategoryAdapter()
    {
        this.selectedIndex = 0;
    }

    public void setHabitants(List<Habitant> habitants)
    {
        this.habitants = habitants;
        this.notifyDataSetChanged();
    }

    public void setListener(OnHabitantCategoryClickListener listener)
    {
        this.listener = listener;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
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
                View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_category, viewGroup, false);
                return new CategoryViewHolder(viewItem);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        if(viewHolder instanceof CategoryViewHolder)
        {
            Habitant habitant = this.habitants.get(i);
            boolean selected = i == this.selectedIndex;

            ((CategoryViewHolder) viewHolder).bind(habitant, selected, i);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.habitants != null ? this.habitants.size() : 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ToggleButton toggleButton;
        private Habitant habitant;
        private boolean selected;
        private int position;

        public CategoryViewHolder(View itemView)
        {
            super(itemView);

            this.toggleButton = (ToggleButton) itemView.findViewById(R.id.toggleButton_category);

            this.toggleButton.setOnClickListener(this);
        }

        public void bind(Habitant habitant, boolean selected, int position)
        {
            this.habitant = habitant;
            this.selected = selected;
            this.position = position;
            String name = habitant.getName();

            this.toggleButton.setText(name);
            this.toggleButton.setTextOn(name);
            this.toggleButton.setTextOff(name);
            this.toggleButton.setChecked(selected);
        }

        @Override
        public void onClick(View v)
        {
            if(!this.selected)
            {
                if (HabitantCategoryAdapter.this.listener != null)
                    HabitantCategoryAdapter.this.listener.onHabitantCategoryClick(v, this.habitant);
                HabitantCategoryAdapter.this.setSelectedIndex(this.position);
            }
            else
            {
                this.toggleButton.setChecked(true);
            }
        }
    }

    public interface OnHabitantCategoryClickListener
    {
        void onHabitantCategoryClick(View caller, Habitant habitant);
    }
}
