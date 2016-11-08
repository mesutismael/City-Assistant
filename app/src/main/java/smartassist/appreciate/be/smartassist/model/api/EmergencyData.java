package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import smartassist.appreciate.be.smartassist.utils.ContactHelper;

/**
 * Created by Inneke on 26/02/2015.
 */
public class EmergencyData
{
    private EmergencyNumbers family;
    private EmergencyNumbers assistants;
    private List<EmergencyNumber> residences;
    private EmergencyNumbers careservices;
    @SerializedName("categories")
    private List<EmergencyCategory> emergencyCategories;
    private PoiItem doctor;
    private PoiItem pharmacist;

    public List<EmergencyNumber> getEmergencyNumbers()
    {
        List<EmergencyNumber> numbers = new ArrayList<>();
        numbers.addAll(this.getNumbers(this.family, ContactHelper.ID_PREFIX_FAMILY));
        numbers.addAll(this.getNumbers(this.assistants, ContactHelper.ID_PREFIX_ASSISTANT));
        numbers.addAll(this.getNumbers(this.residences, ContactHelper.ID_PREFIX_RESIDENCE));
        numbers.addAll(this.getNumbers(this.careservices, ContactHelper.ID_PREFIX_CARESERVICE));
        return numbers;
    }

    public List<RemovedEmergencyItem> getRemoved()
    {
        List<RemovedEmergencyItem> removedItems = new ArrayList<>();
        removedItems.addAll(this.getRemovedItems(this.family, ContactHelper.ID_PREFIX_FAMILY));
        removedItems.addAll(this.getRemovedItems(this.assistants, ContactHelper.ID_PREFIX_ASSISTANT));
        removedItems.addAll(this.getRemovedItems(this.careservices, ContactHelper.ID_PREFIX_CARESERVICE));
        return removedItems;
    }

    public List<EmergencyCategory> getEmergencyCategories()
    {
        return emergencyCategories;
    }

    public PoiItem getDoctor()
    {
        return doctor;
    }

    public PoiItem getPharmacist()
    {
        return pharmacist;
    }

    private List<EmergencyNumber> getNumbers(EmergencyNumbers emergencyNumbers, int idPrefix)
    {
        return emergencyNumbers != null ? this.getNumbers(emergencyNumbers.getEmergencyNumbers(), idPrefix) : new ArrayList<EmergencyNumber>();
    }

    private List<EmergencyNumber> getNumbers(List<EmergencyNumber> emergencyNumbers, int idPrefix)
    {
        List<EmergencyNumber> numbers = new ArrayList<>();

        if(emergencyNumbers != null)
        {
            for(EmergencyNumber number : emergencyNumbers)
            {
                if(number != null)
                {
                    number.alterId(idPrefix);
                    numbers.add(number);
                }
            }
        }

        return numbers;
    }

    private List<RemovedEmergencyItem> getRemovedItems(EmergencyNumbers emergencyNumbers, int idPrefix)
    {
        List<RemovedEmergencyItem> removedItems = new ArrayList<>();

        if(emergencyNumbers != null && emergencyNumbers.getRemoved() != null)
        {
            for(RemovedEmergencyItem removedItem : emergencyNumbers.getRemoved())
            {
                if(removedItem != null)
                {
                    removedItem.alterId(idPrefix);
                    removedItems.add(removedItem);
                }
            }
        }

        return removedItems;
    }
}
