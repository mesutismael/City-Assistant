package smartassist.appreciate.be.smartassist.model.api;

import java.util.List;

/**
 * Created by Inneke on 4/03/2015.
 */
public class RssData
{
    private List<RssItem> items;
    private List<RemovedDataItem> removed;

    public List<RssItem> getItems()
    {
        return items;
    }

    public List<RemovedDataItem> getRemoved()
    {
        return removed;
    }
}
