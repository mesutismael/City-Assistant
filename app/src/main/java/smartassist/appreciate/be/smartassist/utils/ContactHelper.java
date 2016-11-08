package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.model.EmergencyCategory;
import smartassist.appreciate.be.smartassist.model.EmergencyContact;

/**
 * Created by Inneke De Clippel on 15/04/2016.
 */
public class ContactHelper
{
    public static final int ID_PREFIX_FAMILY = 1;
    public static final int ID_PREFIX_ASSISTANT = 2;
    public static final int ID_PREFIX_RESIDENCE = 3;
    public static final int ID_PREFIX_CARESERVICE = 4;
    private static final String TYPE_FAMILY = "EmergencyNumber";
    private static final String TYPE_ASSISTANT = "User";
    private static final String TYPE_RESIDENCE = "Flat";
    private static final String TYPE_CARESERVICE = "CareService";

    public static int alterId(int id, String type)
    {
        if(type == null)
        {
            return id;
        }

        switch (type)
        {
            case TYPE_FAMILY:
                return ContactHelper.alterId(id, ID_PREFIX_FAMILY);

            case TYPE_ASSISTANT:
                return ContactHelper.alterId(id, ID_PREFIX_ASSISTANT);

            case TYPE_RESIDENCE:
                return ContactHelper.alterId(id, ID_PREFIX_RESIDENCE);

            case TYPE_CARESERVICE:
                return ContactHelper.alterId(id, ID_PREFIX_CARESERVICE);

            default:
                return id;
        }
    }

    public static int alterId(int id, int prefix)
    {
        String concatenatedId = String.valueOf(prefix) + String.valueOf(id);

        try
        {
            return Integer.parseInt(concatenatedId);
        }
        catch (NumberFormatException e)
        {
            return id;
        }
    }

    public static int getOwnContactId(Context context)
    {
        int flatId = PreferencesHelper.getFlatId(context);
        return ContactHelper.alterId(flatId, TYPE_RESIDENCE);
    }

    public static Pair<Integer, String> splitId(int id)
    {
        String concatenatedId = String.valueOf(id);
        String prefixAsString = concatenatedId.length() > 0 ? concatenatedId.substring(0, 1) : "";
        String originalIdAsString = concatenatedId.length() > 1 ? concatenatedId.substring(1, concatenatedId.length()) : "";

        try
        {
            int prefix = Integer.parseInt(prefixAsString);
            int originalId = Integer.parseInt(originalIdAsString);

            switch (prefix)
            {
                case ID_PREFIX_FAMILY:
                    return new Pair<>(originalId, TYPE_FAMILY);
                case ID_PREFIX_ASSISTANT:
                    return new Pair<>(originalId, TYPE_ASSISTANT);
                case ID_PREFIX_RESIDENCE:
                    return new Pair<>(originalId, TYPE_RESIDENCE);
                case ID_PREFIX_CARESERVICE:
                    return new Pair<>(originalId, TYPE_CARESERVICE);
                default:
                    return new Pair<>(0, null);
            }
        }
        catch (NumberFormatException e)
        {
            return new Pair<>(0, null);
        }
    }

    public static boolean canChat(int id, boolean qrCode)
    {
        String type = ContactHelper.splitId(id).second;

        if(type == null)
        {
            return false;
        }

        switch (type)
        {
            case TYPE_FAMILY:
                return qrCode;

            case TYPE_RESIDENCE:
            case TYPE_ASSISTANT:
                return true;

            default:
                return false;
        }
    }

    public static List<EmergencyContact> createListOfSelectedContacts(Integer[] selectedIndices, List<EmergencyContact> contacts, List<EmergencyCategory> contactCategories)
    {
        List<EmergencyContact> selectedContacts = new ArrayList<>();

        if(selectedIndices != null && contacts != null)
        {
            int categorySize = contactCategories != null ? contactCategories.size() : 0;
            int contactSize = contacts.size();

            for(int selectedIndex : selectedIndices)
            {
                if(selectedIndex < categorySize)
                {
                    EmergencyCategory category = contactCategories.get(selectedIndex);

                    for(EmergencyContact contact : contacts)
                    {
                        if(category.equals(contact.getCategory()) && !selectedContacts.contains(contact))
                        {
                            selectedContacts.add(contact);
                        }
                    }
                }
                else if(selectedIndex - categorySize < contactSize)
                {
                    EmergencyContact contact = contacts.get(selectedIndex - categorySize);

                    if(!selectedContacts.contains(contact))
                    {
                        selectedContacts.add(contact);
                    }
                }
            }
        }

        return selectedContacts;
    }
}
