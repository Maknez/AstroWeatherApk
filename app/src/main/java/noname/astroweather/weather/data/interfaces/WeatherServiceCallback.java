package noname.astroweather.weather.data.interfaces;

import noname.astroweather.weather.data.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception ex);
}
