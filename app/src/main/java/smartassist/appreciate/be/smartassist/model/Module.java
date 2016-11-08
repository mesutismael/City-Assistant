package smartassist.appreciate.be.smartassist.model;

import smartassist.appreciate.be.smartassist.R;
import smartassist.appreciate.be.smartassist.activities.CalendarActivity;
import smartassist.appreciate.be.smartassist.activities.CarebookActivity;
import smartassist.appreciate.be.smartassist.activities.ChatContactsActivity;
import smartassist.appreciate.be.smartassist.activities.EmergencyActivity;
import smartassist.appreciate.be.smartassist.activities.InvoicesActivity;
import smartassist.appreciate.be.smartassist.activities.MedicationActivity;
import smartassist.appreciate.be.smartassist.activities.NewsActivity;
import smartassist.appreciate.be.smartassist.activities.PhotosActivity;
import smartassist.appreciate.be.smartassist.activities.PoiSelectionActivity;
import smartassist.appreciate.be.smartassist.activities.ResidenceInfoSelectionActivity;
import smartassist.appreciate.be.smartassist.activities.RssActivity;
import smartassist.appreciate.be.smartassist.activities.VitalsActivity;
import smartassist.appreciate.be.smartassist.activities.WeatherActivity;

/**
 * Created by Inneke on 26/01/2015.
 */
public enum Module
{
    POI(R.string.module_poi, R.drawable.shape_module_poi, R.drawable.module_poi, PoiSelectionActivity.class),
    NEWS(R.string.module_news, R.drawable.shape_module_news, R.drawable.module_news, NewsActivity.class),
    WEATHER(R.string.module_weather, R.drawable.shape_module_weather, R.drawable.module_weather, WeatherActivity.class),
    CALENDAR(R.string.module_calendar, R.drawable.shape_module_calendar, R.drawable.module_calendar, CalendarActivity.class),
    EMERGENCY(R.string.module_emergency, R.drawable.shape_module_emergency, R.drawable.module_emergency, EmergencyActivity.class),
    PHOTOS(R.string.module_photos, R.drawable.shape_module_photos, R.drawable.module_photos, PhotosActivity.class),
    RSS(R.string.module_rss, R.drawable.shape_module_rss, R.drawable.module_rss, RssActivity.class),
    MEDICATION(R.string.module_medication, R.drawable.shape_module_medication, R.drawable.module_medication, MedicationActivity.class),
    VITALS(R.string.module_vitals, R.drawable.shape_module_vitals, R.drawable.module_vitals, VitalsActivity.class),
    CAREBOOK(R.string.module_carebook, R.drawable.shape_module_carebook, R.drawable.module_carebook, CarebookActivity.class),
    CHAT(R.string.module_chat, R.drawable.shape_module_chat, R.drawable.module_chat, ChatContactsActivity.class),
    INVOICES(R.string.module_invoice, R.drawable.shape_module_invoices, R.drawable.module_invoices, InvoicesActivity.class),
    HABITANTS(R.string.module_habitants, R.drawable.shape_module_habitants, R.drawable.module_habitants, ResidenceInfoSelectionActivity.class);

    private int text;
    private int background;
    private int image;
    private Class activity;

    Module(int text, int background, int image, Class activity)
    {
        this.text = text;
        this.background = background;
        this.image = image;
        this.activity = activity;
    }

    public int getText()
    {
        return this.text;
    }

    public int getBackground()
    {
        return this.background;
    }

    public int getImage()
    {
        return this.image;
    }

    public Class getActivity()
    {
        return this.activity;
    }
}
