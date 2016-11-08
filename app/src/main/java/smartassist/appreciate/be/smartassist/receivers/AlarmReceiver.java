package smartassist.appreciate.be.smartassist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import smartassist.appreciate.be.smartassist.services.DataService;

/**
 * Created by Inneke on 2/02/2015.
 */
public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("AlarmReceiver", "onReceive");
        Intent serviceIntent = new Intent(context, DataService.class);
        serviceIntent.putExtra(DataService.EXTRA_ACTION, DataService.ACTION_REFRESH);
        context.startService(serviceIntent);
    }
}
