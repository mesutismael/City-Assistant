package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;

/**
 * Created by Inneke on 19/03/2015.
 */
public class NumPadView extends RelativeLayout implements View.OnClickListener
{
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button0;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    private TextView textView9;
    private TextView textView0;

    private int buttonMaxSize;
    private int buttonPaddingHorizontal;
    private int buttonPaddingVertical;

    private OnNumberClickListener listener;

    private static final int ROWS = 4;
    private static final int COLUMNS = 3;
    private static final int ID_BUTTON_1 = 1;
    private static final int ID_BUTTON_2 = 2;
    private static final int ID_BUTTON_3 = 3;
    private static final int ID_BUTTON_4 = 4;
    private static final int ID_BUTTON_5 = 5;
    private static final int ID_BUTTON_6 = 6;
    private static final int ID_BUTTON_7 = 7;
    private static final int ID_BUTTON_8 = 8;
    private static final int ID_BUTTON_9 = 9;
    private static final int ID_BUTTON_0 = 10;

    public NumPadView(Context context)
    {
        this(context, null);
    }

    public NumPadView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public NumPadView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        this.buttonMaxSize = (int) this.getResources().getDimension(R.dimen.pin_button_max_size);
        this.buttonPaddingHorizontal = (int) this.getResources().getDimension(R.dimen.pin_button_padding_horizontal);
        this.buttonPaddingVertical = (int) this.getResources().getDimension(R.dimen.pin_button_padding_vertical);

        this.button1 = this.createButton(ID_BUTTON_1);
        this.button2 = this.createButton(ID_BUTTON_2);
        this.button3 = this.createButton(ID_BUTTON_3);
        this.button4 = this.createButton(ID_BUTTON_4);
        this.button5 = this.createButton(ID_BUTTON_5);
        this.button6 = this.createButton(ID_BUTTON_6);
        this.button7 = this.createButton(ID_BUTTON_7);
        this.button8 = this.createButton(ID_BUTTON_8);
        this.button9 = this.createButton(ID_BUTTON_9);
        this.button0 = this.createButton(ID_BUTTON_0);

        this.addView(this.button1);
        this.addView(this.button2);
        this.addView(this.button3);
        this.addView(this.button4);
        this.addView(this.button5);
        this.addView(this.button6);
        this.addView(this.button7);
        this.addView(this.button8);
        this.addView(this.button9);
        this.addView(this.button0);

        this.textView1 = this.createTextView("1");
        this.textView2 = this.createTextView("2");
        this.textView3 = this.createTextView("3");
        this.textView4 = this.createTextView("4");
        this.textView5 = this.createTextView("5");
        this.textView6 = this.createTextView("6");
        this.textView7 = this.createTextView("7");
        this.textView8 = this.createTextView("8");
        this.textView9 = this.createTextView("9");
        this.textView0 = this.createTextView("0");

