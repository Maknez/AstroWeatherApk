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

public class MoonFragment extends Fragment {
    TextView moonFragmentDataTextView;
    Thread threadOnThatClockIsRunning;
    Clock clock;
    AstroCalculatorCore astroCalculatorCore;

    class ClockMoonFragment extends Clock {

        private ClockMoonFragment(Activity activity) {
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
            moonFragmentDataTextView.setText(astroCalculatorCore.getMoonInfo());
            super.showText();
        }
    }

    @Override
    public void onStart() {
        astroCalculatorCore = new AstroCalculatorCore(getActivity());
        astroCalculatorCore.astroCalculatorCoreObjectsInitialize();
        clock = new MoonFragment.ClockMoonFragment(getActivity());
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
                R.layout.fragment_moon, container, false);

        astroCalculatorCore = new AstroCalculatorCore(getActivity());
        astroCalculatorCore.astroCalculatorCoreObjectsInitialize();
        clock = new MoonFragment.ClockMoonFragment(getActivity());
        moonFragmentDataTextView = rootView.findViewById(R.id.ksiezyczek);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            moonFragmentDataTextView.setBackgroundResource(R.drawable.moon_landscape);
        } else if (config.orientation == 1) {
            moonFragmentDataTextView.setBackgroundResource(R.drawable.moon_portrait);
        }
        moonFragmentDataTextView.setText(astroCalculatorCore.getMoonInfo());
        return rootView;
    }

    @Override
    public void onStop() {
        threadOnThatClockIsRunning.interrupt();
        super.onStop();
    }
}
