package noname.astroweather.weather.data;

import org.json.JSONObject;

public class Wind {
    private String chill;
    private String direction;
    private double speed;

    public String getChill() {
        return chill;
    }

    public String getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void populate(JSONObject data) {
        chill = data.optString("chill");
        direction = data.optString("direction");
        speed = data.optDouble("speed");
    }
}
