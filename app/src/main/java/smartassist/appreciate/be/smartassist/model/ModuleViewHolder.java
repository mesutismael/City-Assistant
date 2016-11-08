package smartassist.appreciate.be.smartassist.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 28/01/2015.
 */
public class ModuleViewHolder
{
    private LayoutModule module;
    private TextView textViewCircle;
    private LinearLayout layoutTile;
    private ImageView imageViewIcon;
    private TextView textViewTile;

    public ModuleViewHolder(LayoutModule module, TextView textViewCircle, LinearLayout layoutTile, ImageView imageViewIcon, TextView textViewTile, int numberOfNotifications)
    {
        this.module = module;
        this.textViewCircle = textViewCircle;
        this.layoutTile = layoutTile;
        this.imageViewIcon = imageViewIcon;
        this.textViewTile = textViewTile;
        this.setNumberOfNotifications(numberOfNotifications);
    }

    public LayoutModule getModule()
    {
        return module;
    }

    public TextView getTextViewCircle()
    {
        return textViewCircle;
    }

    public LinearLayout getLayoutTile()
    {
        return layoutTile;
    }

    public ImageView getImageViewIcon()
    {
        return imageViewIcon;
    }

    public TextView getTextViewTile()
    {
        return textViewTile;
    }

    public void setNumberOfNotifications(int numberOfNotifications)
    {
        this.textViewCircle.setText(String.valueOf(numberOfNotifications));
        this.textViewCircle.setVisibility(numberOfNotifications > 0 ? View.VISIBLE : View.GONE);
    }
}
