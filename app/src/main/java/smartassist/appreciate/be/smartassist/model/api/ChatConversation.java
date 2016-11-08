package smartassist.appreciate.be.smartassist.model.api;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.utils.ContactHelper;

/**
 * Created by Inneke De Clippel on 15/09/2016.
 */
public class ChatConversation
{
    @SerializedName("id")
    private int conversationId;
    @SerializedName("contacts")
    private List<ChatSender> contacts;
    @SerializedName("messages")
    private List<ChatMessage> messages;
    @SerializedName("deleted_at")
    private String deletedAt;

    public int getConversationId()
    {
        return conversationId;
    }

    public List<ChatSender> getContacts()
    {
        return contacts;
    }

    public List<ChatMessage> getMessages()
    {
        return messages;
    }

    public boolean isDeleted()
    {
        return !TextUtils.isEmpty(this.deletedAt);
    }

    public ChatSender getOwnContact(Context context)
    {
        int ownContactId = ContactHelper.getOwnContactId(context);

        if(this.contacts != null)
        {
            for(ChatSender contact : this.contacts)
            {
                if(contact.getContactId() == ownContactId)
                {
                    return contact;
                }
            }
        }

        return null;
    }

    public List<ChatMessage> getUnreadMessages(int ownContactId)
    {
        List<ChatMessage> unread = new ArrayList<>();

        if(this.messages != null && !this.isDeleted())
        {
            for(ChatMessage message : this.messages)
            {
                if (message != null && !message.isRead() && message.getContactId() != ownContactId)
                {
                    message.setConversationId(this.conversationId);
                    unread.add(message);
                }
            }
        }

        return unread;
    }
}
