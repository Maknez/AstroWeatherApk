package noname.astroweather.astro;

import android.app.Activity;
import android.content.SharedPreferences;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import noname.astroweather.R;

public class AstroCalculatorCore {

    private final Activity activity;

    public AstroCalculatorCore(Activity activity) {
        this.activity = activity;
        dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    }

    private AstroCalculator astroCalculator;
    private AstroCalculator.Location astroCalculatorLocation;
    private AstroDateTime astroDateTime;
    private DateFormat dateFormat;
    private String date;
    private SharedPreferences sharedPreferencesWithCustomValues;

    private int getTimeZone() {
        double longitude = Double.valueOf(sharedPreferencesWithCustomValues.getString("Custom_Longitude", String.valueOf(activity.getString(R.string.Default_Longitude))));
        if (longitude >= 15 || longitude <= -15) {
            return (int) Math.floor((longitude + 15) / 30);
        } else {
            return 0;
        }
    }

    private void sharedPreferencesInit() {
        sharedPreferencesWithCustomValues = activity.getSharedPreferences("config.xml", 0);
    }

    public String getMoonInfo() {
        String moonRise = astroCalculator.getMoonInfo().getMoonrise().toString();
        String moonSet = astroCalculator.getMoonInfo().getMoonset().toString();
        String nextFullMoon = astroCalculator.getMoonInfo().getNextFullMoon().toString();
        String NextNewMoon = astroCalculator.getMoonInfo().getNextNewMoon().toString();

        return "Moonrise: " + moonRise.substring(0, moonRise.length() - 6) +
                "\nMoonset: " + moonSet.substring(0, moonSet.length() - 6) +
                "\nMoon age: " + astroCalculator.getMoonInfo().getAge() +
                "\nIllumination: " + astroCalculator.getMoonInfo().getIllumination() +
                "\nNext full moon: " + nextFullMoon.substring(0, nextFullMoon.length() - 6) +
                "\nNext new moon: " + NextNewMoon.substring(0, NextNewMoon.length() - 6);
    }

    public String getSunInfo() {
        String sunrise = astroCalculator.getSunInfo().getSunrise().toString();
        String sunset = astroCalculator.getSunInfo().getSunset().toString();
        String twilightMorning = astroCalculator.getSunInfo().getTwilightMorning().toString();
        String twilightEvening = astroCalculator.getSunInfo().getTwilightMorning().toString();

        return "Sunrise: " + sunrise.substring(0, sunrise.length() - 6) +
                "\nSunset: " + sunset.substring(0, sunset.length() - 6) +
                "\nAzimuth rise: " + astroCalculator.getSunInfo().getAzimuthRise() +
                "\nAzimuth set: " + astroCalculator.getSunInfo().getAzimuthSet() +
                "\nTwilight morning: " + twilightMorning.substring(0, twilightMorning.length() - 6) +
                "\nTwilight evening: " + twilightEvening.substring(0, twilightEvening.length() - 6);
    }

    public void astroCalculatorCoreObjectsInitialize() {
        sharedPreferencesInit();
        dateFormatInitialize();
        astroDateTimeInitialize();
        astroCalculatorLocationInitialize();
        astroCalculatorInitialize();
    }

    private void dateFormatInitialize() {
        date = dateFormat.format(Calendar.getInstance().getTime());
    }

    private void astroCalculatorInitialize() {
        astroCalculator = new AstroCalculator(
                astroDateTime,
                astroCalculatorLocation
        );
    }

    private void astroCalculatorLocationInitialize() {
        astroCalculatorLocation = new AstroCalculator.Location(
                Double.valueOf(sharedPreferencesWithCustomValues.getString("Custom_Longitude", String.valueOf(activity.getResources().getString(R.string.Default_Longitude)))),
                Double.valueOf(sharedPreferencesWithCustomValues.getString("Custom_Latitude", String.valueOf(activity.getResources().getString(R.string.Default_Latitude))))
        );
    }

    private void astroDateTimeInitialize() {
        astroDateTime = new AstroDateTime(
                Integer.valueOf(date.substring(0, date.indexOf("."))),
                Integer.valueOf(date.substring(date.indexOf(".") + 1, date.lastIndexOf("."))),
                Integer.valueOf(date.substring(date.lastIndexOf(".") + 1, date.indexOf(" "))),
                Integer.valueOf(date.substring(date.indexOf(" ") + 1, date.indexOf(":"))),
                Integer.valueOf(date.substring(date.indexOf(":") + 1, date.lastIndexOf(":"))),
                Integer.valueOf(date.substring(date.lastIndexOf(":") + 1, date.length())),
                getTimeZone(),
                false
        );
    }
}
