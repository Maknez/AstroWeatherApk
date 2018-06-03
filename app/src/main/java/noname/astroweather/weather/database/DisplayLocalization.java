package noname.astroweather.weather.database;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        registerForContextMenu(cityCountrylistView);

        cityCountrylistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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
        //TODO: implement method
    }

    private void setAsDefault() {
        //TODO: implement method
    }


    private void initListView() {
        cityCountrylistView = (ListView) findViewById(R.id.cityCountryListView);
    }
}
