package noname.astroweather.data;

import org.json.JSONObject;

public class Location implements JSONPopulator {

    private String city;
    private String region;
    private String country;

    public String getCity() {
        if(city.equals("")) {
            return "";
        }
        return city;
    }

    public String getRegion() {
        if(region.equals("")) {
            return "";
        }
        return region;
    }

    public String getCountry() {
        if(country.equals("")) {
            return "";
        }
        return country;
    }

    public void populate(JSONObject data) {
        city = data.optString("city");
        region = data.optString("region");
        country = data.optString("country");
    }
}
