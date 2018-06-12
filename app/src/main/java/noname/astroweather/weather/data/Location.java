package noname.astroweather.weather.data;

import org.json.JSONObject;

import noname.astroweather.weather.data.interfaces.JSONPopulator;

public class Location implements JSONPopulator {

    private String city;
    private String country;

    public String getCity() {
        if (city.equals("")) {
            return "";
        }
        return city;
    }

    public String getCountry() {
        if (country.equals("")) {
            return "";
        }
        return country;
    }

    public void populate(JSONObject data) {
        city = data.optString("city");
        country = data.optString("country");
    }
}
