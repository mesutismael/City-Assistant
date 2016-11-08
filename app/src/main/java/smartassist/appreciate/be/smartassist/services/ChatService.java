package smartassist.appreciate.be.smartassist.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.ChatContactsActivity;
import smartassist.appreciate.be.smartassist.api.ApiHelper;
import smartassist.appreciate.be.smartassist.asynctasks.InsertMessagesAsyncTask;
import smartassist.appreciate.be.smartassist.contentproviders.NotificationContentProvider;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.ChatConversation;
import smartassist.appreciate.be.smartassist.model.api.ChatMessage;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.model.api.GetChatResponse;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.utils.LifeCycleHandler;
import smartassist.appreciate.be.smartassist.utils.NotificationHelper;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;

/**
 * Created by Inneke De Clippel on 15/04/2016.
 */
public class ChatService extends Service
{
    private long lastCall;
    private boolean destroyed;

    private static final long CALL_INTERVAL_VISIBLE = 5 * 1000; //5 seconds
    private static final long CALL_INTERVAL_INVISIBLE = 60 * 1000; //1 minute

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.destroyed = false;
        this.startNextCall();
    }

    @Override
    public void onDestroy()
    {
        this.destroyed = true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    private void startNextCall()
    {
        if(!this.destroyed)
        {
            long oldTimestamp = PreferencesHelper.getLastChatSync(this);
            long newTimestamp = System.currentTimeMillis();
            this.getChatMessages(oldTimestamp, newTimestamp, true);
        }
    }

    private void startAccessTokenApiCall(final long oldTimestamp, final long newTimestamp)
    {
        int flatId = PreferencesHelper.getFlatId(this);
        String hash = PreferencesHelper.getHash(this);
        String appSecret = Constants.APP_SECRET;

        ApiHelper.getService().getAccessToken(new FlatId(flatId, hash), appSecret, new Callback<ApiAccessToken>()
        {
            @Override
            public void success(ApiAccessToken apiAccessToken, Response response)
            {
                if (apiAccessToken != null && apiAccessToken.getAccessToken() != null)
                {
                    PreferencesHelper.saveAccessToken(ChatService.this, apiAccessToken.getAccessToken());
                    ChatService.this.getChatMessages(oldTimestamp, newTimestamp, false);
                }
                else
                {
                    ChatService.this.planNextCall();
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                ChatService.this.planNextCall();
            }
        });
    }

    private void getChatMessages(final long oldTimestamp, final long newTimestamp, final boolean retry)
    {
        final int ownContactId = ContactHelper.getOwnContactId(this);
        String accessToken = PreferencesHelper.getAccessToken(this);
        int flatId = PreferencesHelper.getFlatId(this);
        String appSecret = Constants.APP_SECRET;
        String oldDate = DateUtils.formatApiDateSeconds(oldTimestamp);
        String newDate = DateUtils.formatApiDateSeconds(newTimestamp);

        this.lastCall = newTimestamp;

        ApiHelper.getService().getChat(appSecret, String.valueOf(flatId), accessToken, oldDate, newDate, new Callback<GetChatResponse>()
        {
            @Override
            public void success(GetChatResponse getChatResponse, Response response)
            {
                if (getChatResponse != null && getChatResponse.getError() == null)
                {
                    PreferencesHelper.saveLastChatSync(ChatService.this, newTimestamp);

                    List<ChatMessage> unreadMessages = new ArrayList<>();
                    if(getChatResponse.getConversations() != null)
                    {
                        for (ChatConversation conversation : getChatResponse.getConversations())
                        {
                            if (conversation != null)
                            {
                                unreadMessages.addAll(conversation.getUnreadMessages(ownContactId));
                            }
                        }
                    }

                    ChatService.this.showNotification(unreadMessages);
                    ChatService.this.planNextCall();

                    InsertMessagesAsyncTask task = new InsertMessagesAsyncTask(ChatService.this, getChatResponse.getConversations());
                    task.execute();
                }
                else if (retry)
                {
                    ChatService.this.startAccessTokenApiCall(oldTimestamp, newTimestamp);
                }
                else
                {
                    ChatService.this.planNextCall();
                }
            }

            @Override
            public void failure(RetrofitError error)
            {
                ChatService.this.planNextCall();
            }
        });
    }

    private void planNextCall()
    {
        this.handler.removeCallbacks(this.callback);
        this.handler.postDelayed(this.callback, CALL_INTERVAL_VISIBLE);
    }

    private void showNotification(List<ChatMessage> messages)
    {
        if(messages.size() > 0)
        {
            if(LifeCycleHandler.appVisible)
            {
                for(ChatMessage message : messages)
                {
                    this.getContentResolver().insert(NotificationContentProvider.CONTENT_URI, NotificationHelper.getContentValues(this, message));
                }
            }
            else
            {
                Intent resultIntent = new Intent(this, ChatContactsActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle(this.getString(R.string.chat_notification_title))
                        .setContentText(this.getString(R.string.chat_notification_message))
                        .setSmallIcon(R.drawable.ic_notification)
                        .setColor(ContextCompat.getColor(this, R.color.general_notification))
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setOnlyAlertOnce(true)
                        .setVibrate(new long[]{0, 400, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);

                NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            }
        }
    }

    private Handler handler = new Handler();
    private Runnable callback = new Runnable()
    {
        @Override
        public void run()
        {
            if(LifeCycleHandler.appVisible)
            {
                ChatService.this.startNextCall();
            }
            else
            {
                long elapsedTime = System.currentTimeMillis() - ChatService.this.lastCall;

                if(elapsedTime >= CALL_INTERVAL_INVISIBLE)
                {
                    ChatService.this.startNextCall();
                }
                else
                {
                    ChatService.this.planNextCall();
                }
            }
        }
    };
}
