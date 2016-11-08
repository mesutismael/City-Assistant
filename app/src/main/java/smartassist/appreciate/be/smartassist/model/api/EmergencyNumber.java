package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.EmergencyTable;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke on 26/02/2015.
 */
public class EmergencyNumber
{
    private int id;
    @SerializedName("category_id")
    private int categoryId;
    private String name;
    private String photo;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("emergency_contact")
    private boolean emergencyContact;
    @SerializedName("qr_code")
    private boolean qrCode;
    private String hash;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("deleted_at")
    private String deletedAt;

    public static final int TYPE_DOCTOR_ON_CALL = 1;
    public static final int TYPE_PHARMACY_ON_CALL = 2;

    public void alterId(int idPrefix)
    {
        this.id = ContactHelper.alterId(this.id, idPrefix);
    }

    public int getId()
    {
        return id;
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

        cv.put(EmergencyTable.COLUMN_EMERGENCY_ID, this.id);
        cv.put(EmergencyTable.COLUMN_CATEGORY_ID, this.categoryId);
        cv.put(EmergencyTable.COLUMN_NAME, this.name);
        cv.put(EmergencyTable.COLUMN_PHOTO, this.photo);
        cv.put(EmergencyTable.COLUMN_NUMBER, this.phoneNumber);
        cv.put(EmergencyTable.COLUMN_HAS_QR, this.qrCode);
        cv.put(EmergencyTable.COLUMN_HASH, this.hash);
        cv.put(EmergencyTable.COLUMN_CAN_CHAT, ContactHelper.canChat(this.id, this.qrCode) ? 1 : 0);
        cv.put(EmergencyTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }

    public ContentValues getContentValuesWithRandomPhoto(long timestamp)
    {
        ContentValues cv = this.getContentValues(timestamp);

        cv.put(EmergencyTable.COLUMN_RANDOM_PHOTO, Math.random());

        return cv;
    }

    public static void saveSpecialNumber(Context context, int type, PoiItem poiItem)
    {
        int id = poiItem != null ? poiItem.getId() : 0;

        switch (type)
        {
            case TYPE_DOCTOR_ON_CALL:
                PreferencesHelper.saveEmergencyDoctorId(context, id);
                break;

            case TYPE_PHARMACY_ON_CALL:
                PreferencesHelper.saveEmergencyPharmacyId(context, id);
                break;
        }
    }
}
