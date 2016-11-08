package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;

/**
 * Created by Inneke De Clippel on 3/08/2016.
 */
public class KeyValueView extends LinearLayout
{
    private TextView textViewKey;
    private TextView textViewValue;
    private String emptyValue;

    public KeyValueView(Context context)
    {
        this(context, null);
    }

    public KeyValueView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public KeyValueView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        this.setOrientation(HORIZONTAL);

        this.textViewKey = new TextView(context);
        this.textViewKey.setTypeface(context, TypefaceHelper.MONTSERRAT_BOLD);
        LayoutParams paramsKey = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        this.addView(this.textViewKey, paramsKey);

        this.textViewValue = new TextView(context);
        this.textViewValue.setTypeface(context, TypefaceHelper.MONTSERRAT_REGULAR);
        LayoutParams paramsValue = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        this.addView(this.textViewValue, paramsValue);

        if(attrs != null)
        {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KeyValueView, 0, 0);

            String emptyValue = a.getString(R.styleable.KeyValueView_emptyValue);
            String key = a.getString(R.styleable.KeyValueView_key);
            String value = a.getString(R.styleable.KeyValueView_value);

            this.setEmptyValue(emptyValue);
            this.setKey(key);
            this.setValue(value);

            a.recycle();
        }
    }

    public void setKey(int keyRes)
    {
        this.textViewKey.setText(keyRes);
    }

    public void setKey(String key)
    {
        this.textViewKey.setText(key);
    }

    public void setValue(int valueRes)
    {
        this.setValue(this.getContext().getString(valueRes));
    }

    public void setValue(String value)
    {
        this.textViewValue.setText(!TextUtils.isEmpty(value) ? value : this.emptyValue);
    }

    public void setEmptyValue(int emptyValueRes)
    {
        this.setEmptyValue(this.getContext().getString(emptyValueRes));
    }

    public void setEmptyValue(String emptyValue)
    {
        this.emptyValue = emptyValue;
    }
}
