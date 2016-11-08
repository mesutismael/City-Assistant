package smartassist.appreciate.be.smartassist.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.fragments.ChatFragment;
import smartassist.appreciate.be.smartassist.utils.NotificationHelper;
import smartassist.appreciate.be.smartassist.utils.TrackHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 13/04/2016.
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener
{
    private TextView textViewTitle;
    private int contactId;

    public static final String KEY_CONVERSATION_ID = ChatFragment.KEY_CONVERSATION_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_chat);

        this.textViewTitle = (TextView) this.findViewById(R.id.textView_topBarTitle);
        LinearLayout layoutBack = (LinearLayout) this.findViewById(R.id.layout_back);

        layoutBack.setOnClickListener(this);

        this.contactId = this.getIntent().getIntExtra(KEY_CONVERSATION_ID, 0);

        this.textViewTitle.setText(R.string.chat_title);

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(this.getIntent().getExtras());

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.layout_back:
                this.startParentActivity();
                break;
        }
    }

    @Override
    public String getTrackingScreenName()
    {
        return TrackHelper.SCREEN_CHAT_DETAIL;
    }

    @Override
    public boolean showNotification(int itemId, int itemType, int extraId)
    {
        return itemType != NotificationHelper.TYPE_CHAT || extraId != this.contactId;
    }

    public void setTopBarTitle(String title)
    {
        this.textViewTitle.setText(title);
    }
}
