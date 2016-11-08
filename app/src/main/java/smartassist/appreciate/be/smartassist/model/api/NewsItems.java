package smartassist.appreciate.be.smartassist.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Inneke on 4/03/2015.
 */
public class NewsItems
{
    @SerializedName("items")
    private List<NewsItem> newsItems;
    private List<RemovedDataItem> removed;

    public List<NewsItem> getNewsItems()
    {
        return newsItems;
    }

    public List<RemovedDataItem> getRemoved()
    {
        return removed;
    }
}
