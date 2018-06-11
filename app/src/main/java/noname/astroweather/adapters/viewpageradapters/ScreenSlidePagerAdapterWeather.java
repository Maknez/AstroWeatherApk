package noname.astroweather.adapters.viewpageradapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import noname.astroweather.weather.BasicInfo;
import noname.astroweather.weather.WeatherForecast;
import noname.astroweather.weather.WindAndHumidity;

public class ScreenSlidePagerAdapterWeather extends FragmentStatePagerAdapter {

    public static final int NUM_PAGES = 3;
    private Fragment[] fragments = {new BasicInfo(), new WeatherForecast(), new WindAndHumidity()};

    public ScreenSlidePagerAdapterWeather(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
