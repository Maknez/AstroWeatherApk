package noname.astroweather.weather.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOperation extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private String CREATE_QUERY = new StringBuilder().append("CREATE TABLE ")
            .append(LocalizationDatabase.TableInfo.TABLE_NAME).append("(")
            .append(LocalizationDatabase.TableInfo.CITY_NAME).append(" TEXT,")
            .append(LocalizationDatabase.TableInfo.COUNTRY_NAME).append(" TEXT,")
            .append(LocalizationDatabase.TableInfo.LATITUDE_VALUE).append(" TEXT,")
            .append(LocalizationDatabase.TableInfo.LONGITUDE_VALUE).append(" TEXT );").toString();

    public DatabaseOperation(Context context) {
        super(context, LocalizationDatabase.TableInfo.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String putInformation(DatabaseOperation dboperation, String cityName, String countryName, String latitude, String longitude) {
        SQLiteDatabase SQ = dboperation.getWritableDatabase();
        Cursor cursor = getInformation(this);
        cursor.moveToFirst();
        boolean isRepeated = false;
        while (cursor.moveToNext()) {
            if (cityName.equals(cursor.getString(0)) && countryName.equals(cursor.getString(1))) {
                isRepeated = true;
            }
        }
        if (!isRepeated) {
            ContentValues cv = new ContentValues();
            cv.put(LocalizationDatabase.TableInfo.CITY_NAME, cityName);
            cv.put(LocalizationDatabase.TableInfo.COUNTRY_NAME, countryName);
            cv.put(LocalizationDatabase.TableInfo.LATITUDE_VALUE, latitude);
            cv.put(LocalizationDatabase.TableInfo.LONGITUDE_VALUE, longitude);
            SQ.insert(LocalizationDatabase.TableInfo.TABLE_NAME, null, cv);
            return "Localization successfully added to Database!";
        } else {
            return "Localization already in Database!";
        }
    }

    public Cursor getInformation(DatabaseOperation dboperation) {
        SQLiteDatabase SQ = dboperation.getReadableDatabase();
        String[] columns = {LocalizationDatabase.TableInfo.CITY_NAME, LocalizationDatabase.TableInfo.COUNTRY_NAME, LocalizationDatabase.TableInfo.LATITUDE_VALUE, LocalizationDatabase.TableInfo.LONGITUDE_VALUE};
        return SQ.query(LocalizationDatabase.TableInfo.TABLE_NAME, columns, null, null, null, null, null);
    }

    public void removeLocalization(DatabaseOperation dboperation, String cityName, String countryName) {
        String selection = LocalizationDatabase.TableInfo.CITY_NAME + " LIKE ? AND " + LocalizationDatabase.TableInfo.COUNTRY_NAME + " LIKE ? ";
        String args[] = {cityName, countryName};
        SQLiteDatabase SQ = dboperation.getWritableDatabase();
        SQ.delete(LocalizationDatabase.TableInfo.TABLE_NAME, selection, args);
    }
}
