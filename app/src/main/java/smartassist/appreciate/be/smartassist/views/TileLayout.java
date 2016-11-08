package smartassist.appreciate.be.smartassist.views;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.LayoutModule;
import smartassist.appreciate.be.smartassist.model.Module;
import smartassist.appreciate.be.smartassist.model.ModuleViewHolder;
import smartassist.appreciate.be.smartassist.utils.ModuleHelper;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;

/**
 * Created by Inneke on 27/01/2015.
 */
public class TileLayout extends RelativeLayout
{
    private OnTileClickListener onTileClickListener;
    private List<ModuleViewHolder> modules;
    private int rows;
    private int columns;
    private int paddingSize;
    private int orientation;
    private List<LayoutModule> tiles;
    private Map<Integer, Integer> notifications;

    private static final int ROWS_LANDSCAPE = 4;
    private static final int COLS_LANDSCAPE = 8;
    private static final int ROWS_PORTRAIT = 8;
    private static final int COLS_PORTRAIT = 4;

    public TileLayout(Context context)
    {
        this(context, null);
    }

    public TileLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TileLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        this.notifications = new HashMap<>();
        this.modules = new ArrayList<>();
        this.rows = 0;
        this.columns = 0;
        this.paddingSize = (int) this.getResources().getDimension(R.dimen.main_tile_margin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if(this.rows == 0 || this.columns == 0 || width == 0 || height == 0)
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else
        {
            int tileWidth = (width - ((this.columns + 1) * this.paddingSize)) / this.columns;
            int tileHeight = (height - ((this.rows + 1) * this.paddingSize)) / this.rows;
            int tileSize = Math.min(tileWidth, tileHeight);

            int newWidth = (this.columns * tileSize) + ((this.columns + 1) * paddingSize);
            int newHeight = (this.rows * tileSize) + ((this.rows + 1) * paddingSize);

            super.onMeasure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if(this.columns != 0 && this.rows != 0 && this.modules != null && this.modules.size() > 0)
        {
            final int tileSize = (right - left - ((this.columns + 1) * this.paddingSize)) / this.columns;
            final int circleSize = (int) this.getResources().getDimension(R.dimen.main_tile_circle);
            final int iconSize = tileSize / 2;

            for(ModuleViewHolder moduleViewHolder : this.modules)
            {
                LayoutModule tile = moduleViewHolder.getModule();

                int col = this.orientation == Configuration.ORIENTATION_LANDSCAPE ? tile.getColumnLandscape() : tile.getColumnPortrait();
                int row = this.orientation == Configuration.ORIENTATION_LANDSCAPE ? tile.getRowLandscape() : tile.getRowPortrait();
                int colSpan = this.orientation == Configuration.ORIENTATION_LANDSCAPE ? tile.getColumnSpanLandscape() : tile.getColumnSpanPortrait();
                int rowSpan = this.orientation == Configuration.ORIENTATION_LANDSCAPE ? tile.getRowSpanLandscape() : tile.getRowSpanPortrait();

                int tileWidth = (colSpan * tileSize) + ((colSpan - 1) * this.paddingSize);
                int tileHeight = (rowSpan * tileSize) + ((rowSpan - 1) * this.paddingSize);
                int tileMarginTop = ((row - 1) * tileSize) + (row  * this.paddingSize);
                int tileMarginLeft = ((col - 1) * tileSize) + (col  * this.paddingSize);

                int circleMarginTop = ((row - 1) * tileSize) + (row  * this.paddingSize) - (circleSize / 2);
                int circleMarginLeft = ((col - 1 + colSpan) * (tileSize + this.paddingSize)) - (circleSize / 2);

                moduleViewHolder.getTextViewCircle().layout(circleMarginLeft, circleMarginTop, circleMarginLeft + circleSize, circleMarginTop + circleSize);
                moduleViewHolder.getLayoutTile().layout(tileMarginLeft, tileMarginTop, tileMarginLeft + tileWidth, tileMarginTop + tileHeight);

                moduleViewHolder.getImageViewIcon().layout((tileWidth - iconSize) / 2, (tileHeight - iconSize) / 2, (tileWidth + iconSize) / 2, (tileHeight + iconSize) / 2);

                if(colSpan >= 2 && rowSpan >= 2)
                {
                    moduleViewHolder.getTextViewTile().setVisibility(VISIBLE);
                    moduleViewHolder.getTextViewTile().measure(MeasureSpec.makeMeasureSpec(tileWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(tileHeight - (tileHeight + iconSize) / 2, MeasureSpec.EXACTLY));
                    moduleViewHolder.getTextViewTile().layout(0, (tileHeight + iconSize) / 2, tileWidth, tileHeight);
                }
                else
                {
                    moduleViewHolder.getTextViewTile().setVisibility(GONE);
                }
            }
        }
        else
        {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    public void setOnTileClickListener(OnTileClickListener onTileClickListener)
    {
        this.onTileClickListener = onTileClickListener;
    }

    public void setTiles(List<LayoutModule> tiles)
    {
        this.tiles = tiles;
        this.createChildViews();
        this.requestLayout();
    }

    public void setOrientation(int orientation)
    {
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            this.rows = ROWS_LANDSCAPE;
            this.columns = COLS_LANDSCAPE;
        }
        else
        {
            this.rows = ROWS_PORTRAIT;
            this.columns = COLS_PORTRAIT;
        }

        if(this.orientation != orientation)
        {
            this.orientation = orientation;
            this.requestLayout();
        }
    }

    private void createChildViews()
    {
        this.modules.clear();
        this.removeAllViews();

        if(this.tiles == null)
            return;

        final int circleSize = (int) this.getResources().getDimension(R.dimen.main_tile_circle);
        final float circleTextSize = this.getResources().getDimension(R.dimen.main_tile_circle_text);
        final float textSize = this.getResources().getDimension(R.dimen.main_tile_text);
        final int textColor = ContextCompat.getColor(this.getContext(), R.color.module_text);
        final int textPaddingTop = (int) this.getResources().getDimension(R.dimen.main_tile_text_padding_top);
        final int textPaddingSides = (int) this.getResources().getDimension(R.dimen.main_tile_text_padding_left_right);

        for(int i = 0; i < this.tiles.size(); i++)
        {
            final LayoutModule tile = this.tiles.get(i);
            final Module module = ModuleHelper.getModule(tile.getId());
            final int imageId = 30 + i;

            if(module == null)
                continue;

            RelativeLayout.LayoutParams paramsCircle = new RelativeLayout.LayoutParams(circleSize, circleSize);
            LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(20, 20);
            LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout layoutTile = new LinearLayout(this.getContext());
            TextView textViewCircle = new TextView(this.getContext());
            ImageView imageViewTile = new ImageView(this.getContext());
            TextView textViewTile = new TextView(this.getContext());

            layoutTile.setBackgroundResource(module.getBackground());
            layoutTile.setOrientation(LinearLayout.VERTICAL);
            layoutTile.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (TileLayout.this.onTileClickListener != null)
                        TileLayout.this.onTileClickListener.onTileClick(module, tile.getId());
                }
            });

            textViewCircle.setBackgroundResource(R.drawable.shape_red_circle);
            textViewCircle.setTextColor(textColor);
            textViewCircle.setTextSize(TypedValue.COMPLEX_UNIT_PX, circleTextSize);
            textViewCircle.setGravity(Gravity.CENTER);
            textViewCircle.setTypeface(this.getContext(), TypefaceHelper.MONTSERRAT_BOLD);

            imageViewTile.setImageResource(module.getImage());
            imageViewTile.setId(imageId);

            textViewTile.setText(module.getText());
            textViewTile.setTextColor(textColor);
            textViewTile.setTypeface(this.getContext(), TypefaceHelper.MONTSERRAT_LIGHT);
            textViewTile.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textViewTile.setGravity(Gravity.CENTER_HORIZONTAL);
            textViewTile.setPadding(textPaddingSides, textPaddingTop, textPaddingSides, 0);

            layoutTile.addView(imageViewTile, paramsImage);
            layoutTile.addView(textViewTile, paramsTitle);
            this.addView(layoutTile);
            this.addView(textViewCircle, paramsCircle);

            int notifications = this.notifications.containsKey(tile.getId()) ? this.notifications.get(tile.getId()) : 0;
            this.modules.add(new ModuleViewHolder(tile, textViewCircle, layoutTile, imageViewTile, textViewTile, notifications));
        }
    }

    public void setNotifications(int moduleId, int numberOfNotifications)
    {
        this.notifications.put(moduleId, numberOfNotifications);

        for(ModuleViewHolder module : this.modules)
            if(module.getModule().getId() == moduleId)
                module.setNumberOfNotifications(numberOfNotifications);
    }

    public interface OnTileClickListener
    {
        void onTileClick(Module module, int moduleId);
    }
}
