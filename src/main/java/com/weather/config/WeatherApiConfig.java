package com.weather.config;

import com.weather.models.WeatherTemplateErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class WeatherApiConfig {
    private final WeatherTemplateErrorHandler weatherTemplateErrorHandler;

    @Value("${api_key}")
    private String apiKey;

    @Value("${api_url}")
    private String apiUrl;

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
    public TransactionTemplate transactionTemplateWithRepeatableRead(PlatformTransactionManager transactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        return transactionTemplate;
    }
}
