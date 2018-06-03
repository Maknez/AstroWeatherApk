package noname.astroweather.weather.database;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import noname.astroweather.R;

public class DisplayLocalization extends AppCompatActivity {

    ListView cityCountrylistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_localization);

        initListView();
        LocalizationAdapter localizationAdapter = new LocalizationAdapter(DisplayLocalization.this, R.layout.display_localization_row);
        DatabaseOperation dboperation = new DatabaseOperation(DisplayLocalization.this);
        Cursor cursor = dboperation.getInformation(dboperation);
        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            String city = cursor.getString(0);
            String country = cursor.getString(1);
            String latitude = cursor.getString(2);
            String longitude = cursor.getString(3);
            Localization localization = new Localization(city, country, latitude, longitude);
            localizationAdapter.add(localization);
        }

        cityCountrylistView.setAdapter(localizationAdapter);

    }

    private void initListView() {
        cityCountrylistView = (ListView) findViewById(R.id.cityCountryListView);
    }
}
