package smartassist.appreciate.be.smartassist.contentproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import smartassist.appreciate.be.smartassist.database.DatabaseHelper;
import smartassist.appreciate.be.smartassist.database.MedicationTable;

/**
 * Created by Inneke De Clippel on 26/04/2016.
 */
public class MedicationContentProvider extends ContentProvider
{
    private SQLiteDatabase database;

    private static final String PROVIDER_NAME = "smartassist.appreciate.be.smartassist.contentproviders.MedicationContentProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");

    private static final int MEDICATION_ITEMS = 1;
    private static final int MEDICATION_ITEM_ID = 2;

    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, MEDICATION_ITEMS);
        uriMatcher.addURI(PROVIDER_NAME, "medication/*", MEDICATION_ITEM_ID);
    }

    @Override
    public boolean onCreate()
    {
        this.database = new DatabaseHelper(this.getContext()).getWritableDatabase();
        return this.database != null;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case MEDICATION_ITEMS:
                return "vnd.android.cursor.dir/" + PROVIDER_NAME;
            case MEDICATION_ITEM_ID:
                return "vnd.android.cursor.item/" + PROVIDER_NAME;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MedicationTable.TABLE_NAME);

        switch (uriMatcher.match(uri))
        {
            case MEDICATION_ITEMS:
                break;

            case MEDICATION_ITEM_ID:
                queryBuilder.appendWhere(MedicationTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(this.database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        long rowID = this.database.replace(MedicationTable.TABLE_NAME, null, values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            this.getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int rowsDeleted;

        switch (uriMatcher.match(uri))
        {
            case MEDICATION_ITEMS:
                rowsDeleted = this.database.delete(MedicationTable.TABLE_NAME, selection, selectionArgs);
                break;

            case MEDICATION_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsDeleted = this.database.delete(MedicationTable.TABLE_NAME, MedicationTable.COLUMN_ID + "=" + id, null);
                else
                    rowsDeleted = this.database.delete(MedicationTable.TABLE_NAME, MedicationTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsDeleted > 0)
            this.getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int rowsUpdated;

        switch (uriMatcher.match(uri))
        {
            case MEDICATION_ITEMS:
                rowsUpdated = this.database.update(MedicationTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case MEDICATION_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsUpdated = this.database.update(MedicationTable.TABLE_NAME, values, MedicationTable.COLUMN_ID + "=" + id, null);
                else
                    rowsUpdated = this.database.update(MedicationTable.TABLE_NAME, values, MedicationTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsUpdated > 0)
            this.getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
