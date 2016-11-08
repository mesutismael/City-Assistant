package smartassist.appreciate.be.smartassist.application;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.LifeCycleHandler;

/**
 * Created by Inneke on 29/01/2015.
 */
public class SmartAssistApplication extends Application
{
    private Tracker tracker;

    @Override
    public void onCreate()
    {
        super.onCreate();

        this.registerActivityLifecycleCallbacks(new LifeCycleHandler());

        //Uncomment this code for 1 run if there are changes made to the database
        //this.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        //PreferencesHelper.clearConfiguration(this);
    }

    public synchronized Tracker getTracker()
    {
        if (this.tracker == null)
        {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            this.tracker = analytics.newTracker(R.xml.app_tracker);

        }
        return this.tracker;
    }
}
