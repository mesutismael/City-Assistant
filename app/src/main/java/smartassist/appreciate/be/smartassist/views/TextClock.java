package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;

/**
 * Created by Inneke on 29/01/2015.
 */
public class TextClock extends android.widget.TextClock
{
    public TextClock(Context context)
    {
        this(context, null);
    }

    public TextClock(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TextClock(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        if(this.isInEditMode())
            return;

        if(attrs != null)
        {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextClock, 0, 0);

            int typefaceNumber = a.getInt(R.styleable.TextClock_typeface, TypefaceHelper.MONTSERRAT_REGULAR);
            this.setTypeface(context, typefaceNumber);

            a.recycle();
        }
    }

    public void setTypeface(Context context, int typefaceNumber)
    {
        this.setTypeface(TypefaceHelper.getTypeface(context, typefaceNumber));
    }
}
