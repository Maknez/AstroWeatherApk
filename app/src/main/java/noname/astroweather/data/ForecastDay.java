package noname.astroweather.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class ForecastDay implements JSONPopulator {

    private int code;
    private int temperature;
    private String description;
    private String day;



    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getDay() {
        return day;
    }

    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperature = data.optInt("temperature");
        description = data.optString("description");
        day = data.optString("day");
    }
}
