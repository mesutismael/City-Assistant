package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartassist.appreciate.be.smartassist.database.ActivityTypesTable;

/**
 * Created by banada ismael on 10/31/2016.
 */

public class ActivitytypeItem {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;
    @SerializedName("images")
    private List<ActivityTypePhoto> images;

    public int getId() {return id;}

    public String getName() {return name;}

    public String getCreatedAt() {return createdAt;}

    public String getUpdatedAt() {return updatedAt;}

    public String getDeletedAt() {return deletedAt;}

    public ContentValues getContentValues(long timestamp)
    {
        final ContentValues cv = new ContentValues();

        cv.put(ActivityTypesTable.COLUMN_ID, this.id);
        cv.put(ActivityTypesTable.COLUMN_NAME, this.name);
        cv.put(ActivityTypesTable.COLUMN_DELETION_DATE, this.deletedAt);
        cv.put(ActivityTypesTable.COLUMN_CREATION_DATE, this.createdAt);
        cv.put(ActivityTypesTable.COLUMN_UPDATE_DATE, timestamp);

        if(images.size()>0 && this.images.get(0).getPhoto_id()!=null) {
            cv.put(ActivityTypesTable.COLUMN_IMAGE_ID, this.images.get(0).getPhoto_id());
            cv.put(ActivityTypesTable.COLUMN_POTRAIT_IMAGE, this.images.get(0).getPortrait_img());
            cv.put(ActivityTypesTable.COLUMN_LANDSCAPE_IMAGE, this.images.get(0).getLandscape_img());
        }




        return cv;
    }

    @Override
    public String toString() {
        return "name: "+this.name;
    }


}
