package smartassist.appreciate.be.smartassist.utils;

import smartassist.appreciate.be.smartassist.model.Module;

/**
 * Created by Inneke on 26/01/2015.
 */
public class ModuleHelper
{
    public static Module getModule(int id)
    {
        switch (id)
        {
            case 1: return Module.POI;
            case 2: return Module.NEWS;
            case 3: return Module.WEATHER;
            case 4: return Module.CALENDAR;
            case 5: return Module.EMERGENCY;
            case 6: return Module.PHOTOS;
            case 7: return Module.RSS;
            case 8: return Module.CAREBOOK;
            case 9: return Module.CHAT;
            case 10: return Module.VITALS;
            case 11: return Module.MEDICATION;
            case 12: return Module.INVOICES;
            case 13: return Module.HABITANTS;
            default: return null;
        }
    }

    public static int getModuleId(Module module)
    {
        if(module == Module.POI)
            return 1;
        if(module == Module.NEWS)
            return 2;
        if(module == Module.WEATHER)
            return 3;
        if(module == Module.CALENDAR)
            return 4;
        if(module == Module.EMERGENCY)
            return 5;
        if(module == Module.PHOTOS)
            return 6;
        if(module == Module.RSS)
            return 7;
        if(module == Module.CAREBOOK)
            return 8;
        if(module == Module.CHAT)
            return 9;
        if(module == Module.VITALS)
            return 10;
        if(module == Module.MEDICATION)
            return 11;
        if(module == Module.INVOICES)
            return 12;
        if(module == Module.HABITANTS)
            return 13;
        return -1;
    }
}
