package noname.astroweather.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import noname.astroweather.R;
import noname.astroweather.adapters.viewpageradapters.ScreenSlidePagerAdapterAllFragments;
import noname.astroweather.adapters.viewpageradapters.ScreenSlidePagerAdapterSunMoon;
import noname.astroweather.adapters.viewpageradapters.ScreenSlidePagerAdapterWeather;
import noname.astroweather.weather.UnitsChanger;
import noname.astroweather.weather.data.Channel;
import noname.astroweather.weather.data.Item;
import noname.astroweather.weather.data.YahooWeatherService;
import noname.astroweather.weather.data.interfaces.WeatherServiceCallback;

public class Application extends AppCompatActivity implements WeatherServiceCallback {

    private TextView clockTextView;
    private TextView longitudeAndLatitudeTextView;
    private PagerAdapter sunMoonPagerAdapter;
    private PagerAdapter weatherPagerAdapter;
    private PagerAdapter allFragmentsPagerAdapter;
    private Thread clockThread;
    private YahooWeatherService yahooWeatherService;
    private SharedPreferences sharedPreferencesWithCustomData;
    private SharedPreferences.Editor offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService;

    private class ClockActivity extends Clock {

        private ClockActivity(Activity activity) {
            super(activity);
        }

        @Override
        public void getClock() {
            runOnUiThread(new Runnable() {
                public void run() {
                    try {

                        String curTime;
                        long analogClockTime = Calendar.getInstance().getTimeInMillis();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(analogClockTime);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);

                        if (hour < 10) {
                            curTime = ("0" + hour) + ":";
                        } else {
                            curTime = hour + ":";
                        }
                        if (minute < 10) {
                            curTime = curTime + ("0" + minute) + ":";
                        } else {
                            curTime = curTime + minute + ":";
                        }
                        if (second < 10) {
                            curTime = curTime + ("0" + second);
                        } else {
                            curTime = curTime + second;
                        }
                        clockTextView.setText(curTime);

                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public boolean checkSize(Configuration config) {
        boolean xlarge = ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public boolean isPortrait(Configuration config) {
        return config.orientation == 1;
    }

    public boolean isLandscape(Configuration config) {
        return config.orientation == 2;
    }

    public void showLongAndLati() {
        longitudeAndLatitudeTextView.setText(
                sharedPreferencesWithCustomData.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude))) +
                        " , " +
                        sharedPreferencesWithCustomData.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))));
    }

