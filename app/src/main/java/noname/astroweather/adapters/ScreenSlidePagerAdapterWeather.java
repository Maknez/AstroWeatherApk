package noname.astroweather.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import noname.astroweather.weather.BasicInfo;
import noname.astroweather.weather.WeatherForecast;
import noname.astroweather.weather.WindAndHumidity;

public class ScreenSlidePagerAdapterWeather extends FragmentStatePagerAdapter {

    public static final int NUM_PAGES = 3;

    public ScreenSlidePagerAdapterWeather(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return new BasicInfo();
            }
            case 1: {
                return new WeatherForecast();
            }
            case 2: {
                return new WindAndHumidity();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
