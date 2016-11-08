package smartassist.appreciate.be.smartassist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.Category;

/**
 * Created by Inneke on 12/02/2015.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>
{
    private List<Category> categories;
    private int selectedIndex;
    private OnCategoryClickListener listener;

    public CategoryAdapter()
    {
        this.selectedIndex = 0;
    }

    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
        this.notifyDataSetChanged();
    }

    public void setListener(OnCategoryClickListener listener)
    {
        this.listener = listener;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
        this.notifyDataSetChanged();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_category, viewGroup, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder viewHolder, int i)
    {
        boolean first = i == 0;
        Category category = first ? null : this.categories.get(i - 1);
        boolean selected = i == this.selectedIndex;

        viewHolder.bind(category, first, selected, i);
    }

    @Override
    public int getItemCount()
    {
        return this.categories != null ? this.categories.size() + 1 : 1;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ToggleButton toggleButton;
        private Category category;
        private boolean selected;
        private int position;

        public CategoryViewHolder(View itemView)
        {
            super(itemView);

            this.toggleButton = (ToggleButton) itemView.findViewById(R.id.toggleButton_category);

            this.toggleButton.setOnClickListener(this);
        }

        public void bind(Category category, boolean first, boolean selected, int position)
        {
            this.category = category;
            this.selected = selected;
            this.position = position;
            String name = first ? this.itemView.getContext().getString(R.string.poi_category_all) : category.getName();
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
                if (CategoryAdapter.this.listener != null)
                    CategoryAdapter.this.listener.onCategoryClick(v, this.category);
                CategoryAdapter.this.setSelectedIndex(this.position);
            }
            else
            {
                this.toggleButton.setChecked(true);
            }
        }
    }

    public interface OnCategoryClickListener
    {
        void onCategoryClick(View caller, Category category);
    }
}
