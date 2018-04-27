package noname.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    TextView editLongitude, editLatitude, editRefresh;
    Button saveLongitude, saveLatitude, saveRefresh, setDefaultLongitude, setDefaultLatitude, setDefaultRefresh;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("savedLongitude", String.valueOf(editLongitude.getText()));
        outState.putString("savedLatitude", String.valueOf(editLatitude.getText()));
        outState.putString("savedRefresh", String.valueOf(editRefresh.getText()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editLongitude = (TextView) findViewById(R.id.editLongitude);
        editLatitude = (TextView) findViewById(R.id.editLatitude);
        editRefresh = (TextView) findViewById(R.id.editRefresh);

        saveLongitude = (Button) findViewById(R.id.saveLongitude);
        saveLatitude = (Button) findViewById(R.id.saveLatitude);
        saveRefresh = (Button) findViewById(R.id.saveRefresh);

        setDefaultLongitude = (Button) findViewById(R.id.setDefaultLongitude);
        setDefaultLatitude = (Button) findViewById(R.id.setDefaultLatitude);
        setDefaultRefresh = (Button) findViewById(R.id.setDefaultRefresh);

        if (savedInstanceState != null) {
            editLongitude.setText(savedInstanceState.getString("savedLongitude"));
            editLatitude.setText(savedInstanceState.getString("savedLatitude"));
            editRefresh.setText(savedInstanceState.getString("savedRefresh"));
        }

        getEditLongitude();
        getEditLatitude();
        getEditRefresh();

        saveLongitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String longitude = String.valueOf(editLongitude.getText());
                if (longitude.equals("")) {
                    Toast.makeText(Settings.this, "You cannot save empty value!", Toast.LENGTH_SHORT).show();
                } else if (longitude.endsWith(".")) {
                    longitude = longitude.substring(0, longitude.length() - 1);
                    editLongitude.setText(longitude);
                } else if (isNumeric(longitude)) {
                    if ((Double.valueOf(longitude) <= 180) && (Double.valueOf(longitude) >= -180)) {
                        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Custom_Longitude", longitude);
                        editor.commit();
                        Toast.makeText(Settings.this, "Your longitude is saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Settings.this, "Bad value (-180 : 180)!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(Settings.this, "Bad value format (-180 : 180)!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = String.valueOf(editLatitude.getText());
                if (latitude.equals("")) {
                    Toast.makeText(Settings.this, "You cannot save empty value!", Toast.LENGTH_SHORT).show();
                } else if (latitude.endsWith(".")) {
                    latitude = latitude.substring(0, latitude.length() - 1);
                    editLatitude.setText(latitude);
                } else if (isNumeric(latitude)) {
                    if ((Double.valueOf(latitude) <= 90) && (Double.valueOf(latitude) >= -90)) {
                        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Custom_Latitude", latitude);
                        editor.commit();
                        Toast.makeText(Settings.this, "Your latitude is saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Settings.this, "Bad value (-90 : 90)!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(Settings.this, "Bad value format (-90 : 90)!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refresh = String.valueOf(editRefresh.getText());
                if (refresh.equals("")) {
                    Toast.makeText(Settings.this, "You cannot save empty value!", Toast.LENGTH_SHORT).show();
                } else if (refresh.endsWith(".")) {
                    refresh = refresh.substring(0, refresh.length() - 1);
                    editRefresh.setText(refresh);
                } else if (isMinutes(refresh)) {
                    if ((Integer.valueOf(refresh) <= 60) && (Integer.valueOf(refresh) >= 1)) {
                        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("Custom_Refresh", refresh);
                        editor.commit();
                        Toast.makeText(Settings.this, "Your Refresh is saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Settings.this, "Bad value in Minutes (1 : 60)!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(Settings.this, "Bad value format in Minutes (1 : 60)!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setDefaultLongitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLongitude.setText(getResources().getString(R.string.Default_Longitude));
                SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Custom_Longitude", editLongitude.getText().toString());
                editor.commit();
            }
        });


        setDefaultLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLatitude.setText(getResources().getString(R.string.Default_Latitude));
                SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Custom_Latitude", editLatitude.getText().toString());
                editor.commit();
            }
        });

        setDefaultRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRefresh.setText(getResources().getString(R.string.Default_Refresh));
                SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Custom_Refresh", editRefresh.getText().toString());
                editor.commit();
            }
        });
    }

    public static boolean isNumeric(String string) {
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }

    public static boolean isMinutes(String string) {
        return string.matches("^[1-9]\\d*$");
    }

    public void getEditLongitude() {
        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
        editLongitude.setText(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude))));
    }

    public void getEditLatitude() {
        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
        editLatitude.setText(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))));
    }

    public void getEditRefresh() {
        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
        editRefresh.setText(sharedPref.getString("Custom_Refresh", String.valueOf(getResources().getString(R.string.Default_Refresh))));
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
//                startActivity(new Intent(this, Settings.class));
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
