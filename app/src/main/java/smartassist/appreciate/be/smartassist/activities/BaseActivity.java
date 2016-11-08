package smartassist.appreciate.be.smartassist.activities;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarEventContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.NotificationContentProvider;
import smartassist.appreciate.be.smartassist.database.CalendarEventTable;
import smartassist.appreciate.be.smartassist.database.NotificationTable;
import smartassist.appreciate.be.smartassist.services.SinchService;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.NotificationHelper;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke on 2/03/2015.
 */
public class BaseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ServiceConnection
{
    private List<MaterialDialog> dialogsAlarm;
    private MaterialDialog dialogUnreadItem;
    private boolean resumed;
    private Handler handlerAlarm;
    private Runnable callbackAlarm;
    private Handler handlerScreenSaver;
    private Runnable callbackScreenSaver;
    private long dialogStartTime;
    private long activityStartTime;
    private SinchService.SinchServiceInterface sinchServiceInterface;

    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("d MMMM H:mm", Locale.getDefault());
    private static final int ALARM_CHECK_INTERVAL = 2 * 60 * 1000; //Check calendar alarms every 2 minutes
    private static final long TIMEOUT_SCREEN_SAVER = 10 * 60 * 1000; // Screen saver starts after 10 min

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.resumed = false;
        this.dialogsAlarm = new ArrayList<>();
        this.handlerAlarm = new Handler();
        this.callbackAlarm = new Runnable()
        {
            @Override
            public void run()
            {
                BaseActivity.this.restartAlarmLoaderIfNeeded(false);
            }
        };
        this.handlerScreenSaver = new Handler();
        this.callbackScreenSaver = new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(BaseActivity.this, ScreenSaverActivity.class);
                BaseActivity.this.startActivity(intent);
            }
        };

        Intent sinchServiceIntent = new Intent(this, SinchService.class);
        this.getApplicationContext().bindService(sinchServiceIntent, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        String screenName = this.getTrackingScreenName();

        if(!TextUtils.isEmpty(screenName))
        {
            TrackHelper.trackScreen(this, screenName);
            this.activityStartTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onStop()
    {
        String screenName = this.getTrackingScreenName();

        if(!TextUtils.isEmpty(screenName))
        {
            long activityEndTime = System.currentTimeMillis();
            TrackHelper.trackTime(this, screenName, this.activityStartTime, activityEndTime);
        }

        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.restartAlarmLoaderIfNeeded(true);
        this.getSupportLoaderManager().restartLoader(Constants.LOADER_NOTIFICATIONS, null, this);
        this.resumed = true;
        this.showDialogsAlarm();
        this.startTimerScreenSaver();
        this.startTimerAlarm();
    }

    @Override
    protected void onPause()
    {
        this.resumed = false;
        this.dismissDialogsAlarm();
        if (this.dialogUnreadItem != null && this.dialogUnreadItem.isShowing())
            this.dialogUnreadItem.dismiss();
        Crouton.cancelAllCroutons();
        this.stopTimerScreenSaver();
        this.stopTimerAlarm();

        super.onPause();
    }

    public void startTimerAlarm()
    {
        this.handlerAlarm.postDelayed(this.callbackAlarm, ALARM_CHECK_INTERVAL);
    }

    public void stopTimerAlarm()
    {
        this.handlerAlarm.removeCallbacks(this.callbackAlarm);
    }

    public void startTimerScreenSaver()
    {
        this.handlerScreenSaver.postDelayed(this.callbackScreenSaver, TIMEOUT_SCREEN_SAVER);
    }

    public void stopTimerScreenSaver()
    {
        this.handlerScreenSaver.removeCallbacks(this.callbackScreenSaver);
    }

    private void restartAlarmLoaderIfNeeded(boolean checkLast)
    {
        long now = System.currentTimeMillis();
        long lastAlarmCheck = PreferencesHelper.getLastAlarmCheck(this);

        if (!checkLast || now - lastAlarmCheck >= ALARM_CHECK_INTERVAL)
        {
            PreferencesHelper.saveLastAlarmCheck(this, now);
            this.getSupportLoaderManager().restartLoader(Constants.LOADER_ALARMS, null, this);
            this.stopTimerAlarm();
            this.startTimerAlarm();
        }
    }

    private void addDialogAlarm(final int id, String title, String body)
    {
        if (body != null && body.length() > 300)
            body = body.substring(0, 300) + "...";

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(title)
                .content(body)
                .positiveText(R.string.calendar_dialog_confirm)
                .callback(new MaterialDialog.ButtonCallback()
                {
                    @Override
                    public void onPositive(MaterialDialog dialog)
                    {
                        BaseActivity.this.dialogsAlarm.remove(dialog);
                        ContentValues cv = new ContentValues();
                        cv.put(CalendarEventTable.COLUMN_ALARM_FINISHED, 1);
                        String whereClause = CalendarEventTable.COLUMN_EVENT_ID + "='" + id + "'";
                        BaseActivity.this.getContentResolver().update(CalendarEventContentProvider.CONTENT_URI, cv, whereClause, null);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialog)
                    {
                        BaseActivity.this.trackDialogTime();
                    }
                })
                .cancelable(false)
                .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf");

        this.applyDialogStyle(builder);

        MaterialDialog materialDialog = builder.build();
        this.dialogsAlarm.add(materialDialog);

        this.dialogStartTime = System.currentTimeMillis();
    }

    private void showDialog(Cursor cursor)
    {
        String title = cursor.getString(cursor.getColumnIndex(NotificationTable.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(NotificationTable.COLUMN_DESCRIPTION));
        final int itemId = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_ITEM_ID));
        final int itemType = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_TYPE));

        if (description != null && description.length() > 300)
            description = description.substring(0, 300) + "...";

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(title)
                .content(description)
                .positiveText(R.string.news_dialog_confirm)
                .callback(new MaterialDialog.ButtonCallback()
                {
                    @Override
                    public void onPositive(MaterialDialog dialog)
                    {
                        String whereClause = NotificationTable.COLUMN_ITEM_ID + "='" + itemId + "' AND " + NotificationTable.COLUMN_TYPE + "='" + itemType + "'";
                        BaseActivity.this.getContentResolver().delete(NotificationContentProvider.CONTENT_URI, whereClause, null);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog)
                    {
                        if (itemType == NotificationHelper.TYPE_NEWS)
                        {
                            String whereClause = NotificationTable.COLUMN_ITEM_ID + "='" + itemId + "' AND " + NotificationTable.COLUMN_TYPE + "='" + itemType + "'";
                            BaseActivity.this.getContentResolver().delete(NotificationContentProvider.CONTENT_URI, whereClause, null);
                            BaseActivity.this.goToNewsDetail(itemId);
                        }
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialog)
                    {
                        BaseActivity.this.trackDialogTime();
                    }
                })
                .cancelable(false)
                .typeface("Montserrat-Regular.otf", "Montserrat-Light.otf");

        this.applyDialogStyle(builder);

        if (itemType == NotificationHelper.TYPE_NEWS)
        {
            builder.negativeText(R.string.news_dialog_detail);
        }

        this.dialogUnreadItem = builder.build();

        if (this.resumed)
            this.dialogUnreadItem.show();

        this.dialogStartTime = System.currentTimeMillis();
    }

    public void applyDialogStyle(MaterialDialog.Builder builder)
    {
    }

    public void goToNewsDetail(int newsId)
    {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.KEY_NEWS_ID, newsId);
        this.startActivity(intent);
    }

    private void showCrouton(Cursor cursor)
    {
        String title = cursor.getString(cursor.getColumnIndex(NotificationTable.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(NotificationTable.COLUMN_DESCRIPTION));
        final int itemId = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_ITEM_ID));
        final int itemType = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_TYPE));
        final int extraId = cursor.getInt(cursor.getColumnIndex(NotificationTable.COLUMN_EXTRA_ID));

        if(this.showNotification(itemId, itemType, extraId))
        {
            View view = LayoutInflater.from(this).inflate(R.layout.view_crouton, null);
            ImageView imageViewIcon = (ImageView) view.findViewById(R.id.imageView_icon);
            RelativeLayout layoutCrouton = (RelativeLayout) view.findViewById(R.id.layout_crouton);
            TextView textViewTitle = (TextView) view.findViewById(R.id.textView_title);
            TextView textViewBody = (TextView) view.findViewById(R.id.textView_body);

            layoutCrouton.setBackgroundResource(this.getCroutonBackground());
            imageViewIcon.setImageResource(itemType == NotificationHelper.TYPE_CHAT ? R.drawable.ic_chat_white : R.drawable.ic_mail_white);
            textViewTitle.setText(title);
            textViewBody.setText(description);

            Configuration config = new Configuration.Builder()
                    .setDuration(6000)
                    .build();
            final Crouton crouton = Crouton.make(this, view);
            crouton.setConfiguration(config);
            crouton.setLifecycleCallback(new LifecycleCallback()
            {
                @Override
                public void onDisplayed()
                {
                }

                @Override
                public void onRemoved()
                {
                    BaseActivity.this.removeNotification(itemId, itemType);
                }
            });

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(itemType == NotificationHelper.TYPE_CHAT)
                    {
                        Intent intent = new Intent(v.getContext(), ChatActivity.class);
                        intent.putExtra(ChatActivity.KEY_CONVERSATION_ID, extraId);
                        BaseActivity.this.startActivity(intent);
                    }

                    crouton.cancel();
                    BaseActivity.this.removeNotification(itemId, itemType);
                }
            });

            if (this.resumed)
                crouton.show();
        }
        else
        {
            this.removeNotification(itemId, itemType);
        }
    }

    private void removeNotification(int itemId, int itemType)
    {
        String whereClause = NotificationTable.COLUMN_ITEM_ID + "='" + itemId + "' AND " + NotificationTable.COLUMN_TYPE + "='" + itemType + "'";
        this.getContentResolver().delete(NotificationContentProvider.CONTENT_URI, whereClause, null);
    }

    public boolean showNotification(int itemId, int itemType, int extraId)
    {
        return true;
    }

    public int getCroutonBackground()
    {
        return R.drawable.shape_week_event;
    }

    private void showDialogsAlarm()
    {
        if (this.resumed && this.dialogsAlarm != null && this.dialogsAlarm.size() > 0)
            for (MaterialDialog dialog : this.dialogsAlarm)
                if (dialog != null && !dialog.isShowing())
                    dialog.show();
    }

    private void dismissDialogsAlarm()
    {
        if (this.dialogsAlarm != null)
            for (MaterialDialog dialog : this.dialogsAlarm)
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_NOTIFICATIONS:
                return new CursorLoader(this, NotificationContentProvider.CONTENT_URI, null, null, null, NotificationTable.COLUMN_HARD_REMINDER + " ASC");

            case Constants.LOADER_ALARMS:
                String whereClauseAlarms = CalendarEventTable.COLUMN_ALARM + " != 0" +
                        " AND " + CalendarEventTable.COLUMN_ALARM + " <= " + System.currentTimeMillis() +
                        " AND " + CalendarEventTable.COLUMN_START + " >= " + System.currentTimeMillis() +
                        " AND " + CalendarEventTable.COLUMN_ALARM_FINISHED + " = 0";
                return new CursorLoader(this, CalendarEventContentProvider.CONTENT_URI, null, whereClauseAlarms, null, null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        switch (loader.getId())
        {
            case Constants.LOADER_NOTIFICATIONS:
                if (this.dialogUnreadItem != null && this.dialogUnreadItem.isShowing())
                    this.dialogUnreadItem.dismiss();
                Crouton.cancelAllCroutons();

                if (data != null && data.moveToFirst())
                {
                    if (data.getInt(data.getColumnIndex(NotificationTable.COLUMN_HARD_REMINDER)) == 1)
                        this.showDialog(data);
                    else
                        this.showCrouton(data);
                }
                break;

            case Constants.LOADER_ALARMS:
                this.dismissDialogsAlarm();

                if (this.dialogsAlarm != null)
                    this.dialogsAlarm.clear();

                if (data != null)
                {
                    data.moveToFirst();
                    while (!data.isAfterLast())
                    {
                        int id = data.getInt(data.getColumnIndex(CalendarEventTable.COLUMN_EVENT_ID));
                        String title = this.getString(R.string.calendar_dialog_alarm_title);
                        String description = data.getString(data.getColumnIndex(CalendarEventTable.COLUMN_DESCRIPTION));
                        long start = data.getLong(data.getColumnIndex(CalendarEventTable.COLUMN_START));
                        String startDate = SDF_DATE.format(new Date(start));
                        String body = this.getString(R.string.calendar_dialog_description, startDate, description);

                        this.addDialogAlarm(id, title, body);
                        data.moveToNext();
                    }

                    this.showDialogsAlarm();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
    }

    public void startParentActivity()
    {
        Intent parentIntent = this.getParentActivityIntent();
        if (parentIntent != null)
        {
            parentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(parentIntent);
            this.finish();
        } else
        {
            this.onNavigateUp();
        }
    }

    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        this.stopTimerScreenSaver();
        this.startTimerScreenSaver();
    }

    private void trackDialogTime()
    {
        long dialogEndTime = System.currentTimeMillis();
        TrackHelper.trackTime(this, TrackHelper.DIALOG_REMINDER, this.dialogStartTime, dialogEndTime);
    }

    public String getTrackingScreenName()
    {
        return null;
    }

    protected void onServiceConnected()
    {
        // for subclasses
    }

    protected void onServiceDisconnected()
    {
        // for subclasses
    }

    public SinchService.SinchServiceInterface getSinchServiceInterface()
    {
        return this.sinchServiceInterface;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        if (SinchService.class.getName().equals(name.getClassName()))
        {
            this.sinchServiceInterface = (SinchService.SinchServiceInterface) service;

            String hash = PreferencesHelper.getHash(this);
            if(!TextUtils.isEmpty(hash))
            {
                this.sinchServiceInterface.startClient(hash);
            }

            this.onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {
        if (SinchService.class.getName().equals(name.getClassName()))
        {
            this.sinchServiceInterface = null;
            this.onServiceDisconnected();
        }
    }
}
