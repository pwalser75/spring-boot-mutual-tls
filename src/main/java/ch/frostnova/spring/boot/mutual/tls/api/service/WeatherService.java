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
     * @param days number of days to forecast (0=today only, 1=today and tomorrow, and so on)
     * @return forecast
     */
    List<DailyWeather> getForecast(int days);
}
