package smartassist.appreciate.be.smartassist.activities;

import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallState;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;
import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.AudioPlayer;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 20/01/2016.
 */
public class CallActivity extends BaseActivity implements View.OnClickListener, VideoCallListener
{
    private TextView textViewTitle;
    private TextView textViewTimerVideo;
    private TextView textViewTimerNoVideo;
    private TextView textViewCaller;
    private ImageView imageViewCaller;
    private ImageView imageViewToggleCamera;
    private ImageView imageViewToggleInput;
    private RelativeLayout layoutLocalVideo;
    private LinearLayout layoutRemoteVideo;
    private LinearLayout layoutNoVideo;
    private String callId;
    private Call call;
    private AudioPlayer audioPlayer;
    private long callStartTime;
    private boolean cameraEnabled;
    private boolean cameraFacingFront;
    private boolean videoOffered;
    private Handler handlerTimer;
    private Runnable callbackTimer;
    private boolean outgoingCall;
    private boolean outgoingCallTracked;
    private String callName;
    private String callPhoto;

    public static final String KEY_OUTGOING_CALL = "outgoing_call";
    public static final String KEY_CALL_ID = "call_id";
    public static final String KEY_CALL_NAME = "call_name";
    public static final String KEY_CALL_PHOTO = "call_photo";
    private static final long TIMER_UPDATE_INTERVAL = 1000; //Every second

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_call);

        this.textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);
        ImageView imageViewHangUp = (ImageView) this.findViewById(R.id.imageView_hangUp);
        this.textViewTimerVideo = (TextView) this.findViewById(R.id.textView_timer);
        this.textViewTimerNoVideo = (TextView) this.findViewById(R.id.textView_timerNoVideo);
        this.textViewCaller = (TextView) this.findViewById(R.id.textView_caller);
        this.imageViewCaller = (ImageView) this.findViewById(R.id.imageView_caller);
        this.imageViewToggleCamera = (ImageView) this.findViewById(R.id.imageView_toggleCamera);
        this.imageViewToggleInput = (ImageView) this.findViewById(R.id.imageView_toggleInput);
        this.layoutLocalVideo = (RelativeLayout) this.findViewById(R.id.layout_localVideo);
        this.layoutRemoteVideo = (LinearLayout) this.findViewById(R.id.layout_remoteVideo);
        this.layoutNoVideo = (LinearLayout) this.findViewById(R.id.layout_noVideo);

        layoutBack.setOnClickListener(this);
        imageViewHangUp.setOnClickListener(this);
        this.imageViewToggleCamera.setOnClickListener(this);
        this.imageViewToggleInput.setOnClickListener(this);

        this.callId = this.getIntent().getStringExtra(KEY_CALL_ID);
        this.outgoingCall = this.getIntent().getBooleanExtra(KEY_OUTGOING_CALL, false);
        this.callName = this.getIntent().getStringExtra(KEY_CALL_NAME);
        this.callPhoto = this.getIntent().getStringExtra(KEY_CALL_PHOTO);

        this.audioPlayer = new AudioPlayer(this);

        this.handlerTimer = new Handler();
        this.callbackTimer = new Runnable()
        {
            @Override
            public void run()
            {
                CallActivity.this.updateTimer();
                CallActivity.this.handlerTimer.postDelayed(CallActivity.this.callbackTimer, TIMER_UPDATE_INTERVAL);
            }
        };
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        this.addVideoViews();
    }

    @Override
    protected void onStop()
    {
        this.removeVideoViews();

        super.onStop();
    }

    @Override
    public void finish()
    {
        this.audioPlayer.stopProgressTone();
        this.handlerTimer.removeCallbacks(this.callbackTimer);

        if(this.call != null)
        {
            this.trackCall(false);

            this.setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            this.call.removeCallListener(this);
            this.call.hangup();
        }

        super.finish();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.layout_back:
                this.startParentActivity();
                break;

            case R.id.imageView_hangUp:
                if(this.call != null)
                {
                    this.call.hangup();
                }
                break;

            case R.id.imageView_toggleCamera:
                this.cameraEnabled = !this.cameraEnabled;
                this.updateLocalVideo();
                break;

            case R.id.imageView_toggleInput:
                VideoController vc = this.getSinchServiceInterface() != null ? this.getSinchServiceInterface().getVideoController() : null;
                if (vc != null)
                {
                    boolean currentlyFacingFront = vc.getCaptureDevicePosition() == Camera.CameraInfo.CAMERA_FACING_FRONT;
                    vc.setCaptureDevicePosition(currentlyFacingFront ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT);
                    this.cameraFacingFront = !currentlyFacingFront;
                    this.updateLocalVideo();
                }
                break;
        }
    }

    @Override
    public void onServiceConnected()
    {
        this.call = this.getSinchServiceInterface().getCall(this.callId);

        if (this.call != null)
        {
            VideoController vc = this.getSinchServiceInterface().getVideoController();
            this.cameraFacingFront = vc == null || vc.getCaptureDevicePosition() == Camera.CameraInfo.CAMERA_FACING_FRONT;
            this.cameraEnabled = true;
            this.videoOffered = this.call.getDetails() != null && this.call.getDetails().isVideoOffered();
            this.call.addCallListener(this);
            this.updateRemoteVideo();
            this.updateLocalVideo();
            if (this.call.getState() == CallState.ESTABLISHED)
            {
                this.addVideoViews();
            }

            String name = !TextUtils.isEmpty(this.callName) ? this.callName : this.call.getRemoteUserId();
            this.textViewCaller.setText(name);
            this.textViewTitle.setText(name);

            if(!TextUtils.isEmpty(this.callPhoto))
            {
                Picasso.with(this)
                        .load(this.callPhoto)
                        .into(this.imageViewCaller);
            }
        }
        else
        {
            this.finish();
        }
    }

    @Override
    public void onCallProgressing(Call call)
    {
        this.audioPlayer.playProgressTone();
    }

    @Override
    public void onCallEstablished(Call call)
    {
        this.trackCall(true);
        this.callStartTime = System.currentTimeMillis();
        this.handlerTimer.post(this.callbackTimer);
        this.audioPlayer.stopProgressTone();
        this.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }

    @Override
    public void onCallEnded(Call call)
    {
        this.finish();
    }

    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> list)
    {

    }

    @Override
    public void onVideoTrackAdded(Call call)
    {
        this.addVideoViews();
    }

    private void addVideoViews()
    {
        VideoController vc = this.getSinchServiceInterface() != null ? this.getSinchServiceInterface().getVideoController() : null;

        if (vc != null)
        {
            this.layoutLocalVideo.addView(vc.getLocalView());
            this.layoutRemoteVideo.addView(vc.getRemoteView());
        }
    }

    private void removeVideoViews()
    {
        VideoController vc = this.getSinchServiceInterface() != null ? this.getSinchServiceInterface().getVideoController() : null;

        if (vc != null)
        {
            this.layoutLocalVideo.removeView(vc.getLocalView());
            this.layoutRemoteVideo.removeView(vc.getRemoteView());
        }
    }

    private void updateTimer()
    {
        long duration = (System.currentTimeMillis() - this.callStartTime) / 1000;
        int minutes = (int) (duration / 60);
        int seconds = (int) (duration % 60);
        String formattedTime = this.getString(R.string.call_timer, minutes, seconds);
        this.textViewTimerVideo.setText(formattedTime);
        this.textViewTimerNoVideo.setText(formattedTime);
    }

    private void updateLocalVideo()
    {
        this.layoutLocalVideo.setVisibility(this.cameraEnabled ? View.VISIBLE : View.INVISIBLE);
        this.imageViewToggleCamera.setVisibility(this.videoOffered ? View.VISIBLE : View.GONE);
        this.imageViewToggleCamera.setImageResource(this.cameraEnabled ? R.drawable.call_camera_enabled : R.drawable.call_camera_disabled);
        this.imageViewToggleInput.setVisibility(this.videoOffered ? View.VISIBLE : View.GONE);
        this.imageViewToggleInput.setImageResource(this.cameraFacingFront ? R.drawable.call_input_front : R.drawable.call_input_back);
    }

    private void updateRemoteVideo()
    {
        this.layoutRemoteVideo.setVisibility(this.videoOffered ? View.VISIBLE : View.GONE);
        this.layoutNoVideo.setVisibility(this.videoOffered ? View.GONE : View.VISIBLE);
        this.textViewTimerVideo.setVisibility(this.videoOffered ? View.VISIBLE : View.INVISIBLE);
    }

    private void trackCall(boolean success)
    {
        if(this.outgoingCall && !this.outgoingCallTracked)
        {
            this.outgoingCallTracked = true;

            if(success)
            {
                TrackHelper.trackCallSuccess(this);
            }
            else
            {
                TrackHelper.trackCallFailure(this);
            }
        }
    }
}