    @Override
    public void onBackPressed() {
        System.exit(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync: {
                yahooWeatherService.refreshWeather();
                refreshFragmentsData();
                Toast.makeText(Application.this, "Data refreshed!", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.settings: {
                startActivity(new Intent(this, Settings.class));
                return true;
            }
            case R.id.about: {
                startActivity(new Intent(this, About.class));
                return true;
            }
            case R.id.exit: {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        Clock clock = new ClockActivity(this);
        Runnable myRunnableThread = clock;
        clockThread = new Thread(myRunnableThread);
        clockThread.start();
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();
        initClockView();
        initLongAndLatiView();
        initSharedPreferences();
        initYahooWeatherService();
        showLongAndLati();
    }

    private void setLayout() {
        Configuration config = getResources().getConfiguration();
        if (checkSize(config)) {
            setContentView(R.layout.activity_application_tablet);
        } else {
            setContentView(R.layout.activity_application_phone);
            if (isLandscape(config)) {
                initViewPagerAllFragments();
            } else if (isPortrait(config)) {
                initViewPagerSunMoon();
                initViewPagerWeather();
            }
        }
    }

    private void initLongAndLatiView() {
        longitudeAndLatitudeTextView = findViewById(R.id.longAndLati);
    }

    private void initClockView() {
        clockTextView = findViewById(R.id.clockOnScreen);
    }

    private void initViewPagerSunMoon() {
        ViewPager mPagerSunMoon = findViewById(R.id.viewPagerSunMoon);
        sunMoonPagerAdapter = new ScreenSlidePagerAdapterSunMoon(getSupportFragmentManager());
        mPagerSunMoon.setAdapter(sunMoonPagerAdapter);
    }

    private void initViewPagerAllFragments() {
        ViewPager mPagerAllFragments = findViewById(R.id.viewPagerAllFragments);
        allFragmentsPagerAdapter = new ScreenSlidePagerAdapterAllFragments(getSupportFragmentManager());
        mPagerAllFragments.setAdapter(allFragmentsPagerAdapter);
    }

    private void initViewPagerWeather() {
        ViewPager mPagerWeather = findViewById(R.id.viewPagerWeather);
        weatherPagerAdapter = new ScreenSlidePagerAdapterWeather(getSupportFragmentManager());
        mPagerWeather.setAdapter(weatherPagerAdapter);
    }

    private void initSharedPreferences() {
        sharedPreferencesWithCustomData = getSharedPreferences("config.xml", 0);
        SharedPreferences offlineDataSharedPreferences = getSharedPreferences("offline_data.xml", 0);
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService = offlineDataSharedPreferences.edit();
    }

    private void initYahooWeatherService() {
        yahooWeatherService = new YahooWeatherService(this, sharedPreferencesWithCustomData, sharedPreferencesWithCustomData.getInt("Custom_Option_To_Refresh_Weather", getResources().getInteger(R.integer.Default_Option_To_Refresh_Weather)));
        yahooWeatherService.refreshWeather();
    }

    @Override
    public void serviceSuccess(Channel channel) {
        try {
            UnitsChanger unitsChanger = new UnitsChanger();
            Item item = channel.getItem();

            saveBasicInfoDataFromYahooWeatherService(channel, unitsChanger, item);
            saveWeatherForecastDataFromYahooWeatherService(channel, unitsChanger);
            saveWindAndHumidityDataFromYahooWeatherService(channel, unitsChanger);
            refreshFragmentsData();

        } catch (IllegalStateException ex) {
            Toast.makeText(Application.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void serviceFailure(Exception ex) {
        Toast.makeText(Application.this, "There is a problem with getting data from Yahoo Weather API! Try again later.", Toast.LENGTH_SHORT).show();
    }

    private void refreshFragmentsData() {
        Configuration config = getResources().getConfiguration();
        if (!checkSize(config)) {
            if (isLandscape(config)) {
                allFragmentsPagerAdapter.notifyDataSetChanged();
            } else if (isPortrait(config)) {
                weatherPagerAdapter.notifyDataSetChanged();
                sunMoonPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void saveWindAndHumidityDataFromYahooWeatherService(Channel channel, UnitsChanger unitsChanger) {
        double windPowerInMPH = channel.getWind().getSpeed();
        double windPowerInKMPH = unitsChanger.milesPerHourTokilometersPerHour(windPowerInMPH);

        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("windPowerInMPHOffline", String.valueOf(windPowerInMPH));
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("windPowerInKMPHOffline", String.valueOf(windPowerInKMPH));
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("windWayOffline", String.valueOf(channel.getWind().getDirection()));
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("humidityOffline", String.valueOf(channel.getAtmosphere().getHumidity()));
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("visibilityOffline", String.valueOf(channel.getAtmosphere().getVisibility()));
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.commit();
    }

    private void saveWeatherForecastDataFromYahooWeatherService(Channel channel, UnitsChanger unitsChanger) {
        for (int i = 0; i < 5; i++) {
            int temperatureHighInFarenheit = channel.getItem().getForecast(i + 1).getTemperatureHigh();
            int temperatureLowInFarenheit = channel.getItem().getForecast(i + 1).getTemperatureLow();
            int temperatureHighInCelsius = unitsChanger.fahrenheitToCelsius(temperatureHighInFarenheit);
            int temperatureLowInCelsius = unitsChanger.fahrenheitToCelsius(temperatureLowInFarenheit);

            offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("resourceIDOffline" + String.valueOf(i), getResourceID(channel, i + 1));
            offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("temperatureLowInFarenheitOffline" + String.valueOf(i), temperatureLowInFarenheit);
            offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("temperatureHighInFarenheitOffline" + String.valueOf(i), temperatureHighInFarenheit);
            offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("temperatureLowInCelsiusOffline" + String.valueOf(i), temperatureLowInCelsius);
            offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("temperatureHighInCelsiusOffline" + String.valueOf(i), temperatureHighInCelsius);
            offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("descriptionOffline" + String.valueOf(i), channel.getItem().getForecast(i + 1).getDay());
            offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.commit();
        }
    }

    private void saveBasicInfoDataFromYahooWeatherService(Channel channel, UnitsChanger unitsChanger, Item item) {
        int resourceID = getResources().getIdentifier("weather_icon_" + channel.getItem().getCondition().getCode(), "drawable", getPackageName());
        int temperatureInFarenheit = item.getCondition().getTemperature();
        int temperatureInCelsius = unitsChanger.fahrenheitToCelsius(temperatureInFarenheit);
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("resourceIDOffline", resourceID);
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("cityOffline", channel.getLocation().getCity());
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("countryOffline", channel.getLocation().getCountry());
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("temperatureInFarenheitOffline", temperatureInFarenheit);
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putInt("temperatureInCelsiusOffline", temperatureInCelsius);
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("descriptionOffline", item.getCondition().getDescription());
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.putString("airPressureOffline", channel.getAtmosphere().getPressure().toString());
        offlineSharedPreferencesEditorToSaveDataFromYahooWeatherService.commit();
    }

    private int getResourceID(Channel channel, int oneOfForecastWeatherDay) {
        return getResources().getIdentifier("weather_icon_" + channel.getItem().getForecast(oneOfForecastWeatherDay).getCode(), "drawable", getPackageName());
    }

    @Override
    public void onStop() {
        clockThread.interrupt();
        super.onStop();
    }

    @Override
    public void onRestart() {
        showLongAndLati();
        yahooWeatherService.refreshWeather();
        refreshFragmentsData();
        super.onRestart();
    }
}