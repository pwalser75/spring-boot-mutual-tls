package ch.frostnova.spring.boot.mutual.tls.api.model;

/**
 * Weather condition enum
 */
public enum WeatherCondition {

    SUNNY(-10, 40),
    PARTIALLY_CLOUDY(-10, 25),
    CLOUDY(-10, 20),
    RAIN(0, 15),
    HAILSTORM(0, 5),
    SNOW(-20, 0);

    private final double tempMin;
    private final double tempMax;

    WeatherCondition(double tempMin, double tempMax) {
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }
}
