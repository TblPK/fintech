package com.weather.services;

import com.weather.exception.WeatherAlreadyExistsException;
import com.weather.exception.WeatherNotFoundException;
import com.weather.models.Weather;
import com.weather.repositories.WeatherRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;

    /**
     * Получение списка данных о погоде в указанном городе на заданную дату.
     *
     * @param cityName Название города.
     * @param date     Заданная дата.
     * @return Список данных о погоде.
     * @throws WeatherNotFoundException Если данные о погоде не найдены.
     */
    public List<Weather> getWeatherListForDate(String cityName, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.of(23, 59, 59));

        if (!weatherRepository.getTempDB().containsKey(cityName)) {
            throw new WeatherNotFoundException(cityName);
        }

        List<Weather> weatherList = weatherRepository.getTempDB().get(cityName).stream()
                .filter(w -> w.getDateTime().isAfter(startOfDay) && w.getDateTime().isBefore(endOfDay)).toList();

        if (weatherList.isEmpty()) {
            throw new WeatherNotFoundException(cityName);
        }
        return weatherList;
    }

    /**
     * Создание нового города с информацией о температуре и дате.
     *
     * @param cityName    Название города.
     * @param temperature Температура.
     * @param dateTime    Дата и время.
     * @throws WeatherAlreadyExistsException Если город уже существует.
     */
    public void createCity(String cityName, double temperature, LocalDateTime dateTime) {
        boolean isExists = weatherRepository.addCity(cityName, temperature, dateTime);
        if (!isExists) {
            throw new WeatherAlreadyExistsException(cityName);
        }
    }

    /**
     * Обновление или добавление данных о погоде на заданную дату в указанном городе.
     *
     * @param cityName    Название города.
     * @param temperature Температура.
     * @param dateTime    Дата и время.
     * @throws WeatherNotFoundException Если данные о погоде не найдены.
     */
    public void updateWeather(String cityName, double temperature, LocalDateTime dateTime) {
        boolean isExists = weatherRepository.getTempDB().containsKey(cityName);
        if (!isExists) {
            throw new WeatherNotFoundException(cityName);
        }
        Optional<Weather> optional = weatherRepository.getTempDB().get(cityName).stream()
                .filter(w -> w.getCityName().equals(cityName) && w.getDateTime().isEqual(dateTime))
                .findFirst();

        if (optional.isEmpty()) {
            weatherRepository.addWeather(cityName, temperature, dateTime);
        } else {
            optional.get().setTemperature(temperature);
        }
    }

    /**
     * Удаление всех данных о погоде для указанного города.
     *
     * @param cityName Название города.
     */
    public void deleteWeathersByCityName(String cityName) {
        weatherRepository.getTempDB().remove(cityName);
    }
}
