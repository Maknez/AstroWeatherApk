package noname.astroweather.adapters.viewpageradapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import noname.astroweather.astro.MoonFragment;
import noname.astroweather.astro.SunFragment;

public class ScreenSlidePagerAdapterSunMoon extends FragmentStatePagerAdapter {

    public static final int NUM_PAGES = 2;
    private Fragment[] fragments = {new SunFragment(), new MoonFragment()};

    public ScreenSlidePagerAdapterSunMoon(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
