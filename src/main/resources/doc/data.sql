TRUNCATE TABLE users;
TRUNCATE TABLE curvepoint;

insert into users(fullname, username, password, role) VALUES
("Administrator", "admin", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "ADMIN"),
("User", "user", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "USER");


INSERT INTO curvepoint(term, value) VALUES
(1.0, 5.0),
(2.0, 20.0);
