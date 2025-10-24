TRUNCATE TABLE users;
TRUNCATE TABLE curvepoint;

insert into users(fullname, username, password, role) VALUES
("Administrator", "admin", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "ADMIN"),
("User", "user", "$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa", "USER"),
("User", "leo", "$2y$10$WhmMe.3P168k0paRosXYZOJAX3yY0CyDYZz4nxZXVw6Zucb0s34Wu", "USER"),
("test", "test", "$2y$10$wh4sBl99K8ZIA1jWGCV0oOEFjk.tO/kTdodo8ldFjfZfU1iYWuml2", "ADMIN");
