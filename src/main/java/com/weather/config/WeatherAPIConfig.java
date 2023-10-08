package com.weather.config;

import com.weather.models.WeatherTemplateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class WeatherAPIConfig {

    private final WeatherTemplateHandler weatherTemplateHandler;

    @Value("${api_key}")
    private String apiKey;

    @Value("${api_url}")
    private String apiUrl;

    @Bean
    public RestTemplate weatherTemplate() {
        return new RestTemplateBuilder()
                .rootUri(apiUrl)
                .errorHandler(weatherTemplateHandler)
                .additionalInterceptors((request, body, execution) -> {
                    request.getHeaders().add("key", apiKey);
                    return execution.execute(request, body);
                })
                .build();
    }
}
