package noname.astroweather.application;

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
import noname.astroweather.adapters.spinneradapters.TemperatureUnitsSpinnerAdapter;
import noname.astroweather.adapters.spinneradapters.WindSpeedUnitsSpinnerAdapter;
import noname.astroweather.weather.data.Channel;
import noname.astroweather.weather.data.Item;
import noname.astroweather.weather.data.interfaces.WeatherServiceCallback;
import noname.astroweather.weather.data.YahooWeatherService;
import noname.astroweather.weather.database.DisplayLocalizationStorage;
import noname.astroweather.weather.database.DatabaseOperation;

public class Settings extends AppCompatActivity implements WeatherServiceCallback {

    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView refreshTextView;
    private TextView cityTextView;
    private TextView countryTextView;

    private Button addToDatabaseButton;
    private Button getFromDatabaseButton;
    private Button saveCustomValuesButton;
    private Button setDefaultValuesButton;

    private Spinner temperatureUnitSpinner;
    private Spinner windSpeedUnitSpinner;
    private int temperatureUnit;
    private int windSpeedUnit;
    private int customOptionToGetWeatherDataFromYahooWeatherAPI;
    private SharedPreferences sharedPreferencesWithCustomData;
    private SharedPreferences.Editor sharedPreferencesWithCustomDataEditorToSaveCustomValues;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("savedCity", String.valueOf(cityTextView.getText()));
        outState.putString("savedCountry", String.valueOf(countryTextView.getText()));
        outState.putString("savedLongitude", String.valueOf(longitudeTextView.getText()));
        outState.putString("savedLatitude", String.valueOf(latitudeTextView.getText()));
        outState.putString("savedRefresh", String.valueOf(refreshTextView.getText()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestart() {
        getSettingsValues();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeSettingsObjects();
        getSettingsValues();

        if (savedInstanceState != null) {
            cityTextView.setText(savedInstanceState.getString("savedCity"));
            countryTextView.setText(savedInstanceState.getString("savedCountry"));
            longitudeTextView.setText(savedInstanceState.getString("savedLongitude"));
            latitudeTextView.setText(savedInstanceState.getString("savedLatitude"));
            refreshTextView.setText(savedInstanceState.getString("savedRefresh"));
        }

        saveCustomValuesButton.setOnClickListener(new View.OnClickListener() {
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
                }
            }
        });

        setDefaultValuesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longitudeTextView.setText(getResources().getString(R.string.Default_Longitude));
                latitudeTextView.setText(getResources().getString(R.string.Default_Latitude));
                refreshTextView.setText(getResources().getString(R.string.Default_Refresh));
                temperatureUnitSpinner.setSelection(getResources().getInteger(R.integer.Default_Temperature_Unit));
                windSpeedUnitSpinner.setSelection(getResources().getInteger(R.integer.Default_Wind_Speed_Unit));
                cityTextView.setText(getResources().getString(R.string.Default_City));
                countryTextView.setText(getResources().getString(R.string.Default_Country));
                saveDefaultValues();
                Toast.makeText(Settings.this, "Values set to default!", Toast.LENGTH_SHORT).show();
            }
        });

        addToDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityTextView.getText().toString();
                String countryName = countryTextView.getText().toString();
                String latitude = latitudeTextView.getText().toString();
                String longitude = longitudeTextView.getText().toString();
                if (cityName.isEmpty() || countryName.isEmpty() || latitude.isEmpty() || longitude.isEmpty()) {
                    Toast.makeText(Settings.this, "Localization cannot be added to Database!", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseOperation dboperation = new DatabaseOperation(Settings.this);
                    String message = dboperation.putInformation(dboperation, cityName, countryName, latitude, longitude);
                    Toast.makeText(Settings.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        getFromDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, DisplayLocalizationStorage.class));
            }
        });

    }

    private void setLocalizationFromYahooWeatherService() {
        YahooWeatherService service = new YahooWeatherService(this, sharedPreferencesWithCustomData, customOptionToGetWeatherDataFromYahooWeatherAPI);
        service.refreshWeather();
    }

    private void setOptionToRefreshWeather(int newOptionValue) {
        customOptionToGetWeatherDataFromYahooWeatherAPI = newOptionValue;
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putInt("Custom_Option_To_Refresh_Weather", newOptionValue);
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
    }

    public boolean isLatitudePossibleToSave() {
        String latitude = String.valueOf(latitudeTextView.getText());
        if (!latitude.equals("")) {
            if (latitude.endsWith(".")) {
                latitude = latitude.substring(0, latitude.length() - 1);
                latitudeTextView.setText(latitude);
            }
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
        String longitude = String.valueOf(longitudeTextView.getText());
        if (!longitude.equals("")) {
            if (longitude.endsWith(".")) {
                longitude = longitude.substring(0, longitude.length() - 1);
                longitudeTextView.setText(longitude);
            }
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
        String city = String.valueOf(cityTextView.getText());
        return !city.equals("");
    }

    public boolean isCountryPossibleToSave() {
        String country = String.valueOf(countryTextView.getText());
        return !country.equals("");
    }

    public boolean isRefreshPossibleToSave() {
        String refresh = String.valueOf(refreshTextView.getText());
        if (refresh.equals("")) {
            Toast.makeText(Settings.this, "You cannot save empty value!", Toast.LENGTH_SHORT).show();
        } else if (refresh.endsWith(".")) {
            refresh = refresh.substring(0, refresh.length() - 1);
            refreshTextView.setText(refresh);
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
        longitudeTextView = findViewById(R.id.editLongitude);
        latitudeTextView = findViewById(R.id.editLatitude);
        refreshTextView = findViewById(R.id.editRefresh);
        cityTextView = findViewById(R.id.editCity);
        countryTextView = findViewById(R.id.editCountry);
    }

    private void initButtons() {
        saveCustomValuesButton = findViewById(R.id.saveValues);
        setDefaultValuesButton = findViewById(R.id.setDefaultValues);
        addToDatabaseButton = findViewById(R.id.addToDatabase);
        getFromDatabaseButton = findViewById(R.id.getFromDatabase);
    }

    private void initSharedPreferences() {
        sharedPreferencesWithCustomData = getSharedPreferences("config.xml", 0);
        sharedPreferencesWithCustomDataEditorToSaveCustomValues = sharedPreferencesWithCustomData.edit();
    }

    private void initTemperatureSpinner() {
        temperatureUnitSpinner = findViewById(R.id.temperatureSpinner);
        TemperatureUnitsSpinnerAdapter temperatureUnitsSpinnerAdapter = new TemperatureUnitsSpinnerAdapter(this);
        temperatureUnitSpinner.setAdapter(temperatureUnitsSpinnerAdapter);
        temperatureUnitSpinner.setSelection(sharedPreferencesWithCustomData.getInt("Temperature_Unit", (getResources().getInteger(R.integer.Default_Temperature_Unit))));
        temperatureUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                sharedPreferencesWithCustomDataEditorToSaveCustomValues.putInt("Temperature_Unit", temperatureUnit);
                sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initWindSpeedSpinner() {
        windSpeedUnitSpinner = findViewById(R.id.windSpeedSpinner);
        WindSpeedUnitsSpinnerAdapter windSpeedUnitsSpinnerAdapter = new WindSpeedUnitsSpinnerAdapter(this);
        windSpeedUnitSpinner.setAdapter(windSpeedUnitsSpinnerAdapter);
        windSpeedUnitSpinner.setSelection(sharedPreferencesWithCustomData.getInt("Wind_Speed_Unit", (getResources().getInteger(R.integer.Default_Wind_Speed_Unit))));
        windSpeedUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        windSpeedUnit = 0;
                        break;
                    case 1:
                        windSpeedUnit = 1;
                        break;
                }
                sharedPreferencesWithCustomDataEditorToSaveCustomValues.putInt("Wind_Speed_Unit", windSpeedUnit);
                sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
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
        windSpeedUnitSpinner.setSelection(sharedPreferencesWithCustomData.getInt("Wind_Speed_Unit", getResources().getInteger(R.integer.Default_Wind_Speed_Unit)));
    }

    private void getTemperatureUnit() {
        temperatureUnitSpinner.setSelection(sharedPreferencesWithCustomData.getInt("Temperature_Unit", getResources().getInteger(R.integer.Default_Temperature_Unit)));
    }

    private void getEditCity() {
        cityTextView.setText(sharedPreferencesWithCustomData.getString("Custom_City", getResources().getString(R.string.Default_City)));
    }

    private void getEditCountry() {
        countryTextView.setText(sharedPreferencesWithCustomData.getString("Custom_Country", getResources().getString(R.string.Default_Country)));
    }

    public void getEditLongitude() {

        longitudeTextView.setText(sharedPreferencesWithCustomData.getString("Custom_Longitude", String.valueOf(getResources().getString(R.string.Default_Longitude))));
    }

    public void getEditLatitude() {

        latitudeTextView.setText(sharedPreferencesWithCustomData.getString("Custom_Latitude", String.valueOf(getResources().getString(R.string.Default_Latitude))));
    }

    public void getEditRefresh() {
        refreshTextView.setText(sharedPreferencesWithCustomData.getString("Custom_Refresh", String.valueOf(getResources().getString(R.string.Default_Refresh))));
    }

    private void saveRefresh() {
        String refresh = String.valueOf(refreshTextView.getText());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Refresh", refresh);
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
    }

    private void saveLatitude() {
        String latitude = String.valueOf(latitudeTextView.getText());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Latitude", latitude);
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
    }

    private void saveLongitude() {
        String longitude = String.valueOf(longitudeTextView.getText());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Longitude", longitude);
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
    }

    private void saveCity() {
        String city = String.valueOf(cityTextView.getText());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_City", city);
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
    }

    private void saveCountry() {
        String country = String.valueOf(countryTextView.getText());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Country", country);
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
    }

    private void saveDefaultValues() {
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putInt("Temperature_Unit", temperatureUnitSpinner.getSelectedItemPosition());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putInt("Wind_Speed_Unit", windSpeedUnitSpinner.getSelectedItemPosition());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_City", cityTextView.getText().toString());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Country", countryTextView.getText().toString());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Longitude", longitudeTextView.getText().toString());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Latitude", latitudeTextView.getText().toString());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.putString("Custom_Refresh", refreshTextView.getText().toString());
        sharedPreferencesWithCustomDataEditorToSaveCustomValues.commit();
    }

    private static boolean isNumeric(String string) {
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }

    private static boolean isMinutes(String string) {
        return string.matches("^[1-9]\\d*$");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings: {
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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    public void serviceSuccess(Channel channel) {
        Item item = channel.getItem();
        if (customOptionToGetWeatherDataFromYahooWeatherAPI == 0) {
            latitudeTextView.setText(String.valueOf(item.getLongitude()));
            longitudeTextView.setText(String.valueOf(item.getLatitude()));
            if (isLatitudePossibleToSave()) {
                saveLatitude();
            }
            if (isLongitudePossibleToSave()) {
                saveLongitude();
            }
        } else if (customOptionToGetWeatherDataFromYahooWeatherAPI == 1) {
            cityTextView.setText(channel.getLocation().getCity());
            countryTextView.setText(channel.getLocation().getCountry());
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
