package noname.astroweather.weather;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import noname.astroweather.R;
import noname.astroweather.data.YahooWeatherService;
import noname.astroweather.data.Channel;
import noname.astroweather.data.Item;
import noname.astroweather.data.WeatherServiceCallback;
import noname.astroweather.data.YahooWeatherService;

public class WindAndHumidity extends Fragment implements WeatherServiceCallback {



    private TextView windPowerTextView;
    private TextView windWayTextView;
    private TextView humidityTextView;
    private TextView visibilityTextView;
    private YahooWeatherService service;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;

    public WindAndHumidity() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_wind_and_humidity, container, false);

        initTextViews(rootView);
        initSharedPreferences();
        service = new YahooWeatherService(this, sharedPreferences, sharedPreferences.getInt("Custom_Option_To_Refresh_Weather", getResources().getInteger(R.integer.Default_Option_To_Refresh_Weather)));
        service.refreshWeather();
        /*dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
*/
        return rootView;
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
        //dialog.hide();

        UnitsChanger unitsChanger = new UnitsChanger();

        int windPowerUnit = sharedPreferences.getInt("Wind_Speed_Unit", (getResources().getInteger(R.integer.Default_Wind_Speed_Unit)));
        double windPowerInMPH = channel.getWind().getSpeed();
        double windPowerInKMPH = unitsChanger.milesPerHourTokilometersPerHour(windPowerInMPH);
        if(windPowerUnit == 0) {
            windPowerTextView.setText(windPowerInMPH + " " + getResources().getString(R.string.wind_power_unit_mph));
        } else if(windPowerUnit == 1) {
            windPowerTextView.setText(windPowerInKMPH + " " + getResources().getString(R.string.wind_power_unit_kmph));
        }
        windWayTextView.setText(channel.getWind().getDirection());
        humidityTextView.setText(channel.getAtmosphere().getHumidity());
        visibilityTextView.setText(channel.getAtmosphere().getVisibility());
    }

    @Override
    public void serviceFailure(Exception ex) {

    }
}
