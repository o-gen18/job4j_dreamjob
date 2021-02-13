CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    photoId TEXT,
    cityId TEXT
);

CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT UNIQUE,
    password TEXT
);

CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    name TEXT UNIQUE
);