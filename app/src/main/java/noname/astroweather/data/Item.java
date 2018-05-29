package noname.astroweather.data;

import org.json.JSONObject;

public class Item implements JSONPopulator {

    private Forecast forecast;
    private Condition condition;
    private Double latitude;
    private Double longitude;

    public Condition getCondition() {
        return condition;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Forecast getForecast() {
        return forecast;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

        latitude = data.optDouble("latitude");
        longitude = data.optDouble("longitude");

        forecast = new Forecast();
        forecast.populate(data.optJSONObject("forecast"));
    }
}
