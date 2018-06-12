package noname.astroweather.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import noname.astroweather.R;

public class WindAndHumidity extends Fragment {

    private TextView windPowerTextView;
    private TextView windWayTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private SharedPreferences sharedPreferencesWithCustomValues;
    private SharedPreferences offlineDataSharedPreferences;

    @Override
    public void onStart() {
        showDataFromSharedPreferences();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_wind_and_humidity, container, false);

        initializeTextViews(rootView);
        initializeSharedPreferences();
        return rootView;
    }

    private void initializeSharedPreferences() {
        sharedPreferencesWithCustomValues = getActivity().getSharedPreferences("config.xml", 0);
        offlineDataSharedPreferences = getActivity().getSharedPreferences("offline_data.xml", 0);
    }

    private void initializeTextViews(ViewGroup rootView) {
        windPowerTextView = rootView.findViewById(R.id.windPowerText);
        windWayTextView = rootView.findViewById(R.id.windWayText);
        humidityTextView = rootView.findViewById(R.id.humidityText);
        visibilityTextView = rootView.findViewById(R.id.visibilityText);
    }


    public void showDataFromSharedPreferences() {
        int windPowerUnit = sharedPreferencesWithCustomValues.getInt("Wind_Speed_Unit", (getResources().getInteger(R.integer.Default_Wind_Speed_Unit)));

        if (windPowerUnit == 0) {
            windPowerTextView.setText(offlineDataSharedPreferences.getString("windPowerInMPHOffline", "0") + " " + getResources().getString(R.string.wind_power_unit_mph));
        } else if (windPowerUnit == 1) {
            windPowerTextView.setText(offlineDataSharedPreferences.getString("windPowerInKMPHOffline", "0") + " " + getResources().getString(R.string.wind_power_unit_kmph));
        }
        windWayTextView.setText(offlineDataSharedPreferences.getString("windWayOffline", "WindWay") + "°");
        humidityTextView.setText(offlineDataSharedPreferences.getString("humidityOffline", "Humidity") + "%");
        visibilityTextView.setText(offlineDataSharedPreferences.getString("visibilityOffline", "Visibility") + "m");
    }
}
