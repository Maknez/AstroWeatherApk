package noname.astroweather.weather.data;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception ex);
}
