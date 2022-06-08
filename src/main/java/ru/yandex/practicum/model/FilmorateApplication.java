package ru.yandex.practicum.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {  // Программа возвращает ТОП-5 фильмов, рекомендованных к просмотру

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
