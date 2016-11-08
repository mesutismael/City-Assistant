package smartassist.appreciate.be.smartassist.model.api;

import android.support.v4.util.Pair;

import com.google.gson.annotations.SerializedName;

import smartassist.appreciate.be.smartassist.model.EmergencyContact;
import smartassist.appreciate.be.smartassist.utils.ContactHelper;

/**
 * Created by Inneke De Clippel on 13/09/2016.
 */
public class CreateConversationContact
{
    @SerializedName("contact_id")
    private int contactId;
    @SerializedName("contact_type")
    private String contactType;

    public CreateConversationContact(EmergencyContact contact)
    {
        Pair<Integer, String> splitId = ContactHelper.splitId(contact.getId());
        this.contactId = splitId.first != null ? splitId.first : 0;
        this.contactType = splitId.second;
    }
}
