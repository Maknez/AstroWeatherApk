package noname.astroweather.weather.data;

import org.json.JSONObject;

import noname.astroweather.weather.data.interfaces.JSONPopulator;

public class Units implements JSONPopulator {

    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
