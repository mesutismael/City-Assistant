package smartassist.appreciate.be.smartassist.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.common.GoogleApiAvailability;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;

import java.util.HashMap;
import java.util.Map;

import smartassist.appreciate.be.smartassist.activities.IncomingCallActivity;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 20/01/2016.
 */
public class SinchService extends Service implements CallClientListener, SinchClientListener
{
    private SinchServiceInterface sinchServiceInterface;
    private SinchClient sinchClient;
    private String userId;
    private StartFailedListener startFailedListener;

    private static final String APP_KEY = "7e51bad0-8efd-45e8-a955-9915fe152735";//"298203b2-78ee-4664-8559-98ed6f559e7e";
    private static final String APP_SECRET = "io05zI/ylkOjzhqxFAf1Kw==";//"VxnbRsMb8ESxPGX5npY5aw==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";//"sandbox.sinch.com";

    private static final String HEADER_NAME = "username";
    private static final String HEADER_PHOTO = "photo";

    @Override
    public void onCreate()
    {
        super.onCreate();

        this.sinchServiceInterface = new SinchServiceInterface();
    }

    @Override
    public void onDestroy()
    {
        if (this.sinchClient != null && this.sinchClient.isStarted())
        {
            this.sinchClient.terminate();
        }

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return this.sinchServiceInterface;
    }

    private void start(String userName)
    {
        if (this.sinchClient == null)
        {
            this.userId = userName;
            this.sinchClient = Sinch.getSinchClientBuilder()
                    .context(this.getApplicationContext())
                    .userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT)
                    .build();

            this.sinchClient.setSupportCalling(true);
            this.sinchClient.startListeningOnActiveConnection();
            if(this.canReceivePushNotifications())
            {
                this.sinchClient.setSupportManagedPush(true);
            }
            this.sinchClient.addSinchClientListener(this);
            this.sinchClient.getCallClient().addCallClientListener(this);
            this.sinchClient.getVideoController().setBorderColor(0.9375f, 0.9375f, 0.9375f);
            this.sinchClient.start();
        }
    }

    private void stop()
    {
        if (this.sinchClient != null)
        {
            this.sinchClient.terminate();
            this.sinchClient = null;
        }
    }

    private boolean isStarted()
    {
        return this.sinchClient != null && this.sinchClient.isStarted();
    }

    private boolean canReceivePushNotifications()
    {
        try
        {
            return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == 0;
        }
        catch (NoClassDefFoundError var2) {
            return false;
        }
    }

    //SinchClientListener

    @Override
    public void onClientFailed(SinchClient client, SinchError error)
    {
        if (SinchService.this.startFailedListener != null)
        {
            SinchService.this.startFailedListener.onStartFailed(error);
        }

        SinchService.this.stop();
    }

    @Override
    public void onClientStarted(SinchClient client)
    {
        if (SinchService.this.startFailedListener != null)
        {
            SinchService.this.startFailedListener.onStarted();
        }
    }

    @Override
    public void onClientStopped(SinchClient client)
    {
    }

    @Override
    public void onLogMessage(int level, String area, String message)
    {
    }

    @Override
    public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration)
    {
    }

    //CallClientListener

    @Override
    public void onIncomingCall(CallClient callClient, Call call)
    {
        Map<String, String> headers = call.getHeaders();
        String username = headers != null ? headers.get(HEADER_NAME) : null;
        String photo = headers != null ? headers.get(HEADER_PHOTO) : null;

        Intent intent = new Intent(SinchService.this, IncomingCallActivity.class);
        intent.putExtra(IncomingCallActivity.KEY_CALL_ID, call.getCallId());
        intent.putExtra(IncomingCallActivity.KEY_CALL_NAME, username);
        intent.putExtra(IncomingCallActivity.KEY_CALL_PHOTO, photo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SinchService.this.startActivity(intent);
    }

    //Interfaces

    public class SinchServiceInterface extends Binder
    {
        public Call callUserVideo(String userId)
        {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_NAME, PreferencesHelper.getFlatName(SinchService.this));
            headers.put(HEADER_PHOTO, PreferencesHelper.getFlatPhoto(SinchService.this));

            return SinchService.this.sinchClient.getCallClient().callUserVideo(userId, headers);
        }

        public Call callUserAudio(String userId)
        {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_NAME, PreferencesHelper.getFlatName(SinchService.this));
            headers.put(HEADER_PHOTO, PreferencesHelper.getFlatPhoto(SinchService.this));

            return SinchService.this.sinchClient.getCallClient().callUser(userId, headers);
        }

        public String getUserName()
        {
            return SinchService.this.userId;
        }

        public boolean isStarted()
        {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName)
        {
            SinchService.this.start(userName);
        }

        public void stopClient()
        {
            SinchService.this.stop();
        }

        public void setStartListener(StartFailedListener listener)
        {
            SinchService.this.startFailedListener = listener;
        }

        public Call getCall(String callId)
        {
            return SinchService.this.sinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController()
        {
            if (!SinchService.this.isStarted())
            {
                return null;
            }
            return SinchService.this.sinchClient.getVideoController();
        }

        public AudioController getAudioController()
        {
            if (!SinchService.this.isStarted())
            {
                return null;
            }
            return SinchService.this.sinchClient.getAudioController();
        }

        public NotificationResult relayRemotePushNotificationPayload(Intent intent)
        {
            String hash = PreferencesHelper.getHash(SinchService.this);
            if(TextUtils.isEmpty(hash))
            {
                return null;
            }
            else
            {
                if(SinchService.this.sinchClient == null)
                {
                    SinchService.this.sinchServiceInterface.startClient(hash);
                }
                return SinchService.this.sinchClient.relayRemotePushNotificationPayload(intent);
            }
        }
    }

    public interface StartFailedListener
    {
        void onStartFailed(SinchError error);
        void onStarted();
    }
}
