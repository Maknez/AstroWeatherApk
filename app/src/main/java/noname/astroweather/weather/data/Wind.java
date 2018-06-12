package noname.astroweather.weather.data;

import org.json.JSONObject;

public class Wind {
    private String direction;
    private double speed;

    public String getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void populate(JSONObject data) {
        direction = data.optString("direction");
        speed = data.optDouble("speed");
    }
}
