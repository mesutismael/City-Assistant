package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke De Clippel on 15/02/2016.
 */
public class BoundedLinearLayout extends LinearLayout
{
    private int boundedWidth;
    private int boundedHeight;

    public BoundedLinearLayout(Context context)
    {
        this(context, null);
    }

    public BoundedLinearLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BoundedLinearLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        if(attrs != null)
        {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BoundedLinearLayout, 0, 0);

            this.boundedWidth = a.getDimensionPixelSize(R.styleable.BoundedLinearLayout_boundedWidth, 0);
            this.boundedHeight = a.getDimensionPixelSize(R.styleable.BoundedLinearLayout_boundedHeight, 0);

            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec)
    {
        int measuredWidth = View.MeasureSpec.getSize(widthSpec);
        int measuredHeight = View.MeasureSpec.getSize(heightSpec);

        if(this.boundedWidth > 0 && this.boundedWidth < measuredWidth)
        {
            int measureMode = View.MeasureSpec.getMode(widthSpec);
            widthSpec = View.MeasureSpec.makeMeasureSpec(this.boundedWidth, measureMode);
        }

        if(this.boundedHeight > 0 && this.boundedHeight < measuredHeight)
        {
            int measureMode = View.MeasureSpec.getMode(heightSpec);
            heightSpec = View.MeasureSpec.makeMeasureSpec(this.boundedHeight, measureMode);
        }

        super.onMeasure(widthSpec, heightSpec);
    }
}
