package noname.astroweather.data;

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
import noname.astroweather.R;

public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private String location;
    private Exception error;


    public String getLocation() {
        return location;
    }

        SharedPreferences sharedPreferences;

    public YahooWeatherService(WeatherServiceCallback callback, SharedPreferences sharedPreferences) {
        this.callback = callback;
        this.sharedPreferences = sharedPreferences;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshWeather(String locationToRefresh) {
        this.location = locationToRefresh;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String cityCountryToFind = sharedPreferences.getString("Custom_City", "Będków") + ", " + sharedPreferences.getString("Custom_Country", "Poland");
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", cityCountryToFind);
                String YQLLL = String.format("select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"(%s,%s)\")", sharedPreferences.getString("Custom_Latitude", String.valueOf(19.7543569)), sharedPreferences.getString("Custom_Longitude", String.valueOf(51.5873166)));


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
                        callback.serviceFailure(new LocationWeatherException("No weather information found for " + location));
                        return;
                    }

                    Channel channel = new Channel();
                    channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));

                    callback.serviceSuccess(channel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(location);
    }
}
