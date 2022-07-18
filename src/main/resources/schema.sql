CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar,
    email varchar NOT NULL,
    login varchar NOT NULL,
    birthday date
);

CREATE TABLE IF NOT EXISTS friends (
   id_user INTEGER REFERENCES users (id) NOT NULL,
   id_friend INTEGER REFERENCES users (id) NOT NULL,
   id_status INTEGER REFERENCES statuses (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS statuses (
    id INTEGER GENERATED BY DEFAULT AS PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER GENERATED BY DEFAULT AS PRIMARY KEY,
    name varchar NOT NULL,
    description varchar,
    release_date date,
    duration int,
    id_rating INTEGER REFERENCES ratings (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    id_film INTEGER REFERENCES films (id) NOT NULL,
    id_genre INTEGER REFERENCES genres (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER GENERATED BY DEFAULT AS PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS ratings (
    id INTEGER GENERATED BY DEFAULT AS PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS likes (
    id_user INTEGER REFERENCES users (id) NOT NULL,
    id_film INTEGER REFERENCES films (id) NOT NULL
);