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

public class BasicInfo extends Fragment implements WeatherServiceCallback {

    TextView cityTextView;
    TextView countryTextView;
    TextView temperatureTextView;
    TextView airPressureTextView;
    TextView descriptionTextView;
    TextView offlineDataTextView;

    ImageView weatherImageView;

    private YahooWeatherService service;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences offlineDataSharedPreferences;
    SharedPreferences.Editor editor;
    public BasicInfo() {

    }

    @Override
    public void onStart() {
        refreshWeather();
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
        initProgressBar(rootView);
        initYahooWeatherService();
        setProgressBarVisibility(View.VISIBLE);
        return rootView;
    }

    private void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
    }

    private void initProgressBar(ViewGroup rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
    }

    private void refreshWeather() {
        service.refreshWeather();
    }

    private void initYahooWeatherService() {
        service = new YahooWeatherService(this, sharedPreferences, sharedPreferences.getInt("Custom_Option_To_Refresh_Weather", getResources().getInteger(R.integer.Default_Option_To_Refresh_Weather)));
    }

    private void initSharedPreferences() {
        sharedPreferences = getActivity().getSharedPreferences("config.xml", 0);
        offlineDataSharedPreferences = getActivity().getSharedPreferences("offline_data.xml", 0);
        editor = offlineDataSharedPreferences.edit();
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

    @Override
    public void serviceSuccess(Channel channel) {

        try {
            Item item = channel.getItem();

            UnitsChanger unitsChanger = new UnitsChanger();

            int resourceID = getResources().getIdentifier("weather_icon_" + channel.getItem().getCondition().getCode(), "drawable", getContext().getPackageName());

            weatherImageView.setImageResource(resourceID);
            cityTextView.setText(channel.getLocation().getCity());
            countryTextView.setText(channel.getLocation().getCountry());

            int temperatureInFarenheit = item.getCondition().getTemperature();
            int temperatureInCelsius = unitsChanger.fahrenheitToCelsius(temperatureInFarenheit);
            int temperatureUnit = sharedPreferences.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit)));
            if (temperatureUnit == 0) {
                temperatureTextView.setText(temperatureInCelsius + " " + getResources().getString(R.string.temperature_unit_celsius));
            } else if (temperatureUnit == 1) {
                temperatureTextView.setText(temperatureInFarenheit + " " + getResources().getString(R.string.temperature_unit_farenheit));
            }
            descriptionTextView.setText(item.getCondition().getDescription());
            airPressureTextView.setText(channel.getAtmosphere().getPressure().toString() + " in");

            editor.putInt("resourceIDOffline", resourceID);
            editor.putString("cityOffline", channel.getLocation().getCity());
            editor.putString("countryOffline", channel.getLocation().getCountry());
            editor.putInt("temperatureInFarenheitOffline", temperatureInFarenheit);
            editor.putInt("temperatureInCelsiusOffline", temperatureInCelsius);
            editor.putString("descriptionOffline", item.getCondition().getDescription());
            editor.putString("airPressureOffline", channel.getAtmosphere().getPressure().toString());
            editor.commit();

            setProgressBarVisibility(View.INVISIBLE);
        } catch(IllegalStateException ex) {
        }
    }

    @Override
    public void serviceFailure(Exception ex) {
        weatherImageView.setImageResource(offlineDataSharedPreferences.getInt("resourceIDOffline", 44));
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

        offlineDataTextView.setVisibility(View.VISIBLE);

        setProgressBarVisibility(View.INVISIBLE);

        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
