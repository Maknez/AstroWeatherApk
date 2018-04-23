package noname.astroweather;


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

import java.util.Calendar;
import java.util.Date;

public class SunFragment extends Fragment {
    private Date currentDate;
    TextView mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_sun, container, false);

        mView = (TextView) rootView.findViewById(R.id.sloneczko);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.sun_landscape);
        } else if (config.orientation == 1) {
            //pionowe
            mView.setBackgroundResource(R.drawable.sun_portrait);
        }


        SharedPreferences sharedPref = getActivity().getSharedPreferences("config.xml", 0);


        currentDate = Calendar.getInstance().getTime();
        AstroDateTime astroDateTime = new AstroDateTime(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay(), currentDate.getHours(), currentDate.getMinutes(), currentDate.getSeconds(), currentDate.getTimezoneOffset(), true);
        AstroCalculator.Location location = new AstroCalculator.Location(
                Double.valueOf(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude)))),
                Double.valueOf(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))))
        );

        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);

        mView.setText("Sunrise: " + astroCalculator.getSunInfo().getSunrise() + "\nSunset: " + astroCalculator.getSunInfo().getSunset() + "\n\nAzimuth rise: " + astroCalculator.getSunInfo().getAzimuthRise() + "\nAzimuth set: " +  + astroCalculator.getSunInfo().getAzimuthSet() + "\n\nTwilight morning: " + astroCalculator.getSunInfo().getTwilightMorning() + "\nTwilight evening: " + astroCalculator.getSunInfo().getTwilightEvening());

        return rootView;
    }
}
