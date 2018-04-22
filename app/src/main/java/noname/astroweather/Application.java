package noname.astroweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Calendar;



public class Application extends AppCompatActivity {

    TextView clockView;
    ViewPager mPager;
    PagerAdapter mPagerAdapter;


    public boolean checkSize(Configuration config) {
        boolean xlarge = ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putInt("currentFragment", mPager.getCurrentItem());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (checkSize(config)) {
            if (config.orientation == 2) {
                setContentView(R.layout.activity_application_landscape_tablet);
            } else if (config.orientation == 1) {
                setContentView(R.layout.activity_application_portrait_tablet);
            }
        } else {
            if (config.orientation == 2) {
                setContentView(R.layout.activity_application_landscape_phone);
            } else if (config.orientation == 1) {
                setContentView(R.layout.activity_application_portrait_phone);
                mPager = (ViewPager) findViewById(R.id.viewPager);
                mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                mPager.setAdapter(mPagerAdapter);
            }
        }
        clockView = (TextView) findViewById(R.id.clockOnScreen);

        Thread myThread;
        Runnable myRunnableThread = new Clock();
        myThread = new Thread(myRunnableThread);
        myThread.start();




    }

    public void getClock() {

        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    String curTime;
                    long analogClockTime = Calendar.getInstance().getTimeInMillis();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(analogClockTime);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    if (hour < 10) {
                        curTime = ("0" + hour) + ":";
                    } else {
                        curTime = hour + ":";
                    }
                    if (minute < 10) {
                        curTime = curTime + ("0" + minute) + ":";
                    } else {
                        curTime = curTime + minute + ":";
                    }
                    if (second < 10) {
                        curTime = curTime + ("0" + second);
                    } else {
                        curTime = curTime + second;
                    }

                    clockView.setText(curTime);

                } catch (Exception e) {
                }
            }
        });
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


    @Override
    public void onBackPressed() {

        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
                startActivity(new Intent(this, Settings.class));
                return true;
            }

            case R.id.about: {
                startActivity(new Intent(this, About.class));
                return true;
            }

            case R.id.exit: {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}