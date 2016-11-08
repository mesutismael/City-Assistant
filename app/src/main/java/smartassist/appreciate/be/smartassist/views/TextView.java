package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;

/**
 * Created by Inneke on 29/01/2015.
 */
public class TextView extends android.widget.TextView
{
    private boolean capFirstChar;

    public TextView(Context context)
    {
        this(context, null);
    }

    public TextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        if(this.isInEditMode())
            return;

        if(attrs != null)
        {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextView, 0, 0);

            int typefaceNumber = a.getInt(R.styleable.TextView_typeface, TypefaceHelper.MONTSERRAT_REGULAR);
            this.setTypeface(context, typefaceNumber);
            this.capFirstChar = a.getBoolean(R.styleable.TextView_capFirstChar, false);

            a.recycle();
        }
    }

    public void setTypeface(Context context, int typefaceNumber)
    {
        this.setTypeface(TypefaceHelper.getTypeface(context, typefaceNumber));
    }

    @Override
    public void setText(CharSequence text, BufferType type)
    {
        if(this.capFirstChar)
        {
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)).toUpperCase() + text.subSequence(1, text.length());
            else if (text.length() > 0)
                text = String.valueOf(text.charAt(0)).toUpperCase();
        }
        super.setText(text, type);
    }
}
