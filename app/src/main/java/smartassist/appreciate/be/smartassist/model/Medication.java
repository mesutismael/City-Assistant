package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.database.MedicationTable;

/**
 * Created by Inneke De Clippel on 4/08/2016.
 */
public class Medication
{
    private int id;
    private String name;
    private String dose;
    private int time1;
    private int time2;
    private int time3;
    private int time4;
    private int time5;
    private int time6;
    private int time7;

    public static final int TIME_NONE = 0;
    public static final int TIME_DURING = 1;
    public static final int TIME_BEFORE = 2;
    public static final int TIME_AFTER = 3;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDose()
    {
        return dose;
    }

    public int getTime1()
    {
        return time1;
    }

    public int getTime2()
    {
        return time2;
    }

    public int getTime3()
    {
        return time3;
    }

    public int getTime4()
    {
        return time4;
    }

    public int getTime5()
    {
        return time5;
    }

    public int getTime6()
    {
        return time6;
    }

    public int getTime7()
    {
        return time7;
    }

    public static int getImageForTime(int time)
    {
        switch (time)
        {
            case TIME_DURING:
                return R.drawable.medication_during;

            case TIME_BEFORE:
                return R.drawable.medication_before;

            case TIME_AFTER:
                return R.drawable.medication_after;

            default:
                return 0;
        }
    }

    public static List<Medication> constructListFromCursor(Cursor cursor)
    {
        List<Medication> medications = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                medications.add(Medication.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return medications;
    }

    public static Medication constructFromCursor(Cursor cursor)
    {
        Medication medication = new Medication();

        medication.id = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_MEDICATION_ID));
        medication.name = cursor.getString(cursor.getColumnIndex(MedicationTable.COLUMN_NAME));
        medication.dose = cursor.getString(cursor.getColumnIndex(MedicationTable.COLUMN_DOSE));
        medication.time1 = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_TIME_1));
        medication.time2 = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_TIME_2));
        medication.time3 = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_TIME_3));
        medication.time4 = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_TIME_4));
        medication.time5 = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_TIME_5));
        medication.time6 = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_TIME_6));
        medication.time7 = cursor.getInt(cursor.getColumnIndex(MedicationTable.COLUMN_TIME_7));

        return medication;
    }
}
