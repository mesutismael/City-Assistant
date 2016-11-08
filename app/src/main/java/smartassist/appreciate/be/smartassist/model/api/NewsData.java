package smartassist.appreciate.be.smartassist.model.api;

import java.util.List;

/**
 * Created by Inneke on 23/02/2015.
 */
public class NewsData
{
    private NewsItems items;
    private List<NewsCategory> categories;

    public List<NewsItem> getItems()
    {
        if(this.items == null)
            return null;
        return this.items.getNewsItems();
    }

    public List<RemovedDataItem> getRemoved()
    {
        if(this.items == null)
            return null;
        return this.items.getRemoved();
    }

    public List<NewsCategory> getCategories()
    {
        return categories;
    }
}
