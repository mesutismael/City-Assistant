package smartassist.appreciate.be.smartassist.contentproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import smartassist.appreciate.be.smartassist.database.ChatSenderTable;
import smartassist.appreciate.be.smartassist.database.DatabaseHelper;

/**
 * Created by Inneke De Clippel on 8/09/2016.
 */
public class ChatSenderContentProvider extends ContentProvider
{
    private SQLiteDatabase database;

    private static final String PROVIDER_NAME = "smartassist.appreciate.be.smartassist.contentproviders.ChatSenderContentProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/");

    private static final int CHAT_SENDERS = 1;

    private static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, null, CHAT_SENDERS);
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
            case CHAT_SENDERS:
                return "vnd.android.cursor.dir/" + PROVIDER_NAME;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ChatSenderTable.TABLE_NAME);
        queryBuilder.setProjectionMap(ChatSenderTable.PROJECTION_MAP);

        switch (uriMatcher.match(uri))
        {
            case CHAT_SENDERS:
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
        long rowID = this.database.replace(ChatSenderTable.TABLE_NAME, null, values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
            this.getContext().getContentResolver().notifyChange(ChatMessageContentProvider.CONTENT_URI, null);
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
            case CHAT_SENDERS:
                rowsDeleted = this.database.delete(ChatSenderTable.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsDeleted > 0)
        {
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
            this.getContext().getContentResolver().notifyChange(ChatMessageContentProvider.CONTENT_URI, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int rowsUpdated;

        switch (uriMatcher.match(uri))
        {
            case CHAT_SENDERS:
                rowsUpdated = this.database.update(ChatSenderTable.TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if(rowsUpdated > 0)
        {
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
            this.getContext().getContentResolver().notifyChange(ChatMessageContentProvider.CONTENT_URI, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        int rowsInserted = 0;

        switch (uriMatcher.match(uri))
        {
            case CHAT_SENDERS:
                DatabaseUtils.InsertHelper inserter = new DatabaseUtils.InsertHelper(this.database, ChatSenderTable.TABLE_NAME);

                this.database.beginTransaction();
                try
                {
                    if(values != null)
                    {
                        for (ContentValues cv : values)
                        {
                            if(cv != null)
                            {
                                inserter.prepareForInsert();
                                inserter.bind(inserter.getColumnIndex(ChatSenderTable.COLUMN_CONVERSATION_ID), cv.getAsInteger(ChatSenderTable.COLUMN_CONVERSATION_ID));
                                inserter.bind(inserter.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_ID), cv.getAsInteger(ChatSenderTable.COLUMN_CONTACT_ID));
                                inserter.bind(inserter.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_NAME), cv.getAsString(ChatSenderTable.COLUMN_CONTACT_NAME));
                                inserter.bind(inserter.getColumnIndex(ChatSenderTable.COLUMN_CONTACT_PHOTO), cv.getAsString(ChatSenderTable.COLUMN_CONTACT_PHOTO));
                                inserter.bind(inserter.getColumnIndex(ChatSenderTable.COLUMN_JOIN_DATE), cv.getAsLong(ChatSenderTable.COLUMN_JOIN_DATE));
                                inserter.bind(inserter.getColumnIndex(ChatSenderTable.COLUMN_LEAVE_DATE), cv.getAsLong(ChatSenderTable.COLUMN_LEAVE_DATE));

                                long rowId = inserter.execute();

                                if (rowId != -1)
                                {
                                    rowsInserted++;
                                }
                            }
                        }
                    }

                    this.database.setTransactionSuccessful();
                }
                catch (Exception e)
                {
                    rowsInserted = 0;
                }
                finally
                {
                    this.database.endTransaction();
                    inserter.close();
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (rowsInserted > 0)
        {
            this.getContext().getContentResolver().notifyChange(CONTENT_URI, null);
            this.getContext().getContentResolver().notifyChange(ChatMessageContentProvider.CONTENT_URI, null);
        }

        return rowsInserted;
    }
}
