package noname.astroweather.weather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import noname.astroweather.R;
import noname.astroweather.weather.data.Channel;
import noname.astroweather.weather.data.Item;
import noname.astroweather.weather.data.interfaces.WeatherServiceCallback;
import noname.astroweather.weather.data.YahooWeatherService;

public class BasicInfo extends Fragment {

    TextView cityTextView;
    TextView countryTextView;
    TextView temperatureTextView;
    TextView airPressureTextView;
    TextView descriptionTextView;
    TextView offlineDataTextView;
    ImageView weatherImageView;

    SharedPreferences sharedPreferences;
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
                R.layout.fragment_basic_info, container, false);

        initTextViews(rootView);
        initImageView(rootView);
        initSharedPreferences();

        return rootView;
    }

    private void initSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences("config.xml", 0);
        offlineDataSharedPreferences = getActivity().getSharedPreferences("offline_data.xml", 0);
    }

    private void initImageView(ViewGroup rootView) {
        weatherImageView = (ImageView) rootView.findViewById(R.id.weatherImage);
    }

    private void initTextViews(ViewGroup rootView) {
        cityTextView = (TextView) rootView.findViewById(R.id.cityText);
        countryTextView = (TextView) rootView.findViewById(R.id.countryText);
        temperatureTextView = (TextView) rootView.findViewById(R.id.temperatureText);
        airPressureTextView = (TextView) rootView.findViewById(R.id.airPressureText);
        descriptionTextView = (TextView) rootView.findViewById(R.id.descriptionText);
        offlineDataTextView = (TextView) rootView.findViewById(R.id.offlineDataTextView);
    }

    private void showDataFromSharedPreferences() {
        weatherImageView.setImageResource(offlineDataSharedPreferences.getInt("resourceIDOffline", getResources().getIdentifier("weather_icon_" + 44, "drawable", getContext().getPackageName())));
        cityTextView.setText(offlineDataSharedPreferences.getString("cityOffline", "cityName"));
        countryTextView.setText(offlineDataSharedPreferences.getString("countryOffline", "countryName"));

        int temperatureInFarenheit = offlineDataSharedPreferences.getInt("temperatureInFarenheitOffline", 0);
        int temperatureInCelsius = offlineDataSharedPreferences.getInt("temperatureInCelsiusOffline", 0);
        int temperatureUnit = sharedPreferences.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit)));
        if (temperatureUnit == 0) {
            temperatureTextView.setText(temperatureInCelsius + " " + getResources().getString(R.string.temperature_unit_celsius));
        } else if (temperatureUnit == 1) {
            temperatureTextView.setText(temperatureInFarenheit + " " + getResources().getString(R.string.temperature_unit_farenheit));
        }
        descriptionTextView.setText(offlineDataSharedPreferences.getString("descriptionOffline", "description"));
        airPressureTextView.setText(offlineDataSharedPreferences.getString("airPressureOffline", "airPressure") + " in");
    }
}
