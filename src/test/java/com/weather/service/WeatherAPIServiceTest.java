package com.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.config.ObjectConfig;
import com.weather.config.WeatherApiConfig;
import com.weather.exception.BadRequestToWeatherApiException;
import com.weather.models.Properties;
import com.weather.models.WeatherApiDto;
import com.weather.models.WeatherTemplateErrorHandler;
import com.weather.services.WeatherAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({WeatherAPIService.class, Properties.class, WeatherTemplateErrorHandler.class, WeatherApiConfig.class, ObjectConfig.class})
class WeatherAPIServiceTest {

    @Autowired
    WeatherAPIService weatherAPIService;

    @Autowired
    Properties properties;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockRestServiceServer server;

    @Autowired
    RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getCurrentWeatherReturnsWeather() throws JsonProcessingException {
        String expectedJson = createWeatherDto();
        WeatherApiDto expected = objectMapper.readValue(expectedJson, WeatherApiDto.class);
        String city = expected.getName();

        server.expect(requestToUriTemplate(
                properties.apiUrl() + "/current.json?q={cityName}&key={key}",
                city, properties.apiKey()
        )).andRespond(withSuccess().body(expectedJson).contentType(MediaType.APPLICATION_JSON));

        WeatherApiDto actual = weatherAPIService.getCurrentWeather(city);

        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getCountry(), expected.getCountry());
    }

    @Test
    void getCurrentWeatherReturnNoMatchingLocationFound() {
        String expected = "{\"error\":{\"code\":1006,\"message\":\"No matching location found.\"}}";
        String city = "notCity";

        server.expect(requestToUriTemplate(
                properties.apiUrl() + "/current.json?q={cityName}&key={key}",
                city, properties.apiKey()
        )).andRespond(withBadRequest().body(expected));

        assertThrows(BadRequestToWeatherApiException.class, () -> weatherAPIService.getCurrentWeather(city), expected);
    }


    String createWeatherDto() {
        return "{\"location\":{\"name\":\"Moscow\",\"region\":\"Moscow City\",\"country\":\"Russia\",\"lat\":55.75,\"lon\":37.62,\"tz_id\":\"Europe/Moscow\",\"localtime\":\"2023-11-11 18:46\"},\"current\":{\"last_updated_epoch\":1699717500,\"last_updated\":\"2023-11-11 18:45\",\"temp_c\":4.0,\"temp_f\":39.2,\"is_day\":0,\"condition\":{\"text\":\"Partly cloudy\",\"icon\":\"//cdn.weatherapi.com/weather/64x64/night/116.png\",\"code\":1003},\"wind_mph\":11.9,\"wind_kph\":19.1,\"wind_degree\":150,\"wind_dir\":\"SSE\",\"pressure_mb\":1014.0,\"pressure_in\":29.94,\"precip_mm\":0.0,\"precip_in\":0.0,\"humidity\":87,\"cloud\":25,\"feelslike_c\":0.7,\"feelslike_f\":33.2,\"vis_km\":10.0,\"vis_miles\":6.0,\"uv\":1.0,\"gust_mph\":13.4,\"gust_kph\":21.6}}";
    }

}