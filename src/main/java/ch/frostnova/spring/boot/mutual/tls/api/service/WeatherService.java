package ch.frostnova.spring.boot.mutual.tls.api.service;

import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;

import java.util.List;

/**
 * Weather service
 */
public interface WeatherService {

    /**
     * Get forecast for the next few days
     *
     * @return forecast
     */
    List<DailyWeather> getForecast(int days);
}
