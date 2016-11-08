package smartassist.appreciate.be.smartassist.decorations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke De Clippel on 26/01/2016.
 */
public class DividerDecoration extends RecyclerView.ItemDecoration
{
    private Context context;
    private float height;
    private Paint paint;
    private boolean bottomDivider;

    public DividerDecoration(Context context)
    {
        this.context = context;
        this.setHeightRes(R.dimen.list_divider_height);

        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(Color.LTGRAY);
    }

    public void setColor(int color)
    {
        this.paint.setColor(color);
    }

    public void setColorRes(@ColorRes int colorRes)
    {
        this.paint.setColor(ContextCompat.getColor(this.context, colorRes));
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public void setHeightRes(@DimenRes int heightRes)
    {
        this.height = this.context.getResources().getDimension(heightRes);
    }

    public void setBottomDivider(boolean bottomDivider)
    {
        this.bottomDivider = bottomDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildLayoutPosition(view) < 1)
            return;

        if (parent.getLayoutManager() instanceof LinearLayoutManager)
        {
            switch (((LinearLayoutManager) parent.getLayoutManager()).getOrientation())
            {
                case LinearLayoutManager.VERTICAL:
                    outRect.top = (int) this.height;

                    int layoutPosition = parent.getChildLayoutPosition(view);
                    int lastItem = parent.getAdapter().getItemCount() - 1;
                    if(this.bottomDivider && layoutPosition == lastItem)
                    {
                        outRect.bottom = (int) this.height;
                    }
                    break;

                case LinearLayoutManager.HORIZONTAL:
                    outRect.left = (int) this.height;
                    break;
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        if (parent.getLayoutManager() instanceof LinearLayoutManager)
        {
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            int childCount = parent.getChildCount();
            float left = parent.getPaddingLeft();
            float top = parent.getPaddingTop();
            float right = parent.getWidth() - parent.getPaddingRight();
            float bottom = parent.getHeight() - parent.getPaddingBottom();

            switch (((LinearLayoutManager) parent.getLayoutManager()).getOrientation())
            {
                case LinearLayoutManager.VERTICAL:
                    for (int i = 0; i < childCount; i++)
                    {
                        View child = parent.getChildAt(i);

                        if (parent.getChildLayoutPosition(child) < 1)
                            continue;

                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                        float decorationBottom = layoutManager.getDecoratedTop(child) - params.topMargin;
                        float decorationTop = decorationBottom - this.height;

                        c.drawRect(left, decorationTop, right, decorationBottom, this.paint);

                        int layoutPosition = parent.getChildLayoutPosition(child);
                        int lastItem = parent.getAdapter().getItemCount() - 1;
                        if(this.bottomDivider && layoutPosition == lastItem)
                        {
                            decorationTop = layoutManager.getDecoratedBottom(child) + params.bottomMargin;
                            decorationBottom = decorationTop + this.height;

                            c.drawRect(left, decorationTop, right, decorationBottom, this.paint);
                        }
                    }
                    break;

                case LinearLayoutManager.HORIZONTAL:
                    for (int i = 0; i < childCount; i++)
                    {
                        View child = parent.getChildAt(i);

                        if (parent.getChildLayoutPosition(child) < 1)
                            continue;

                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                        float decorationRight = layoutManager.getDecoratedLeft(child) - params.leftMargin;
                        float decorationLeft = decorationRight - this.height;

                        c.drawRect(decorationLeft, top, decorationRight, bottom, this.paint);
                    }
                    break;
            }
        }

    }
}
