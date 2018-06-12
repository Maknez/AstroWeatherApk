package noname.astroweather.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import noname.astroweather.R;

public class WeatherForecast extends Fragment {

    private static final int FORECAST_DAY_NUMBER = 5;
    private TextView[] weatherTextView = new TextView[FORECAST_DAY_NUMBER];
    private TextView[] temperatureHighTextView = new TextView[FORECAST_DAY_NUMBER];
    private TextView[] temperatureLowTextView = new TextView[FORECAST_DAY_NUMBER];
    private ImageView[] weatherImageView = new ImageView[FORECAST_DAY_NUMBER];
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
                R.layout.fragment_weather_forecast, container, false);

        initializeImageViews(rootView);
        initializeTextViews(rootView);
        initializeSharedPreferences();
        return rootView;
    }

    private void initializeSharedPreferences() {
        sharedPreferencesWithCustomValues = getActivity().getSharedPreferences("config.xml", 0);
        offlineDataSharedPreferences = getActivity().getSharedPreferences("offline_data.xml", 0);
    }

    private void initializeTextViews(ViewGroup rootView) {
        for (int i = 0; i < FORECAST_DAY_NUMBER; i++) {
            weatherTextView[i] = rootView.findViewById(getResources().getIdentifier("weatherText" + (i + 1), "id", getContext().getPackageName()));
            temperatureHighTextView[i] = rootView.findViewById(getResources().getIdentifier("temperatureHighText" + (i + 1), "id", getContext().getPackageName()));
            temperatureLowTextView[i] = rootView.findViewById(getResources().getIdentifier("temperatureLowText" + (i + 1), "id", getContext().getPackageName()));
        }
    }

    private void initializeImageViews(ViewGroup rootView) {
        for (int i = 0; i < FORECAST_DAY_NUMBER; i++) {
            weatherImageView[i] = rootView.findViewById(getResources().getIdentifier("weatherImage" + (i + 1), "id", getContext().getPackageName()));
        }
    }

    private void showDataFromSharedPreferences() {
        int temperatureUnit = sharedPreferencesWithCustomValues.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit)));
        for (int i = 0; i < FORECAST_DAY_NUMBER; i++) {
            int temperatureHighInFarenheit = offlineDataSharedPreferences.getInt("temperatureHighInFarenheitOffline" + String.valueOf(i), 0);
            int temperatureLowInFarenheit = offlineDataSharedPreferences.getInt("temperatureLowInFarenheitOffline" + String.valueOf(i), 0);
            int temperatureHighInCelsius = offlineDataSharedPreferences.getInt("temperatureHighInCelsiusOffline" + String.valueOf(i), 0);
            int temperatureLowInCelsius = offlineDataSharedPreferences.getInt("temperatureLowInCelsiusOffline" + String.valueOf(i), 0);

            if (temperatureUnit == 0) {
                temperatureHighTextView[i].setText(String.valueOf(temperatureHighInCelsius) + " " + getResources().getString(R.string.temperature_unit_celsius));
                temperatureLowTextView[i].setText(String.valueOf(temperatureLowInCelsius) + " " + getResources().getString(R.string.temperature_unit_celsius));
            } else if (temperatureUnit == 1) {
                temperatureHighTextView[i].setText(String.valueOf(temperatureHighInFarenheit) + " " + getResources().getString(R.string.temperature_unit_farenheit));
                temperatureLowTextView[i].setText(String.valueOf(temperatureLowInFarenheit) + " " + getResources().getString(R.string.temperature_unit_farenheit));
            }
            weatherImageView[i].setImageResource(offlineDataSharedPreferences.getInt("resourceIDOffline" + String.valueOf(i), getResources().getIdentifier("weather_icon_" + 44, "drawable", getContext().getPackageName())));
            weatherTextView[i].setText(offlineDataSharedPreferences.getString("descriptionOffline" + String.valueOf(i), "0"));
        }
    }
}