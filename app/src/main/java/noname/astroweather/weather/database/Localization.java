package noname.astroweather.weather.database;

public class Localization {
    private String city;
    private String country;
    private String longitude;
    private String latitude;

    Localization(String city, String country, String latitude, String longitude) {
        this.city = city;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
