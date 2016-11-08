package smartassist.appreciate.be.smartassist.activities;

import com.afollestad.materialdialogs.MaterialDialog;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke on 18/03/2015.
 */
public class BaseScreenSaverActivity extends BaseActivity
{
    @Override
    public void applyDialogStyle(MaterialDialog.Builder builder)
    {
        builder.btnSelector(R.drawable.selector_button_dialog_screen_saver);
        builder.positiveColorRes(R.color.selector_button_dialog_screen_saver_text);
        builder.neutralColorRes(R.color.selector_button_dialog_screen_saver_text);
        builder.negativeColorRes(R.color.selector_button_dialog_screen_saver_text);
        builder.background(R.drawable.shape_dialog_screen_saver);
    }

    @Override
    public int getCroutonBackground()
    {
        return R.drawable.shape_crouton_screen_saver;
    }

    @Override
    public void onUserInteraction()
    {
    }

    @Override
    public void startTimerScreenSaver()
    {
    }

    @Override
    public void stopTimerScreenSaver()
    {
    }
}
