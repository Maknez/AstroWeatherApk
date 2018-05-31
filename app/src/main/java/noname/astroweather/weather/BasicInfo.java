package noname.astroweather.weather;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import noname.astroweather.R;
import noname.astroweather.data.Channel;
import noname.astroweather.data.Item;
import noname.astroweather.data.WeatherServiceCallback;
import noname.astroweather.data.YahooWeatherService;


/**
 * A simple {@link Fragment} subclass.
 */
public class BasicInfo extends Fragment implements WeatherServiceCallback {

    TextView cityTextView;
    TextView countryTextView;
    TextView temperatureTextView;
    TextView airPressureTextView;
    TextView descriptionTextView;

    ImageView weatherImageView;

    private YahooWeatherService service;
    private ProgressDialog dialog;
    SharedPreferences sharedPreferences;

    public BasicInfo() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_basic_info, container, false);


        initTextViews(rootView);
        initImageView(rootView);
        initSharedPreferences();
        service = new YahooWeatherService(this, sharedPreferences);
        service.refreshWeather("Łódź, PL");
        /*dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();*/

        return rootView;
    }

    private void initSharedPreferences() {
    sharedPreferences = getActivity().getSharedPreferences("config.xml", 0);
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
    }

    @Override
    public void serviceSuccess(Channel channel) {
        //dialog.hide();
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
        airPressureTextView.setText(channel.getAtmosphere().getPressure().toString() + " hPa");
    }

    @Override
    public void serviceFailure(Exception ex) {
        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
