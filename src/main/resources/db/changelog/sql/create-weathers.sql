CREATE TABLE weather
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    city_id INT  NOT NULL,
    date_time    DATE NOT NULL,
    weather_type_id INT  NOT NULL,
    FOREIGN KEY (city_id) REFERENCES city (id),
    FOREIGN KEY (weather_type_id) REFERENCES weather_type (id)
);
