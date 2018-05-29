package noname.astroweather.weather;

public class TemperatureUnitsChanger {

    public int fahrenheitToCelsius(int fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }
    public int celsiusToFahrenheit(int celsius) {
        return ((celsius * 9 / 5) + 32);
    }
}
