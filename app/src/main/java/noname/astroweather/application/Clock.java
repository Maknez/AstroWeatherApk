package noname.astroweather.application;

import android.app.Activity;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import noname.astroweather.R;

public class Clock implements Runnable {
    private Activity thisActivity;
    private Date nextRefreshTime;

    public Clock(Activity activity) {
        thisActivity = activity;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                getClock();
                Thread.sleep(1000);
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

    private void refresh(int hour, int minute, int second) {
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
    
    public void showText() {
    }

    public void setNewValue() {
    }
}

