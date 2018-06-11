package noname.astroweather.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import noname.astroweather.R;
import noname.astroweather.weather.data.YahooWeatherService;
import noname.astroweather.weather.data.Channel;
import noname.astroweather.weather.data.interfaces.WeatherServiceCallback;

public class WindAndHumidity extends Fragment {

    private TextView windPowerTextView;
    private TextView windWayTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private SharedPreferences sharedPreferences;
    SharedPreferences offlineDataSharedPreferences;

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

        initTextViews(rootView);
        initSharedPreferences();

        return rootView;
    }
    private void initSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences("config.xml", 0);
        offlineDataSharedPreferences = getActivity().getSharedPreferences("offline_data.xml", 0);
    }

    private void initTextViews(ViewGroup rootView) {
        windPowerTextView = (TextView) rootView.findViewById(R.id.windPowerText);
        windWayTextView = (TextView) rootView.findViewById(R.id.windWayText);
        humidityTextView = (TextView) rootView.findViewById(R.id.humidityText);
        visibilityTextView = (TextView) rootView.findViewById(R.id.visibilityText);
    }


    public void showDataFromSharedPreferences(){
        int windPowerUnit = sharedPreferences.getInt("Wind_Speed_Unit", (getResources().getInteger(R.integer.Default_Wind_Speed_Unit)));
        if (windPowerUnit == 0) {
            windPowerTextView.setText(offlineDataSharedPreferences.getString("windPowerInMPHOffline", "0") + " " + getResources().getString(R.string.wind_power_unit_mph));
        } else if (windPowerUnit == 1) {
            windPowerTextView.setText(offlineDataSharedPreferences.getString("windPowerInKMPHOffline", "0") + " " + getResources().getString(R.string.wind_power_unit_kmph));
        }
        windWayTextView.setText(offlineDataSharedPreferences.getString("windWayOffline", "WindWay"));
        humidityTextView.setText(offlineDataSharedPreferences.getString("humidityOffline", "Humidity"));
        visibilityTextView.setText(offlineDataSharedPreferences.getString("visibilityOffline", "Visibility"));
    }
}
