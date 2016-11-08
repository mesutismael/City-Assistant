package smartassist.appreciate.be.smartassist.receivers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import smartassist.appreciate.be.smartassist.services.GcmIntentService;

/**
 * Created by Inneke De Clippel on 21/01/2016.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        intent.setComponent(comp);
        startWakefulService(context, intent);
        this.setResultCode(Activity.RESULT_OK);
    }
}
