package smartassist.appreciate.be.smartassist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 3/08/2016.
 */
public class HabitantTable
{
    public static final String TABLE_NAME = "habitants";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HABITANT_ID = "habitant_id";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FLAT_NUMBER = "flat_number";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_INSURANCE_NUMBER = "insurance_number";
    public static final String COLUMN_CIVIL_STATE = "civil_state";
    public static final String COLUMN_PARTNER = "partner";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_START = "start";
/*    public static final String COLUMN_STOP = "stop";
    public static final String COLUMN_STOP_REASON = "stop_reason";
    public static final String COLUMN_STOP_DATE = "stop_date";*/
    public static final String COLUMN_MUTUALITY = "mutuality";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_KATZ = "katz";
    public static final String COLUMN_BEL = "bel";
    public static final String COLUMN_BLOOD_TYPE = "blood_type";
    public static final String COLUMN_DOCTOR = "doctor";
    public static final String COLUMN_NURSE = "nurse";
    public static final String COLUMN_INTOLERANCES = "intolerances";
    public static final String COLUMN_ALLERGIES = "allergies";
    public static final String COLUMN_DISEASES = "diseases";
    public static final String COLUMN_AIDS = "aids";
    public static final String COLUMN_REMARKS = "remarks";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    public static final String COLUMN_ID_FULL = TABLE_NAME + "_" + COLUMN_ID;
    public static final String COLUMN_HABITANT_ID_FULL = TABLE_NAME + "_" + COLUMN_HABITANT_ID;
    public static final String COLUMN_PHOTO_FULL = TABLE_NAME + "_" + COLUMN_PHOTO;
    public static final String COLUMN_NAME_FULL = TABLE_NAME + "_" + COLUMN_NAME;
    public static final String COLUMN_FLAT_NUMBER_FULL = TABLE_NAME + "_" + COLUMN_FLAT_NUMBER;
    public static final String COLUMN_BIRTHDAY_FULL = TABLE_NAME + "_" + COLUMN_BIRTHDAY;
    public static final String COLUMN_INSURANCE_NUMBER_FULL = TABLE_NAME + "_" + COLUMN_INSURANCE_NUMBER;
    public static final String COLUMN_CIVIL_STATE_FULL = TABLE_NAME + "_" + COLUMN_CIVIL_STATE;
    public static final String COLUMN_PARTNER_FULL = TABLE_NAME + "_" + COLUMN_PARTNER;
    public static final String COLUMN_PHONE_FULL = TABLE_NAME + "_" + COLUMN_PHONE;
    public static final String COLUMN_EMAIL_FULL = TABLE_NAME + "_" + COLUMN_EMAIL;
    public static final String COLUMN_START_FULL = TABLE_NAME + "_" + COLUMN_START;
/*    public static final String COLUMN_STOP_FULL = TABLE_NAME + "_" + COLUMN_STOP;
    public static final String COLUMN_STOP_REASON_FULL = TABLE_NAME + "_" + COLUMN_STOP_REASON;
    public static final String COLUMN_STOP_DATE_FULL = TABLE_NAME + "_" + COLUMN_STOP_DATE;*/
    public static final String COLUMN_MUTUALITY_FULL = TABLE_NAME + "_" + COLUMN_MUTUALITY;
    public static final String COLUMN_LENGTH_FULL = TABLE_NAME + "_" + COLUMN_LENGTH;
    public static final String COLUMN_WEIGHT_FULL = TABLE_NAME + "_" + COLUMN_WEIGHT;
    public static final String COLUMN_KATZ_FULL = TABLE_NAME + "_" + COLUMN_KATZ;
    public static final String COLUMN_BEL_FULL = TABLE_NAME + "_" + COLUMN_BEL;
    public static final String COLUMN_BLOOD_TYPE_FULL = TABLE_NAME + "_" + COLUMN_BLOOD_TYPE;
    public static final String COLUMN_DOCTOR_FULL = TABLE_NAME + "_" + COLUMN_DOCTOR;
    public static final String COLUMN_NURSE_FULL = TABLE_NAME + "_" + COLUMN_NURSE;
    public static final String COLUMN_INTOLERANCES_FULL = TABLE_NAME + "_" + COLUMN_INTOLERANCES;
    public static final String COLUMN_ALLERGIES_FULL = TABLE_NAME + "_" + COLUMN_ALLERGIES;
    public static final String COLUMN_DISEASES_FULL = TABLE_NAME + "_" + COLUMN_DISEASES;
    public static final String COLUMN_AIDS_FULL = TABLE_NAME + "_" + COLUMN_AIDS;
    public static final String COLUMN_REMARKS_FULL = TABLE_NAME + "_" + COLUMN_REMARKS;
    public static final String COLUMN_LAST_UPDATE_FULL = TABLE_NAME + "_" + COLUMN_LAST_UPDATE;

    public static final Map<String, String> PROJECTION_MAP;

