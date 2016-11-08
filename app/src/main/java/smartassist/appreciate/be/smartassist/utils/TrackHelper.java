package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import smartassist.appreciate.be.smartassist.application.SmartAssistApplication;

/**
 * Created by thijscoorevits on 13/10/15.
 */
public class TrackHelper
{
    public static final String SCREEN_MAIN = "Overzicht";
    public static final String SCREEN_PHOTOS = "Foto's";
    public static final String SCREEN_RSS = "Nieuws";
    public static final String SCREEN_NEWS = "Berichten";
    public static final String SCREEN_POI = "In de buurt";
    public static final String SCREEN_EMERGENCY = "Contactpersonen";
    public static final String SCREEN_CALENDAR = "Agenda";
    public static final String SCREEN_WEATHER = "Weerbericht";
    public static final String SCREEN_NEWS_DETAIL = "Berichten detail";
    public static final String SCREEN_PHOTO_DETAIL = "Foto detail";
    public static final String SCREEN_POI_DETAIL = "In de buurt detail";
    public static final String SCREEN_RSS_DETAIL = "Nieuws detail";
    public static final String SCREEN_CHAT = "Chat";
    public static final String SCREEN_CHAT_DETAIL = "Chat detail";
    public static final String SCREEN_MEDICATION = "Medicatie";
    public static final String SCREEN_VITALS = "Vitale waarden";
    public static final String SCREEN_CAREBOOK = "Zorgboekje";
    public static final String SCREEN_INVOICE = "Facturatie";
    public static final String SCREEN_INVOICE_DETAIL = "Facturatie detail";
    public static final String SCREEN_HABITANTS = "Bewonersfiche";
    public static final String SCREEN_RESIDENT_INFO = "Residentie info";
    public static final String SCREEN_RESIDENT_INFO_DETAIL = "Residentie info detail";
    public static final String SCREEN_NEARBY_ACTIVITIES = "Nearby_activities";
    public static final String SCREEN_NEARBY_ACTIVITIES_DETAIL = "Nearby_activities_detail";
    public static final String SCREEN_POI_SELECTION = "Poi_selection";

    public static final String DIALOG_REMINDER = "Reminder";
    public static final String APP = "SmartAssist app";

    public static final String CATEGORY_CALL = "Gesprekken";
    public static final String ACTION_CALL_OUTGOING = "Uitgaand gesprek";
    public static final String LABEL_CALL_OUTGOING_SUCCESS = "Opgenomen";
    public static final String LABEL_CALL_OUTGOING_FAILURE = "Niet opgenomen";

    public static void trackTime(Context context, String screenName, long startTime, long endTime)
    {
        if (context != null && context.getApplicationContext() != null && context.getApplicationContext() instanceof SmartAssistApplication)
        {
            long time = endTime - startTime;
            Tracker tracker = ((SmartAssistApplication) context.getApplicationContext()).getTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(screenName)
                    .setAction("Flat id: " + String.valueOf(PreferencesHelper.getFlatId(context)))
                    .setLabel(DateUtils.formatElapsedTime(time / 1000))
                    .build());
        }
    }

    public static void trackScreen(Context context, String screenName)
    {
        if (context != null && context.getApplicationContext() != null && context.getApplicationContext() instanceof SmartAssistApplication)
        {
            Tracker tracker = ((SmartAssistApplication) context.getApplicationContext()).getTracker();
            tracker.setScreenName(screenName);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public static void trackAction(Context context, String category, String action, String label)
    {
        if (context != null && context.getApplicationContext() != null && context.getApplicationContext() instanceof SmartAssistApplication)
        {
            Tracker tracker = ((SmartAssistApplication) context.getApplicationContext()).getTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .build());
        }
    }

    public static void trackCallSuccess(Context context)
    {
        trackAction(context, CATEGORY_CALL, ACTION_CALL_OUTGOING, LABEL_CALL_OUTGOING_SUCCESS);
    }

    public static void trackCallFailure(Context context)
    {
        trackAction(context, CATEGORY_CALL, ACTION_CALL_OUTGOING, LABEL_CALL_OUTGOING_FAILURE);
    }
}
