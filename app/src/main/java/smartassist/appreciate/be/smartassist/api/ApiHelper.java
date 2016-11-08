package smartassist.appreciate.be.smartassist.api;

import retrofit.RestAdapter;
import smartassist.appreciate.be.smartassist.BuildConfig;

/**
 * Created by Inneke on 28/01/2015.
 */
public class ApiHelper
{
    private static SmartAssistApi service;

    public static SmartAssistApi getService()
    {
        if(service == null)
        {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BuildConfig.API_BASE_URI)
                    .setLogLevel(RestAdapter.LogLevel.FULL) //Log the result
                    .build();

            service = restAdapter.create(SmartAssistApi.class);
        }

        return service;
    }
}
