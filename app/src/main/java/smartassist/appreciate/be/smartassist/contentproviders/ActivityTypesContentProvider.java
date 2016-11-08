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

import java.util.HashMap;
import java.util.Map;

import smartassist.appreciate.be.smartassist.database.ActivityTable;
import smartassist.appreciate.be.smartassist.database.ActivityTypesTable;
import smartassist.appreciate.be.smartassist.database.DatabaseHelper;

/**
 * Created by Banada ismael on 10/31/2016.
 */
public class ActivityTypesContentProvider extends ContentProvider
{
    private static final String PROVIDER_NAME = "smartassist.appreciate.be.smartassist.contentproviders.ActivityTypesContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");
    private static final int  ACTIVITY_TYPES = 1;
    private static final int ACTIVITY_TYPE_ID = 2;
    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, ACTIVITY_TYPES);
        uriMatcher.addURI(PROVIDER_NAME, "activitytypes/*", ACTIVITY_TYPE_ID);
    }

    private SQLiteDatabase database;

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
            case ACTIVITY_TYPES:
                return "vnd.android.cursor.dir/" + PROVIDER_NAME;
            case ACTIVITY_TYPE_ID:
                return "vnd.android.cursor.item/" + PROVIDER_NAME;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String tables = ActivityTypesTable.TABLE_NAME +
                " LEFT JOIN " + ActivityTable.TABLE_NAME +
                " ON (" + ActivityTypesTable.TABLE_NAME + "." +
                ActivityTypesTable.COLUMN_ID + " = " + ActivityTable.TABLE_NAME + "." + ActivityTable.COLUMN_ACTIVITY_TYPE_ID +")";
        queryBuilder.setTables(tables);

        Map<String, String> projectionMap = new HashMap<>();
        projectionMap.putAll(ActivityTypesTable.PROJECTION_MAP);
        projectionMap.putAll(ActivityTable.PROJECTION_MAP);
        queryBuilder.setProjectionMap(projectionMap);
        switch (uriMatcher.match(uri))
        {
            case ACTIVITY_TYPES:
                break;

            case ACTIVITY_TYPE_ID:
                queryBuilder.appendWhere(ActivityTypesTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
        long rowID = this.database.replace(ActivityTypesTable.TABLE_NAME, null, values);

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
            case ACTIVITY_TYPES:
                rowsDeleted = this.database.delete(ActivityTypesTable.TABLE_NAME, selection, selectionArgs);
                break;

            case ACTIVITY_TYPE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsDeleted = this.database.delete(ActivityTypesTable.TABLE_NAME, ActivityTypesTable.COLUMN_ID + "=" + id, null);
                else
                    rowsDeleted = this.database.delete(ActivityTypesTable.TABLE_NAME, ActivityTypesTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
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
            case ACTIVITY_TYPES:
                rowsUpdated = this.database.update(ActivityTypesTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case ACTIVITY_TYPE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsUpdated = this.database.update(ActivityTypesTable.TABLE_NAME, values, ActivityTypesTable.COLUMN_ID + "=" + id, null);
                else
                    rowsUpdated = this.database.update(ActivityTypesTable.TABLE_NAME, values, ActivityTypesTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsUpdated > 0)
            this.getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }




}
