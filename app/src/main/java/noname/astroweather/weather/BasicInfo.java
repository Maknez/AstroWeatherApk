package noname.astroweather.weather;


import android.app.ProgressDialog;
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


    public BasicInfo() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_basic_info, container, false);


        initTextViews(rootView);
        initImageView(rootView);

        service = new YahooWeatherService(this);
        service.refreshWeather("Łódź, PL");
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        return rootView;
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
        dialog.hide();
        Item item = channel.getItem();
        
        int resourceID = getResources().getIdentifier("weather_icon_" + channel.getItem().getCondition().getCode(), "drawable", getContext().getPackageName());

        weatherImageView.setImageResource(resourceID);
        cityTextView.setText(service.getLocation());
        temperatureTextView.setText(item.getCondition().getTemperature() + "  " + channel.getUnits().getTemperature());
        descriptionTextView.setText(item.getCondition().getDescription());
    }

    @Override
    public void serviceFailure(Exception ex) {
        Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
