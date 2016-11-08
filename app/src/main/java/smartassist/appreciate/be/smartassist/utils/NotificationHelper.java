package smartassist.appreciate.be.smartassist.utils;

import android.content.ContentValues;
import android.content.Context;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.database.NotificationTable;
import smartassist.appreciate.be.smartassist.model.api.CalendarEvent;
import smartassist.appreciate.be.smartassist.model.api.ChatMessage;
import smartassist.appreciate.be.smartassist.model.api.NewsItem;
import smartassist.appreciate.be.smartassist.model.api.ResidentNews;

/**
 * Created by Inneke on 16/03/2015.
 */
public class NotificationHelper
{
    public static final int TYPE_NEWS = 1;
    public static final int TYPE_EVENT = 2;
    public static final int TYPE_CHAT = 3;

    public static ContentValues getContentValues(long timestamp, NewsItem newsItem)
    {
        ContentValues cv = new ContentValues();

        cv.put(NotificationTable.COLUMN_ITEM_ID, newsItem.getId());
        cv.put(NotificationTable.COLUMN_TITLE, newsItem.getTitle());
        cv.put(NotificationTable.COLUMN_DESCRIPTION, newsItem.getBody());
        cv.put(NotificationTable.COLUMN_TYPE, TYPE_NEWS);
        cv.put(NotificationTable.COLUMN_HARD_REMINDER, newsItem.isHardReminder() ? 1 : 0);
        cv.put(NotificationTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }

    public static ContentValues getContentValues(long timestamp, ResidentNews residentNews)
    {
        ContentValues cv = new ContentValues();

        cv.put(NotificationTable.COLUMN_ITEM_ID, residentNews.getId());
        cv.put(NotificationTable.COLUMN_TITLE, residentNews.getTitle());
        cv.put(NotificationTable.COLUMN_DESCRIPTION, residentNews.getBody());
        cv.put(NotificationTable.COLUMN_TYPE, TYPE_NEWS);
        cv.put(NotificationTable.COLUMN_HARD_REMINDER, residentNews.isHardReminder() ? 1 : 0);
        cv.put(NotificationTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }

    public static ContentValues getContentValues(long timestamp, Context context, CalendarEvent event)
    {
        ContentValues cv = new ContentValues();

        cv.put(NotificationTable.COLUMN_ITEM_ID, event.getId());
        cv.put(NotificationTable.COLUMN_TITLE, context.getString(R.string.calendar_dialog_unread_title));
        cv.put(NotificationTable.COLUMN_DESCRIPTION, event.getDescription());
        cv.put(NotificationTable.COLUMN_TYPE, TYPE_EVENT);
        cv.put(NotificationTable.COLUMN_HARD_REMINDER, event.isHardReminder() ? 1 : 0);
        cv.put(NotificationTable.COLUMN_LAST_UPDATE, timestamp);

        return cv;
    }

    public static ContentValues getContentValues(Context context, ChatMessage message)
    {
        ContentValues cv = new ContentValues();

        cv.put(NotificationTable.COLUMN_ITEM_ID, message.getId());
        cv.put(NotificationTable.COLUMN_EXTRA_ID, message.getConversationId());
        cv.put(NotificationTable.COLUMN_TITLE, context.getString(R.string.chat_dialog_unread_title));
        cv.put(NotificationTable.COLUMN_DESCRIPTION, message.getMessage());
        cv.put(NotificationTable.COLUMN_TYPE, TYPE_CHAT);
        cv.put(NotificationTable.COLUMN_HARD_REMINDER, false);
        cv.put(NotificationTable.COLUMN_LAST_UPDATE, 0);

        return cv;
    }
}
