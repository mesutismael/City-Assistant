package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.EmergencyTable;

/**
 * Created by Inneke De Clippel on 4/08/2016.
 */
public class EmergencyContact implements Comparable<EmergencyContact>
{
    private int id;
    private String name;
    private String number;
    private boolean qr;
    private String hash;
    private String photo;
    private double randomPhoto;
    private CategoryPhoto categoryPhoto;
    private EmergencyCategory category;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getNumber()
    {
        return number;
    }

    public boolean hasQr()
    {
        return qr;
    }

    public String getHash()
    {
        return hash;
    }

    public String getPhoto()
    {
        return photo;
    }

    public double getRandomPhoto()
    {
        return randomPhoto;
    }

    public CategoryPhoto getCategoryPhoto()
    {
        return categoryPhoto;
    }

    public EmergencyCategory getCategory()
    {
        return category;
    }

    public static List<EmergencyContact> constructListFromCursor(Cursor cursor)
    {
        List<EmergencyContact> contacts = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                contacts.add(EmergencyContact.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return contacts;
    }

    public static EmergencyContact constructFromCursor(Cursor cursor)
    {
        EmergencyContact contact = new EmergencyContact();

        contact.id = cursor.getInt(cursor.getColumnIndex(EmergencyTable.COLUMN_EMERGENCY_ID_FULL));
        contact.name = cursor.getString(cursor.getColumnIndex(EmergencyTable.COLUMN_NAME_FULL));
        contact.number = cursor.getString(cursor.getColumnIndex(EmergencyTable.COLUMN_NUMBER_FULL));
        contact.qr = cursor.getInt(cursor.getColumnIndex(EmergencyTable.COLUMN_HAS_QR_FULL)) == 1;
        contact.hash = cursor.getString(cursor.getColumnIndex(EmergencyTable.COLUMN_HASH_FULL));
        contact.photo = cursor.getString(cursor.getColumnIndex(EmergencyTable.COLUMN_PHOTO_FULL));
        contact.randomPhoto = cursor.getDouble(cursor.getColumnIndex(EmergencyTable.COLUMN_RANDOM_PHOTO_FULL));
        contact.categoryPhoto = CategoryPhoto.constructFromCursor(cursor, CategoryPhoto.TYPE_EMERGENCY);
        contact.category = EmergencyCategory.constructFromCursor(cursor);

        return contact;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        EmergencyContact contact = (EmergencyContact) o;

        if (this.id != contact.id)
        {
            return false;
        }
        if (this.name != null ? !this.name.equals(contact.name) : contact.name != null)
        {
            return false;
        }
        return this.hash != null ? this.hash.equals(contact.hash) : contact.hash == null;
    }

    @Override
    public int compareTo(EmergencyContact another)
    {
        String otherName = another != null ? another.getName() : null;

        if(this.name == null)
        {
            return otherName == null ? 0 : -1;
        }
        else
        {
            return otherName == null ? 1 : this.name.compareTo(otherName);
        }
    }
}
