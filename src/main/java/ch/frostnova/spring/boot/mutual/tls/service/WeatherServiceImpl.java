package ch.frostnova.spring.boot.mutual.tls.service;

import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import ch.frostnova.spring.boot.mutual.tls.api.model.WeatherCondition;
import ch.frostnova.spring.boot.mutual.tls.api.service.WeatherService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

/**
 * Weather service test implementation
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    @Override
    public List<DailyWeather> getForecast(int days) {
        List<DailyWeather> forecast = new LinkedList<>();
        var today = LocalDate.now();
        for (var i = 0; i < days; i++) {

            var conditions = WeatherCondition.values();
            var randomCondition = conditions[(int) (Math.random() * conditions.length)];
            var randomTemperature = randomCondition.getTempMin() + Math.random() * (randomCondition.getTempMax() - randomCondition.getTempMin());

            var dailyWeather = new DailyWeather();
            dailyWeather.setDate(today.plus(i, ChronoUnit.DAYS));
            dailyWeather.setWeatherCondition(randomCondition);
            dailyWeather.setTemperatureCelsius((int) (10 * (randomTemperature) / 10d));
            forecast.add(dailyWeather);
        }

        return forecast;
    }
}
