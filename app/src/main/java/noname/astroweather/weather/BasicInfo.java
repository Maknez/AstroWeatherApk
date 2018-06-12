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

public class BasicInfo extends Fragment {

    private TextView cityTextView;
    private TextView countryTextView;
    private TextView temperatureTextView;
    private TextView airPressureTextView;
    private TextView descriptionTextView;
    private ImageView weatherImageView;
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
                R.layout.fragment_basic_info, container, false);

        initializeTextViews(rootView);
        initializeImageView(rootView);
        initializeSharedPreferences();
        return rootView;
    }

    private void initializeSharedPreferences() {
        sharedPreferencesWithCustomValues = getActivity().getSharedPreferences("config.xml", 0);
        offlineDataSharedPreferences = getActivity().getSharedPreferences("offline_data.xml", 0);
    }

    private void initializeImageView(ViewGroup rootView) {
        weatherImageView = rootView.findViewById(R.id.weatherImage);
    }

    private void initializeTextViews(ViewGroup rootView) {
        cityTextView = rootView.findViewById(R.id.cityText);
        countryTextView = rootView.findViewById(R.id.countryText);
        temperatureTextView = rootView.findViewById(R.id.temperatureText);
        airPressureTextView = rootView.findViewById(R.id.airPressureText);
        descriptionTextView = rootView.findViewById(R.id.descriptionText);
    }

    private void showDataFromSharedPreferences() {
        int temperatureInFarenheit = offlineDataSharedPreferences.getInt("temperatureInFarenheitOffline", 0);
        int temperatureInCelsius = offlineDataSharedPreferences.getInt("temperatureInCelsiusOffline", 0);
        int temperatureUnit = sharedPreferencesWithCustomValues.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit)));

        if (temperatureUnit == 0) {
            temperatureTextView.setText(temperatureInCelsius + " " + getResources().getString(R.string.temperature_unit_celsius));
        } else if (temperatureUnit == 1) {
            temperatureTextView.setText(temperatureInFarenheit + " " + getResources().getString(R.string.temperature_unit_farenheit));
        }

        weatherImageView.setImageResource(offlineDataSharedPreferences.getInt("resourceIDOffline", getResources().getIdentifier("weather_icon_" + 44, "drawable", getContext().getPackageName())));
        cityTextView.setText(offlineDataSharedPreferences.getString("cityOffline", "cityName"));
        countryTextView.setText(offlineDataSharedPreferences.getString("countryOffline", "countryName"));
        descriptionTextView.setText(offlineDataSharedPreferences.getString("descriptionOffline", "description"));
        airPressureTextView.setText(offlineDataSharedPreferences.getString("airPressureOffline", "airPressure") + " in");
    }
}
