package noname.astroweather.data;

import org.json.JSONObject;

public class Location {

    private String city;
    private String region;
    private String country;

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public void populate(JSONObject data) {
        city = data.optString("city");
        region = data.optString("region");
        country = data.optString("country");
    }
}
