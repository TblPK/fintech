package com.weather.config;

import com.weather.models.WeatherTemplateErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class WeatherApiConfig {
    private final WeatherTemplateErrorHandler weatherTemplateErrorHandler;

    @Value("${api_key}")
    private String apiKey;

    @Value("${api_url}")
    private String apiUrl;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Bean
    public RestTemplate weatherTemplate() {
        return new RestTemplateBuilder()
                .rootUri(apiUrl)
                .errorHandler(weatherTemplateErrorHandler)
                .additionalInterceptors((request, body, execution) -> {
                    request.getHeaders().add("key", apiKey);
                    return execution.execute(request, body);
                })
                .build();
    }

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    @Bean
    public Connection anotherDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
