package noname.astroweather.core;

import android.app.Activity;
import android.content.SharedPreferences;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import noname.astroweather.R;

public class Clock implements Runnable {
    Activity thisActivity;
    Date nextRefreshTime;
    DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    String date;

    public Clock(Activity activity) {
        thisActivity = activity;
    }


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

    public void getClock() {

        thisActivity.runOnUiThread(new Runnable() {
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

    public void refresh(int hour, int minute, int second) {
        if ((nextRefreshTime.getHours() == hour) && (nextRefreshTime.getMinutes() == minute) && (nextRefreshTime.getSeconds() == second)) {
            setNewValue();
            showText();
            setNewRefreshTime();
        }
    }

    public void setNewRefreshTime() {
        SharedPreferences sharedPref = thisActivity.getSharedPreferences("config.xml", 0);
        nextRefreshTime = Calendar.getInstance().getTime();
        nextRefreshTime.setMinutes(nextRefreshTime.getMinutes() + Integer.parseInt(sharedPref.getString("Custom_Refresh", String.valueOf(thisActivity.getResources().getString(R.string.Default_Refresh)))));
    }

    public int getTimeZone() {
        SharedPreferences sharedPref = thisActivity.getSharedPreferences("config.xml", 0);
        double longitude = Double.valueOf(sharedPref.getString("Custom_Longitude", String.valueOf(thisActivity.getString(R.string.Default_Longitude))));
        if(longitude >= 15 || longitude <= -15) {
            return (int)Math.floor((longitude + 15) / 30);
        }
        else {
            return 0;
        }
    }

    public void showText() {
    }

    public void setNewValue() {
    }
}

