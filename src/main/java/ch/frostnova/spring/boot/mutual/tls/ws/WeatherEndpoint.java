package ch.frostnova.spring.boot.mutual.tls.ws;

import ch.frostnova.spring.boot.mutual.tls.api.model.DailyWeather;
import ch.frostnova.spring.boot.mutual.tls.api.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST endpoint for weather forecast
 */
@RestController
@RequestMapping(value = "/api")
public class WeatherEndpoint {

    @Autowired
    private WeatherService weatherService;

    @GetMapping(path = "/weather/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DailyWeather> getForecast(@RequestParam(name = "days", required = false) Integer days) {
        return weatherService.getForecast(Optional.ofNullable(days).orElse(6));
    }
}
