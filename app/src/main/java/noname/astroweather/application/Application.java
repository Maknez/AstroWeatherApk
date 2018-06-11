package noname.astroweather.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

import noname.astroweather.R;
import noname.astroweather.adapters.viewpageradapters.ScreenSlidePagerAdapterAllFragments;
import noname.astroweather.adapters.viewpageradapters.ScreenSlidePagerAdapterSunMoon;
import noname.astroweather.adapters.viewpageradapters.ScreenSlidePagerAdapterWeather;
import noname.astroweather.weather.BasicInfo;
import noname.astroweather.weather.UnitsChanger;
import noname.astroweather.weather.data.Channel;
import noname.astroweather.weather.data.Item;
import noname.astroweather.weather.data.YahooWeatherService;
import noname.astroweather.weather.data.interfaces.WeatherServiceCallback;


public class Application extends AppCompatActivity implements WeatherServiceCallback {

    private TextView clockView;
    private TextView longAndLatiView;
    private ViewPager mPagerSunMoon;
    private ViewPager mPagerWeather;
    private ViewPager mPagerAllFragments;
    private PagerAdapter mPagerAdapterSunMoon;
    private PagerAdapter mPagerAdapterWeather;
    private PagerAdapter mPagerAdapterAllFragments;
    private Thread myThread;
    private Clock clock;


