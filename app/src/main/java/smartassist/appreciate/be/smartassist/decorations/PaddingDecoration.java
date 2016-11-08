package smartassist.appreciate.be.smartassist.decorations;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke De Clippel on 26/01/2016.
 */
public class PaddingDecoration extends RecyclerView.ItemDecoration
{
    private Context context;
    private float paddingVertical;
    private float paddingHorizontal;
    private boolean addPaddingToTop;
    private boolean doubleSpaceBetweenItems;

    public PaddingDecoration(Context context)
    {
        this.context = context;
        this.addPaddingToTop = true;
        this.doubleSpaceBetweenItems = false;
        this.setPaddingRes(R.dimen.list_padding_vertical_inner, R.dimen.list_padding_horizontal_inner);
    }

    public void setPadding(float paddingVertical, float paddingHorizontal)
    {
        this.paddingVertical = paddingVertical;
        this.paddingHorizontal = paddingHorizontal;
    }

    public void setPaddingRes(@DimenRes int paddingVerticalRes, @DimenRes int paddingHorizontalRes)
    {
        this.paddingVertical = paddingVerticalRes != 0 ? this.context.getResources().getDimension(paddingVerticalRes) : 0;
        this.paddingHorizontal = paddingHorizontalRes != 0 ? this.context.getResources().getDimension(paddingHorizontalRes) : 0;
    }

    /**
     * This is currently only supported for GridLayoutManagers.
     * @param addPaddingToTop false when the first item doesn't need to have top padding. Default is true.
     */
    public void setAddPaddingToTop(boolean addPaddingToTop)
    {
        this.addPaddingToTop = addPaddingToTop;
    }

    /**
     * When x is the padding in pixels between items without a divider, it is better to use a spacing
     * of 2x when also drawing a divider. This will cause the items to have the same amount of padding
     * above and below. Otherwise the divider will be in the center of the padding and it will seem
     * the item only has a padding of x/2. This is currently only supported for LinearLayoutManagers.
     * @param doubleSpaceBetweenItems true when using dividers. Default is false.
     */
    public void setDoubleSpaceBetweenItems(boolean doubleSpaceBetweenItems)
    {
        this.doubleSpaceBetweenItems = doubleSpaceBetweenItems;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getLayoutManager() instanceof  GridLayoutManager)
        {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanSize = params.getSpanSize();
            int spanIndex = params.getSpanIndex();
            int spanCount = layoutManager.getSpanCount();
            int row = layoutManager.getSpanSizeLookup().getSpanGroupIndex(parent.getChildLayoutPosition(view), spanCount);
            int lastRow = layoutManager.getSpanSizeLookup().getSpanGroupIndex(parent.getAdapter().getItemCount() - 1, spanCount);

            switch (layoutManager.getOrientation())
            {
                case GridLayoutManager.VERTICAL:
                    outRect.left = (int) ((spanCount - spanIndex) * this.paddingHorizontal / spanCount);
                    outRect.right = (int) ((spanSize + spanIndex) * this.paddingHorizontal / spanCount);
                    outRect.top = (int) (row == 0 ? this.addPaddingToTop ? this.paddingVertical : 0 : this.paddingVertical / 2);
                    outRect.bottom = (int) (row == lastRow ? this.paddingVertical : this.paddingVertical / 2);
                    break;

                case GridLayoutManager.HORIZONTAL:
                    outRect.left = (int) (row == 0 ? this.addPaddingToTop ? this.paddingHorizontal : 0 : this.paddingHorizontal / 2);
                    outRect.right = (int) (row == lastRow ? this.paddingHorizontal : this.paddingHorizontal / 2);
                    outRect.top = (int) ((spanCount - spanIndex) * this.paddingVertical / spanCount);
                    outRect.bottom = (int) ((spanSize + spanIndex) * this.paddingVertical / spanCount);
                    break;
            }

        }
        else if(parent.getLayoutManager() instanceof LinearLayoutManager)
        {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int position = parent.getChildLayoutPosition(view);
            int lastItem = parent.getAdapter().getItemCount() - 1;

            switch (layoutManager.getOrientation())
            {
                case LinearLayoutManager.VERTICAL:
                    outRect.left = (int) this.paddingHorizontal;
                    outRect.right = (int) this.paddingHorizontal;
                    outRect.top = (int) (position == 0 || this.doubleSpaceBetweenItems ? this.paddingVertical : this.paddingVertical / 2);
                    outRect.bottom = (int) (position == lastItem || this.doubleSpaceBetweenItems ? this.paddingVertical : this.paddingVertical / 2);
                    break;

                case LinearLayoutManager.HORIZONTAL:
                    outRect.left = (int) (position == 0 || this.doubleSpaceBetweenItems ? this.paddingHorizontal : this.paddingHorizontal / 2);
                    outRect.right = (int) (position == lastItem || this.doubleSpaceBetweenItems ? this.paddingHorizontal : this.paddingHorizontal / 2);
                    outRect.top = (int) this.paddingVertical;
                    outRect.bottom = (int) this.paddingVertical;
                    break;
            }
        }
    }
}
