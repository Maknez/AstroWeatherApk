package noname.astroweather.adapters.viewpageradapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import noname.astroweather.astro.MoonFragment;
import noname.astroweather.astro.SunFragment;
import noname.astroweather.weather.BasicInfo;
import noname.astroweather.weather.WeatherForecast;
import noname.astroweather.weather.WindAndHumidity;

public class ScreenSlidePagerAdapterAllFragments extends FragmentStatePagerAdapter {

    private static final int NUMBER_OF_ALL_FRAGMENTS = 5;
    private Fragment[] fragments = {new BasicInfo(), new WeatherForecast(), new WindAndHumidity(), new SunFragment(), new MoonFragment()};

    public ScreenSlidePagerAdapterAllFragments(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return NUMBER_OF_ALL_FRAGMENTS;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}