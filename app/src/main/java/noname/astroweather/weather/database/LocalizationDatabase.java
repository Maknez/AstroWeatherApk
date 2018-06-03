package noname.astroweather.weather.database;

import android.provider.BaseColumns;

public class LocalizationDatabase {
    public LocalizationDatabase() {

    }

    public static abstract class TableInfo implements BaseColumns {
        public static final String DATABASE_NAME = "localization_database";
        public static final String TABLE_NAME = "city_country_table";
        public static final String CITY_NAME = "city_name";
        public static final String COUNTRY_NAME = "country_name";
    }
}
