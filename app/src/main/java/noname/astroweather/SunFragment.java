package noname.astroweather;


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
import java.util.Date;
import java.util.TimeZone;

public class SunFragment extends Fragment {
    TextView mView;
    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    String date;
    Thread myThread;
    Date nextRefreshTime;
    AstroCalculator astroCalculator;


    @Override
    public void onStart() {
        setNewRefreshTime();
        Runnable myRunnableThread = new SunFragment.Clock();
        myThread = new Thread(myRunnableThread);
        myThread.start();
        super.onStart();
    }

    class Clock implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    getClock();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }

    public void getClock() {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    long analogClockTime = Calendar.getInstance().getTimeInMillis();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(analogClockTime);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    refresh(hour, minute, second);

                } catch (Exception e) {
                }
            }
        });
    }

    private void refresh(int hour, int minute, int second) {

        if ((nextRefreshTime.getHours() == hour) && (nextRefreshTime.getMinutes() == minute) && (nextRefreshTime.getSeconds() == second)) {
            //System.out.println("REFRESH TIME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
            date = df.format(Calendar.getInstance().getTime());
            showText(astroCalculator);
        }
        setNewRefreshTime();
    }

    private void setNewRefreshTime() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("config.xml", 0);
        nextRefreshTime = Calendar.getInstance().getTime();
        nextRefreshTime.setMinutes(nextRefreshTime.getMinutes() + Integer.parseInt(sharedPref.getString("Custom_Refresh", String.valueOf(getResources().getString(R.string.Default_Refresh)))));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_sun, container, false);

        mView = (TextView) rootView.findViewById(R.id.sloneczko);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == 2) {
            mView.setBackgroundResource(R.drawable.sun_landscape);
        } else if (config.orientation == 1) {
            //pionowe
            mView.setBackgroundResource(R.drawable.sun_portrait);
        }


        SharedPreferences sharedPref = getActivity().getSharedPreferences("config.xml", 0);


        date = df.format(Calendar.getInstance().getTime());
        TimeZone timeZone= TimeZone.getDefault();

        AstroDateTime astroDateTime = new AstroDateTime(
                Integer.valueOf(date.substring(0, date.indexOf("."))),
                Integer.valueOf(date.substring(date.indexOf(".") + 1, date.lastIndexOf("."))),
                Integer.valueOf(date.substring(date.lastIndexOf(".") + 1, date.indexOf(" "))),
                Integer.valueOf(date.substring(date.indexOf(" ") + 1, date.indexOf(":"))),
                Integer.valueOf(date.substring(date.indexOf(":") + 1, date.lastIndexOf(":"))),
                Integer.valueOf(date.substring(date.lastIndexOf(":") + 1, date.length())),
                (timeZone.getRawOffset() / 3600000),
                true);

        AstroCalculator.Location location = new AstroCalculator.Location(
                Double.valueOf(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude)))),
                Double.valueOf(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))))
        );

        astroCalculator = new AstroCalculator(astroDateTime, location);

        showText(astroCalculator);

        return rootView;
    }


    public void showText(AstroCalculator astroCalculator){
        mView.setText("Sunrise:\n" + astroCalculator.getSunInfo().getSunrise() +
                "\nSunset:\n" + astroCalculator.getSunInfo().getSunset() +
                "\nAzimuth rise:\n" + astroCalculator.getSunInfo().getAzimuthRise() +
                "\nAzimuth set:\n" +  + astroCalculator.getSunInfo().getAzimuthSet() +
                "\nTwilight morning:\n" + astroCalculator.getSunInfo().getTwilightMorning() +
                "\nTwilight evening:\n" + astroCalculator.getSunInfo().getTwilightEvening());
    }
    @Override
    public void onStop() {
        myThread.interrupt();
        super.onStop();
    }

}


