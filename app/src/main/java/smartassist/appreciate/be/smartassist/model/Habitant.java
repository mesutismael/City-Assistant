package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.HabitantTable;

/**
 * Created by Inneke De Clippel on 3/08/2016.
 */
public class Habitant
{
    private int id;
    private String photo;
    private String name;
    public String flatNumber;
    private long birthday;
    private String insuranceNumber;
    private String civilState;
    private String partner;
    private String phone;
    private String email;
    private long start;
/*    private long stop;
    private String stopReason;
    private long stopDate;*/
    private String mutuality;
    private String length;
    private String weight;
    private String katz;
    private String bel;
    private String bloodType;
    private String doctor;
    private String nurse;
    private String intolerances;
    private String allergies;
    private String diseases;
    private String aids;
    private String remarks;

    public int getId()
    {
        return id;
    }

    public String getPhoto()
    {
        return photo;
    }

    public String getName()
    {
        return name;
    }

    public String getFlatNumber(){return flatNumber;}

    public long getBirthday()
    {
        return birthday;
    }

    public String getInsuranceNumber()
    {
        return insuranceNumber;
    }

    public String getCivilState()
    {
        return civilState;
    }

    public String getPartner()
    {
        return partner;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getEmail()
    {
        return email;
    }

    public long getStart()
    {
        return start;
    }
/*
    public long getStop()
    {
        return stop;
    }

    public String getStopReason()
    {
        return stopReason;
    }

    public long getStopDate()
    {
        return stopDate;
    }*/

    public String getMutuality()
    {
        return mutuality;
    }

    public String getLength()
    {
        return length;
    }

    public String getWeight()
    {
        return weight;
    }

    public String getKatz()
    {
        return katz;
    }

    public String getBel()
    {
        return bel;
    }

    public String getBloodType()
    {
        return bloodType;
    }

    public String getDoctor()
    {
        return doctor;
    }

    public String getNurse()
    {
        return nurse;
    }

    public String getIntolerances()
    {
        return intolerances;
    }

    public String getAllergies()
    {
        return allergies;
    }

    public String getDiseases()
    {
        return diseases;
    }

    public String getAids()
    {
        return aids;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public static List<Habitant> constructListFromCursor(Cursor cursor)
    {
        List<Habitant> habitants = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                habitants.add(Habitant.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return habitants;
    }

    public static Habitant constructFromCursor(Cursor cursor)
    {
        Habitant habitant = new Habitant();

        habitant.id = cursor.getInt(cursor.getColumnIndex(HabitantTable.COLUMN_ID_FULL));
        habitant.photo = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_PHOTO_FULL));
        habitant.name = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_NAME_FULL));
        habitant.flatNumber = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_FLAT_NUMBER_FULL));
        habitant.birthday = cursor.getLong(cursor.getColumnIndex(HabitantTable.COLUMN_BIRTHDAY_FULL));
        habitant.insuranceNumber = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_INSURANCE_NUMBER_FULL));
        habitant.civilState = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_CIVIL_STATE_FULL));
        habitant.partner = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_PARTNER_FULL));
        habitant.phone = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_PHONE_FULL));
        habitant.email = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_EMAIL_FULL));
        habitant.start = cursor.getLong(cursor.getColumnIndex(HabitantTable.COLUMN_START_FULL));
/*        habitant.stop = cursor.getLong(cursor.getColumnIndex(HabitantTable.COLUMN_STOP_FULL));
        habitant.stopReason = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_STOP_REASON_FULL));
        habitant.stopDate = cursor.getLong(cursor.getColumnIndex(HabitantTable.COLUMN_STOP_DATE_FULL));*/
        habitant.mutuality = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_MUTUALITY_FULL));
        habitant.length = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_LENGTH_FULL));
        habitant.weight = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_WEIGHT_FULL));
        habitant.katz = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_KATZ_FULL));
        habitant.bel = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_BEL_FULL));
        habitant.bloodType = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_BLOOD_TYPE_FULL));
        habitant.doctor = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_DOCTOR_FULL));
        habitant.nurse = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_NURSE_FULL));
        habitant.intolerances = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_INTOLERANCES_FULL));
        habitant.allergies = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_ALLERGIES_FULL));
        habitant.diseases = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_DISEASES_FULL));
        habitant.aids = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_AIDS_FULL));
        habitant.remarks = cursor.getString(cursor.getColumnIndex(HabitantTable.COLUMN_REMARKS_FULL));

        return habitant;
    }
}
