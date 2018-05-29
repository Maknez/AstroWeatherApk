package noname.astroweather.astro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import noname.astroweather.core.Clock;
import noname.astroweather.R;

public class MoonFragment extends Fragment {
    TextView mView;
    Thread myThread;
    Clock clock;
    AstroCore astroCore;

    class ClockMoonFragment extends Clock {

        private ClockMoonFragment(Activity activity) {
            super(activity);
        }

        @Override
        public void setNewValue() {
            astroCore = new AstroCore(getActivity());
            astroCore.astroInit();
            super.setNewValue();
        }

        @Override
        public void showText() {
            mView.setText(astroCore.getMoonInfo());
            super.showText();
        }
    }

    @Override
    public void onStart() {
        astroCore = new AstroCore(getActivity());
        astroCore.astroInit();
        clock = new MoonFragment.ClockMoonFragment(getActivity());
        Runnable myRunnableThread = clock;
        clock.setNewRefreshTime();
        myThread = new Thread(myRunnableThread);
        myThread.start();

        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_moon, container, false);

        astroCore = new AstroCore(getActivity());
        astroCore.astroInit();

        clock = new MoonFragment.ClockMoonFragment(getActivity());
        mView = rootView.findViewById(R.id.ksiezyczek);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.moon_landscape);
        } else if (config.orientation == 1) {
            mView.setBackgroundResource(R.drawable.moon_portrait);
        }

        mView.setText(astroCore.getMoonInfo());

        return rootView;
    }

    @Override
    public void onStop() {
        myThread.interrupt();
        super.onStop();
    }
}
