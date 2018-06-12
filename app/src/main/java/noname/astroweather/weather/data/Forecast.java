package noname.astroweather.weather.data;

import org.json.JSONObject;

import noname.astroweather.weather.data.interfaces.JSONPopulator;

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

