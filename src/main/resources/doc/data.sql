TRUNCATE TABLE users;
TRUNCATE TABLE curvepoint;

insert into users(fullname, username, password, role) VALUES
("leopold", "leopold", "$2y$10$DJm1hUaiJxKIL3IAwjdfWuhd6YeQusQ3KI4u0B/5ISwS81a/NFaIC", "ADMIN"),
("shiori", "shiori", "$2y$10$DJm1hUaiJxKIL3IAwjdfWuhd6YeQusQ3KI4u0B/5ISwS81a/NFaIC", "USER");
