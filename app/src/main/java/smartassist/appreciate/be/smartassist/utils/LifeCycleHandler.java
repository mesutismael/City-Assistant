package smartassist.appreciate.be.smartassist.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import smartassist.appreciate.be.smartassist.receivers.AlarmReceiver;

/**
 * Created by Inneke on 2/02/2015.
 */
public class LifeCycleHandler implements Application.ActivityLifecycleCallbacks
{
    private int started;
    private int stopped;
    private long appStartTime;
    public static boolean appVisible;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {
    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {
    }

    @Override
    public void onActivityStarted(Activity activity)
    {
        ++this.started;

        if(this.started == 1)
        {
            this.startRepeatingAlarm(activity);
            this.appStartTime = System.currentTimeMillis();
        }

        LifeCycleHandler.appVisible = this.started > this.stopped;
    }

    @Override
    public void onActivityStopped(final Activity activity)
    {
        ++this.stopped;

        if(this.stopped >= this.started)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                public void run()
                {
                    if(LifeCycleHandler.this.stopped >= LifeCycleHandler.this.started)
                    {
                        LifeCycleHandler.this.stopped = 0;
                        LifeCycleHandler.this.started = 0;
                        LifeCycleHandler.this.stopRepeatingAlarm(activity);
                        long appEndTime = System.currentTimeMillis();
                        TrackHelper.trackTime(activity, TrackHelper.APP, LifeCycleHandler.this.appStartTime, appEndTime);
                    }
                }
            }, 300);
        }

        LifeCycleHandler.appVisible = this.started > this.stopped;
    }

    private void startRepeatingAlarm(Context context)
    {
        Log.d("AlarmReceiver", "Starting the alarm");
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //TODO change refresh interval. Now it is set to every 20 minutes.
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 300000, 1200000, alarmIntent);
    }

    private void stopRepeatingAlarm(Context context)
    {
        Log.d("AlarmReceiver", "Canceling the alarm");
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.cancel(alarmIntent);
    }
}
