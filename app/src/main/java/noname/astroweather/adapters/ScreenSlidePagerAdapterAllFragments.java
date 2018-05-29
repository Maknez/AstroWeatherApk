package noname.astroweather.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import noname.astroweather.astro.MoonFragment;
import noname.astroweather.astro.SunFragment;
import noname.astroweather.weather.BasicInfo;
import noname.astroweather.weather.WeatherForecast;
import noname.astroweather.weather.WindAndHumidity;

public class ScreenSlidePagerAdapterAllFragments extends FragmentStatePagerAdapter {

    public static final int NUM_PAGES = 5;

    public ScreenSlidePagerAdapterAllFragments(FragmentManager fm) {
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
            case 3: {
                return new SunFragment();
            }
            case 4: {
                return new MoonFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}