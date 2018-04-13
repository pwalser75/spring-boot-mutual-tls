package ch.frostnova.spring.boot.mutual.tls.api.service;

import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;

import java.util.List;

/**
 * Weather service
 *
 * @author wap
 * @since 13.04.2018
 */
public interface WeatherService {

    /**
     * Get forecast for the next few days
     *
     * @return forecast
     */
    List<DailyWeather> getForecast(int days);
}
