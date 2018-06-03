package noname.astroweather.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import noname.astroweather.R;
import noname.astroweather.adapters.TemperatureUnitsSpinnerAdapter;
import noname.astroweather.adapters.WindSpeedUnitsSpinnerAdapter;
import noname.astroweather.data.Channel;
import noname.astroweather.data.Item;
import noname.astroweather.data.WeatherServiceCallback;
import noname.astroweather.data.YahooWeatherService;

public class Settings extends AppCompatActivity implements WeatherServiceCallback {

    TextView editLongitude;
    TextView editLatitude;
    TextView editRefresh;
    TextView editCity;
    TextView editCountry;

    Button saveValues;
    Button setDefaultValues;

    Spinner temperatureSpinner;
    Spinner windSpeedSpinner;
    private int temperatureUnit;
    private int windUnit;
    private int option;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

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

        initializeSettingsObjects();

        if (savedInstanceState != null) {
            editLongitude.setText(savedInstanceState.getString("savedLongitude"));
            editLatitude.setText(savedInstanceState.getString("savedLatitude"));
            editRefresh.setText(savedInstanceState.getString("savedRefresh"));
        }

        getSettingsValues();

        saveValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCityPossibleToSave() && isCountryPossibleToSave()) {
                    saveCity();
                    saveCountry();
                    setOptionToRefreshWeather(0);
                    setLocalizationFromYahooWeatherService();
                    if (isRefreshPossibleToSave()) {
                        saveRefresh();
                        Toast.makeText(Settings.this, "Values save properly!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Settings.this, "Some values are not save!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else if (isLongitudePossibleToSave() && isLatitudePossibleToSave()) {
                    saveLongitude();
                    saveLatitude();
                    setOptionToRefreshWeather(1);
                    setLocalizationFromYahooWeatherService();
                    if (isRefreshPossibleToSave()) {
                        saveRefresh();
                        Toast.makeText(Settings.this, "Values save properly!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Settings.this, "Some values are not save!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        });

        setDefaultValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLongitude.setText(getResources().getString(R.string.Default_Longitude));
                editLatitude.setText(getResources().getString(R.string.Default_Latitude));
                editRefresh.setText(getResources().getString(R.string.Default_Refresh));
                temperatureSpinner.setSelection(getResources().getInteger(R.integer.Default_Temperature_Unit));
                windSpeedSpinner.setSelection(getResources().getInteger(R.integer.Default_Wind_Speed_Unit));
                editCity.setText(getResources().getString(R.string.Default_City));
                editCountry.setText(getResources().getString(R.string.Default_Country));

                saveDefaultValues();

                Toast.makeText(Settings.this, "Values set to default!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLocalizationFromYahooWeatherService() {

        YahooWeatherService service = new YahooWeatherService(this, sharedPref, option);
        service.refreshWeather();
    }

    private void setOptionToRefreshWeather(int opt) {
        this.option = opt;
        editor.putInt("Custom_Option_To_Refresh_Weather", opt);
        editor.commit();
    }

    public boolean isLatitudePossibleToSave() {
        String latitude = String.valueOf(editLatitude.getText());
        if (latitude.equals("")) {
        } else if (latitude.endsWith(".")) {
            latitude = latitude.substring(0, latitude.length() - 1);
            editLatitude.setText(latitude);
        }
        if (isNumeric(latitude)) {
            if ((Double.valueOf(latitude) <= 90) && (Double.valueOf(latitude) >= -90)) {
                return true;
            } else {
                Toast.makeText(Settings.this, "Bad latitude value (-90 : 90)!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Settings.this, "Bad latitude value format (-90 : 90)!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isLongitudePossibleToSave() {
        String longitude = String.valueOf(editLongitude.getText());
        if (longitude.equals("")) {
        } else if (longitude.endsWith(".")) {
            longitude = longitude.substring(0, longitude.length() - 1);
            editLongitude.setText(longitude);
        }
        if (isNumeric(longitude)) {
            if ((Double.valueOf(longitude) <= 180) && (Double.valueOf(longitude) >= -180)) {
                return true;
            } else {
                Toast.makeText(Settings.this, "Bad longitude value (-180 : 180)!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Settings.this, "Bad longitude value format (-180 : 180)!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isCityPossibleToSave() {
        String city = String.valueOf(editCity.getText());
        if (city.equals("")) {
        } else {
            return true;
        }
        return false;
    }

    public boolean isCountryPossibleToSave() {
        String country = String.valueOf(editCountry.getText());
        if (country.equals("")) {
        } else {
            return true;
        }
        return false;
    }

    public boolean isRefreshPossibleToSave() {
        String refresh = String.valueOf(editRefresh.getText());
        if (refresh.equals("")) {
            Toast.makeText(Settings.this, "You cannot save empty value!", Toast.LENGTH_SHORT).show();
        } else if (refresh.endsWith(".")) {
            refresh = refresh.substring(0, refresh.length() - 1);
            editRefresh.setText(refresh);
        } else if (isMinutes(refresh)) {
            if ((Integer.valueOf(refresh) <= 60) && (Integer.valueOf(refresh) >= 1)) {
                return true;
            } else {
                Toast.makeText(Settings.this, "Bad Refresh value in Minutes (1 : 60)!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Settings.this, "Bad Refresh value format in Minutes (1 : 60)!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initializeSettingsObjects() {
        initSharedPreferences();
        initTextViews();
        initButtons();
        initTemperatureSpinner();
        initWindSpeedSpinner();
    }

    private void initTextViews() {
        editLongitude = (TextView) findViewById(R.id.editLongitude);
        editLatitude = (TextView) findViewById(R.id.editLatitude);
        editRefresh = (TextView) findViewById(R.id.editRefresh);
        editCity = (TextView) findViewById(R.id.editCity);
        editCountry = (TextView) findViewById(R.id.editCountry);
    }

    private void initButtons() {
        saveValues = (Button) findViewById(R.id.saveValues);
        setDefaultValues = (Button) findViewById(R.id.setDefaultValues);
    }

    private void initSharedPreferences() {
        sharedPref = getSharedPreferences("config.xml", 0);
        editor = sharedPref.edit();
    }

    private void initTemperatureSpinner() {
        temperatureSpinner = (Spinner) findViewById(R.id.temperatureSpinner);
        TemperatureUnitsSpinnerAdapter temperatureUnitsSpinnerAdapter = new TemperatureUnitsSpinnerAdapter(this);
        temperatureSpinner.setAdapter(temperatureUnitsSpinnerAdapter);
        temperatureSpinner.setSelection(sharedPref.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit))));
        temperatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        temperatureUnit = 0;
                        break;
                    case 1:
                        temperatureUnit = 1;
                        break;
                }
                editor.putInt("Temperature_Unit", temperatureUnit);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initWindSpeedSpinner() {
        windSpeedSpinner = (Spinner) findViewById(R.id.windSpeedSpinner);
        WindSpeedUnitsSpinnerAdapter windSpeedUnitsSpinnerAdapter = new WindSpeedUnitsSpinnerAdapter(this);
        windSpeedSpinner.setAdapter(windSpeedUnitsSpinnerAdapter);
        windSpeedSpinner.setSelection(sharedPref.getInt("Wind_Speed_Unit", (getResources().getInteger(R.integer.Default_Wind_Speed_Unit))));
        windSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        windUnit = 0;
                        break;
                    case 1:
                        windUnit = 1;
                        break;
                }
                editor.putInt("Wind_Speed_Unit", windUnit);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSettingsValues() {
        getEditLongitude();
        getEditLatitude();
        getEditRefresh();
        getEditCity();
        getEditCountry();
        getTemperatureUnit();
        getWindSpeedUnit();
    }

    private void getWindSpeedUnit() {
        windSpeedSpinner.setSelection(getResources().getInteger(R.integer.Default_Wind_Speed_Unit));
    }

    private void getTemperatureUnit() {
        temperatureSpinner.setSelection(getResources().getInteger(R.integer.Default_Temperature_Unit));
    }

    private void getEditCity() {
        editCity.setText(sharedPref.getString("Custom_City", getResources().getString(R.string.Default_City)));
    }

    private void getEditCountry() {
        editCountry.setText(sharedPref.getString("Custom_Country", getResources().getString(R.string.Default_Country)));
    }

    public void getEditLongitude() {

        editLongitude.setText(sharedPref.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude))));
    }

    public void getEditLatitude() {

        editLatitude.setText(sharedPref.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))));
    }

    public void getEditRefresh() {
        editRefresh.setText(sharedPref.getString("Custom_Refresh", String.valueOf(getResources().getString(R.string.Default_Refresh))));
    }

    private void saveRefresh() {
        String refresh = String.valueOf(editRefresh.getText());
        editor.putString("Custom_Refresh", refresh);
        editor.commit();
    }

    private void saveLatitude() {
        String latitude = String.valueOf(editLatitude.getText());
        editor.putString("Custom_Latitude", latitude);
        editor.commit();
    }

    private void saveLongitude() {
        String longitude = String.valueOf(editLongitude.getText());
        editor.putString("Custom_Longitude", longitude);
        editor.commit();
    }

    private void saveCity() {
        String city = String.valueOf(editCity.getText());
        editor.putString("Custom_City", city);
        editor.commit();
    }

    private void saveCountry() {
        String country = String.valueOf(editCountry.getText());
        editor.putString("Custom_Country", country);
        editor.commit();
    }

    private void saveCustomValues() {
        saveLongitude();
        saveLatitude();
        saveCity();
        saveCountry();
        saveRefresh();
    }

    private void saveDefaultValues() {
        editor.putInt("Temperature_Unit", temperatureSpinner.getSelectedItemPosition());
        editor.putInt("Wind_Speed_Unit", windSpeedSpinner.getSelectedItemPosition());
        editor.putString("Custom_City", editCity.getText().toString());
        editor.putString("Custom_Country", editCountry.getText().toString());
        editor.putString("Custom_Longitude", editLongitude.getText().toString());
        editor.putString("Custom_Latitude", editLatitude.getText().toString());
        editor.putString("Custom_Refresh", editRefresh.getText().toString());
        editor.commit();
    }

    public static boolean isNumeric(String string) {
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }

    public static boolean isMinutes(String string) {
        return string.matches("^[1-9]\\d*$");
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


    @Override
    public void serviceSuccess(Channel channel) {
        Item item = channel.getItem();
        if (option == 0) {
            editLatitude.setText(String.valueOf(item.getLatitude()));
            editLongitude.setText(String.valueOf(item.getLongitude()));
            if (isLatitudePossibleToSave()) {
                saveLatitude();
            }
            if (isLongitudePossibleToSave()) {
                saveLongitude();
            }
        } else if (option == 1) {
            editCity.setText(channel.getLocation().getCity());
            editCountry.setText(channel.getLocation().getCountry());
            if (isCityPossibleToSave()) {
                saveCity();
            }
            if (isCountryPossibleToSave()) {
                saveCountry();
            }
        }
    }

    @Override
    public void serviceFailure(Exception ex) {
        Toast.makeText(Settings.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
