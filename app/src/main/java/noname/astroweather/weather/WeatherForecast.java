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
import noname.astroweather.weather.data.interfaces.WeatherServiceCallback;
import noname.astroweather.weather.data.YahooWeatherService;

public class WeatherForecast extends Fragment implements WeatherServiceCallback {

    private static final int FORECAST_DAY_NUMBER = 5;
    private TextView[] weatherTextView = new TextView[5];
    private TextView[] temperatureHighTextView = new TextView[5];
    private TextView[] temperatureLowTextView = new TextView[5];
    private ImageView[] weatherImageView = new ImageView[5];
    private YahooWeatherService service;
    SharedPreferences sharedPreferences;
    SharedPreferences offlineDataSharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressBar progressBar;


    @Override
    public void onResume() {
        refreshWeather();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_weather_forecast, container, false);

        initImageViews(rootView);
        initTextViews(rootView);
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

    private void initTextViews(ViewGroup rootView) {
        for (int i = 0; i < FORECAST_DAY_NUMBER; i++) {
            weatherTextView[i] = (TextView) rootView.findViewById(getResources().getIdentifier("weatherText" + (i + 1), "id", getContext().getPackageName()));
            temperatureHighTextView[i] = (TextView) rootView.findViewById(getResources().getIdentifier("temperatureHighText" + (i + 1), "id", getContext().getPackageName()));
            temperatureLowTextView[i] = (TextView) rootView.findViewById(getResources().getIdentifier("temperatureLowText" + (i + 1), "id", getContext().getPackageName()));
        }
    }

    private void initImageViews(ViewGroup rootView) {
        for (int i = 0; i < FORECAST_DAY_NUMBER; i++) {
            weatherImageView[i] = (ImageView) rootView.findViewById(getResources().getIdentifier("weatherImage" + (i + 1), "id", getContext().getPackageName()));
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        try {
            UnitsChanger unitsChanger = new UnitsChanger();
            int temperatureUnit = sharedPreferences.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit)));
            for (int i = 0; i < FORECAST_DAY_NUMBER; i++) {
                weatherImageView[i].setImageResource(getResourceID(channel, i + 1));
                int temperatureHighInFarenheit = channel.getItem().getForecast(i + 1).getTemperatureHigh();
                int temperatureLowInFarenheit = channel.getItem().getForecast(i + 1).getTemperatureLow();
                int temperatureHighInCelsius = unitsChanger.fahrenheitToCelsius(temperatureHighInFarenheit);
                int temperatureLowInCelsius = unitsChanger.fahrenheitToCelsius(temperatureLowInFarenheit);
                if (temperatureUnit == 0) {
                    temperatureHighTextView[i].setText(temperatureHighInCelsius + " " + getResources().getString(R.string.temperature_unit_celsius));
                    temperatureLowTextView[i].setText(temperatureLowInCelsius + " " + getResources().getString(R.string.temperature_unit_celsius));
                } else if (temperatureUnit == 1) {
                    temperatureHighTextView[i].setText(temperatureHighInFarenheit + " " + getResources().getString(R.string.temperature_unit_farenheit));
                    temperatureLowTextView[i].setText(temperatureLowInFarenheit + " " + getResources().getString(R.string.temperature_unit_farenheit));
                }
                weatherTextView[i].setText(channel.getItem().getForecast(i + 1).getDay());

                editor.putInt("resourceIDOffline" + String.valueOf(i), getResourceID(channel, i + 1));
                editor.putInt("temperatureLowInFarenheitOffline" + String.valueOf(i), temperatureLowInFarenheit);
                editor.putInt("temperatureHighInFarenheitOffline" + String.valueOf(i), temperatureHighInFarenheit);
                editor.putInt("temperatureLowInCelsiusOffline" + String.valueOf(i), temperatureLowInCelsius);
                editor.putInt("temperatureHighInCelsiusOffline" + String.valueOf(i), temperatureHighInCelsius);
                editor.putString("descriptionOffline" + String.valueOf(i), channel.getItem().getForecast(i + 1).getDay());
                editor.commit();
            }



            setProgressBarVisibility(View.INVISIBLE);
        } catch (IllegalStateException ex) {
        }

    }

    private int getResourceID(Channel channel, int currentDay) {
        return getResources().getIdentifier("weather_icon_" + channel.getItem().getForecast(currentDay).getCode(), "drawable", getContext().getPackageName());
    }

    @Override
    public void serviceFailure(Exception ex) {

        int temperatureUnit = sharedPreferences.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit)));
        for (int i = 0; i < FORECAST_DAY_NUMBER; i++) {
            weatherImageView[i].setImageResource(offlineDataSharedPreferences.getInt("resourceIDOffline", getResources().getIdentifier("weather_icon_" + 44, "drawable", getContext().getPackageName())));

            if (temperatureUnit == 0) {
                temperatureHighTextView[i].setText(offlineDataSharedPreferences.getInt("temperatureHighInCelcius" + String.valueOf(i), 0));
                temperatureLowTextView[i].setText(offlineDataSharedPreferences.getInt("temperatureLowInCelcius" + String.valueOf(i), 0));
            } else if (temperatureUnit == 1) {
                temperatureHighTextView[i].setText(offlineDataSharedPreferences.getInt("temperatureHighInFarenheit" + String.valueOf(i), 0));
                temperatureLowTextView[i].setText(offlineDataSharedPreferences.getInt("temperatureLowInFarenheit" + String.valueOf(i), 0));
            }
            weatherTextView[i].setText(offlineDataSharedPreferences.getInt("descriptionOffline" + String.valueOf(i), 0));
        }
        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        setProgressBarVisibility(View.INVISIBLE);
    }
}