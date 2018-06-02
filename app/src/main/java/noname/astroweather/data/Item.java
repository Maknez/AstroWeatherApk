package noname.astroweather.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class Item implements JSONPopulator {

    private Forecast[] forecast = new Forecast[6];
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

    public Forecast getForecast(int currentDay) {
        return forecast[currentDay];
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

        latitude = data.optDouble("long");
        longitude = data.optDouble("lat");


        JSONArray jsonArray = data.optJSONArray("forecast");
        for(int i = 0; i < 6; i++) {
            try {
                forecast[i] = new Forecast();
                forecast[i].populate(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

