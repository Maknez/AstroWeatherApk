package noname.astroweather.weather;


import android.app.ProgressDialog;
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


    private static final int FORECAST_DAY_NUMBER = 6;
    private TextView weatherTextView1;
    private TextView weatherTextView2;
    private TextView weatherTextView3;
    private TextView weatherTextView4;
    private TextView weatherTextView5;

    private TextView temperatureHighTextView1;
    private TextView temperatureHighTextView2;
    private TextView temperatureHighTextView3;
    private TextView temperatureHighTextView4;
    private TextView temperatureHighTextView5;

    private TextView temperatureLowTextView1;
    private TextView temperatureLowTextView2;
    private TextView temperatureLowTextView3;
    private TextView temperatureLowTextView4;
    private TextView temperatureLowTextView5;

    private ImageView weatherImageView1;
    private ImageView weatherImageView2;
    private ImageView weatherImageView3;
    private ImageView weatherImageView4;
    private ImageView weatherImageView5;

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
        weatherTextView1 = (TextView) rootView.findViewById(R.id.weatherText1);
        weatherTextView2 = (TextView) rootView.findViewById(R.id.weatherText2);
        weatherTextView3 = (TextView) rootView.findViewById(R.id.weatherText3);
        weatherTextView4 = (TextView) rootView.findViewById(R.id.weatherText4);
        weatherTextView5 = (TextView) rootView.findViewById(R.id.weatherText5);

        temperatureHighTextView1 = (TextView) rootView.findViewById(R.id.temperatureHighText1);
        temperatureHighTextView2 = (TextView) rootView.findViewById(R.id.temperatureHighText2);
        temperatureHighTextView3 = (TextView) rootView.findViewById(R.id.temperatureHighText3);
        temperatureHighTextView4 = (TextView) rootView.findViewById(R.id.temperatureHighText4);
        temperatureHighTextView5 = (TextView) rootView.findViewById(R.id.temperatureHighText5);

        temperatureLowTextView1 = (TextView) rootView.findViewById(R.id.temperatureLowText1);
        temperatureLowTextView2 = (TextView) rootView.findViewById(R.id.temperatureLowText2);
        temperatureLowTextView3 = (TextView) rootView.findViewById(R.id.temperatureLowText3);
        temperatureLowTextView4 = (TextView) rootView.findViewById(R.id.temperatureLowText4);
        temperatureLowTextView5 = (TextView) rootView.findViewById(R.id.temperatureLowText5);
    }

    private void initImageViews(ViewGroup rootView) {
        weatherImageView1 = (ImageView) rootView.findViewById(R.id.weatherImage1);
        weatherImageView2 = (ImageView) rootView.findViewById(R.id.weatherImage2);
        weatherImageView3 = (ImageView) rootView.findViewById(R.id.weatherImage3);
        weatherImageView4 = (ImageView) rootView.findViewById(R.id.weatherImage4);
        weatherImageView5 = (ImageView) rootView.findViewById(R.id.weatherImage5);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        //dialog.hide();

        weatherImageView1.setImageResource(getResourceID(channel, 1));
        weatherImageView2.setImageResource(getResourceID(channel, 2));
        weatherImageView3.setImageResource(getResourceID(channel, 3));
        weatherImageView4.setImageResource(getResourceID(channel, 4));
        weatherImageView5.setImageResource(getResourceID(channel, 5));

        temperatureHighTextView1.setText(channel.getItem().getForecast(1).getTemperatureHigh() + " " + channel.getUnits().getTemperature());
        temperatureHighTextView2.setText(channel.getItem().getForecast(2).getTemperatureHigh() + " " + channel.getUnits().getTemperature());
        temperatureHighTextView3.setText(channel.getItem().getForecast(3).getTemperatureHigh() + " " + channel.getUnits().getTemperature());
        temperatureHighTextView4.setText(channel.getItem().getForecast(4).getTemperatureHigh() + " " + channel.getUnits().getTemperature());
        temperatureHighTextView5.setText(channel.getItem().getForecast(5).getTemperatureHigh() + " " + channel.getUnits().getTemperature());

        temperatureLowTextView1.setText(channel.getItem().getForecast(1).getTemperatureLow() + " " + channel.getUnits().getTemperature());
        temperatureLowTextView2.setText(channel.getItem().getForecast(2).getTemperatureLow() + " " + channel.getUnits().getTemperature());
        temperatureLowTextView3.setText(channel.getItem().getForecast(3).getTemperatureLow() + " " + channel.getUnits().getTemperature());
        temperatureLowTextView4.setText(channel.getItem().getForecast(4).getTemperatureLow() + " " + channel.getUnits().getTemperature());
        temperatureLowTextView5.setText(channel.getItem().getForecast(5).getTemperatureLow() + " " + channel.getUnits().getTemperature());

        weatherTextView1.setText(channel.getItem().getForecast(1).getDay());
        weatherTextView2.setText(channel.getItem().getForecast(2).getDay());
        weatherTextView3.setText(channel.getItem().getForecast(3).getDay());
        weatherTextView4.setText(channel.getItem().getForecast(4).getDay());
        weatherTextView5.setText(channel.getItem().getForecast(5).getDay());

    }

    private int getResourceID(Channel channel, int currentDay) {
        return getResources().getIdentifier("weather_icon_" + channel.getItem().getForecast(currentDay).getCode(), "drawable", getContext().getPackageName());
    }

    @Override
    public void serviceFailure(Exception ex) {
        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
