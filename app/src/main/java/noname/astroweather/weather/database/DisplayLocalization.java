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

public class DisplayLocalization extends AppCompatActivity {

    private ListView cityCountryListView;
    private int currentItem;
    private LocalizationAdapter localizationAdapter;
    private DatabaseOperation dbOperation;
    private Cursor cursor;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("savedCurrentItem", currentItem);
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
        initLocalizationAdapter();
        initDatabaseOperation();
        initCursor();
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
        currentItem = position;
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

    private void initCursor() {
        cursor = dbOperation.getInformation(dbOperation);
    }

    private void initDatabaseOperation() {
        dbOperation = new DatabaseOperation(DisplayLocalization.this);
    }

    private void initLocalizationAdapter() {
        localizationAdapter = new LocalizationAdapter(DisplayLocalization.this, R.layout.display_localization_row);
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
                setAsDefault();
                return true;
            case R.id.removeFromDatabase:
                removeFromDatabase();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void removeFromDatabase() {
        cursor.moveToPosition(currentItem + 1);
        dbOperation.removeLocalization(dbOperation, cursor.getString(0), cursor.getString(1));
        initLocalizationAdapter();
        initDatabaseOperation();
        initCursor();
        getLocalizationFromDatabase();
        setLocalizationAdapterToCityCountryListView();
        Toast.makeText(DisplayLocalization.this, "Localization properly removed from Database!", Toast.LENGTH_SHORT).show();
    }

    private void setAsDefault() {
        cursor.moveToPosition(currentItem + 1);
        SharedPreferences sharedPreferences = getSharedPreferences("config.xml", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Custom_City", cursor.getString(0));
        editor.putString("Custom_Country", cursor.getString(1));
        editor.putString("Custom_Latitude", cursor.getString(2));
        editor.putString("Custom_Longitude", cursor.getString(3));
        editor.commit();
        Toast.makeText(DisplayLocalization.this, "City " + cursor.getString(0) + " set as default!", Toast.LENGTH_SHORT).show();
    }


    private void initListView() {
        cityCountryListView = (ListView) findViewById(R.id.cityCountryListView);
    }
}
