package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartassist.appreciate.be.smartassist.database.PoiCategoryTable;

/**
 * Created by Inneke on 10/02/2015.
 */
public class PoiCategory
{
    private int id;
    @SerializedName("name_nl")
    private String name;
    private List<Image> images;
    @SerializedName("main_category")
    private boolean mainCategory;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;

    public int getId()
    {
        return id;
    }

    public List<Image> getImages()
    {
        return images;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }

    public String getUpdatedAt()
    {
        return updatedAt;
    }

    public String getDeletedAt()
    {
        return deletedAt;
    }

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();

        cv.put(PoiCategoryTable.COLUMN_CATEGORY_ID, this.id);
        cv.put(PoiCategoryTable.COLUMN_NAME, this.name);

        if(this.images != null)
        {
            if(this.images.size() > 0 && this.images.get(0) != null)
            {
                cv.put(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_1, this.images.get(0).getPortraitImage());
                cv.put(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_1, this.images.get(0).getLandscapeImage());
            }
            if(this.images.size() > 1 && this.images.get(1) != null)
            {
                cv.put(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_2, this.images.get(1).getPortraitImage());
                cv.put(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_2, this.images.get(1).getLandscapeImage());
            }
            if(this.images.size() > 2 && this.images.get(2) != null)
            {
                cv.put(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_3, this.images.get(2).getPortraitImage());
                cv.put(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_3, this.images.get(2).getLandscapeImage());
            }
            if(this.images.size() > 3 && this.images.get(3) != null)
            {
                cv.put(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_4, this.images.get(3).getPortraitImage());
                cv.put(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_4, this.images.get(3).getLandscapeImage());
            }
            if(this.images.size() > 4 && this.images.get(4) != null)
            {
                cv.put(PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_5, this.images.get(4).getPortraitImage());
                cv.put(PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_5, this.images.get(4).getLandscapeImage());
            }
        }

        cv.put(PoiCategoryTable.COLUMN_IS_MAIN, this.mainCategory ? 1 : 0);
        cv.put(PoiCategoryTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }
}
