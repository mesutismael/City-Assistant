package smartassist.appreciate.be.smartassist.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Inneke De Clippel on 19/01/2016.
 */
public class TypefaceHelper
{
    private static Typeface typefaceRegular;
    private static Typeface typefaceBold;
    private static Typeface typefaceBlack;
    private static Typeface typefaceLight;
    private static Typeface typefaceHairline;

    public static final int MONTSERRAT_REGULAR = 1;
    public static final int MONTSERRAT_BOLD = 2;
    public static final int MONTSERRAT_BLACK = 3;
    public static final int MONTSERRAT_LIGHT = 4;
    public static final int MONTSERRAT_HAIRLINE = 5;

    public static Typeface getTypeface(Context context, int typefaceNumber)
    {
        switch (typefaceNumber)
        {
            case MONTSERRAT_REGULAR:
                return getTypefaceRegular(context);

            case MONTSERRAT_BOLD:
                return getTypefaceBold(context);

            case MONTSERRAT_BLACK:
                return getTypefaceBlack(context);

            case MONTSERRAT_LIGHT:
                return getTypefaceLight(context);

            case MONTSERRAT_HAIRLINE:
                return getTypefaceHairline(context);

            default:
                return getTypefaceRegular(context);
        }
    }

    public static Typeface getTypefaceRegular(Context context)
    {
        if(typefaceRegular == null)
        {
            typefaceRegular = Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.otf");
        }

        return typefaceRegular;
    }

    public static Typeface getTypefaceBold(Context context)
    {
        if(typefaceBold == null)
        {
            typefaceBold = Typeface.createFromAsset(context.getAssets(), "Montserrat-Bold.otf");
        }

        return typefaceBold;
    }

    public static Typeface getTypefaceBlack(Context context)
    {
        if(typefaceBlack == null)
        {
            typefaceBlack = Typeface.createFromAsset(context.getAssets(), "Montserrat-Black.otf");
        }

        return typefaceBlack;
    }

    public static Typeface getTypefaceLight(Context context)
    {
        if(typefaceLight == null)
        {
            typefaceLight = Typeface.createFromAsset(context.getAssets(), "Montserrat-Light.otf");
        }

        return typefaceLight;
    }

    public static Typeface getTypefaceHairline(Context context)
    {
        if(typefaceHairline == null)
        {
            typefaceHairline = Typeface.createFromAsset(context.getAssets(), "Montserrat-Hairline.otf");
        }

        return typefaceHairline;
    }
}
