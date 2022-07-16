# java-filmorate
Template repository for Filmorate project.

Ссылка на ER-диаграмму БД:
https://dbdiagram.io/d/62bf3e8369be0b672c839750

Пояснение к схеме базы данных.
База данных состоит из 8ми таблиц:
- users
- friends
- statuses
- films
- film_genres
- genres
- ratings
- likes.
База данных содержит связи "один-ко многим".
База данных нормализована до 3ей формы.

Примеры запросов для основных операций приложения.

Пример 1. Получить список всех пользователей
SELECT * FROM users;

Пример 2. Получить список полей фильма с заданным Id.
SELECT * FROM films WHERE id_film = ?;

Пример 3. Вывести список общих друзей.
SELECT t1.id_friend AS id
FROM (SELECT id_friend FROM friends WHERE id_user = ? AND id_status = ?) AS t1
INNER JOIN (SELECT id_friend FROM friends WHERE id_user = ? AND id_status = ?) AS t2
ON t1.id_friend = t2.id_friend;

Пример 4. Вывести ТОП-10 фильмов.
SELECT films.id, COUNT(likes.*) AS count_of_likes
FROM films LEFT INNER JOIN likes 
ON films.id = likes.id_film 
GROUP BY films.id 
ORDER BY COUNT(likes.*) DESC TOP 10