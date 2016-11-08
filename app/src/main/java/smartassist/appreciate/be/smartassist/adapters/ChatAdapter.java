package smartassist.appreciate.be.smartassist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.ChatMessage;
import smartassist.appreciate.be.smartassist.utils.ChatHelper;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 15/04/2016.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<ChatMessage> messages;
    private OnChatClickListener listener;
    private int ownId;

    private static final int TYPE_OTHER = 0;
    private static final int TYPE_SELF = 1;

    public ChatAdapter(Context context)
    {
        this.ownId = ContactHelper.getOwnContactId(context);
    }

    public void setMessages(List<ChatMessage> messages)
    {
        this.messages = messages;
        this.notifyDataSetChanged();
    }

    public void setListener(OnChatClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position)
    {
        return this.messages.get(position).getContactId() == this.ownId ? TYPE_SELF : TYPE_OTHER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        switch (i)
        {
            case TYPE_OTHER:
                View viewOther = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_chat_other, viewGroup, false);
                return new ChatOtherViewHolder(viewOther);

            case TYPE_SELF:
                View viewSelf = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_chat_self, viewGroup, false);
                return new ChatSelfViewHolder(viewSelf);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        if(viewHolder instanceof ChatOtherViewHolder)
        {
            ChatMessage message = this.messages.get(i);

            ((ChatOtherViewHolder) viewHolder).bind(message);
        }
        else if(viewHolder instanceof ChatSelfViewHolder)
        {
            ChatMessage message = this.messages.get(i);

            ((ChatSelfViewHolder) viewHolder).bind(message);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.messages != null ? this.messages.size() : 0;
    }

    public class ChatOtherViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageViewOther;
        private TextView textViewName;
        private TextView textViewMessage;
        private TextView textViewDate;

        public ChatOtherViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewOther = (ImageView) itemView.findViewById(R.id.imageView_other);
            this.textViewName = (TextView)itemView.findViewById(R.id.textView_name);
            this.textViewMessage = (TextView)itemView.findViewById(R.id.textView_message);
            this.textViewDate = (TextView)itemView.findViewById(R.id.textView_date);
        }

        public void bind(ChatMessage message)
        {
            this.textViewName.setText(message.getContactName());
            this.textViewMessage.setText(message.getMessage());
            this.textViewDate.setText(DateUtils.formatChatDate(message.getSentAt()));

            if(!TextUtils.isEmpty(message.getContactPhoto()))
            {
                Picasso.with(this.imageViewOther.getContext())
                        .load(message.getContactPhoto())
                        .into(this.imageViewOther);
            }
            else
            {
                this.imageViewOther.setImageResource(R.drawable.placeholder_chat_single);
            }
        }
    }

    public class ChatSelfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewMessage;
        private TextView textViewDate;
        private TextView textViewState;
        private ChatMessage message;

        public ChatSelfViewHolder(View itemView)
        {
            super(itemView);

            this.textViewMessage = (TextView)itemView.findViewById(R.id.textView_message);
            this.textViewDate = (TextView)itemView.findViewById(R.id.textView_date);
            this.textViewState = (TextView)itemView.findViewById(R.id.textView_state);

            itemView.setOnClickListener(this);
        }

        public void bind(ChatMessage message)
        {
            this.message = message;

            this.textViewMessage.setText(message.getMessage());
            this.textViewDate.setText(DateUtils.formatChatDate(message.getSentAt()));
            int stateRes = ChatHelper.getMessageForState(message.getState());
            this.textViewState.setText(stateRes > 0 ? this.textViewState.getContext().getString(stateRes) : null);
            this.textViewState.setVisibility(ChatHelper.shouldShowMessage(message.getState()) ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v)
        {
            if(ChatAdapter.this.listener != null && this.message.getState() == ChatHelper.STATE_ERROR)
                ChatAdapter.this.listener.onChatClick(v, this.message);
        }
    }

    public interface OnChatClickListener
    {
        void onChatClick(View caller, ChatMessage message);
    }
}
