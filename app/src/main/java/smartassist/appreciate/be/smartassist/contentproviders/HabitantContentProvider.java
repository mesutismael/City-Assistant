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
import smartassist.appreciate.be.smartassist.database.HabitantTable;

/**
 * Created by Inneke De Clippel on 3/08/2016.
 */
public class HabitantContentProvider extends ContentProvider
{
    private SQLiteDatabase database;

    private static final String PROVIDER_NAME = "smartassist.appreciate.be.smartassist.contentproviders.HabitantContentProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");

    private static final int HABITANTS = 1;
    private static final int HABITANT_ID = 2;

    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, HABITANTS);
        uriMatcher.addURI(PROVIDER_NAME, "habitant/*", HABITANT_ID);
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
            case HABITANTS:
                return "vnd.android.cursor.dir/" + PROVIDER_NAME;
            case HABITANT_ID:
                return "vnd.android.cursor.item/" + PROVIDER_NAME;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(HabitantTable.TABLE_NAME);
        queryBuilder.setProjectionMap(HabitantTable.PROJECTION_MAP);

        switch (uriMatcher.match(uri))
        {
            case HABITANTS:
                break;

            case HABITANT_ID:
                queryBuilder.appendWhere(HabitantTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
        long rowID = this.database.replace(HabitantTable.TABLE_NAME, null, values);

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
            case HABITANTS:
                rowsDeleted = this.database.delete(HabitantTable.TABLE_NAME, selection, selectionArgs);
                break;

            case HABITANT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsDeleted = this.database.delete(HabitantTable.TABLE_NAME, HabitantTable.COLUMN_ID + "=" + id, null);
                else
                    rowsDeleted = this.database.delete(HabitantTable.TABLE_NAME, HabitantTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
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
            case HABITANTS:
                rowsUpdated = this.database.update(HabitantTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case HABITANT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsUpdated = this.database.update(HabitantTable.TABLE_NAME, values, HabitantTable.COLUMN_ID + "=" + id, null);
                else
                    rowsUpdated = this.database.update(HabitantTable.TABLE_NAME, values, HabitantTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsUpdated > 0)
            this.getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
