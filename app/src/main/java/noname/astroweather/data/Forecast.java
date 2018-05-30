package noname.astroweather.data;

import org.json.JSONObject;
//https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys
public class Forecast implements JSONPopulator {

    private int code;
    private int temperatureHigh;
    private int temperatureLow;
    private String description;
    private String day;

    public int getTemperatureLow() {
        return temperatureLow;
    }

    public int getCode() {
        return code;
    }

    public int getTemperatureHigh() {
        return temperatureHigh;
    }

    public String getDescription() {
        return description;
    }

    public String getDay() {
        return day;
    }

    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperatureHigh = data.optInt("high");
        temperatureLow = data.optInt("low");
        description = data.optString("text");
        day = data.optString("day");
    }

}

