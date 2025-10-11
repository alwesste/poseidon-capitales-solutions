TRUNCATE TABLE users;
TRUNCATE TABLE curvepoint;
TRUNCATE TABLE rating;
TRUNCATE TABLE ruleName;
TRUNCATE TABLE bidList;
TRUNCATE TABLE trade;

insert into users(fullname, username, password, role) VALUES
('Administrator', 'admin', '$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa', 'ADMIN'),
('User', 'user', '$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa', 'USER');

INSERT INTO curvepoint(term, value) VALUES
(1.0, 6.0),
(2.0, 20.0),
(3.0, 15.0);

INSERT INTO rating(moodysRating, sandPRating, fitchRating, orderNumber) VALUES
(10, 9, 7, 4),
(5, 6, 9, 8),
(3,10, 3, 9);

INSERT INTO ruleName(name, description, json, template, sqlStr, sqlPart) VALUES
('test', 'test', 'test', 'test', 'test', 'test'),
('leNom', 'laDescription', 'leJson', 'leTemplate', 'leSqlStr', 'leSqlPart'),
('test', 'test', 'test', 'test', 'test', 'test');

INSERT INTO bidList(account, type, bidQuantity) VALUES
('myAccount', 'number1', 200.0),
('anotherAccount', 'number2', 300.0),
('lastAccount', 'number3', 400.0);

INSERT INTO trade(account, type, buyQuantity) VALUES
('myAccount', 'number1', 200.0),
('anotherAccount', 'number2', 300.0),
('lastAccount', 'number3', 400.0);
