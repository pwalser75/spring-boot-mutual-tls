package ch.frostnova.spring.boot.mutual.tls.api.model;

import ch.frostnova.spring.boot.mutual.tls.api.converter.LocalDateConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

/**
 * Daily weather data
 */
public class DailyWeather {

    @JsonProperty("date")
    @JsonSerialize(using = LocalDateConverter.Serializer.class)
    @JsonDeserialize(using = LocalDateConverter.Deserializer.class)
    private LocalDate date;

    @JsonProperty("temperature")
    private double temperatureCelsius;

    @JsonProperty("condition")
    private WeatherCondition weatherCondition;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
}
