package noname.astroweather.weather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperation extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public String CREATE_QUERY = new StringBuilder().append("CREATE TABLE ")
            .append(LocalizationDatabase.TableInfo.TABLE_NAME).append("(")
            .append(LocalizationDatabase.TableInfo.CITY_NAME).append(" TEXT,")
            .append(LocalizationDatabase.TableInfo.COUNTRY_NAME).append(" TEXT,")
            .append(LocalizationDatabase.TableInfo.LATITUDE_VALUE).append(" TEXT,")
            .append(LocalizationDatabase.TableInfo.LONGITUDE_VALUE).append(" TEXT );").toString();

    public DatabaseOperation(Context context) {
        super(context, LocalizationDatabase.TableInfo.DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database operation", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database operation", "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(DatabaseOperation dboperation, String cityName, String countryName, String latitude, String longitude) {
        SQLiteDatabase SQ = dboperation.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LocalizationDatabase.TableInfo.CITY_NAME, cityName);
        cv.put(LocalizationDatabase.TableInfo.COUNTRY_NAME, countryName);
        cv.put(LocalizationDatabase.TableInfo.LATITUDE_VALUE, latitude);
        cv.put(LocalizationDatabase.TableInfo.LONGITUDE_VALUE, longitude);
        long k = SQ.insert(LocalizationDatabase.TableInfo.TABLE_NAME, null, cv);
        Log.d("Database operation", "One raw inserted");
    }

    public Cursor getInformation(DatabaseOperation dboperation) {
        SQLiteDatabase SQ = dboperation.getReadableDatabase();
        String[] columns = {LocalizationDatabase.TableInfo.CITY_NAME, LocalizationDatabase.TableInfo.COUNTRY_NAME};
        Cursor cursor = SQ.query(LocalizationDatabase.TableInfo.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }
}
