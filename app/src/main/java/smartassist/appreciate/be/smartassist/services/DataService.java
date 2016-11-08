package smartassist.appreciate.be.smartassist.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.RetrofitError;
import smartassist.appreciate.be.smartassist.api.ApiHelper;
import smartassist.appreciate.be.smartassist.contentproviders.ActivitiesContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ActivityTypesContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarBirthdayContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.CalendarEventContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.CareBookContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatConversationContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatMessageContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ChatSenderContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.EmergencyCategoryContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.EmergencyContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.HabitantContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.InvoiceContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.MedicationContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ModuleContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.NewsCategoryContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.NewsContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.NotificationContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.OpeningTimesContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.PhotoContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.PoiCategoryContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.PoiContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.ResidentNewsContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.RssContentProvider;
import smartassist.appreciate.be.smartassist.contentproviders.WeatherContentProvider;
import smartassist.appreciate.be.smartassist.database.ActivityTable;
import smartassist.appreciate.be.smartassist.database.ActivityTypesTable;
import smartassist.appreciate.be.smartassist.database.CalendarBirthdayTable;
import smartassist.appreciate.be.smartassist.database.CalendarEventTable;
import smartassist.appreciate.be.smartassist.database.CareBookTable;
import smartassist.appreciate.be.smartassist.database.EmergencyCategoryTable;
import smartassist.appreciate.be.smartassist.database.EmergencyTable;
import smartassist.appreciate.be.smartassist.database.HabitantTable;
import smartassist.appreciate.be.smartassist.database.InvoiceTable;
import smartassist.appreciate.be.smartassist.database.MedicationTable;
import smartassist.appreciate.be.smartassist.database.ModuleTable;
import smartassist.appreciate.be.smartassist.database.NewsCategoryTable;
import smartassist.appreciate.be.smartassist.database.NewsTable;
import smartassist.appreciate.be.smartassist.database.NotificationTable;
import smartassist.appreciate.be.smartassist.database.OpeningTimesTable;
import smartassist.appreciate.be.smartassist.database.PhotoTable;
import smartassist.appreciate.be.smartassist.database.PoiCategoryTable;
import smartassist.appreciate.be.smartassist.database.PoiTable;
import smartassist.appreciate.be.smartassist.database.ResidentNewsTable;
import smartassist.appreciate.be.smartassist.database.RssTable;
import smartassist.appreciate.be.smartassist.database.WeatherTable;
import smartassist.appreciate.be.smartassist.model.api.ActivityItem;
import smartassist.appreciate.be.smartassist.model.api.ActivitytypeItem;
import smartassist.appreciate.be.smartassist.model.api.ApiAccessToken;
import smartassist.appreciate.be.smartassist.model.api.ApiConfig;
import smartassist.appreciate.be.smartassist.model.api.ApiModule;
import smartassist.appreciate.be.smartassist.model.api.CalendarBirthday;
import smartassist.appreciate.be.smartassist.model.api.CalendarEvent;
import smartassist.appreciate.be.smartassist.model.api.EmergencyCategory;
import smartassist.appreciate.be.smartassist.model.api.EmergencyNumber;
import smartassist.appreciate.be.smartassist.model.api.Flat;
import smartassist.appreciate.be.smartassist.model.api.FlatId;
import smartassist.appreciate.be.smartassist.model.api.Habitant;
import smartassist.appreciate.be.smartassist.model.api.HabitantInfo;
import smartassist.appreciate.be.smartassist.model.api.HealthCareBookItem;
import smartassist.appreciate.be.smartassist.model.api.Image;
import smartassist.appreciate.be.smartassist.model.api.Invoice;
import smartassist.appreciate.be.smartassist.model.api.MedicationItem;
import smartassist.appreciate.be.smartassist.model.api.ModuleData;
import smartassist.appreciate.be.smartassist.model.api.NewsCategory;
import smartassist.appreciate.be.smartassist.model.api.NewsItem;
import smartassist.appreciate.be.smartassist.model.api.OpeningTime;
import smartassist.appreciate.be.smartassist.model.api.PhotoItem;
import smartassist.appreciate.be.smartassist.model.api.PoiCategory;
import smartassist.appreciate.be.smartassist.model.api.PoiItem;
import smartassist.appreciate.be.smartassist.model.api.RemovedDataItem;
import smartassist.appreciate.be.smartassist.model.api.RemovedEmergencyItem;
import smartassist.appreciate.be.smartassist.model.api.Residence;
import smartassist.appreciate.be.smartassist.model.api.ResidentNews;
import smartassist.appreciate.be.smartassist.model.api.RssItem;
import smartassist.appreciate.be.smartassist.model.api.WeatherItem;
import smartassist.appreciate.be.smartassist.utils.Constants;
import smartassist.appreciate.be.smartassist.utils.NotificationHelper;
import smartassist.appreciate.be.smartassist.utils.PreferencesHelper;
/**
 * Created by Inneke on 23/03/2015.
 */
public class DataService extends IntentService
{
    public static final String BROADCAST_ACTION_REFRESH = "data_refresh";
    public static final String BROADCAST_ACTION_DELETE = "data_delete";
    public static final String KEY_SUCCESS = "api_data_success";
    public static final String EXTRA_SEND_RESULT = "send_result";
    public static final String EXTRA_ACTION = "data_action";
    public static final int ACTION_REFRESH = 1;
    public static final int ACTION_DELETE = 2;
    private static final int METHOD_DO_NOTHING = 0;
    private static final int METHOD_INSERT = 1;
    private static final int METHOD_UPDATE = 2;
    private static final int METHOD_DELETE = 3;
    private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

    static
    {
        SDF_TIME.setTimeZone(Constants.TIME_ZONE_BACKEND);
    }

