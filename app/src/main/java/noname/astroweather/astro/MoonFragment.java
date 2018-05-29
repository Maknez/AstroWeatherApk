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

public class MoonFragment extends Fragment {
    TextView mView;
    String date;
    Thread myThread;

    Clock clock; //= new MoonFragment.ClockMoonFragment(getActivity());
    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    AstroCalculator astroCalculator;
    AstroCalculator.Location location;
    AstroDateTime astroDateTime;

    class ClockMoonFragment extends Clock {

        private ClockMoonFragment(Activity activity) {
            super(activity);
        }

        @Override
        public void setNewValue() {
            astroInit();
            super.setNewValue();
        }

        @Override
        public void showText() {
            String moonRise = astroCalculator.getMoonInfo().getMoonrise().toString();
            String moonSet = astroCalculator.getMoonInfo().getMoonset().toString();
            String nextFullMoon = astroCalculator.getMoonInfo().getNextFullMoon().toString();
            String NextNewMoon= astroCalculator.getMoonInfo().getNextNewMoon().toString();
            mView.setText(
                    "Moonrise: " + moonRise.substring(0, moonRise.length() - 6) +
                    "\nMoonset: " + moonSet.substring(0, moonSet.length() - 6) +
                    "\nMoon age: " + astroCalculator.getMoonInfo().getAge() +
                    "\nIllumination: " + astroCalculator.getMoonInfo().getIllumination() +
                    "\nNext full moon: " + nextFullMoon.substring(0, nextFullMoon.length() - 6) +
                    "\nNext new moon: " + NextNewMoon.substring(0, NextNewMoon.length() - 6)
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
        clock = new MoonFragment.ClockMoonFragment(getActivity());
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
                R.layout.fragment_moon, container, false);

        clock = new MoonFragment.ClockMoonFragment(getActivity());
        mView = rootView.findViewById(R.id.ksiezyczek);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.moon_landscape);
        } else if (config.orientation == 1) {
            //pionowe
            mView.setBackgroundResource(R.drawable.moon_portrait);
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
