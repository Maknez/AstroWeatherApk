package noname.astroweather.weather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOperation extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public String CREATE_QUERY = "CREATE TABLE " +
            LocalizationDatabase.TableInfo.TABLE_NAME +
            "(" +
            LocalizationDatabase.TableInfo.CITY_NAME +
            " TEXT," +
            LocalizationDatabase.TableInfo.COUNTRY_NAME +
            " TEXT );";

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

    public void putInformation(DatabaseOperation dboperation, String cityName, String countryName) {
        SQLiteDatabase SQ = dboperation.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LocalizationDatabase.TableInfo.CITY_NAME, cityName);
        cv.put(LocalizationDatabase.TableInfo.COUNTRY_NAME, countryName);
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