    private YahooWeatherService service;
    private SharedPreferences sharedPreferences;
    private SharedPreferences offlineDataSharedPreferences;
    private SharedPreferences.Editor editor;


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
                        clockView.setText(curTime);

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
        if (config.orientation == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLandscape(Configuration config) {
        if (config.orientation == 2) {
            return true;
        } else {
            return false;
        }
    }

    public void showLongAndLati() {
        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
        longAndLatiView.setText(
                sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude))) +
                        " , " +
                        sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))));
    }

    @Override
    public void onBackPressed() {/*
        Configuration config = getResources().getConfiguration();
        if (checkSize(config) && config.orientation == 2) {
            if (mPagerSunMoon.getCurrentItem() == 0) {
                System.exit(1);
            } else {
                mPagerSunMoon.setCurrentItem(mPagerSunMoon.getCurrentItem() - 1);
            }
        } else {
            super.onBackPressed();
        }
*/
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
                service.refreshWeather();
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
        clock = new ClockActivity(this);
        Runnable myRunnableThread = clock;
        myThread = new Thread(myRunnableThread);
        myThread.start();
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
        longAndLatiView = (TextView) findViewById(R.id.longAndLati);
    }

    private void initClockView() {
        clockView = (TextView) findViewById(R.id.clockOnScreen);
    }

    private void initViewPagerSunMoon() {
        mPagerSunMoon = (ViewPager) findViewById(R.id.viewPagerSunMoon);
        mPagerAdapterSunMoon = new ScreenSlidePagerAdapterSunMoon(getSupportFragmentManager());
        mPagerSunMoon.setAdapter(mPagerAdapterSunMoon);
    }

    private void initViewPagerAllFragments() {
        mPagerAllFragments = (ViewPager) findViewById(R.id.viewPagerAllFragments);
        mPagerAdapterAllFragments = new ScreenSlidePagerAdapterAllFragments(getSupportFragmentManager());
        mPagerAllFragments.setAdapter(mPagerAdapterAllFragments);
    }

    private void initViewPagerWeather() {
        mPagerWeather = (ViewPager) findViewById(R.id.viewPagerWeather);
        mPagerAdapterWeather = new ScreenSlidePagerAdapterWeather(getSupportFragmentManager());
        mPagerWeather.setAdapter(mPagerAdapterWeather);
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences("config.xml", 0);
        offlineDataSharedPreferences = getSharedPreferences("offline_data.xml", 0);
        editor = offlineDataSharedPreferences.edit();
    }

    private void initYahooWeatherService() {
        service = new YahooWeatherService(this, sharedPreferences, sharedPreferences.getInt("Custom_Option_To_Refresh_Weather", getResources().getInteger(R.integer.Default_Option_To_Refresh_Weather)));
        service.refreshWeather();
    }

    @Override
    public void serviceSuccess(Channel channel) {
        UnitsChanger unitsChanger = new UnitsChanger();
        Item item = channel.getItem();

        saveBasicInfoDataFromYahooWeatherService(channel, unitsChanger, item);
        saveWeatherForecastDataFromYahooWeatherService(channel, unitsChanger);
        saveWindAndHumidityDataFromYahooWeatherService(channel, unitsChanger);

        refreshFragmentsData();
    }

    private void refreshFragmentsData() {
        Configuration config = getResources().getConfiguration();
        if (!checkSize(config)) {
            if (isLandscape(config)) {
                mPagerAdapterAllFragments.notifyDataSetChanged();

            } else if (isPortrait(config)) {
                mPagerAdapterWeather.notifyDataSetChanged();
                mPagerAdapterSunMoon.notifyDataSetChanged();
            }
        }
    }

    private void saveWindAndHumidityDataFromYahooWeatherService(Channel channel, UnitsChanger unitsChanger) {
        double windPowerInMPH = channel.getWind().getSpeed();
        double windPowerInKMPH = unitsChanger.milesPerHourTokilometersPerHour(windPowerInMPH);

        editor.putString("windPowerInMPHOffline", String.valueOf(windPowerInMPH));
        editor.putString("windPowerInKMPHOffline", String.valueOf(windPowerInKMPH));
        editor.putString("windWayOffline", String.valueOf(channel.getWind().getDirection()));
        editor.putString("humidityOffline", String.valueOf(channel.getAtmosphere().getHumidity()));
        editor.putString("visibilityOffline", String.valueOf(channel.getAtmosphere().getVisibility()));
        editor.commit();
    }

    private void saveWeatherForecastDataFromYahooWeatherService(Channel channel, UnitsChanger unitsChanger) {
        for (int i = 0; i < 5; i++) {
            int temperatureHighInFarenheit = channel.getItem().getForecast(i + 1).getTemperatureHigh();
            int temperatureLowInFarenheit = channel.getItem().getForecast(i + 1).getTemperatureLow();
            int temperatureHighInCelsius = unitsChanger.fahrenheitToCelsius(temperatureHighInFarenheit);
            int temperatureLowInCelsius = unitsChanger.fahrenheitToCelsius(temperatureLowInFarenheit);

            editor.putInt("resourceIDOffline" + String.valueOf(i), getResourceID(channel, i + 1));
            editor.putInt("temperatureLowInFarenheitOffline" + String.valueOf(i), temperatureLowInFarenheit);
            editor.putInt("temperatureHighInFarenheitOffline" + String.valueOf(i), temperatureHighInFarenheit);
            editor.putInt("temperatureLowInCelsiusOffline" + String.valueOf(i), temperatureLowInCelsius);
            editor.putInt("temperatureHighInCelsiusOffline" + String.valueOf(i), temperatureHighInCelsius);
            editor.putString("descriptionOffline" + String.valueOf(i), channel.getItem().getForecast(i + 1).getDay());
            editor.commit();
        }
    }

    private void saveBasicInfoDataFromYahooWeatherService(Channel channel, UnitsChanger unitsChanger, Item item) {
        int resourceID = getResources().getIdentifier("weather_icon_" + channel.getItem().getCondition().getCode(), "drawable", getPackageName());
        int temperatureInFarenheit = item.getCondition().getTemperature();
        int temperatureInCelsius = unitsChanger.fahrenheitToCelsius(temperatureInFarenheit);
        editor.putInt("resourceIDOffline", resourceID);
        editor.putString("cityOffline", channel.getLocation().getCity());
        editor.putString("countryOffline", channel.getLocation().getCountry());
        editor.putInt("temperatureInFarenheitOffline", temperatureInFarenheit);
        editor.putInt("temperatureInCelsiusOffline", temperatureInCelsius);
        editor.putString("descriptionOffline", item.getCondition().getDescription());
        editor.putString("airPressureOffline", channel.getAtmosphere().getPressure().toString());
        editor.commit();
    }

    private int getResourceID(Channel channel, int currentDay) {
        return getResources().getIdentifier("weather_icon_" + channel.getItem().getForecast(currentDay).getCode(), "drawable", getPackageName());
    }

    @Override
    public void serviceFailure(Exception ex) {
    }

    @Override
    public void onStop() {
        myThread.interrupt();
        super.onStop();
    }

    @Override
    public void onRestart() {
        showLongAndLati();
        service.refreshWeather();
        super.onRestart();
    }
}