package noname.astroweather.weather.data;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import noname.astroweather.Exception.LocationWeatherException;
import noname.astroweather.weather.data.interfaces.WeatherServiceCallback;


public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private Exception error;
    private SharedPreferences sharedPreferences;
    private int option;
    private String YQL;

    public YahooWeatherService(WeatherServiceCallback callback, SharedPreferences sharedPref, int option) {
        this.callback = callback;
        this.sharedPreferences = sharedPref;
        this.option = option;
    }


    @SuppressLint("StaticFieldLeak")
    public void refreshWeather() {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                if (option == 0) {
                    String cityCountryToFind = sharedPreferences.getString("Custom_City", "Będków") + ", " + sharedPreferences.getString("Custom_Country", "Poland");
                    YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", cityCountryToFind);
                } else if (option == 1) {

                    String longLangToFind = sharedPreferences.getString("Custom_Longitude", String.valueOf(51.5873166)) + "," + sharedPreferences.getString("Custom_Latitude", String.valueOf(19.7543569));
                    System.out.println(longLangToFind);
                    YQL = String.format("select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"(%s)\")", longLangToFind);
                }


                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();

                } catch (MalformedURLException e) {
                    error = e;
                } catch (IOException e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s == null && error != null) {
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    JSONObject queryResults = data.optJSONObject("query");
                    int count = queryResults.optInt("count");
                    if (count == 0) {
                        callback.serviceFailure(new LocationWeatherException("No weather information found"));
                        return;
                    } else if (queryResults.optJSONObject("results").optJSONObject("channel").optJSONObject("location") == null) {
                        callback.serviceFailure(new LocationWeatherException("Problem with infromation for current localization"));
                        return;
                    } else {
                        Channel channel = new Channel();
                        channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));

                        callback.serviceSuccess(channel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
