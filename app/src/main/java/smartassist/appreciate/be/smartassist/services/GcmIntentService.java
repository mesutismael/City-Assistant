package smartassist.appreciate.be.smartassist.services;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.SinchHelpers;

import smartassist.appreciate.be.smartassist.receivers.GcmBroadcastReceiver;

/**
 * Created by Inneke De Clippel on 21/01/2016.
 */
public class GcmIntentService extends IntentService implements ServiceConnection
{
    private Intent intent;

    public GcmIntentService()
    {
        this("GcmIntentService");
    }

    public GcmIntentService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (SinchHelpers.isSinchPushIntent(intent))
        {
            this.intent = intent;

            Intent serviceIntent = new Intent(this, SinchService.class);
            this.getApplicationContext().bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
        }
        else
        {
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service)
    {
        if (SinchService.class.getName().equals(name.getClassName()) && SinchHelpers.isSinchPushIntent(this.intent))
        {
            SinchService.SinchServiceInterface sinchServiceInterface = (SinchService.SinchServiceInterface) service;

            if(sinchServiceInterface != null)
            {
                NotificationResult result = sinchServiceInterface.relayRemotePushNotificationPayload(this.intent);
                Log.d("GcmIntentService", "- DisplayName: "+result.getDisplayName());
                Log.d("GcmIntentService", "- Call: "+String.valueOf(result.isCall()));
                Log.d("GcmIntentService", "- CallResult callId: "+(result.getCallResult() != null ? result.getCallResult().getCallId() : "null"));
                Log.d("GcmIntentService", "- CallResult remoteUserId: "+(result.getCallResult() != null ? result.getCallResult().getRemoteUserId() : "null"));
                Log.d("GcmIntentService", "- CallResult videoOffered: "+(result.getCallResult() != null ? String.valueOf(result.getCallResult().isVideoOffered()) : "null"));
                Log.d("GcmIntentService", "- CallResult timedOut: "+(result.getCallResult() != null ? String.valueOf(result.getCallResult().isTimedOut()) : "null"));
                Log.d("GcmIntentService", "- Valid: "+String.valueOf(result.isValid()));
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(this.intent);
    }

    @Override
    public void onServiceDisconnected(ComponentName name)
    {

    }
}
