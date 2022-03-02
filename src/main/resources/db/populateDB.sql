DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (id, user_id, date_time, description, calories)
VALUES (10003, 100000, '2022-03-02 10:00:00', 'user meal 1', 1000),
       (10004, 100000, '2022-03-02 15:00:00', 'user meal 2', 1200),
       (10005, 100000, '2022-03-01 12:00:00', 'user meal 3', 1500),
       (10006, 100001, '2022-03-02 10:00:00', 'admin meal 1', 1000),
       (10007, 100001, '2022-03-02 15:00:00', 'admin meal 2', 1200),
       (10008, 100001, '2022-03-01 10:00:00', 'admin meal 3', 1500)