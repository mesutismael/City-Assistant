package smartassist.appreciate.be.smartassist.adapters;

import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.model.ChatConversation;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;
import smartassist.appreciate.be.smartassist.utils.DateUtils;
import smartassist.appreciate.be.smartassist.utils.TypefaceHelper;
import smartassist.appreciate.be.smartassist.views.TextView;

/**
 * Created by Inneke De Clippel on 18/04/2016.
 */
public class ChatContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<ChatConversation> conversations;
    private OnContactClickListener listener;

    private static final int TYPE_CONVERSATION = 0;

    public ChatContactAdapter()
    {
    }

    public void setConversations(List<ChatConversation> conversations)
    {
        this.conversations = conversations;
        this.notifyDataSetChanged();
    }

    public void setListener(OnContactClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position)
    {
        return TYPE_CONVERSATION;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i)
    {
        switch (i)
        {
            case TYPE_CONVERSATION:
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_chat_contact, viewGroup, false);
                return new ContactViewHolder(v);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i)
    {
        if(viewHolder instanceof ContactViewHolder)
        {
            ChatConversation conversation = this.conversations.get(i);

            ((ContactViewHolder) viewHolder).bind(conversation, i);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.conversations != null ? this.conversations.size() : 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView imageViewLarge;
        private ImageView imageViewSmall;
        private TextView textViewTitle;
        private TextView textViewMessage;
        private TextView textViewDate;
        private Button buttonMore;
        private ChatConversation conversation;

        public ContactViewHolder(View itemView)
        {
            super(itemView);

            this.imageViewLarge = (ImageView) itemView.findViewById(R.id.imageView_large);
            this.imageViewSmall = (ImageView) itemView.findViewById(R.id.imageView_small);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.textView_name);
            this.textViewMessage = (TextView) itemView.findViewById(R.id.textView_message);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textView_date);
            this.buttonMore = (Button) itemView.findViewById(R.id.button_more);

            this.buttonMore.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(ChatConversation conversation, int position)
        {
            this.conversation = conversation;

            boolean landscape = this.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            boolean large = (landscape && (position % 10 == 4 || position % 10 == 8)) || (!landscape && position % 3 == 2);

            ImageView imageViewPhoto = large ? this.imageViewLarge : this.imageViewSmall;
            this.imageViewLarge.setVisibility(large ? View.VISIBLE : View.GONE);
            this.imageViewSmall.setVisibility(large ? View.GONE : View.VISIBLE);

            int ownId = ContactHelper.getOwnContactId(this.itemView.getContext());

            if(conversation.isGroupChat())
            {
                imageViewPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageViewPhoto.setImageResource(R.drawable.placeholder_chat_group);
            }
            else
            {
                imageViewPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String photo = conversation.getSenderPhoto(ownId);

                if(!TextUtils.isEmpty(photo))
                {
                    Picasso.with(imageViewPhoto.getContext())
                            .load(photo)
                            .into(imageViewPhoto);
                }
                else
                {
                    imageViewPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageViewPhoto.setImageResource(R.drawable.placeholder_chat_single);
                }
            }

            String names = conversation.getParticipantNames(ownId);

            this.textViewTitle.setText(TextUtils.isEmpty(names) ? this.itemView.getContext().getString(R.string.chat_conversation_no_participants) : names);
            this.textViewMessage.setText(conversation.getLastMessage());
            this.textViewMessage.setTypeface(this.textViewMessage.getContext(), conversation.isLastMessageRead() ? TypefaceHelper.MONTSERRAT_LIGHT : TypefaceHelper.MONTSERRAT_BOLD);
            this.textViewDate.setText(DateUtils.formatChatDate(conversation.getLastMessageSentAt()));
        }

        @Override
        public void onClick(View v)
        {
            if(ChatContactAdapter.this.listener != null)
            {
                ChatContactAdapter.this.listener.onContactClick(v, this.conversation);
            }
        }
    }

    public interface OnContactClickListener
    {
        void onContactClick(View caller, ChatConversation conversation);
    }
}
