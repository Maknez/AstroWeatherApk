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


        currentDate = Calendar.getInstance().getTime();
        AstroDateTime astroDateTime = new AstroDateTime(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay(), currentDate.getHours(), currentDate.getMinutes(), currentDate.getSeconds(), currentDate.getTimezoneOffset(), true);
        AstroCalculator.Location location = new AstroCalculator.Location(
                Double.valueOf(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude)))),
                Double.valueOf(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))))
        );

        AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);



        mView.setText("Moonrise: " + astroCalculator.getMoonInfo().getMoonrise() + "\nMoonset: " + astroCalculator.getMoonInfo().getMoonset() + "\n\nMoon age: "  + astroCalculator.getMoonInfo().getAge() + "\nIllumination: " + astroCalculator.getMoonInfo().getIllumination() + "\n\nNext full moon: " + astroCalculator.getMoonInfo().getNextFullMoon() + "\nNext new moon: " + astroCalculator.getMoonInfo().getNextNewMoon());


        return rootView;
    }
}
