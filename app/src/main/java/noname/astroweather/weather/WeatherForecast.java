package noname.astroweather.weather;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import noname.astroweather.R;
import noname.astroweather.data.Channel;
import noname.astroweather.data.WeatherServiceCallback;
import noname.astroweather.data.YahooWeatherService;

public class WeatherForecast extends Fragment implements WeatherServiceCallback {

    private static final int FORECAST_DAY_NUMBER = 5;
    private TextView[] weatherTextView = new TextView[5];
    private TextView[] temperatureHighTextView = new TextView[5];
    private TextView[] temperatureLowTextView = new TextView[5];
    private ImageView[] weatherImageView = new ImageView[5];
    private YahooWeatherService service;
    private ProgressDialog dialog;

    public WeatherForecast() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_weather_forecast, container, false);

        initImageViews(rootView);
        initTextViews(rootView);

        service = new YahooWeatherService(this);
        service.refreshWeather("Łódź, PL");
  /*      dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
*/

        return rootView;
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
        //dialog.hide();
        UnitsChanger unitsChanger = new UnitsChanger();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("config.xml", 0);
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
        }
    }

    private int getResourceID(Channel channel, int currentDay) {
        return getResources().getIdentifier("weather_icon_" + channel.getItem().getForecast(currentDay).getCode(), "drawable", getContext().getPackageName());
    }

    @Override
    public void serviceFailure(Exception ex) {
        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}