package smartassist.appreciate.be.smartassist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.video.VideoCallListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.utils.AudioPlayer;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 20/01/2016.
 */
public class IncomingCallActivity extends BaseActivity implements View.OnClickListener, VideoCallListener
{
    private TextView textViewTitle;
    private TextView textViewCaller;
    private ImageView imageViewCaller;
    private ImageView imageViewAccept;
    private String callId;
    private String callName;
    private String callPhoto;
    private Call call;
    private AudioPlayer audioPlayer;

    public static final String KEY_CALL_ID = "call_id";
    public static final String KEY_CALL_NAME = "call_name";
    public static final String KEY_CALL_PHOTO = "call_photo";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_incoming_call);

        this.textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);
        this.imageViewAccept = (ImageView) this.findViewById(R.id.imageView_accept);
        ImageView imageViewDecline = (ImageView) this.findViewById(R.id.imageView_decline);
        this.textViewCaller = (TextView) this.findViewById(R.id.textView_caller);
        this.imageViewCaller = (ImageView) this.findViewById(R.id.imageView_caller);

        layoutBack.setOnClickListener(this);
        this.imageViewAccept.setOnClickListener(this);
        imageViewDecline.setOnClickListener(this);

        this.callId = this.getIntent().getExtras().getString(KEY_CALL_ID);
        this.callName = this.getIntent().getStringExtra(KEY_CALL_NAME);
        this.callPhoto = this.getIntent().getStringExtra(KEY_CALL_PHOTO);

        this.audioPlayer = new AudioPlayer(this);
        this.audioPlayer.playRingtone();
    }

    @Override
    public void finish()
    {
        this.audioPlayer.stopRingtone();

        if(this.call != null)
        {
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

            case R.id.imageView_accept:
                if (this.call != null)
                {
                    this.call.answer();
                    this.call = null;

                    Intent callIntent = new Intent(v.getContext(), CallActivity.class);
                    callIntent.putExtra(CallActivity.KEY_CALL_ID, this.callId);
                    callIntent.putExtra(CallActivity.KEY_CALL_NAME, this.callName);
                    callIntent.putExtra(CallActivity.KEY_CALL_PHOTO, this.callPhoto);
                    this.startActivity(callIntent);
                }

                this.finish();
                break;

            case R.id.imageView_decline:
                this.finish();
                break;
        }
    }

    @Override
    protected void onServiceConnected()
    {
        this.call = this.getSinchServiceInterface().getCall(this.callId);

        if (this.call != null)
        {
            this.call.addCallListener(this);
            this.imageViewAccept.setImageResource(this.call.getDetails() != null && this.call.getDetails().isVideoOffered() ? R.drawable.call_accept_video : R.drawable.call_accept);

            String name = !TextUtils.isEmpty(this.callName) ? this.callName : this.call.getRemoteUserId();
            this.textViewCaller.setText(name);
            this.textViewTitle.setText(name);

            if(!TextUtils.isEmpty(this.callPhoto))
            {
                Picasso.with(this)
                        .load(this.callPhoto)
                        .into(this.imageViewCaller);
            }
        } else
        {
            this.finish();
        }
    }

    @Override
    public void onCallProgressing(Call call)
    {

    }

    @Override
    public void onCallEstablished(Call call)
    {

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

    }
}
