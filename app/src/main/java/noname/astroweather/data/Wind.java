package noname.astroweather.data;

import org.json.JSONObject;

class Wind {
    private int chill;
    private int direction;
    private int speed;

    public int getChill() {
        return chill;
    }

    public int getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void populate(JSONObject data) {
        chill = data.optInt("chill");
        direction = data.optInt("direction");
        speed = data.optInt("speed");
    }
}
