package smartassist.appreciate.be.smartassist.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.PinUnlockActivity;
import smartassist.appreciate.be.smartassist.views.Button;
import smartassist.appreciate.be.smartassist.views.NumPadView;

/**
 * Created by Inneke on 19/03/2015.
 */
public class PinUnlockFragment extends Fragment implements NumPadView.OnNumberClickListener, View.OnClickListener
{
    private LinearLayout layoutPinImages;
    private ImageView imageViewNumber1;
    private ImageView imageViewNumber2;
    private ImageView imageViewNumber3;
    private ImageView imageViewNumber4;

    private String enteredPin;
    private float baseTranslation;
    private float baseDuration;
    private boolean animating;

    public static final String KEY_PIN_SUCCESS = "pin_success";
    public static final String KEY_NEWS_ID = "news_id";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pin_unlock, container, false);

        this.layoutPinImages = (LinearLayout) view.findViewById(R.id.layout_pinImages);
        this.imageViewNumber1 = (ImageView) view.findViewById(R.id.imageView_pin1);
        this.imageViewNumber2 = (ImageView) view.findViewById(R.id.imageView_pin2);
        this.imageViewNumber3 = (ImageView) view.findViewById(R.id.imageView_pin3);
        this.imageViewNumber4 = (ImageView) view.findViewById(R.id.imageView_pin4);
        NumPadView numPadView = (NumPadView) view.findViewById(R.id.numPadView);
        Button buttonDelete = (Button) view.findViewById(R.id.button_delete);
        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel);

        numPadView.setListener(this);
        buttonDelete.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        this.baseTranslation = this.getResources().getDimension(R.dimen.pin_incorrect_animation_translate);
        this.baseDuration = 40;
        this.animating = false;

        return view;
    }

    @Override
    public void onPause()
    {
        if(this.animating)
        {
            this.animating = false;
            this.layoutPinImages.clearAnimation();
            this.enteredPin = "";
            this.setImages(this.enteredPin.length());
        }
        super.onPause();
    }

    @Override
    public void onNumberClick(int number)
    {
        if(this.animating)
            return;

        if(this.enteredPin == null)
            this.enteredPin = "";

        if(this.enteredPin.length() < 4)
            this.enteredPin += number;

        this.setImages(this.enteredPin.length());

        if(this.enteredPin.equals("1234"))
        {
            //TODO check with real pin code
            if(this.getActivity() != null && this.getActivity() instanceof PinUnlockActivity)
            {
                int newsId = this.getArguments() == null ? 0 : this.getArguments().getInt(KEY_NEWS_ID, 0);
                ((PinUnlockActivity) this.getActivity()).sendResult(true, newsId);
            }
        }
        else if(this.enteredPin.length() == 4)
        {
            this.startShakeAnimation(1);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_delete:
                if(!this.animating && this.enteredPin != null && this.enteredPin.length() > 0)
                {
                    this.enteredPin = this.enteredPin.substring(0, this.enteredPin.length() - 1);
                    this.setImages(this.enteredPin.length());
                }
                break;

            case R.id.button_cancel:
                if(this.getActivity() != null && this.getActivity() instanceof PinUnlockActivity)
                    ((PinUnlockActivity) this.getActivity()).sendResult(false, 0);
                break;
        }
    }

    private void setImages(int numbersPressed)
    {
        this.imageViewNumber1.setImageResource(numbersPressed > 0 ? R.drawable.shape_button_pin_oval_pressed : R.drawable.shape_button_pin_oval);
        this.imageViewNumber2.setImageResource(numbersPressed > 1 ? R.drawable.shape_button_pin_oval_pressed : R.drawable.shape_button_pin_oval);
        this.imageViewNumber3.setImageResource(numbersPressed > 2 ? R.drawable.shape_button_pin_oval_pressed : R.drawable.shape_button_pin_oval);
        this.imageViewNumber4.setImageResource(numbersPressed > 3 ? R.drawable.shape_button_pin_oval_pressed : R.drawable.shape_button_pin_oval);
    }

    private void startShakeAnimation(final int number)
    {
        if(number > 0 && number <= 5)
        {
            if(number == 1)
                this.animating = true;

            final float startX;
            final float stopX;

            switch (number)
            {
                case 1:
                    startX = this.baseTranslation * 0f;
                    stopX = this.baseTranslation * 1f;
                    break;
                case 2:
                    startX = this.baseTranslation * 1f;
                    stopX = this.baseTranslation * -1f;
                    break;
                case 3:
                    startX = this.baseTranslation * -1;
                    stopX = this.baseTranslation * 0.5f;
                    break;
                case 4:
                    startX = this.baseTranslation * 0.5f;
                    stopX = this.baseTranslation * -0.5f;
                    break;
                case 5:
                    startX = this.baseTranslation * -0.5f;
                    stopX = this.baseTranslation * 0f;
                    break;
                default:
                    startX = this.baseTranslation * 0f;
                    stopX = this.baseTranslation * 0f;
                    break;
            }

            final int duration = (int) ((Math.abs(startX) + Math.abs(stopX)) * this.baseDuration / this.baseTranslation);

            Animation animationTranslate = new TranslateAnimation(Animation.ABSOLUTE, startX, Animation.ABSOLUTE, stopX,
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
            animationTranslate.setDuration(duration);
            animationTranslate.setFillAfter(true);
            animationTranslate.setInterpolator(new AccelerateDecelerateInterpolator());
            animationTranslate.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {

                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    if(number == 3)
                    {
                        PinUnlockFragment.this.enteredPin = "";
                        PinUnlockFragment.this.setImages(PinUnlockFragment.this.enteredPin.length());
                    }
                    else if(number == 5)
                        PinUnlockFragment.this.animating = false;
                    PinUnlockFragment.this.startShakeAnimation(number + 1);
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {

                }
            });
            this.layoutPinImages.startAnimation(animationTranslate);
        }
    }
}
