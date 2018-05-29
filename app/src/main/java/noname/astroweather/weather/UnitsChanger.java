package noname.astroweather.weather;

public class UnitsChanger {

    public int fahrenheitToCelsius(int fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }

    public int celsiusToFahrenheit(int celsius) {
        return ((celsius * 9 / 5) + 32);
    }

    public double milesPerHourTokilometersPerHour(double mph) {
        return (mph * 1.609344);
    }

    public double kilometersPerHourToMilesPerHour(double kmph) {
        return (kmph / 1.609344);
    }
}
