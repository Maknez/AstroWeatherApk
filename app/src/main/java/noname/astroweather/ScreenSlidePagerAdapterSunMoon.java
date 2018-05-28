package noname.astroweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapterSunMoon extends FragmentStatePagerAdapter {

    public static final int NUM_PAGES = 2;

    public ScreenSlidePagerAdapterSunMoon(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: {
                return new SunFragment();
            }
            case 1: {
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
