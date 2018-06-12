package noname.astroweather.application;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

import noname.astroweather.R;

public class Clock implements Runnable {
    private Activity activityOnThatClockIsRunning;
    private Date timeOfNextRefresh;

    public Clock(Activity activity) {
        activityOnThatClockIsRunning = activity;
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
        activityOnThatClockIsRunning.runOnUiThread(new Runnable() {
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
        if ((timeOfNextRefresh.getHours() == hour) && (timeOfNextRefresh.getMinutes() == minute) && (timeOfNextRefresh.getSeconds() == second)) {
            setNewValue();
            showText();
            setNewRefreshTime();
        }
    }

    public void setNewRefreshTime() {
        SharedPreferences sharedPref = activityOnThatClockIsRunning.getSharedPreferences("config.xml", 0);
        timeOfNextRefresh = Calendar.getInstance().getTime();
        timeOfNextRefresh.setMinutes(timeOfNextRefresh.getMinutes() + Integer.parseInt(sharedPref.getString("Custom_Refresh", String.valueOf(activityOnThatClockIsRunning.getResources().getString(R.string.Default_Refresh)))));
    }

    public void showText() {
    }

    public void setNewValue() {
    }
}

