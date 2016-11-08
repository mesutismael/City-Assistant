package smartassist.appreciate.be.smartassist.receivers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Inneke on 29/01/2015.
 */
public class DeviceAdminRcvr extends DeviceAdminReceiver
{
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.d("DeviceAdminRcvr", "onEnabled");
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.d("DeviceAdminRcvr", "onDisabled");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        super.onPasswordFailed(context, intent);
        Log.d("DeviceAdminRcvr", "onPasswordFailed");
    }

    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        super.onProfileProvisioningComplete(context, intent);
        Log.d("DeviceAdminRcvr", "onProfileProvisioningComplete");
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg)
    {
        super.onLockTaskModeEntering(context, intent, pkg);
        Log.d("DeviceAdminRcvr", "onLockTaskModeEntering "+pkg);
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent)
    {
        super.onLockTaskModeExiting(context, intent);
        Log.d("DeviceAdminRcvr", "onLockTaskModeExiting");
    }
}