        this.addView(this.textView1);
        this.addView(this.textView2);
        this.addView(this.textView3);
        this.addView(this.textView4);
        this.addView(this.textView5);
        this.addView(this.textView6);
        this.addView(this.textView7);
        this.addView(this.textView8);
        this.addView(this.textView9);
        this.addView(this.textView0);
    }

    public void setListener(OnNumberClickListener listener)
    {
        this.listener = listener;
    }

    private Button createButton(int id)
    {
        Button button = new Button(this.getContext());
        button.setId(id);
        button.setOnClickListener(this);
        button.setBackgroundResource(R.drawable.selector_button_pin_oval);
        button.setMinWidth(0);
        button.setMinHeight(0);
        button.setPadding(0, 0, 0, 0);
        button.setGravity(Gravity.CENTER);
        return button;
    }

    private TextView createTextView(String text)
    {
        TextView textView = new TextView(this.getContext());
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.pin_button_text));
        textView.setTypeface(this.getContext(), TypefaceHelper.MONTSERRAT_HAIRLINE);
        return textView;
    }

    @Override
    public void onClick(View v)
    {
        int number = -1;

        switch (v.getId())
        {
            case ID_BUTTON_1: number = 1; break;
            case ID_BUTTON_2: number = 2; break;
            case ID_BUTTON_3: number = 3; break;
            case ID_BUTTON_4: number = 4; break;
            case ID_BUTTON_5: number = 5; break;
            case ID_BUTTON_6: number = 6; break;
            case ID_BUTTON_7: number = 7; break;
            case ID_BUTTON_8: number = 8; break;
            case ID_BUTTON_9: number = 9; break;
            case ID_BUTTON_0: number = 0; break;
        }

        if(number != -1 && this.listener != null)
            this.listener.onNumberClick(number);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);

        final int width = r - l;
        final int height = b - t;
        final int buttonAvailableWidth = (width - ((COLUMNS - 1) * this.buttonPaddingHorizontal)) / COLUMNS;
        final int buttonAvailableHeight = (height - ((ROWS - 1) * this.buttonPaddingVertical)) / ROWS;
        final int buttonSize = Math.min(Math.min(buttonAvailableWidth, buttonAvailableHeight), this.buttonMaxSize);
        final int totalWidth = ((COLUMNS - 1) * this.buttonPaddingHorizontal) + (COLUMNS * buttonSize);
        final int totalHeight = ((ROWS - 1) * this.buttonPaddingVertical) + (ROWS * buttonSize);
        final int totalPaddingLeft = (width - totalWidth) / 2;
        final int totalPaddingTop = (height - totalHeight) / 2;

        final int leftCol0 = totalPaddingLeft;
        final int leftCol1 = totalPaddingLeft + (buttonSize + this.buttonPaddingHorizontal);
        final int leftCol2 = totalPaddingLeft + (buttonSize + this.buttonPaddingHorizontal) * 2;
        final int topRow0 = totalPaddingTop;
        final int topRow1 = totalPaddingTop + (buttonSize + this.buttonPaddingVertical);
        final int topRow2 = totalPaddingTop + (buttonSize + this.buttonPaddingVertical) * 2;
        final int topRow3 = totalPaddingTop + (buttonSize + this.buttonPaddingVertical) * 3;

        this.button1.layout(leftCol0, topRow0, leftCol0 + buttonSize, topRow0 + buttonSize);
        this.button2.layout(leftCol1, topRow0, leftCol1 + buttonSize, topRow0 + buttonSize);
        this.button3.layout(leftCol2, topRow0, leftCol2 + buttonSize, topRow0 + buttonSize);
        this.button4.layout(leftCol0, topRow1, leftCol0 + buttonSize, topRow1 + buttonSize);
        this.button5.layout(leftCol1, topRow1, leftCol1 + buttonSize, topRow1 + buttonSize);
        this.button6.layout(leftCol2, topRow1, leftCol2 + buttonSize, topRow1 + buttonSize);
        this.button7.layout(leftCol0, topRow2, leftCol0 + buttonSize, topRow2 + buttonSize);
        this.button8.layout(leftCol1, topRow2, leftCol1 + buttonSize, topRow2 + buttonSize);
        this.button9.layout(leftCol2, topRow2, leftCol2 + buttonSize, topRow2 + buttonSize);
        this.button0.layout(leftCol1, topRow3, leftCol1 + buttonSize, topRow3 + buttonSize);

        this.layoutTextView(this.textView1, buttonSize, leftCol0, topRow0);
        this.layoutTextView(this.textView2, buttonSize, leftCol1, topRow0);
        this.layoutTextView(this.textView3, buttonSize, leftCol2, topRow0);
        this.layoutTextView(this.textView4, buttonSize, leftCol0, topRow1);
        this.layoutTextView(this.textView5, buttonSize, leftCol1, topRow1);
        this.layoutTextView(this.textView6, buttonSize, leftCol2, topRow1);
        this.layoutTextView(this.textView7, buttonSize, leftCol0, topRow2);
        this.layoutTextView(this.textView8, buttonSize, leftCol1, topRow2);
        this.layoutTextView(this.textView9, buttonSize, leftCol2, topRow2);
        this.layoutTextView(this.textView0, buttonSize, leftCol1, topRow3);
    }

    private void layoutTextView(TextView textView, int buttonSize, int leftCol, int topRow)
    {
        final int textSize = (int) (buttonSize * 0.65);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        final Paint paint = textView.getPaint();

        final int textViewHeight = textSize;

        String text = textView.getText().toString();
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        final int textHeight = textBounds.height();
        final int textWidth = (int) paint.measureText(text);
        final int negativePaddingTop = (textViewHeight - textHeight) / 2;

        final int extraPaddingTop = (buttonSize - textViewHeight) / 2;
        int extraPaddingLeft = (buttonSize - textWidth) / 2;
        textView.layout(leftCol + extraPaddingLeft, topRow + extraPaddingTop - negativePaddingTop, leftCol + extraPaddingLeft + textWidth, topRow + extraPaddingTop - negativePaddingTop + textViewHeight);
    }

    public interface OnNumberClickListener
    {
        void onNumberClick(int number);
    }
}
