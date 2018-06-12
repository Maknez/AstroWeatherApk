package noname.astroweather.astro;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import noname.astroweather.application.Clock;
import noname.astroweather.R;

public class SunFragment extends Fragment {
    TextView sunFragmentDataTextView;
    Thread threadOnThatClockIsRunning;
    Clock clock;
    AstroCalculatorCore astroCalculatorCore;

    private class ClockSunFragment extends Clock {

        private ClockSunFragment(Activity activity) {
            super(activity);
        }

        @Override
        public void setNewValue() {
            astroCalculatorCore = new AstroCalculatorCore(getActivity());
            astroCalculatorCore.astroCalculatorCoreObjectsInitialize();
            super.setNewValue();
        }

        @Override
        public void showText() {
            sunFragmentDataTextView.setText(astroCalculatorCore.getSunInfo());
            super.showText();
        }
    }

    @Override
    public void onStart() {
        astroCalculatorCore = new AstroCalculatorCore(getActivity());
        astroCalculatorCore.astroCalculatorCoreObjectsInitialize();
        clock = new ClockSunFragment(getActivity());
        Runnable myRunnableThread = clock;
        clock.setNewRefreshTime();
        threadOnThatClockIsRunning = new Thread(myRunnableThread);
        threadOnThatClockIsRunning.start();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_sun, container, false);

        astroCalculatorCore = new AstroCalculatorCore(getActivity());
        astroCalculatorCore.astroCalculatorCoreObjectsInitialize();
        clock = new ClockSunFragment(getActivity());
        sunFragmentDataTextView = rootView.findViewById(R.id.sloneczko);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            sunFragmentDataTextView.setBackgroundResource(R.drawable.sun_landscape);
        } else if (config.orientation == 1) {
            sunFragmentDataTextView.setBackgroundResource(R.drawable.sun_portrait);
        }
        sunFragmentDataTextView.setText(astroCalculatorCore.getSunInfo());
        return rootView;
    }

    @Override
    public void onStop() {
        threadOnThatClockIsRunning.interrupt();
        super.onStop();
    }
}