    private boolean sendResult;
    private boolean firstTry;
    public DataService()
    {
        super("DataService");
    }
    public DataService(String name)
    {
        super(name);
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        this.sendResult = intent.getBooleanExtra(EXTRA_SEND_RESULT, false);
        int action = intent.getIntExtra(EXTRA_ACTION, ACTION_REFRESH);
        if(action == ACTION_REFRESH)
        {
            this.firstTry = true;
            long oldTimestamp = PreferencesHelper.getTimestamp(this);
            long newTimestamp = System.currentTimeMillis();
            this.removeIncompleteData(oldTimestamp);
            this.startConfigApiCall(oldTimestamp, newTimestamp);
        }
        else if(action == ACTION_DELETE)
        {
            this.clearDatabase();
        }
    }
    private void sendResult(boolean result, String broadcastAction)
    {
        Intent intent = new Intent(broadcastAction);
        intent.putExtra(KEY_SUCCESS, result);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private void clearDatabase()
    {
        ContentResolver contentResolver = this.getContentResolver();
        contentResolver.delete(ModuleContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(PoiCategoryContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(PoiContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(OpeningTimesContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(CalendarEventContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(CalendarBirthdayContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(NewsContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(NewsCategoryContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(WeatherContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(RssContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(EmergencyContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(EmergencyCategoryContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(PhotoContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(NotificationContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(CareBookContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(MedicationContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(InvoiceContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(HabitantContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(ChatMessageContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(ChatConversationContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(ChatSenderContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(ResidentNewsContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(ActivitiesContentProvider.CONTENT_URI, null, null);
        contentResolver.delete(ActivityTypesContentProvider.CONTENT_URI, null, null);
        PreferencesHelper.clearConfiguration(this);
        if (DataService.this.sendResult)
            DataService.this.sendResult(true, BROADCAST_ACTION_DELETE);
    }
    private void removeIncompleteData(long oldTimestamp)
    {
        ContentResolver contentResolver = this.getContentResolver();
        String moduleWhere = ModuleTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String poiCategoryWhere = PoiCategoryTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String poiWhere = PoiTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String openingTimesWhere = OpeningTimesTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String calendarEventWhere = CalendarEventTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String calendarBirthdayWhere = CalendarBirthdayTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String newsWhere = NewsTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String newsCategoryWhere = NewsCategoryTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String weatherWhere = WeatherTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String rssWhere = RssTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String emergencyWhere = EmergencyTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String emergencyCategoryWhere = EmergencyCategoryTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String photoWhere = PhotoTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String notificationWhere = NotificationTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String careBookWhere = CareBookTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String medicationWhere = MedicationTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String invoiceWhere = InvoiceTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String habitantsWhere = HabitantTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String residentsinfoWhere = ResidentNewsTable.COLUMN_LAST_UPDATE + ">" + oldTimestamp;
        String activitiesWhere = ActivityTable.COLUMN_UPDATED_AT + ">" + oldTimestamp;
        String activityTypesWhere = ActivityTypesTable.COLUMN_UPDATE_DATE + ">" + oldTimestamp;
        int modules = contentResolver.delete(ModuleContentProvider.CONTENT_URI, moduleWhere, null);
        int poiCat = contentResolver.delete(PoiCategoryContentProvider.CONTENT_URI, poiCategoryWhere, null);
        int poi = contentResolver.delete(PoiContentProvider.CONTENT_URI, poiWhere, null);
        int opening = contentResolver.delete(OpeningTimesContentProvider.CONTENT_URI, openingTimesWhere, null);
        int event = contentResolver.delete(CalendarEventContentProvider.CONTENT_URI, calendarEventWhere, null);
        int birthday = contentResolver.delete(CalendarBirthdayContentProvider.CONTENT_URI, calendarBirthdayWhere, null);
        int news = contentResolver.delete(NewsContentProvider.CONTENT_URI, newsWhere, null);
        int newsCats = contentResolver.delete(NewsCategoryContentProvider.CONTENT_URI, newsCategoryWhere, null);
        int weather = contentResolver.delete(WeatherContentProvider.CONTENT_URI, weatherWhere, null);
        int rss = contentResolver.delete(RssContentProvider.CONTENT_URI, rssWhere, null);
        int emergency = contentResolver.delete(EmergencyContentProvider.CONTENT_URI, emergencyWhere, null);
        int emergencyCategory = contentResolver.delete(EmergencyCategoryContentProvider.CONTENT_URI, emergencyCategoryWhere, null);
        int photo = contentResolver.delete(PhotoContentProvider.CONTENT_URI, photoWhere, null);
        int notification = contentResolver.delete(NotificationContentProvider.CONTENT_URI, notificationWhere, null);
        int carebook = contentResolver.delete(CareBookContentProvider.CONTENT_URI, careBookWhere, null);
        int medication = contentResolver.delete(MedicationContentProvider.CONTENT_URI, medicationWhere, null);
        int invoices = contentResolver.delete(InvoiceContentProvider.CONTENT_URI, invoiceWhere, null);
        int habitants = contentResolver.delete(HabitantContentProvider.CONTENT_URI, habitantsWhere, null);
        int residents = contentResolver.delete(ResidentNewsContentProvider.CONTENT_URI, residentsinfoWhere, null);
        int activites = contentResolver.delete(ActivitiesContentProvider.CONTENT_URI, activitiesWhere, null);
        int activityTypes = contentResolver.delete(ActivityTypesContentProvider.CONTENT_URI, activityTypesWhere, null);
        Log.d("DataService", "Deleted modules:"+modules);
        Log.d("DataService", "Deleted poi categories:"+poiCat);
        Log.d("DataService", "Deleted poi:"+poi);
        Log.d("DataService", "Deleted opening times:"+opening);
        Log.d("DataService", "Deleted events:"+event);
        Log.d("DataService", "Deleted birthdays:"+birthday);
        Log.d("DataService", "Deleted news:"+news);
        Log.d("DataService", "Deleted news categories:"+newsCats);
        Log.d("DataService", "Deleted weather:"+weather);
        Log.d("DataService", "Deleted rss:"+rss);
        Log.d("DataService", "Deleted emergency:"+emergency);
        Log.d("DataService", "Deleted emergency categories:"+emergencyCategory);
        Log.d("DataService", "Deleted photos:"+photo);
        Log.d("DataService", "Deleted notifications:"+notification);
        Log.d("DataService", "Deleted carebook items:"+carebook);
        Log.d("DataService", "Deleted medication items:"+medication);
        Log.d("DataService", "Deleted invoices:"+invoices);
        Log.d("DataService", "Deleted habitants:"+habitants);
        Log.d("DataService", "Deleted residents:"+residents);
        Log.d("DataService", "Deleted activities:"+activites);
        Log.d("DataService", "Deleted activityTypes:"+activityTypes);
    }
    private void startAccessTokenApiCall(long oldTimestamp, long newTimestamp)
    {
        this.firstTry = false;
        int flatId = PreferencesHelper.getFlatId(this);
        String hash = PreferencesHelper.getHash(this);
        String appSecret = Constants.APP_SECRET;
        try
        {
            ApiAccessToken apiAccessToken = ApiHelper.getService().getAccessToken(new FlatId(flatId, hash), appSecret);
            if (apiAccessToken != null && apiAccessToken.getAccessToken() != null)
            {
                PreferencesHelper.saveAccessToken(this, apiAccessToken.getAccessToken());
                this.startConfigApiCall(oldTimestamp, newTimestamp);
            } else if (DataService.this.sendResult)
            {
                this.sendResult(false, BROADCAST_ACTION_REFRESH);
            }
        }
        catch (RetrofitError e)
        {
            if (this.sendResult)
            {
                this.sendResult(false, BROADCAST_ACTION_REFRESH);
            }
        }
    }
    private void startConfigApiCall(long oldTimestamp, long newTimestamp)
    {
        String accessToken = PreferencesHelper.getAccessToken(this);
        int flatId = PreferencesHelper.getFlatId(this);
        String appSecret = Constants.APP_SECRET;
        String oldDate = this.getDate(oldTimestamp);
        String newDate = this.getDate(newTimestamp);

        try
        {
            ApiConfig apiConfig = ApiHelper.getService().
                    getFlatConfiguration(appSecret, String.valueOf(flatId), accessToken, oldDate, newDate, flatId);
            if (apiConfig != null && apiConfig.getFlat() != null)
            {
                this.saveAllDataInDatabase(oldTimestamp, newTimestamp, apiConfig.getFlat());
                PreferencesHelper.saveTimestamp(this, newTimestamp);
                if (this.sendResult)
                    this.sendResult(true, BROADCAST_ACTION_REFRESH);
            } else if (this.firstTry)
            {
                this.startAccessTokenApiCall(oldTimestamp, newTimestamp);
            } else if (this.sendResult)
            {
                this.sendResult(false, BROADCAST_ACTION_REFRESH);
            }
        }
        catch (RetrofitError e)
        {

            if (this.sendResult)
            {
                this.sendResult(false, BROADCAST_ACTION_REFRESH);
            }
            e.printStackTrace();
        }
    }
    private void saveAllDataInDatabase(long oldTimestamp, long newTimestamp, Flat flat)
    {
        Residence residence = flat.getResidence();
        ModuleData flatModules = flat.getModuleData();
        ModuleData residenceModules = residence != null ? residence.getModuleData() : null;
        if (residence != null)
        {
            PreferencesHelper.saveResidenceName(this, residence.getName());
            PreferencesHelper.saveFlatNumber(this, flat.getNumber());
            PreferencesHelper.saveCity(this, residence.getLocality());
            PreferencesHelper.saveLatitude(this, residence.getLatitude());
            PreferencesHelper.saveLongitude(this, residence.getLongitude());
            PreferencesHelper.saveResidentInfoPhoto(this,flat.getPhoto());
            PreferencesHelper.saveResidentNewsPhoto(this,residence.getPhoto());

            //Module layout
            this.saveLayoutInDatabase(newTimestamp, residence.getLayout());
        }
        PreferencesHelper.saveFlatName(this, flat.getName());
        PreferencesHelper.saveFlatPhoto(this, flat.getPhoto());
        //POI
        if (residenceModules != null && residenceModules.getNearByData().getPoiData() != null)
            this.savePoiInDatabase(newTimestamp, oldTimestamp, residenceModules.getNearByData().getPoiData().getPoiItems());
        if (residenceModules != null && residenceModules.getNearByData().getPoiData() != null)
            this.removePoiFromDatabase(residenceModules.getNearByData().getPoiData().getRemoved());
        if (flatModules != null && residenceModules.getNearByData().getPoiData() != null)
            this.removePoiFromDatabase(residenceModules.getNearByData().getPoiData().getRemoved());
        //POI categories
        if (residenceModules != null && residenceModules.getNearByData().getPoiData() != null)
            this.savePoiCategoriesInDatabase(newTimestamp, oldTimestamp, residenceModules.getNearByData().getPoiData().getCategories().getPoiItems().getPoiCategoryList());
        else if (flatModules != null && residenceModules.getNearByData().getPoiData() != null)
            this.savePoiCategoriesInDatabase(newTimestamp, oldTimestamp, residenceModules.getNearByData().getPoiData().getCategories().getPoiItems().getPoiCategoryList());

        //Activities
        if (residenceModules != null && residenceModules.getNearByData().getActivity().getActivityItems() != null)
            this.saveActivityInDatabase(newTimestamp, oldTimestamp, residenceModules.getNearByData().getActivity().getActivityItems().getActivityItems());

        if (residenceModules != null && residenceModules.getNearByData().getPoiData().getRemoved() != null)
            this.removeActivityFromDatabase(residenceModules.getNearByData().getActivity().getActivityItems().getRemoved());

        //activity types
        if (residenceModules != null && residenceModules.getNearByData().getActivity().getActivityTypes().getActivityTypeItems() != null)
            this.saveActivityTypesInDatabase(newTimestamp, oldTimestamp, residenceModules.getNearByData().getActivity().getActivityTypes().getActivityTypeItems().getActivityTypesItems());

        //Calendar events
        if (residenceModules != null && residenceModules.getCalendarData() != null)
            this.saveCalendarEventsInDatabase(newTimestamp, oldTimestamp, CalendarEvent.LEVEL_FLAT, residenceModules.getCalendarData().getItems());
        if (flatModules != null && flatModules.getCalendarData() != null)
            this.saveCalendarEventsInDatabase(newTimestamp, oldTimestamp, CalendarEvent.LEVEL_FLAT, flatModules.getCalendarData().getItems());
        if (residenceModules != null && residenceModules.getCalendarData() != null)
            this.removeCalendarEventsFromDatabase(residenceModules.getCalendarData().getRemoved());
        if (flatModules != null && flatModules.getCalendarData() != null)
            this.removeCalendarEventsFromDatabase(flatModules.getCalendarData().getRemoved());
        //Calendar birthdays
        if (residenceModules != null && residenceModules.getCalendarData() != null)
            this.saveCalendarBirthdaysInDatabase(newTimestamp, oldTimestamp, residenceModules.getCalendarData().getBirthdays());
        if (flatModules != null && flatModules.getCalendarData() != null)
            this.saveCalendarBirthdaysInDatabase(newTimestamp, oldTimestamp, flatModules.getCalendarData().getBirthdays());
        //News
        if (residenceModules != null && residenceModules.getNewsData() != null)
            this.saveNewsInDatabase(newTimestamp, oldTimestamp, residenceModules.getNewsData().getItems());
        if (flatModules != null && flatModules.getNewsData() != null)
            this.saveNewsInDatabase(newTimestamp, oldTimestamp, flatModules.getNewsData().getItems());
        if (residenceModules != null && residenceModules.getNewsData() != null)
            this.removeNewsFromDatabase(residenceModules.getNewsData().getRemoved());
        if (flatModules != null && flatModules.getNewsData() != null)
            this.removeNewsFromDatabase(flatModules.getNewsData().getRemoved());
        //News categories
        if (residenceModules != null && residenceModules.getNewsData() != null)
            this.saveNewsCategoriesInDatabase(newTimestamp, oldTimestamp, residenceModules.getNewsData().getCategories());
        else if (flatModules != null && flatModules.getNewsData() != null)
            this.saveNewsCategoriesInDatabase(newTimestamp, oldTimestamp, flatModules.getNewsData().getCategories());
        //Weather
        this.deleteAllWeatherFromDatabase();
        if (residenceModules != null)
            this.saveWeatherInDatabase(newTimestamp, residenceModules.getWeatherItems());
        if (flatModules != null)
            this.saveWeatherInDatabase(newTimestamp, flatModules.getWeatherItems());
        //RSS
        if (residenceModules != null && residenceModules.getRssData() != null)
            this.saveRssInDatabase(newTimestamp, oldTimestamp, residenceModules.getRssData().getItems());
        if (flatModules != null && flatModules.getRssData() != null)
            this.saveRssInDatabase(newTimestamp, oldTimestamp, flatModules.getRssData().getItems());
        if (residenceModules != null && residenceModules.getRssData() != null)
            this.removeRssFromDatabase(residenceModules.getRssData().getRemoved());
        if (flatModules != null && flatModules.getRssData() != null)
            this.removeRssFromDatabase(flatModules.getRssData().getRemoved());
        //Emergency numbers
        if (flatModules != null && flatModules.getEmergencyData() != null)
            this.saveEmergencyInDatabase(newTimestamp, oldTimestamp, flatModules.getEmergencyData().getEmergencyNumbers());
        if (residenceModules != null && residenceModules.getEmergencyData() != null)
            this.saveEmergencyInDatabase(newTimestamp, oldTimestamp, residenceModules.getEmergencyData().getEmergencyNumbers());
        if (flatModules != null && flatModules.getEmergencyData() != null)
            this.removeEmergencyNumbersFromDatabase(flatModules.getEmergencyData().getRemoved());
        if (residenceModules != null && residenceModules.getEmergencyData() != null)
            this.removeEmergencyNumbersFromDatabase(residenceModules.getEmergencyData().getRemoved());
        //Special emergency numbers
        if(residenceModules != null && residenceModules.getEmergencyData() != null)
            EmergencyNumber.saveSpecialNumber(this, EmergencyNumber.TYPE_DOCTOR_ON_CALL, residenceModules.getEmergencyData().getDoctor());
        if(residenceModules != null && residenceModules.getEmergencyData() != null)
            EmergencyNumber.saveSpecialNumber(this, EmergencyNumber.TYPE_PHARMACY_ON_CALL, residenceModules.getEmergencyData().getPharmacist());
        //Emergency categories
        if (residenceModules != null && residenceModules.getEmergencyData() != null)
            this.saveEmergencyCategoriesInDatabase(newTimestamp, oldTimestamp, residenceModules.getEmergencyData().getEmergencyCategories());
        else if (flatModules != null && flatModules.getEmergencyData() != null)
            this.saveEmergencyCategoriesInDatabase(newTimestamp, oldTimestamp, flatModules.getEmergencyData().getEmergencyCategories());
        //Photos
        if (flatModules != null && flatModules.getPhotoData() != null)
        {
            this.savePhotosInDatabase(newTimestamp, oldTimestamp, true, flatModules.getPhotoData().getScreenSavers());
            this.savePhotosInDatabase(newTimestamp, oldTimestamp, false, flatModules.getPhotoData().getPhotoStream());
        }
        if (residenceModules != null && residenceModules.getPhotoData() != null)
        {
            this.savePhotosInDatabase(newTimestamp, oldTimestamp, true, residenceModules.getPhotoData().getScreenSavers());
            this.savePhotosInDatabase(newTimestamp, oldTimestamp, false, residenceModules.getPhotoData().getPhotoStream());
        }
        //Care book
        if (flatModules != null)
            this.saveCareBookItemsInDatabase(newTimestamp, oldTimestamp, flatModules.getCareBookItems());
        //Medication
        if(flat.getHabitants() != null)
        {
            for(Habitant habitant : flat.getHabitants())
            {
                this.saveMedicationInDatabase(newTimestamp, oldTimestamp, habitant.getMedicationItems());
            }
        }
        //Invoices
        if(residenceModules != null)
            this.saveInvoicesInDatabase(newTimestamp, oldTimestamp, residenceModules.getInvoices());
        if(flatModules != null)
            this.saveInvoicesInDatabase(newTimestamp, oldTimestamp, flatModules.getInvoices());
        //Habitants
        if(flatModules != null && flatModules.getResidenceInfo().getHabitants()!=null)
            this.saveHabitantsInDatabase(newTimestamp, oldTimestamp, flatModules.getResidenceInfo().getHabitants());
        //Resident News
        if(flatModules != null && flatModules.getResidenceInfo().getResidentNews()!=null)
            this.saveResidenceNewsInDatabase(newTimestamp, oldTimestamp, flatModules.getResidenceInfo().getResidentNews());
    }
    private void saveLayoutInDatabase(long newTimestamp, List<ApiModule> layout)
    {
        if (layout == null || layout.size() == 0)
            return;
        final Uri uri = ModuleContentProvider.CONTENT_URI;
        this.getContentResolver().delete(uri, null, null);
        for (ApiModule module : layout)
            if (module != null)
                this.getContentResolver().insert(uri, module.getContentValues(newTimestamp));
    }
    private void savePoiInDatabase(long newTimestamp, long timestamp, List<PoiItem> poiItems)
    {
        if (poiItems == null || poiItems.size() == 0)
            return;
        final Uri uri = PoiContentProvider.CONTENT_URI;
        for (PoiItem poiItem : poiItems)
            if (poiItem != null)
            {
                Log.d("Update", "POI item " + poiItem.getId() + " - method " + this.getDatabaseMethod(timestamp, poiItem.getCreatedAt(), poiItem.getUpdatedAt(), poiItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, poiItem.getCreatedAt(), poiItem.getUpdatedAt(), poiItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, poiItem.getContentValuesWithRandomPhoto(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        int updated = this.getContentResolver().update(uri, poiItem.getContentValues(newTimestamp), PoiTable.COLUMN_POI_ID + "='" + poiItem.getId() + "'", null);
                        if (updated == 0)
                            this.getContentResolver().insert(uri, poiItem.getContentValuesWithRandomPhoto(newTimestamp));
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, PoiTable.COLUMN_POI_ID + "='" + poiItem.getId() + "'", null);
                        break;
                }
                this.saveOpeningTimesInDatabase(newTimestamp, timestamp, poiItem.getOpeningTimes());
            }
    }
    private void savePoiCategoriesInDatabase(long newTimestamp, long timestamp, List<PoiCategory> poiCategories)
    {
        if (poiCategories == null || poiCategories.size() == 0)
            return;
        final Uri uri = PoiCategoryContentProvider.CONTENT_URI;
        for (PoiCategory poiCategory : poiCategories)
            if (poiCategory != null)
            {
                Log.d("Update", "POI category " + poiCategory.getId() + " - method " + this.getDatabaseMethod(timestamp, poiCategory.getCreatedAt(), poiCategory.getUpdatedAt(), poiCategory.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, poiCategory.getCreatedAt(), poiCategory.getUpdatedAt(), poiCategory.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, poiCategory.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, poiCategory.getContentValues(newTimestamp), PoiCategoryTable.COLUMN_CATEGORY_ID + "='" + poiCategory.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, PoiCategoryTable.COLUMN_CATEGORY_ID + "='" + poiCategory.getId() + "'", null);
                        break;
                }
                if(poiCategory.getImages() != null)
                {
                    if(poiCategory.getImages().size() > 0)
                        this.updatePoiCategoryImage(timestamp, poiCategory.getImages().get(0), poiCategory.getId(), 0);
                    if(poiCategory.getImages().size() > 1)
                        this.updatePoiCategoryImage(timestamp, poiCategory.getImages().get(1), poiCategory.getId(), 1);
                    if(poiCategory.getImages().size() > 2)
                        this.updatePoiCategoryImage(timestamp, poiCategory.getImages().get(2), poiCategory.getId(), 2);
                    if(poiCategory.getImages().size() > 3)
                        this.updatePoiCategoryImage(timestamp, poiCategory.getImages().get(3), poiCategory.getId(), 3);
                    if(poiCategory.getImages().size() > 4)
                        this.updatePoiCategoryImage(timestamp, poiCategory.getImages().get(4), poiCategory.getId(), 4);
                }
            }
    }





    private void saveActivityInDatabase(long newTimestamp, long timestamp, List<ActivityItem> activityItems)
    {
        if (activityItems == null || activityItems.size() == 0)
            return;
        final Uri uri = ActivitiesContentProvider.CONTENT_URI;
        for (ActivityItem activityItem : activityItems)
            if (activityItem != null)
            {
                Log.d("Update", "activity item " + activityItem.getId() + " - method " + this.getDatabaseMethod(timestamp, activityItem.getCreatedAt(), activityItem.getUpdatedAt(), activityItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, activityItem.getCreatedAt(), activityItem.getUpdatedAt(), activityItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, activityItem.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                     this.getContentResolver().update(uri, activityItem.getContentValues(newTimestamp), ActivityTable.COLUMN_ID + "='" + activityItem.getId() + "'", null);

                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, ActivityTable.COLUMN_ID + "='" + activityItem.getId() + "'", null);
                        break;
                }
            }
    }


    private void saveActivityTypesInDatabase(long newTimestamp, long timestamp, List<ActivitytypeItem> activitytypeItems)
    {
        if (activitytypeItems == null || activitytypeItems.size() == 0)
            return;
        final Uri uri = ActivityTypesContentProvider.CONTENT_URI;
        for (ActivitytypeItem activitytypeItem : activitytypeItems)
            if (activitytypeItem != null)
            {
                Log.d("Update", "Activity types " + activitytypeItem.getId() + " - method " + this.getDatabaseMethod(timestamp, activitytypeItem.getCreatedAt(), activitytypeItem.getUpdatedAt(), activitytypeItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, activitytypeItem.getCreatedAt(), activitytypeItem.getUpdatedAt(), activitytypeItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, activitytypeItem.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, activitytypeItem.getContentValues(newTimestamp), ActivityTypesTable.COLUMN_ID + "='" + activitytypeItem.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, ActivityTypesTable.COLUMN_ID + "='" + activitytypeItem.getId() + "'", null);
                        break;
                }

            }
    }
    private void updatePoiCategoryImage(long timestamp, Image image, int poiCategoryId, int index)
    {
        if(image == null)
            return;
        final Uri uri = PoiCategoryContentProvider.CONTENT_URI;
        String columnPortrait;
        String columnLandscape;
        switch (index)
        {
            case 0:
                columnPortrait = PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_1;
                columnLandscape = PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_1;
                break;
            case 1:
                columnPortrait = PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_2;
                columnLandscape = PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_2;
                break;
            case 2:
                columnPortrait = PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_3;
                columnLandscape = PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_3;
                break;
            case 3:
                columnPortrait = PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_4;
                columnLandscape = PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_4;
                break;
            case 4:
                columnPortrait = PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_5;
                columnLandscape = PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_5;
                break;
            default:
                columnPortrait = PoiCategoryTable.COLUMN_IMAGE_PORTRAIT_1;
                columnLandscape = PoiCategoryTable.COLUMN_IMAGE_LANDSCAPE_1;
                break;
        }
        switch (this.getDatabaseMethod(timestamp, image.getCreatedAt(), image.getUpdatedAt(), image.getDeletedAt()))
        {
            case METHOD_INSERT:
            case METHOD_UPDATE:
                ContentValues cvUpdate = new ContentValues();
                cvUpdate.put(columnPortrait, image.getPortraitImage());
                cvUpdate.put(columnLandscape, image.getLandscapeImage());
                this.getContentResolver().update(uri, cvUpdate, PoiCategoryTable.COLUMN_CATEGORY_ID + "='" + poiCategoryId + "'", null);
                break;
            case METHOD_DELETE:
                ContentValues cvDelete = new ContentValues();
                cvDelete.putNull(columnPortrait);
                cvDelete.putNull(columnLandscape);
                this.getContentResolver().update(uri, cvDelete, PoiCategoryTable.COLUMN_CATEGORY_ID + "='" + poiCategoryId + "'", null);
                break;
        }
    }
    private void saveOpeningTimesInDatabase(long newTimestamp, long timestamp, List<OpeningTime> openingTimes)
    {
        if (openingTimes == null || openingTimes.size() == 0)
            return;
        final Uri uri = OpeningTimesContentProvider.CONTENT_URI;
        for (OpeningTime openingTime : openingTimes)
            if (openingTime != null)
            {
                Log.d("Update", "openingTime " + openingTime.getId() + " - method " + this.getDatabaseMethod(timestamp, openingTime.getCreatedAt(), openingTime.getUpdatedAt(), openingTime.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, openingTime.getCreatedAt(), openingTime.getUpdatedAt(), openingTime.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, openingTime.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, openingTime.getContentValues(newTimestamp), OpeningTimesTable.COLUMN_OPENING_TIME_ID + "='" + openingTime.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, OpeningTimesTable.COLUMN_OPENING_TIME_ID + "='" + openingTime.getId() + "'", null);
                        break;
                }
            }
    }
    private void saveCalendarEventsInDatabase(long newTimestamp, long timestamp, int level, List<CalendarEvent> calendarEvents)
    {
        if (calendarEvents == null || calendarEvents.size() == 0)
            return;
        final Uri uri = CalendarEventContentProvider.CONTENT_URI;
        final Uri notificationUri = NotificationContentProvider.CONTENT_URI;
        for (CalendarEvent calendarEvent : calendarEvents)
            if (calendarEvent != null)
            {
                Log.d("Update", "calendarEvent " + calendarEvent.getId() + " - method " + this.getDatabaseMethod(timestamp, calendarEvent.getCreatedAt(), calendarEvent.getUpdatedAt(), calendarEvent.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, calendarEvent.getCreatedAt(), calendarEvent.getUpdatedAt(), calendarEvent.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, calendarEvent.getContentValues(newTimestamp, level));
                        if (timestamp != 0 && calendarEvent.isInstantReminder())
                            this.getContentResolver().insert(notificationUri, NotificationHelper.getContentValues(newTimestamp, this, calendarEvent));
                        break;
                    case METHOD_UPDATE:
                        int updated = this.getContentResolver().update(uri, calendarEvent.getContentValues(newTimestamp, level), CalendarEventTable.COLUMN_EVENT_ID + "='" + calendarEvent.getId() + "'", null);
                        if (updated == 0)
                            this.getContentResolver().insert(uri, calendarEvent.getContentValues(newTimestamp, level));
                        if (timestamp != 0)
                        {
                            if(!calendarEvent.isInstantReminder())
                            {
                                String notificationWhereDelete = NotificationTable.COLUMN_ITEM_ID + "='" + calendarEvent.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_EVENT + "'";
                                this.getContentResolver().delete(notificationUri, notificationWhereDelete, null);
                            }
                            else
                            {
                                String notificationWhere = NotificationTable.COLUMN_ITEM_ID + "='" + calendarEvent.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_EVENT + "'";
                                int updatedNotification = this.getContentResolver().update(notificationUri, NotificationHelper.getContentValues(newTimestamp, this, calendarEvent), notificationWhere, null);
                                if(updatedNotification == 0)
                                    this.getContentResolver().insert(notificationUri, NotificationHelper.getContentValues(newTimestamp, this, calendarEvent));
                            }
                        }
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, CalendarEventTable.COLUMN_EVENT_ID + "='" + calendarEvent.getId() + "'", null);
                        if (timestamp != 0)
                        {
                            String notificationWhereDelete = NotificationTable.COLUMN_ITEM_ID + "='" + calendarEvent.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_EVENT + "'";
                            this.getContentResolver().delete(notificationUri, notificationWhereDelete, null);
                        }
                        break;
                }
            }
    }
    private void saveCalendarBirthdaysInDatabase(long newTimestamp, long timestamp, List<CalendarBirthday> calendarBirthdays)
    {
        if (calendarBirthdays == null || calendarBirthdays.size() == 0)
            return;
        final Uri uri = CalendarBirthdayContentProvider.CONTENT_URI;
        for (CalendarBirthday calendarBirthday : calendarBirthdays)
            if (calendarBirthday != null)
            {
                Log.d("Update", "calendarBirthday " + calendarBirthday.getId() + " - method " + this.getDatabaseMethod(timestamp, calendarBirthday.getCreatedAt(), calendarBirthday.getUpdatedAt(), calendarBirthday.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, calendarBirthday.getCreatedAt(), calendarBirthday.getUpdatedAt(), calendarBirthday.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, calendarBirthday.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, calendarBirthday.getContentValues(newTimestamp), CalendarBirthdayTable.COLUMN_BIRTHDAY_ID + "='" + calendarBirthday.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, CalendarBirthdayTable.COLUMN_BIRTHDAY_ID + "='" + calendarBirthday.getId() + "'", null);
                        break;
                }
            }
    }
    private void saveNewsInDatabase(long newTimestamp, long timestamp, List<NewsItem> newsItems)
    {
        if (newsItems == null || newsItems.size() == 0)
            return;
        final Uri uri = NewsContentProvider.CONTENT_URI;
        final Uri notificationUri = NotificationContentProvider.CONTENT_URI;
        int unreadMessages = PreferencesHelper.getUnreadNews(this);
        for (NewsItem newsItem : newsItems)
            if (newsItem != null)
            {
                Log.d("Update", "newsItem " + newsItem.getId() + " - method " + this.getDatabaseMethod(timestamp, newsItem.getCreatedAt(), newsItem.getUpdatedAt(), newsItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, newsItem.getCreatedAt(), newsItem.getUpdatedAt(), newsItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        unreadMessages++;
                        this.getContentResolver().insert(uri, newsItem.getContentValuesWithRandomPhoto(newTimestamp));
                        if (timestamp != 0)
                            this.getContentResolver().insert(notificationUri, NotificationHelper.getContentValues(newTimestamp, newsItem));
                        break;
                    case METHOD_UPDATE:
                        unreadMessages++;
                        int updated = this.getContentResolver().update(uri, newsItem.getContentValues(newTimestamp), NewsTable.COLUMN_NEWS_ID + "='" + newsItem.getId() + "'", null);
                        if (updated == 0)
                            this.getContentResolver().insert(uri, newsItem.getContentValuesWithRandomPhoto(newTimestamp));
                        if (timestamp != 0)
                        {
                            String notificationWhere = NotificationTable.COLUMN_ITEM_ID + "='" + newsItem.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_NEWS + "'";
                            int updatedNotification = this.getContentResolver().update(notificationUri, NotificationHelper.getContentValues(newTimestamp, newsItem), notificationWhere, null);
                            if(updatedNotification == 0)
                                this.getContentResolver().insert(notificationUri, NotificationHelper.getContentValues(newTimestamp, newsItem));
                        }
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, NewsTable.COLUMN_NEWS_ID + "='" + newsItem.getId() + "'", null);
                        if (timestamp != 0)
                        {
                            String notificationWhereDelete = NotificationTable.COLUMN_ITEM_ID + "='" + newsItem.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_NEWS + "'";
                            this.getContentResolver().delete(notificationUri, notificationWhereDelete, null);
                        }
                        break;
                }
            }
        if (timestamp != 0)
            PreferencesHelper.saveUnreadNews(this, unreadMessages);
    }
    private void saveNewsCategoriesInDatabase(long newTimestamp, long timestamp, List<NewsCategory> newsCategories)
    {
        if (newsCategories == null || newsCategories.size() == 0)
            return;
        final Uri uri = NewsCategoryContentProvider.CONTENT_URI;
        for (NewsCategory newsCategory : newsCategories)
            if (newsCategory != null)
            {
                Log.d("Update", "newsCategory " + newsCategory.getId() + " - method " + this.getDatabaseMethod(timestamp, newsCategory.getCreatedAt(), newsCategory.getUpdatedAt(), newsCategory.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, newsCategory.getCreatedAt(), newsCategory.getUpdatedAt(), newsCategory.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, newsCategory.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, newsCategory.getContentValues(newTimestamp), NewsCategoryTable.COLUMN_CATEGORY_ID + "='" + newsCategory.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, NewsCategoryTable.COLUMN_CATEGORY_ID + "='" + newsCategory.getId() + "'", null);
                        break;
                }
                if(newsCategory.getImages() != null)
                {
                    if(newsCategory.getImages().size() > 0)
                        this.updateNewsCategoryImage(timestamp, newsCategory.getImages().get(0), newsCategory.getId(), 0);
                    if(newsCategory.getImages().size() > 1)
                        this.updateNewsCategoryImage(timestamp, newsCategory.getImages().get(1), newsCategory.getId(), 1);
                    if(newsCategory.getImages().size() > 2)
                        this.updateNewsCategoryImage(timestamp, newsCategory.getImages().get(2), newsCategory.getId(), 2);
                    if(newsCategory.getImages().size() > 3)
                        this.updateNewsCategoryImage(timestamp, newsCategory.getImages().get(3), newsCategory.getId(), 3);
                    if(newsCategory.getImages().size() > 4)
                        this.updateNewsCategoryImage(timestamp, newsCategory.getImages().get(4), newsCategory.getId(), 4);
                }
            }
    }
    private void updateNewsCategoryImage(long timestamp, Image image, int newsCategoryId, int index)
    {
        if(image == null)
            return;
        final Uri uri = NewsCategoryContentProvider.CONTENT_URI;
        String columnPortrait;
        String columnLandscape;
        switch (index)
        {
            case 0:
                columnPortrait = NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_1;
                columnLandscape = NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_1;
                break;
            case 1:
                columnPortrait = NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_2;
                columnLandscape = NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_2;
                break;
            case 2:
                columnPortrait = NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_3;
                columnLandscape = NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_3;
                break;
            case 3:
                columnPortrait = NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_4;
                columnLandscape = NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_4;
                break;
            case 4:
                columnPortrait = NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_5;
                columnLandscape = NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_5;
                break;
            default:
                columnPortrait = NewsCategoryTable.COLUMN_IMAGE_PORTRAIT_1;
                columnLandscape = NewsCategoryTable.COLUMN_IMAGE_LANDSCAPE_1;
                break;
        }
        switch (this.getDatabaseMethod(timestamp, image.getCreatedAt(), image.getUpdatedAt(), image.getDeletedAt()))
        {
            case METHOD_INSERT:
            case METHOD_UPDATE:
                ContentValues cvUpdate = new ContentValues();
                cvUpdate.put(columnPortrait, image.getPortraitImage());
                cvUpdate.put(columnLandscape, image.getLandscapeImage());
                this.getContentResolver().update(uri, cvUpdate, NewsCategoryTable.COLUMN_CATEGORY_ID + "='" + newsCategoryId + "'", null);
                break;
            case METHOD_DELETE:
                ContentValues cvDelete = new ContentValues();
                cvDelete.putNull(columnPortrait);
                cvDelete.putNull(columnLandscape);
                this.getContentResolver().update(uri, cvDelete, NewsCategoryTable.COLUMN_CATEGORY_ID + "='" + newsCategoryId + "'", null);
                break;
        }
    }
    private void saveWeatherInDatabase(long newTimestamp, List<WeatherItem> weatherItems)
    {
        if (weatherItems == null || weatherItems.size() == 0)
            return;
        final Uri uri = WeatherContentProvider.CONTENT_URI;
        for (WeatherItem weatherItem : weatherItems)
            if (weatherItem != null)
                this.getContentResolver().insert(uri, weatherItem.getContentValues(newTimestamp));
    }
    private void saveRssInDatabase(long newTimestamp, long timestamp, List<RssItem> rssItems)
    {
        if (rssItems == null || rssItems.size() == 0)
            return;
        final Uri uri = RssContentProvider.CONTENT_URI;
        for (RssItem rssItem : rssItems)
        {
            if (rssItem != null)
            {
                Log.d("Update", "rssItem " + rssItem.getId() + " - method " + this.getDatabaseMethod(timestamp, rssItem.getCreatedAt(), rssItem.getUpdatedAt(), rssItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, rssItem.getCreatedAt(), rssItem.getUpdatedAt(), rssItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, rssItem.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        int updated = this.getContentResolver().update(uri, rssItem.getContentValues(newTimestamp), RssTable.COLUMN_RSS_ID + "='" + rssItem.getId() + "'", null);
                        if (updated == 0)
                            this.getContentResolver().insert(uri, rssItem.getContentValues(newTimestamp));
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, RssTable.COLUMN_RSS_ID + "='" + rssItem.getId() + "'", null);
                        break;
                }
            }
        }
    }
    private void saveEmergencyInDatabase(long newTimestamp, long timestamp, List<EmergencyNumber> emergencyNumbers)
    {
        if (emergencyNumbers == null || emergencyNumbers.size() == 0)
            return;
        final Uri uri = EmergencyContentProvider.CONTENT_URI;
        for (EmergencyNumber emergencyNumber : emergencyNumbers)
        {
            if (emergencyNumber != null)
            {
                Log.d("Update", "emergencyNumber " + emergencyNumber.getId() + " - method " + this.getDatabaseMethod(timestamp, emergencyNumber.getCreatedAt(), emergencyNumber.getUpdatedAt(), emergencyNumber.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, emergencyNumber.getCreatedAt(), emergencyNumber.getUpdatedAt(), emergencyNumber.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, emergencyNumber.getContentValuesWithRandomPhoto(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, emergencyNumber.getContentValues(newTimestamp), EmergencyTable.COLUMN_EMERGENCY_ID + "='" + emergencyNumber.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, EmergencyTable.COLUMN_EMERGENCY_ID + "='" + emergencyNumber.getId() + "'", null);
                        break;
                }
            }
        }
    }
    private void saveEmergencyCategoriesInDatabase(long newTimestamp, long timestamp, List<EmergencyCategory> emergencyCategories)
    {
        if (emergencyCategories == null || emergencyCategories.size() == 0)
            return;
        final Uri uri = EmergencyCategoryContentProvider.CONTENT_URI;
        for (EmergencyCategory emergencyCategory : emergencyCategories)
            if (emergencyCategory != null)
            {
                Log.d("Update", "emergencyCategory " + emergencyCategory.getId() + " - method " + this.getDatabaseMethod(timestamp, emergencyCategory.getCreatedAt(), emergencyCategory.getUpdatedAt(), emergencyCategory.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, emergencyCategory.getCreatedAt(), emergencyCategory.getUpdatedAt(), emergencyCategory.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, emergencyCategory.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, emergencyCategory.getContentValues(newTimestamp), EmergencyCategoryTable.COLUMN_CATEGORY_ID + "='" + emergencyCategory.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, EmergencyCategoryTable.COLUMN_CATEGORY_ID + "='" + emergencyCategory.getId() + "'", null);
                        break;
                }
                if(emergencyCategory.getImages() != null)
                {
                    if(emergencyCategory.getImages().size() > 0)
                        this.updateEmergencyCategoryImage(timestamp, emergencyCategory.getImages().get(0), emergencyCategory.getId(), 0);
                    if(emergencyCategory.getImages().size() > 1)
                        this.updateEmergencyCategoryImage(timestamp, emergencyCategory.getImages().get(1), emergencyCategory.getId(), 1);
                    if(emergencyCategory.getImages().size() > 2)
                        this.updateEmergencyCategoryImage(timestamp, emergencyCategory.getImages().get(2), emergencyCategory.getId(), 2);
                    if(emergencyCategory.getImages().size() > 3)
                        this.updateEmergencyCategoryImage(timestamp, emergencyCategory.getImages().get(3), emergencyCategory.getId(), 3);
                    if(emergencyCategory.getImages().size() > 4)
                        this.updateEmergencyCategoryImage(timestamp, emergencyCategory.getImages().get(4), emergencyCategory.getId(), 4);
                }
            }
    }
    private void updateEmergencyCategoryImage(long timestamp, Image image, int emergencyCategoryId, int index)
    {
        if(image == null)
            return;
        final Uri uri = EmergencyCategoryContentProvider.CONTENT_URI;
        String columnPortrait;
        String columnLandscape;
        switch (index)
        {
            case 0:
                columnPortrait = EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_1;
                columnLandscape = EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_1;
                break;
            case 1:
                columnPortrait = EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_2;
                columnLandscape = EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_2;
                break;
            case 2:
                columnPortrait = EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_3;
                columnLandscape = EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_3;
                break;
            case 3:
                columnPortrait = EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_4;
                columnLandscape = EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_4;
                break;
            case 4:
                columnPortrait = EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_5;
                columnLandscape = EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_5;
                break;
            default:
                columnPortrait = EmergencyCategoryTable.COLUMN_IMAGE_PORTRAIT_1;
                columnLandscape = EmergencyCategoryTable.COLUMN_IMAGE_LANDSCAPE_1;
                break;
        }
        switch (this.getDatabaseMethod(timestamp, image.getCreatedAt(), image.getUpdatedAt(), image.getDeletedAt()))
        {
            case METHOD_INSERT:
            case METHOD_UPDATE:
                ContentValues cvUpdate = new ContentValues();
                cvUpdate.put(columnPortrait, image.getPortraitImage());
                cvUpdate.put(columnLandscape, image.getLandscapeImage());
                this.getContentResolver().update(uri, cvUpdate, EmergencyCategoryTable.COLUMN_CATEGORY_ID + "='" + emergencyCategoryId + "'", null);
                break;
            case METHOD_DELETE:
                ContentValues cvDelete = new ContentValues();
                cvDelete.putNull(columnPortrait);
                cvDelete.putNull(columnLandscape);
                this.getContentResolver().update(uri, cvDelete, EmergencyCategoryTable.COLUMN_CATEGORY_ID + "='" + emergencyCategoryId + "'", null);
                break;
        }
    }
    private void savePhotosInDatabase(long newTimestamp, long timestamp, boolean screenSaver, List<PhotoItem> photoItems)
    {
        if (photoItems == null || photoItems.size() == 0)
            return;
        final Uri uri = PhotoContentProvider.CONTENT_URI;
        for (PhotoItem photoItem : photoItems)
            if (photoItem != null)
            {
                Log.d("Update", "photoItem " + photoItem.getId() + " - method " + this.getDatabaseMethod(timestamp, photoItem.getCreatedAt(), photoItem.getUpdatedAt(), photoItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, photoItem.getCreatedAt(), photoItem.getUpdatedAt(), photoItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, photoItem.getContentValues(newTimestamp, screenSaver));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, photoItem.getContentValues(newTimestamp, screenSaver), PhotoTable.COLUMN_PHOTO_ID + "='" + photoItem.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, PhotoTable.COLUMN_PHOTO_ID + "='" + photoItem.getId() + "'", null);
                        break;
                }
            }
    }
    private void saveCareBookItemsInDatabase(long newTimestamp, long timestamp, List<HealthCareBookItem> careBookItems)
    {
        if (careBookItems == null || careBookItems.size() == 0)
            return;
        final Uri uri = CareBookContentProvider.CONTENT_URI;
        for (HealthCareBookItem careBookItem : careBookItems)
        {
            if (careBookItem != null)
            {
                Log.d("Update", "careBookItem " + careBookItem.getId() + " - method " + this.getDatabaseMethod(timestamp, careBookItem.getCreatedAt(), careBookItem.getUpdatedAt(), careBookItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, careBookItem.getCreatedAt(), careBookItem.getUpdatedAt(), careBookItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, careBookItem.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, careBookItem.getContentValues(newTimestamp), CareBookTable.COLUMN_CAREBOOK_ID + "='" + careBookItem.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, CareBookTable.COLUMN_CAREBOOK_ID + "='" + careBookItem.getId() + "'", null);
                        break;
                }
            }
        }
    }
    private void saveMedicationInDatabase(long newTimestamp, long timestamp, List<MedicationItem> medicationItems)
    {
        if (medicationItems == null || medicationItems.size() == 0)
            return;
        final Uri uri = MedicationContentProvider.CONTENT_URI;
        for (MedicationItem medication : medicationItems)
        {
            if (medication != null)
            {
                Log.d("Update", "medication " + medication.getId() + " - method " + this.getDatabaseMethod(timestamp, medication.getCreatedAt(), medication.getUpdatedAt(), medication.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, medication.getCreatedAt(), medication.getUpdatedAt(), medication.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, medication.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, medication.getContentValues(newTimestamp), MedicationTable.COLUMN_MEDICATION_ID + "='" + medication.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, MedicationTable.COLUMN_MEDICATION_ID + "='" + medication.getId() + "'", null);
                        break;
                }
            }
        }
    }
    private void saveInvoicesInDatabase(long newTimestamp, long timestamp, List<Invoice> invoices)
    {
        if (invoices == null || invoices.size() == 0)
            return;
        final Uri uri = InvoiceContentProvider.CONTENT_URI;
        for (Invoice invoice : invoices)
        {
            if (invoice != null)
            {
                Log.d("Update", "invoice " + invoice.getId() + " - method " + this.getDatabaseMethod(timestamp, invoice.getCreatedAt(), invoice.getUpdatedAt(), invoice.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, invoice.getCreatedAt(), invoice.getUpdatedAt(), invoice.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, invoice.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, invoice.getContentValues(newTimestamp), InvoiceTable.COLUMN_INVOICE_ID + "='" + invoice.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, InvoiceTable.COLUMN_INVOICE_ID + "='" + invoice.getId() + "'", null);
                        break;
                }
            }
        }
    }
    private void saveHabitantsInDatabase(long newTimestamp, long timestamp, List<HabitantInfo> habitants)
    {
        if (habitants == null || habitants.size() == 0)
            return;
        final Uri uri = HabitantContentProvider.CONTENT_URI;
        for (HabitantInfo habitant : habitants)
        {
            if (habitant != null)
            {
                Log.d("Update", "habitant " + habitant.getId() + " - method " + this.getDatabaseMethod(timestamp, habitant.getCreatedAt(), habitant.getUpdatedAt(), habitant.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, habitant.getCreatedAt(), habitant.getUpdatedAt(), habitant.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        this.getContentResolver().insert(uri, habitant.getContentValues(newTimestamp));
                        break;
                    case METHOD_UPDATE:
                        this.getContentResolver().update(uri, habitant.getContentValues(newTimestamp), HabitantTable.COLUMN_HABITANT_ID + "='" + habitant.getId() + "'", null);
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, HabitantTable.COLUMN_HABITANT_ID + "='" + habitant.getId() + "'", null);
                        break;
                }
            }
        }
    }



    private void saveResidenceNewsInDatabase(long newTimestamp, long timestamp, List<ResidentNews> residentNews)
    {
        if (residentNews == null || residentNews.size() == 0)
            return;
        final Uri uri = ResidentNewsContentProvider.CONTENT_URI;
        final Uri notificationUri = NotificationContentProvider.CONTENT_URI;
        int unreadMessages = PreferencesHelper.getUnreadNews(this);
        for (ResidentNews residentnewsItem : residentNews)
            if (residentnewsItem != null)
            {
                Log.d("Update", "residentNews " + residentnewsItem.getId() + " - method " + this.getDatabaseMethod(timestamp, residentnewsItem.getCreatedAt(), residentnewsItem.getUpdatedAt(), residentnewsItem.getDeletedAt()));
                switch (this.getDatabaseMethod(timestamp, residentnewsItem.getCreatedAt(), residentnewsItem.getUpdatedAt(), residentnewsItem.getDeletedAt()))
                {
                    case METHOD_INSERT:
                        unreadMessages++;
                        this.getContentResolver().insert(uri, residentnewsItem.getContentValues(newTimestamp));
                        if (timestamp != 0)
                            this.getContentResolver().insert(notificationUri, NotificationHelper.getContentValues(newTimestamp, residentnewsItem));
                        break;
                    case METHOD_UPDATE:
                        unreadMessages++;
                        this.getContentResolver().update(uri, residentnewsItem.getContentValues(newTimestamp), ResidentNewsTable.COLUMN_ID + "='" + residentnewsItem.getId() + "'", null);
                        if (timestamp != 0)
                        {
                            String notificationWhere = NotificationTable.COLUMN_ITEM_ID + "='" + residentnewsItem.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_NEWS + "'";
                            int updatedNotification = this.getContentResolver().update(notificationUri, NotificationHelper.getContentValues(newTimestamp, residentnewsItem), notificationWhere, null);
                            if(updatedNotification == 0)
                                this.getContentResolver().insert(notificationUri, NotificationHelper.getContentValues(newTimestamp, residentnewsItem));
                        }
                        break;
                    case METHOD_DELETE:
                        this.getContentResolver().delete(uri, ResidentNewsTable.COLUMN_ID + "='" + residentnewsItem.getId() + "'", null);
                        if (timestamp != 0)
                        {
                            String notificationWhereDelete = NotificationTable.COLUMN_ITEM_ID + "='" + residentnewsItem.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_NEWS + "'";
                            this.getContentResolver().delete(notificationUri, notificationWhereDelete, null);
                        }
                        break;
                }
            }
        if (timestamp != 0)
            PreferencesHelper.saveUnreadNews(this, unreadMessages);
    }


    private void removePoiFromDatabase(List<RemovedDataItem> removed)
    {
        if (removed == null || removed.size() == 0)
            return;
        Log.d("Update", "Removing " + removed.size() + " poi items");
        final Uri uri = PoiContentProvider.CONTENT_URI;
        for (RemovedDataItem removedItem : removed)
            if (removedItem != null)
                this.getContentResolver().delete(uri, PoiTable.COLUMN_POI_ID + "='" + removedItem.getId() + "'", null);
    }

    private void removeActivityFromDatabase(List<RemovedDataItem> removed)
    {
        if (removed == null || removed.size() == 0)
            return;
        Log.d("Update", "Removing " + removed.size() + " poi items");
        final Uri uri = ActivitiesContentProvider.CONTENT_URI;
        for (RemovedDataItem removedItem : removed)
            if (removedItem != null)
                this.getContentResolver().delete(uri, ActivityTable.COLUMN_ID + "='" + removedItem.getId() + "'", null);
    }
    private void removeCalendarEventsFromDatabase(List<RemovedDataItem> removed)
    {
        if (removed == null || removed.size() == 0)
            return;
        Log.d("Update", "Removing " + removed.size() + " calendar event items");
        final Uri uri = CalendarEventContentProvider.CONTENT_URI;
        final Uri notificationUri = NotificationContentProvider.CONTENT_URI;
        for (RemovedDataItem removedItem : removed)
            if (removedItem != null)
            {
                this.getContentResolver().delete(uri, CalendarEventTable.COLUMN_EVENT_ID + "='" + removedItem.getId() + "'", null);
                String notificationWhereDelete = NotificationTable.COLUMN_ITEM_ID + "='" + removedItem.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_EVENT + "'";
                this.getContentResolver().delete(notificationUri, notificationWhereDelete, null);
            }
    }
    private void removeNewsFromDatabase(List<RemovedDataItem> removed)
    {
        if (removed == null || removed.size() == 0)
            return;
        Log.d("Update", "Removing " + removed.size() + " news items");
        final Uri uri = NewsContentProvider.CONTENT_URI;
        final Uri notificationUri = NotificationContentProvider.CONTENT_URI;
        for (RemovedDataItem removedItem : removed)
            if (removedItem != null)
            {
                this.getContentResolver().delete(uri, NewsTable.COLUMN_NEWS_ID + "='" + removedItem.getId() + "'", null);
                String notificationWhereDelete = NotificationTable.COLUMN_ITEM_ID + "='" + removedItem.getId() + "' AND " + NotificationTable.COLUMN_TYPE + "='" + NotificationHelper.TYPE_NEWS + "'";
                this.getContentResolver().delete(notificationUri, notificationWhereDelete, null);
            }
    }
    private void removeRssFromDatabase(List<RemovedDataItem> removed)
    {
        if (removed == null || removed.size() == 0)
            return;
        Log.d("Update", "Removing " + removed.size() + " rss items");
        final Uri uri = RssContentProvider.CONTENT_URI;
        for (RemovedDataItem removedItem : removed)
            if (removedItem != null)
                this.getContentResolver().delete(uri, RssTable.COLUMN_RSS_ID + "='" + removedItem.getId() + "'", null);
    }
    private void removeEmergencyNumbersFromDatabase(List<RemovedEmergencyItem> removed)
    {
        if (removed == null || removed.size() == 0)
            return;
        Log.d("Update", "Removing " + removed.size() + " emergency numbers");
        final Uri uri = EmergencyContentProvider.CONTENT_URI;
        for (RemovedDataItem removedItem : removed)
            if (removedItem != null)
                this.getContentResolver().delete(uri, EmergencyTable.COLUMN_EMERGENCY_ID + "='" + removedItem.getId() + "'", null);
    }
    private void deleteAllWeatherFromDatabase()
    {
        this.getContentResolver().delete(WeatherContentProvider.CONTENT_URI, null, null);
    }
    private int getDatabaseMethod(long timestamp, String created, String updated, String deleted)
    {
        long createTime = this.getMillis(created);
        long updateTime = this.getMillis(updated);
        long deleteTime = this.getMillis(deleted);
        if(createTime >= timestamp && deleteTime == 0)
            return METHOD_INSERT;
        if(createTime < timestamp && deleteTime == 0 && updateTime != 0 && updateTime >= timestamp)
            return METHOD_UPDATE;
        if(createTime < timestamp && deleteTime != 0 && deleteTime >= timestamp)
            return METHOD_DELETE;
        return METHOD_DO_NOTHING;
    }
    private long getMillis(String date)
    {
        if(date == null)
            return 0;
        try
        {
            return SDF_TIME.parse(date).getTime();
        }
        catch (ParseException e)
        {
            return 0;
        }
    }
    private String getDate(long millis)
    {
        return SDF_TIME.format(new Date(millis));
    }

}