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

import smartassist.appreciate.be.smartassist.database.ChatConversationTable;
import smartassist.appreciate.be.smartassist.database.ChatMessageTable;
import smartassist.appreciate.be.smartassist.database.DatabaseHelper;

/**
 * Created by Inneke De Clippel on 8/09/2016.
 */
public class ChatConversationContentProvider extends ContentProvider
{
    private SQLiteDatabase database;

    private static final String PROVIDER_NAME = "smartassist.appreciate.be.smartassist.contentproviders.ChatConversationContentProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");

    private static final int CHAT_CONVERSATIONS = 1;

    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, CHAT_CONVERSATIONS);
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
            case CHAT_CONVERSATIONS:
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
            case CHAT_CONVERSATIONS:
                String tables = ChatConversationTable.TABLE_NAME
                        + " LEFT JOIN " + ChatMessageTable.TABLE_NAME
                        + " ON " + ChatConversationTable.TABLE_NAME + "." + ChatConversationTable.COLUMN_CONVERSATION_ID + " = " + ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_CONVERSATION_ID
                        + " LEFT JOIN " + ChatMessageTable.TABLE_NAME + " helper"
                        + " ON " + ChatConversationTable.TABLE_NAME + "." + ChatConversationTable.COLUMN_CONVERSATION_ID + " = helper." + ChatMessageTable.COLUMN_CONVERSATION_ID
                        + " AND " + ChatMessageTable.TABLE_NAME + "." + ChatMessageTable.COLUMN_SENT_AT + " < helper." + ChatMessageTable.COLUMN_SENT_AT;

                Map<String, String> projectionMap = new HashMap<>();
                projectionMap.putAll(ChatConversationTable.PROJECTION_MAP);
                projectionMap.putAll(ChatMessageTable.PROJECTION_MAP);

                selection = "helper." + ChatMessageTable.COLUMN_ID + " IS NULL"
                        + " AND " + ChatConversationTable.TABLE_NAME + "." + ChatConversationTable.COLUMN_JOIN_DATE + " >= " + ChatConversationTable.TABLE_NAME + "." + ChatConversationTable.COLUMN_LEAVE_DATE;

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
        long rowID = this.database.replace(ChatConversationTable.TABLE_NAME, null, values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
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
            case CHAT_CONVERSATIONS:
                rowsDeleted = this.database.delete(ChatConversationTable.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsDeleted > 0)
        {
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int rowsUpdated;

        switch (uriMatcher.match(uri))
        {
            case CHAT_CONVERSATIONS:
                rowsUpdated = this.database.update(ChatConversationTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsUpdated > 0)
        {
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        }
        return rowsUpdated;
    }
}
