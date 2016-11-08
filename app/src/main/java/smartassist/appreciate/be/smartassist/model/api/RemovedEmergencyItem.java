package smartassist.appreciate.be.smartassist.model.api;

import smartassist.appreciate.be.smartassist.utils.ContactHelper;

/**
 * Created by Inneke De Clippel on 15/02/2016.
 */
public class RemovedEmergencyItem extends RemovedDataItem
{
    public void alterId(int idPrefix)
    {
        super.setId(ContactHelper.alterId(super.getId(), idPrefix));
    }
}
