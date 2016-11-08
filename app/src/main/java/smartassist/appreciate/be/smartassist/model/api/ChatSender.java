package smartassist.appreciate.be.smartassist.model.api;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.database.ChatSenderTable;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;
import smartassist.appreciate.be.smartassist.utils.DateUtils;

/**
 * Created by Inneke De Clippel on 13/09/2016.
 */
public class ChatSender
{
    @SerializedName("id")
    private int contactId;
    @SerializedName("type")
    private String contactType;
    @SerializedName("name")
    private String name;
    @SerializedName("photo")
    private String photo;
    @SerializedName("joined_at")
    private String joinDate;
    @SerializedName("left_at")
    private String leaveDate;

    public int getContactId()
    {
        return ContactHelper.alterId(this.contactId, this.contactType);
    }

    public long getJoinDate()
    {
        return DateUtils.parseApiDateSeconds(this.joinDate);
    }

    public long getLeaveDate()
    {
        return DateUtils.parseApiDateSeconds(this.leaveDate);
    }

    public ContentValues getContentValues(int conversationId)
    {
        final ContentValues cv = new ContentValues();

        cv.put(ChatSenderTable.COLUMN_CONVERSATION_ID, conversationId);
        cv.put(ChatSenderTable.COLUMN_CONTACT_ID, this.getContactId());
        cv.put(ChatSenderTable.COLUMN_CONTACT_NAME, this.name);
        cv.put(ChatSenderTable.COLUMN_CONTACT_PHOTO, this.photo);
        cv.put(ChatSenderTable.COLUMN_JOIN_DATE, this.getJoinDate());
        cv.put(ChatSenderTable.COLUMN_LEAVE_DATE, this.getLeaveDate());

        return cv;
    }
}
