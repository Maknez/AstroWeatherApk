package noname.astroweather.data;

import org.json.JSONObject;
//https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys
public class Forecast implements JSONPopulator {

    private static final int FORECAST_DAY_NUMBER = 6;

    private int code;
    private String temperatureHigh;
    private String temperatureLow;
    private String description;
    private String day;

    public String getTemperatureLow() {
        return temperatureLow;
    }

    public int getCode() {

        return code;
    }

    public String getTemperatureHigh() {
        return temperatureHigh;
    }

    public String getDescription() {
        return description;
    }

    public String getDay() {
        return day;
    }

    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperatureHigh = data.optString("high");
        temperatureLow = data.optString("low");
        description = data.optString("text");
        day = data.optString("day");
    }

    /*
    private ForecastDay[] forecastDays = new ForecastDay[6];

    public ForecastDay getForecastDays(int currentDay) {
        return forecastDays[currentDay];
    }

    @Override
    public void populate(JSONObject data) {
        for(int i = 0; i < FORECAST_DAY_NUMBER; i++) {
            forecastDays[i] = new ForecastDay();
            System.out.println(forecastDays[i]);
            forecastDays[i].populate(data.optJSONObject("1"));
        }

    }
*/
/*
    private ForecastDay[] forecastDay = new ForecastDay[6];



    public ForecastDay getForecastDay(int currentDay) {
        return forecastDay[currentDay];
    }

    public void populate(JSONObject data) {

        for (int iterator = 0; iterator < FORECAST_DAY_NUMBER; iterator++) {
            forecastDay[iterator] = new ForecastDay(iterator);
            forecastDay[iterator].populate(data.optJSONObject(String.valueOf(iterator)));
        }
    }*/

/*
    private ForecastDay forecastDay1 = new ForecastDay(1);
    private ForecastDay forecastDay2 = new ForecastDay(2);
    private ForecastDay forecastDay3 = new ForecastDay(3);
    private ForecastDay forecastDay4 = new ForecastDay(4);
    private ForecastDay forecastDay5 = new ForecastDay(5);


    public ForecastDay getForecastDay(int currentDay) {
        switch (currentDay) {
            case 1:
                return forecastDay1;
            case 2:
                return forecastDay2;
            case 3:
                return forecastDay3;
            case 4:
                return forecastDay4;
            case 5:
                return forecastDay5;
            default:
                return null;
        }

    }

    public void populate(JSONObject data) {
        forecastDay1.populate(data.optJSONObject(String.valueOf(1)));
        forecastDay2.populate(data.optJSONObject(String.valueOf(2)));
        forecastDay3.populate(data.optJSONObject(String.valueOf(3)));
        forecastDay4.populate(data.optJSONObject(String.valueOf(4)));
        forecastDay5.populate(data.optJSONObject(String.valueOf(5)));
    }*/
}

