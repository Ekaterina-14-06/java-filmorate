package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {  // Программа возвращает ТОП-10 фильмов, рекомендованных к просмотру.
	// Согласно техническому заданию в финальный проект спринта 9 добавлен следующий функционал:
	// - объединение пользователей в комьюнити;
	// - составление рейтинга фильмов по отзывам пользователей;
	// - добавление пользователями друг друга в друзья;
	// - возможность пользователям ставить фильмам лайки.

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