    static
    {
        PROJECTION_MAP = new HashMap<>();
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ID, TABLE_NAME + "." + COLUMN_ID + " AS " + COLUMN_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_HABITANT_ID, TABLE_NAME + "." + COLUMN_HABITANT_ID + " AS " + COLUMN_HABITANT_ID_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_PHOTO, TABLE_NAME + "." + COLUMN_PHOTO + " AS " + COLUMN_PHOTO_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NAME, TABLE_NAME + "." + COLUMN_NAME + " AS " + COLUMN_NAME_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_FLAT_NUMBER, TABLE_NAME + "." + COLUMN_FLAT_NUMBER + " AS " + COLUMN_FLAT_NUMBER_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_BIRTHDAY, TABLE_NAME + "." + COLUMN_BIRTHDAY + " AS " + COLUMN_BIRTHDAY_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INSURANCE_NUMBER, TABLE_NAME + "." + COLUMN_INSURANCE_NUMBER + " AS " + COLUMN_INSURANCE_NUMBER_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_CIVIL_STATE, TABLE_NAME + "." + COLUMN_CIVIL_STATE + " AS " + COLUMN_CIVIL_STATE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_PARTNER, TABLE_NAME + "." + COLUMN_PARTNER + " AS " + COLUMN_PARTNER_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_PHONE, TABLE_NAME + "." + COLUMN_PHONE + " AS " + COLUMN_PHONE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_EMAIL, TABLE_NAME + "." + COLUMN_EMAIL + " AS " + COLUMN_EMAIL_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_START, TABLE_NAME + "." + COLUMN_START + " AS " + COLUMN_START_FULL);
/*        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_STOP, TABLE_NAME + "." + COLUMN_STOP + " AS " + COLUMN_STOP_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_STOP_REASON, TABLE_NAME + "." + COLUMN_STOP_REASON + " AS " + COLUMN_STOP_REASON_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_STOP_DATE, TABLE_NAME + "." + COLUMN_STOP_DATE + " AS " + COLUMN_STOP_DATE_FULL);*/
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_MUTUALITY, TABLE_NAME + "." + COLUMN_MUTUALITY + " AS " + COLUMN_MUTUALITY_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LENGTH, TABLE_NAME + "." + COLUMN_LENGTH + " AS " + COLUMN_LENGTH_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_WEIGHT, TABLE_NAME + "." + COLUMN_WEIGHT + " AS " + COLUMN_WEIGHT_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_KATZ, TABLE_NAME + "." + COLUMN_KATZ + " AS " + COLUMN_KATZ_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_BEL, TABLE_NAME + "." + COLUMN_BEL + " AS " + COLUMN_BEL_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_BLOOD_TYPE, TABLE_NAME + "." + COLUMN_BLOOD_TYPE + " AS " + COLUMN_BLOOD_TYPE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DOCTOR, TABLE_NAME + "." + COLUMN_DOCTOR + " AS " + COLUMN_DOCTOR_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_NURSE, TABLE_NAME + "." + COLUMN_NURSE + " AS " + COLUMN_NURSE_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_INTOLERANCES, TABLE_NAME + "." + COLUMN_INTOLERANCES + " AS " + COLUMN_INTOLERANCES_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_ALLERGIES, TABLE_NAME + "." + COLUMN_ALLERGIES + " AS " + COLUMN_ALLERGIES_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_DISEASES, TABLE_NAME + "." + COLUMN_DISEASES + " AS " + COLUMN_DISEASES_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_AIDS, TABLE_NAME + "." + COLUMN_AIDS + " AS " + COLUMN_AIDS_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_REMARKS, TABLE_NAME + "." + COLUMN_REMARKS + " AS " + COLUMN_REMARKS_FULL);
        PROJECTION_MAP.put(TABLE_NAME + "." + COLUMN_LAST_UPDATE, TABLE_NAME + "." + COLUMN_LAST_UPDATE + " AS " + COLUMN_LAST_UPDATE_FULL);
    }

    private static final String CREATE_TABLE = "create table IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_HABITANT_ID + " integer, "
            + COLUMN_PHOTO + " text, "
            + COLUMN_NAME + " text, "
            + COLUMN_FLAT_NUMBER+ " text, "
            + COLUMN_BIRTHDAY + " integer, "
            + COLUMN_INSURANCE_NUMBER + " text, "
            + COLUMN_CIVIL_STATE + " text, "
            + COLUMN_PARTNER + " text, "
            + COLUMN_PHONE + " text, "
            + COLUMN_EMAIL + " text, "
            + COLUMN_START + " integer, "
/*            + COLUMN_STOP + " integer, "
            + COLUMN_STOP_REASON + " text, "
            + COLUMN_STOP_DATE + " integer, "*/
            + COLUMN_MUTUALITY + " text, "
            + COLUMN_LENGTH + " text, "
            + COLUMN_WEIGHT + " text, "
            + COLUMN_KATZ + " text, "
            + COLUMN_BEL + " text, "
            + COLUMN_BLOOD_TYPE + " text, "
            + COLUMN_DOCTOR + " text, "
            + COLUMN_NURSE + " text, "
            + COLUMN_INTOLERANCES + " text, "
            + COLUMN_ALLERGIES + " text, "
            + COLUMN_DISEASES + " text, "
            + COLUMN_AIDS + " text, "
            + COLUMN_REMARKS + " text, "
            + COLUMN_LAST_UPDATE + " long"
            + ");";

    public static void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        while (oldVersion < newVersion)
        {
            switch (oldVersion)
            {
                case 1:
                    //No changes
                    break;
            }

            oldVersion++;
        }
    }

}
