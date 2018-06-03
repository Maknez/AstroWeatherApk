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
import noname.astroweather.data.YahooWeatherService;
import noname.astroweather.data.Channel;
import noname.astroweather.data.WeatherServiceCallback;

public class WindAndHumidity extends Fragment implements WeatherServiceCallback {

    private TextView windPowerTextView;
    private TextView windWayTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private YahooWeatherService service;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    public WindAndHumidity() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_wind_and_humidity, container, false);

        initTextViews(rootView);
        initSharedPreferences();
        initYahooWeatherService();
        initProgressBar(rootView);
        setProgressBarVisibility(View.VISIBLE);
        refreshWeather();

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
    }

    private void initTextViews(ViewGroup rootView) {
        windPowerTextView = (TextView) rootView.findViewById(R.id.windPowerText);
        windWayTextView = (TextView) rootView.findViewById(R.id.windWayText);
        humidityTextView = (TextView) rootView.findViewById(R.id.humidityText);
        visibilityTextView = (TextView) rootView.findViewById(R.id.visibilityText);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        UnitsChanger unitsChanger = new UnitsChanger();

        int windPowerUnit = sharedPreferences.getInt("Wind_Speed_Unit", (getResources().getInteger(R.integer.Default_Wind_Speed_Unit)));
        double windPowerInMPH = channel.getWind().getSpeed();
        double windPowerInKMPH = unitsChanger.milesPerHourTokilometersPerHour(windPowerInMPH);
        if (windPowerUnit == 0) {
            windPowerTextView.setText(windPowerInMPH + " " + getResources().getString(R.string.wind_power_unit_mph));
        } else if (windPowerUnit == 1) {
            windPowerTextView.setText(windPowerInKMPH + " " + getResources().getString(R.string.wind_power_unit_kmph));
        }
        windWayTextView.setText(channel.getWind().getDirection());
        humidityTextView.setText(channel.getAtmosphere().getHumidity());
        visibilityTextView.setText(channel.getAtmosphere().getVisibility());
        setProgressBarVisibility(View.INVISIBLE);
    }

    @Override
    public void serviceFailure(Exception ex) {
        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        setProgressBarVisibility(View.INVISIBLE);
    }
}
