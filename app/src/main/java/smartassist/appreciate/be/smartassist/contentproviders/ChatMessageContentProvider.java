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

import java.util.HashMap;
import java.util.Map;

import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.database.ChatSenderTable;
import smartassist.appreciate.be.smartassist.database.DatabaseHelper;

/**
 * Created by Inneke De Clippel on 8/09/2016.
 */
public class ChatMessageContentProvider extends ContentProvider
{
    private SQLiteDatabase database;

    private static final String PROVIDER_NAME = "smartassist.appreciate.be.smartassist.contentproviders.ChatMessageContentProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");

    private static final int CHAT_ITEMS = 1;

    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, CHAT_ITEMS);
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
            case CHAT_ITEMS:
                return "vnd.android.cursor.dir/" + PROVIDER_NAME;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri))
        {
            case CHAT_ITEMS:
                String tables = ChatMessageTable.TABLE_NAME
                        + " JOIN " + ChatSenderTable.TABLE_NAME
                        + " ON " + ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_CONTACT_ID + " = " + ChatSenderTable.TABLE_NAME + "." + ChatSenderTable.COLUMN_CONTACT_ID
                        + " AND " + ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_CONVERSATION_ID + " = " + ChatSenderTable.TABLE_NAME + "." + ChatSenderTable.COLUMN_CONVERSATION_ID;

                Map<String, String> projectionMap = new HashMap<>();
                projectionMap.putAll(ChatMessageTable.PROJECTION_MAP);
                projectionMap.putAll(ChatSenderTable.PROJECTION_MAP);

                queryBuilder.setTables(tables);
                queryBuilder.setProjectionMap(projectionMap);

                Cursor cursor = queryBuilder.query(this.database, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(this.getContext().getContentResolver(), uri);
                return cursor;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        long rowID = this.database.replace(ChatMessageTable.TABLE_NAME, null, values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
            this.getContext().getContentResolver().notifyChange(ChatConversationContentProvider.CONTENT_URI, null);
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
            case CHAT_ITEMS:
                rowsDeleted = this.database.delete(ChatMessageTable.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsDeleted > 0)
        {
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
            this.getContext().getContentResolver().notifyChange(ChatConversationContentProvider.CONTENT_URI, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int rowsUpdated;

        switch (uriMatcher.match(uri))
        {
            case CHAT_ITEMS:
                rowsUpdated = this.database.update(ChatMessageTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsUpdated > 0)
        {
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
            this.getContext().getContentResolver().notifyChange(ChatConversationContentProvider.CONTENT_URI, null);
        }
        return rowsUpdated;
    }
}
