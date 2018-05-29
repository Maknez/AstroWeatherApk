package noname.astroweather.data;

import noname.astroweather.data.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception ex);
}
