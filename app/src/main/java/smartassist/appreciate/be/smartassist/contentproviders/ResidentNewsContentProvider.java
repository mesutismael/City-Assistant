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
import smartassist.appreciate.be.smartassist.database.ResidentNewsTable;

/**
 * Created by Banada ismael on 24/10/2016.
 */
public class ResidentNewsContentProvider extends ContentProvider
{
    private static final String PROVIDER_NAME = "smartassist.appreciate.be.smartassist.contentproviders.ResidentNewsContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");
    private static final int RESIDENT_NEWS = 1;
    private static final int RESIDENT_NEWS_ID = 2;
    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, RESIDENT_NEWS);
        uriMatcher.addURI(PROVIDER_NAME, "residentnews/*", RESIDENT_NEWS_ID);
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
            case RESIDENT_NEWS:
                return "vnd.android.cursor.dir/" + PROVIDER_NAME;
            case RESIDENT_NEWS_ID:
                return "vnd.android.cursor.item/" + PROVIDER_NAME;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ResidentNewsTable.TABLE_NAME);

        switch (uriMatcher.match(uri))
        {
            case RESIDENT_NEWS:
                break;

            case RESIDENT_NEWS_ID:
                queryBuilder.appendWhere(ResidentNewsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
        long rowID = this.database.replace(ResidentNewsTable.TABLE_NAME, null, values);

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
            case RESIDENT_NEWS:
                rowsDeleted = this.database.delete(ResidentNewsTable.TABLE_NAME, selection, selectionArgs);
                break;

            case RESIDENT_NEWS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsDeleted = this.database.delete(ResidentNewsTable.TABLE_NAME, ResidentNewsTable.COLUMN_ID + "=" + id, null);
                else
                    rowsDeleted = this.database.delete(ResidentNewsTable.TABLE_NAME, ResidentNewsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
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
            case RESIDENT_NEWS:
                rowsUpdated = this.database.update(ResidentNewsTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            case RESIDENT_NEWS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsUpdated = this.database.update(ResidentNewsTable.TABLE_NAME, values, ResidentNewsTable.COLUMN_ID + "=" + id, null);
                else
                    rowsUpdated = this.database.update(ResidentNewsTable.TABLE_NAME, values, ResidentNewsTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsUpdated > 0)
            this.getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
