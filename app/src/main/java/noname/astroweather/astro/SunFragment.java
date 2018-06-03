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
    TextView mView;
    Thread myThread;
    Clock clock;
    AstroCore astroCore;

    private class ClockSunFragment extends Clock {

        private ClockSunFragment(Activity activity) {
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
            mView.setText(astroCore.getSunInfo());
            super.showText();
        }

    }

    @Override
    public void onStart() {
        astroCore = new AstroCore(getActivity());
        astroCore.astroInit();
        clock = new ClockSunFragment(getActivity());
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
                R.layout.fragment_sun, container, false);

        astroCore = new AstroCore(getActivity());
        astroCore.astroInit();

        clock = new ClockSunFragment(getActivity());
        mView = rootView.findViewById(R.id.sloneczko);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.sun_landscape);
        } else if (config.orientation == 1) {
            mView.setBackgroundResource(R.drawable.sun_portrait);
        }

        mView.setText(astroCore.getSunInfo());

        return rootView;
    }

    @Override
    public void onStop() {
        myThread.interrupt();
        super.onStop();
    }
}


