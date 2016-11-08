package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.model.EmergencyContact;

/**
 * Created by Inneke De Clippel on 13/09/2016.
 */
public class CreateConversationRequest
{
    @SerializedName("contacts")
    private List<CreateConversationContact> contacts;

    public CreateConversationRequest(List<EmergencyContact> contacts)
    {
        this.contacts = new ArrayList<>();

        if(contacts != null)
        {
            for(EmergencyContact contact : contacts)
            {
                if(contact != null)
                {
                    this.contacts.add(new CreateConversationContact(contact));
                }
            }
        }
    }

    public List<CreateConversationContact> getContacts()
    {
        return contacts;
    }
}
