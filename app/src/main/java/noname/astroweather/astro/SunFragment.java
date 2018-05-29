package noname.astroweather.astro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import noname.astroweather.core.Clock;
import noname.astroweather.R;

public class SunFragment extends Fragment {
    TextView mView;
    String date;
    Thread myThread;

    Clock clock; //= new ClockSunFragment(this.getActivity());
    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    AstroCalculator astroCalculator;
    AstroCalculator.Location location;
    AstroDateTime astroDateTime;

    private class ClockSunFragment extends Clock {

        private ClockSunFragment(Activity activity) {
            super(activity);
        }

        @Override
        public void setNewValue() {
            astroInit();
            super.setNewValue();
        }

        @Override
        public void showText() {
            String sunrise = astroCalculator.getSunInfo().getSunrise().toString();
            String sunset = astroCalculator.getSunInfo().getSunset().toString();
            String twilightMorning = astroCalculator.getSunInfo().getTwilightMorning().toString();
            String twilightEvening = astroCalculator.getSunInfo().getTwilightMorning().toString();
            mView.setText(
                    "Sunrise: " + sunrise.substring(0, sunrise.length() - 6) +
                    "\nSunset: " + sunset.substring(0, sunset.length() - 6) +
                    "\nAzimuth rise: " + astroCalculator.getSunInfo().getAzimuthRise() +
                    "\nAzimuth set: " +astroCalculator.getSunInfo().getAzimuthSet() +
                    "\nTwilight morning: " + twilightMorning.substring(0, twilightMorning.length() - 6) +
                    "\nTwilight evening: " + twilightEvening.substring(0, twilightEvening.length() - 6)
            );
            super.showText();
        }

    }

    public void astroInit() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("config.xml", 0);

        date = df.format(Calendar.getInstance().getTime());

        astroDateTime = new AstroDateTime(
                Integer.valueOf(date.substring(0, date.indexOf("."))),
                Integer.valueOf(date.substring(date.indexOf(".") + 1, date.lastIndexOf("."))),
                Integer.valueOf(date.substring(date.lastIndexOf(".") + 1, date.indexOf(" "))),
                Integer.valueOf(date.substring(date.indexOf(" ") + 1, date.indexOf(":"))),
                Integer.valueOf(date.substring(date.indexOf(":") + 1, date.lastIndexOf(":"))),
                Integer.valueOf(date.substring(date.lastIndexOf(":") + 1, date.length())),
                clock.getTimeZone(),
                false
        );

        location = new AstroCalculator.Location(
                Double.valueOf(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude)))),
                Double.valueOf(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))))
        );

        astroCalculator = new AstroCalculator(
                astroDateTime,
                location
        );
    }

    @Override
    public void onStart() {
        clock = new ClockSunFragment(getActivity());
        Runnable myRunnableThread = clock;
        clock.setNewRefreshTime();
        myThread = new Thread(myRunnableThread);
        myThread.start();
        astroInit();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_sun, container, false);

        clock = new ClockSunFragment(getActivity());
        mView = rootView.findViewById(R.id.sloneczko);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.sun_landscape);
        } else if (config.orientation == 1) {
            //pionowe
            mView.setBackgroundResource(R.drawable.sun_portrait);
        }

        astroInit();
        clock.showText();

        return rootView;
    }

    @Override
    public void onStop() {
        myThread.interrupt();
        super.onStop();
    }
}


