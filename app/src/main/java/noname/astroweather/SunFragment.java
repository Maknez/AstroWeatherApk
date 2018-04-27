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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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


        DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        TimeZone timeZone= TimeZone.getDefault();

        AstroDateTime astroDateTime = new AstroDateTime(
                Integer.valueOf(date.substring(0, date.indexOf("."))),
                Integer.valueOf(date.substring(date.indexOf(".") + 1, date.lastIndexOf("."))),
                Integer.valueOf(date.substring(date.lastIndexOf(".") + 1, date.indexOf(" "))),
                Integer.valueOf(date.substring(date.indexOf(" ") + 1, date.indexOf(":"))),
                Integer.valueOf(date.substring(date.indexOf(":") + 1, date.lastIndexOf(":"))),
                Integer.valueOf(date.substring(date.lastIndexOf(":") + 1, date.length())),
                (timeZone.getRawOffset() / 3600000),
                true);

        AstroCalculator.Location location = new AstroCalculator.Location(
                Double.valueOf(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude)))),
                Double.valueOf(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))))
        );

        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);

        mView.setText("Sunrise:\n" + astroCalculator.getSunInfo().getSunrise() +
                "\nSunset:\n" + astroCalculator.getSunInfo().getSunset() +
                "\nAzimuth rise:\n" + astroCalculator.getSunInfo().getAzimuthRise() +
                "\nAzimuth set:\n" +  + astroCalculator.getSunInfo().getAzimuthSet() +
                "\nTwilight morning:\n" + astroCalculator.getSunInfo().getTwilightMorning() +
                "\nTwilight evening:\n" + astroCalculator.getSunInfo().getTwilightEvening());

        return rootView;
    }
}
