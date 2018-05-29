package noname.astroweather.data;

import org.json.JSONObject;

class Forecast implements JSONPopulator {

    private static final int FORECAST_DAY_NUMBER = 5;
    /*private int[] code = new int[5];
    private int[] temperature = new int[5];
    private String[] description = new String[5];
    private String[] day = new String[5];

    public int[] getCode() {
        return code;
    }

    public int[] getTemperature() {
        return temperature;
    }

    public String[] getDescription() {
        return description;
    }

    public String[] getDay() {
        return day;
    }*/

    public void populate(JSONObject data) {
       /* for (int iterator = 0; iterator < FORECAST_DAY_NUMBER; iterator++) {
            code[iterator] = data.optJSONObject(String.valueOf(iterator)).optInt("code");
            temperature[iterator] = data.optJSONObject(String.valueOf(iterator)).optInt("temp");
            description[iterator] = data.optJSONObject(String.valueOf(iterator)).optString("text");
            day[iterator] = data.optJSONObject(String.valueOf(iterator)).optString("day");
        }*/
    }
}
