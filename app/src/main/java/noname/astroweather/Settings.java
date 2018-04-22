package noname.astroweather;

import android.content.Context;
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

import org.w3c.dom.Text;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static noname.astroweather.R.string.Custom_Longitude;

public class Settings extends AppCompatActivity {


    TextView editLongitude, editLatitude;
    Button saveLongitude, saveLatitude, setDefaultLongitude, setDefaultLatitude;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("savedLongitude", String.valueOf(editLongitude.getText()));
        outState.putString("savedLatitude", String.valueOf(editLatitude.getText()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editLongitude = (TextView) findViewById(R.id.editLongitude);
        editLatitude = (TextView) findViewById(R.id.editLatitude);
        saveLongitude = (Button) findViewById(R.id.saveLongitude);
        saveLatitude = (Button) findViewById(R.id.saveLatitude);
        setDefaultLongitude = (Button) findViewById(R.id.setDefaultLongitude);
        setDefaultLatitude = (Button) findViewById(R.id.setDefaultLatitude);

        if (savedInstanceState != null) {
            editLongitude.setText(savedInstanceState.getString("savedLongitude"));
            editLatitude.setText(savedInstanceState.getString("savedLatitude"));
        }

        getEditLongitude();
        getEditLatitude();

        saveLongitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String longitude = String.valueOf(editLongitude.getText());

                if (longitude.endsWith(".")) {
                    longitude = longitude.substring(0, longitude.length() - 1);
                    editLongitude.setText(longitude);
                }
                if (longitude.equals("")) {
                    Toast.makeText(Settings.this, "You cannot save empty value!", Toast.LENGTH_LONG).show();
                } else if ((Double.valueOf(longitude) <= 180) && (Double.valueOf(longitude) >= 0)) {
                    Toast.makeText(Settings.this, "Your Longitude is saved!", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Custom_Longitude", longitude);
                    editor.commit();
                } else {
                    Toast.makeText(Settings.this, "Bad value (0 : 180)!", Toast.LENGTH_LONG).show();
                }
            }
        });

        saveLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = String.valueOf(editLatitude.getText());

                if (latitude.endsWith(".")) {
                    latitude = latitude.substring(0, latitude.length() - 1);
                    editLatitude.setText(latitude);
                }
                if (latitude.equals("")) {
                    Toast.makeText(Settings.this, "You cannot save empty value!", Toast.LENGTH_LONG).show();
                } else if ((Double.valueOf(latitude) <= 180) && (Double.valueOf(latitude) >= 0)) {
                    Toast.makeText(Settings.this, "Your Latitude is saved!", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Custom_Latitude", latitude);
                    editor.commit();
                } else {
                    Toast.makeText(Settings.this, "Bad value (0 : 180)!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setDefaultLongitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLongitude.setText(getResources().getString(R.string.Default_Longitude));
            }
        });


        setDefaultLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLatitude.setText(getResources().getString(R.string.Default_Latitude));
            }
        });
    }


    public void getEditLongitude() {
        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
        editLongitude.setText(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude))));
    }

    public void getEditLatitude() {
        SharedPreferences sharedPref = getSharedPreferences("config.xml", 0);
        editLatitude.setText(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))));
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
