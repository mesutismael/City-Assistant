package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

import smartassist.appreciate.be.smartassist.R;

/**
 * Created by Inneke De Clippel on 20/01/2016.
 */
public class AudioPlayer
{
    private Context context;
    private MediaPlayer mediaPlayer;
    private AudioTrack progressTone;

    private final static int SAMPLE_RATE = 16000;

    public AudioPlayer(Context context)
    {
        this.context = context.getApplicationContext();
    }

    public void playRingtone()
    {
        AudioManager audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_NORMAL);

        // Honour silent mode
        switch (audioManager.getRingerMode())
        {
            case AudioManager.RINGER_MODE_NORMAL:
                this.mediaPlayer = new MediaPlayer();
                this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);

                try
                {
                    this.mediaPlayer.setDataSource(this.context, Uri.parse("android.resource://" + this.context.getPackageName() + "/" + R.raw.phone_loud1));
                    this.mediaPlayer.prepare();
                    this.mediaPlayer.setLooping(true);
                    this.mediaPlayer.start();
                }
                catch (IOException e)
                {
                    Log.e("AudioPlayer", "Could not setup media player for ringtone");
                    this.mediaPlayer = null;
                }
                break;

            case AudioManager.RINGER_MODE_VIBRATE:
                Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{1000, 1000}, 0);
                break;
        }
    }

    public void stopRingtone()
    {
        Vibrator vibrator = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();

        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    public void playProgressTone()
    {
        this.stopProgressTone();
        try
        {
            this.progressTone = createProgressTone(this.context);
            this.progressTone.play();
        } catch (Exception e)
        {
            Log.e("AudioPlayer", "Could not play progress tone", e);
        }
    }

    public void stopProgressTone()
    {
        if (this.progressTone != null)
        {
            this.progressTone.stop();
            this.progressTone.release();
            this.progressTone = null;
        }
    }

    private static AudioTrack createProgressTone(Context context) throws IOException
    {
        AssetFileDescriptor fd = context.getResources().openRawResourceFd(R.raw.progress_tone);
        int length = (int) fd.getLength();

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, length, AudioTrack.MODE_STATIC);

        byte[] data = new byte[length];
        readFileToBytes(fd, data);

        audioTrack.write(data, 0, data.length);
        audioTrack.setLoopPoints(0, data.length / 2, 30);

        return audioTrack;
    }

    private static void readFileToBytes(AssetFileDescriptor fd, byte[] data) throws IOException
    {
        FileInputStream inputStream = fd.createInputStream();

        int bytesRead = 0;
        while (bytesRead < data.length)
        {
            int res = inputStream.read(data, bytesRead, (data.length - bytesRead));
            if (res == -1)
            {
                break;
            }
            bytesRead += res;
        }
    }
}
