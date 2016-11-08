package smartassist.appreciate.be.smartassist.model;

import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.database.OpeningTimesTable;
import smartassist.appreciate.be.smartassist.database.PoiTable;

/**
 * Created by Inneke De Clippel on 4/08/2016.
 */
public class Poi
{
    private int id;
    private String name;
    private String image;
    private String description;
    private String street;
    private String number;
    private String postalCode;
    private String city;
    private String phone;
    private double randomPhoto;
    private int favorite;
    private CategoryPhoto categoryPhoto;
    private int OpeningTimeTimes;


    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }


    public String getImage() {return image;}

    public String getDescription() {return description;}

    public String getPhone()
    {
        return phone;
    }

    public double getRandomPhoto()
    {
        return randomPhoto;
    }

    public CategoryPhoto getCategoryPhoto()
    {
        return categoryPhoto;
    }

    public int getFavorite() {
        return favorite;
    }

    public int getOpeningTimeTimes() {
        return OpeningTimeTimes;
    }

    public String getFullAddress()
    {
        StringBuilder address = new StringBuilder();

        if(this.street != null)
            address.append(this.street);
        if(this.number != null)
            address.append(" ").append(this.number);
        if(address.length() > 0)
            address.append("\n");
        if(this.postalCode != null)
            address.append(this.postalCode);
        if(this.city != null)
            address.append(" ").append(this.city);

        return address.toString();
    }

    public static List<Poi> constructListFromCursor(Cursor cursor)
    {
        List<Poi> poiItems = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst())
        {
            do
            {
                poiItems.add(Poi.constructFromCursor(cursor));
            }
            while (cursor.moveToNext());
        }

        return poiItems;
    }

    public static Poi constructFromCursor(Cursor cursor)
    {
        Poi poi = new Poi();

        poi.id = cursor.getInt(cursor.getColumnIndex(PoiTable.COLUMN_POI_ID_FULL));
        poi.name = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_NAME_FULL));
        poi.image = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_IMAGE_FULL));
        poi.description = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_NAME_FULL));
        poi.street = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_STREET_FULL));
        poi.number = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_NUMBER_FULL));
        poi.postalCode = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_POSTAL_CODE_FULL));
        poi.city = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_LOCALITY_FULL));
        poi.phone = cursor.getString(cursor.getColumnIndex(PoiTable.COLUMN_PHONE_FULL));
        poi.randomPhoto = cursor.getDouble(cursor.getColumnIndex(PoiTable.COLUMN_RANDOM_PHOTO_FULL));
        poi.favorite = cursor.getInt(cursor.getColumnIndex(PoiTable.COLUMN_FAVORITE_FULL));
        poi.OpeningTimeTimes = cursor.getInt(cursor.getColumnIndex(PoiTable.COLUMN_OPENING_TIMES));
        poi.categoryPhoto = CategoryPhoto.constructFromCursor(cursor, CategoryPhoto.TYPE_POI);

        return poi;
    }



}
