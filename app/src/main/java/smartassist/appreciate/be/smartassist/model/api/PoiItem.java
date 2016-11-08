package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import smartassist.appreciate.be.smartassist.database.PoiTable;
import smartassist.appreciate.be.smartassist.utils.Constants;

/**
 * Created by Inneke on 10/02/2015.
 */
public class PoiItem
{
    private int id;
    @SerializedName("poi_category_id")
    private int categoryId;
    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("street")
    private String street;
    @SerializedName("number")
    private String number;
    @SerializedName("postal_code")
    private String postalCode;
    @SerializedName("locality")
    private String locality;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("email")
    private String email;
    @SerializedName("opening_hours")
    private List<OpeningTime> openingTimes;
    @SerializedName("favorite")
    private boolean favorite;
    @SerializedName("distance")
    private double distance;
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

    public List<OpeningTime> getOpeningTimes()
    {
        return openingTimes;
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

        cv.put(PoiTable.COLUMN_POI_ID, this.id);
        cv.put(PoiTable.COLUMN_CATEGORY_ID, this.categoryId);
        cv.put(PoiTable.COLUMN_IMAGE, this.image);
        cv.put(PoiTable.COLUMN_NAME, this.name);
        cv.put(PoiTable.COLUMN_DESCRIPTION, this.description);
        cv.put(PoiTable.COLUMN_STREET, this.street);
        cv.put(PoiTable.COLUMN_NUMBER, this.number);
        cv.put(PoiTable.COLUMN_POSTAL_CODE, this.postalCode);
        cv.put(PoiTable.COLUMN_LOCALITY, this.locality);
        cv.put(PoiTable.COLUMN_LATITUDE, this.latitude);
        cv.put(PoiTable.COLUMN_LONGITUDE, this.longitude);
        cv.put(PoiTable.COLUMN_PHONE, this.phoneNumber);
        cv.put(PoiTable.COLUMN_EMAIL, this.email);
        cv.put(PoiTable.COLUMN_FAVORITE, this.favorite(this.favorite));
        cv.put(PoiTable.COLUMN_DISTANCE, this.distance);
        cv.put(PoiTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }

    public ContentValues getContentValuesWithRandomPhoto(long timestamp)
    {
        ContentValues cv = this.getContentValues(timestamp);

        cv.put(PoiTable.COLUMN_RANDOM_PHOTO, Math.random());

        return cv;
    }

    public int favorite(boolean favorite)
    {
      return favorite? Constants.TRUE:Constants.FALSE;
    }
}
