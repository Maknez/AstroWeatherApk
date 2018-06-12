package noname.astroweather.weather.database;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import noname.astroweather.R;

public class DisplayLocalizationStorage extends AppCompatActivity {

    private ListView cityCountryListView;
    private int singleLocalizationItemInListViewThatUserPressed;
    private LocalizationAdapter localizationAdapter;
    private DatabaseOperation dbOperation;
    private Cursor cursor;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("savedCurrentItem", singleLocalizationItemInListViewThatUserPressed);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_localization);

        if (savedInstanceState != null) {
            setPosition(savedInstanceState.getInt("savedCurrentItem"));
        }

        initListView();
        initializeLocalizationAdapter();
        initializeDatabaseOperation();
        initializeCursor();
        getLocalizationFromDatabase();
        setLocalizationAdapterToCityCountryListView();
        registerForContextMenu(cityCountryListView);

        cityCountryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setPosition(position);
                return false;
            }
        });
    }

    private void setLocalizationAdapterToCityCountryListView() {
        cityCountryListView.setAdapter(localizationAdapter);
    }

    private void setPosition(int position) {
        singleLocalizationItemInListViewThatUserPressed = position;
    }

    private void getLocalizationFromDatabase() {
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String city = cursor.getString(0);
            String country = cursor.getString(1);
            String latitude = cursor.getString(2);
            String longitude = cursor.getString(3);
            Localization localization = new Localization(city, country, latitude, longitude);
            localizationAdapter.add(localization);
        }
    }

    private void initializeCursor() {
        cursor = dbOperation.getInformation(dbOperation);
    }

    private void initializeDatabaseOperation() {
        dbOperation = new DatabaseOperation(DisplayLocalizationStorage.this);
    }

    private void initializeLocalizationAdapter() {
        localizationAdapter = new LocalizationAdapter(DisplayLocalizationStorage.this, R.layout.display_localization_row);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.single_localization_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setAsDefault:
                setAsDefaultValues();
                return true;
            case R.id.removeFromDatabase:
                removeLocalizationFromDatabase();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void removeLocalizationFromDatabase() {
        cursor.moveToPosition(singleLocalizationItemInListViewThatUserPressed + 1);
        dbOperation.removeLocalization(dbOperation, cursor.getString(0), cursor.getString(1));
        initializeLocalizationAdapter();
        initializeDatabaseOperation();
        initializeCursor();
        getLocalizationFromDatabase();
        setLocalizationAdapterToCityCountryListView();
        Toast.makeText(DisplayLocalizationStorage.this, "Localization properly removed from Database!", Toast.LENGTH_SHORT).show();
    }

    private void setAsDefaultValues() {
        cursor.moveToPosition(singleLocalizationItemInListViewThatUserPressed + 1);
        SharedPreferences sharedPreferences = getSharedPreferences("config.xml", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Custom_City", cursor.getString(0));
        editor.putString("Custom_Country", cursor.getString(1));
        editor.putString("Custom_Latitude", cursor.getString(2));
        editor.putString("Custom_Longitude", cursor.getString(3));
        editor.commit();
        Toast.makeText(DisplayLocalizationStorage.this, "City " + cursor.getString(0) + " set as default!", Toast.LENGTH_SHORT).show();
    }

    private void initListView() {
        cityCountryListView = findViewById(R.id.cityCountryListView);
    }
}
