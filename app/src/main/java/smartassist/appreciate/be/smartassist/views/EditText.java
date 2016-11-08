package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;

/**
 * Created by Inneke De Clippel on 13/04/2016.
 */
public class EditText extends android.widget.EditText
{
    public EditText(Context context)
    {
        this(context, null);
    }

    public EditText(Context context, AttributeSet attrs)
    {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        if(this.isInEditMode())
            return;

        if(attrs != null)
        {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditText, 0, 0);

            int typefaceNumber = a.getInt(R.styleable.EditText_typeface, TypefaceHelper.MONTSERRAT_REGULAR);
            this.setTypeface(context, typefaceNumber);

            a.recycle();
        }
    }

    public void setTypeface(Context context, int typefaceNumber)
    {
        this.setTypeface(TypefaceHelper.getTypeface(context, typefaceNumber));
    }
}
