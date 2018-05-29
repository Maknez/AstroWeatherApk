package noname.astroweather.data;

import org.json.JSONObject;

class Atmosphere implements JSONPopulator {

    private int humidity;
    private Double pressure;
    private Double visibility;

    public int getHumidity() {
        return humidity;
    }
    public Double getPressure() {
        return pressure;
    }
    public Double getVisibility() {
        return visibility;
    }

    public void populate(JSONObject data) {
        humidity = data.optInt("humidity");
        pressure = data.optDouble("pressure");
        visibility = data.optDouble("visibility");
    }
}
