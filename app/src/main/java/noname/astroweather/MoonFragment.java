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

public class MoonFragment extends Fragment {
    private Date currentDate;
    TextView mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_moon, container, false);

        mView = (TextView) rootView.findViewById(R.id.ksiezyczek);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.moon_landscape);
        } else if (config.orientation == 1) {
            //pionowe
            mView.setBackgroundResource(R.drawable.moon_portrait);
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
                true);        AstroCalculator.Location location = new AstroCalculator.Location(
                Double.valueOf(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude)))),
                Double.valueOf(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))))
        );

        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);



        mView.setText("Moonrise:\n" + astroCalculator.getMoonInfo().getMoonrise() +
                "\nMoonset:\n" + astroCalculator.getMoonInfo().getMoonset() +
                "\nMoon age:\n"  + astroCalculator.getMoonInfo().getAge() +
                "\nIllumination:\n" + astroCalculator.getMoonInfo().getIllumination() +
                "\nNext full moon:\n" + astroCalculator.getMoonInfo().getNextFullMoon() +
                "\nNext new moon:\n" + astroCalculator.getMoonInfo().getNextNewMoon());


        return rootView;
    }
}
