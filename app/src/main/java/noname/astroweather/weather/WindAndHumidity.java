package noname.astroweather.weather;


import android.app.ProgressDialog;
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

    public WindAndHumidity() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_wind_and_humidity, container, false);

        initTextViews(rootView);

        service = new YahooWeatherService(this);
        service.refreshWeather("Łódź, PL");
        /*dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();
*/
        return rootView;
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

        windPowerTextView.setText(channel.getWind().getSpeed());
        windWayTextView.setText(channel.getWind().getDirection());
        humidityTextView.setText(channel.getAtmosphere().getHumidity());
        visibilityTextView.setText(channel.getAtmosphere().getVisibility());
    }

    @Override
    public void serviceFailure(Exception ex) {

    }
}
