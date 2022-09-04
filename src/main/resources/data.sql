CREATE TABLE book(
id SERIAL PRIMARY KEY,
title VARCHAR(100),
author VARCHAR(100)
)

CREATE TABLE reader(
id SERIAL PRIMARY KEY,
name VARCHAR(100)
)

CREATE TABLE book_reader(
book_id INT REFERENCES book(id),
reader_id INT REFERENCES reader(id),
CONSTRAINT book_reader_pkey PRIMARY KEY(book_id, reader_id)
)

INSERT INTO book(title, author) VALUES('Java From Epam', 'Igor Blinov');
INSERT INTO book(title, author) VALUES('The Art of Loving', 'Erich Fromm');
INSERT INTO book(title, author) VALUES('The Subtle Art of Not Giving a Fuck', 'Mark Manson');
INSERT INTO book(title, author) VALUES('No Brakes: My Top Gear Years', 'Jeremy Clarkson');
INSERT INTO book(title, author) VALUES('Change or die', 'John Brandon');
INSERT INTO book(title, author) VALUES('The Last Wish', 'Andrzej Sapkowski');
INSERT INTO book(title, author) VALUES('Season of Storms', 'Andrzej Sapkowski');
INSERT INTO book(title, author) VALUES('Women', 'Charles Bukowski');
INSERT INTO book(title, author) VALUES('Ninety-Three', 'Victor Hugo');
INSERT INTO book(title, author) VALUES('How to Stop Worrying and Start Living', 'Dale Carnegie');

INSERT INTO reader(name) VALUES('Andrey Kravchenko');
INSERT INTO reader(name) VALUES('Danil Kolyagin');
INSERT INTO reader(name) VALUES('Danil Shevchenko');
INSERT INTO reader(name) VALUES('Mikhail Ivchenko');
INSERT INTO reader(name) VALUES('Aleksandra Kosharnaya');
INSERT INTO reader(name) VALUES('Darya Shevchenko');
INSERT INTO reader(name) VALUES('Nikolai Dudnik');
INSERT INTO reader(name) VALUES('Anastasia Shvidka');
INSERT INTO reader(name) VALUES('Aleksandr Voroncov');
INSERT INTO reader(name) VALUES('Vitaliy Dyma');

INSERT INTO book_reader(book_id, reader_id) VALUES(1, 1);
INSERT INTO book_reader(book_id, reader_id) VALUES(3, 1);
INSERT INTO book_reader(book_id, reader_id) VALUES(4, 1);
INSERT INTO book_reader(book_id, reader_id) VALUES(10, 2);
INSERT INTO book_reader(book_id, reader_id) VALUES(2, 2);
INSERT INTO book_reader(book_id, reader_id) VALUES(4, 3);
INSERT INTO book_reader(book_id, reader_id) VALUES(7, 3);
INSERT INTO book_reader(book_id, reader_id) VALUES(8, 3);
INSERT INTO book_reader(book_id, reader_id) VALUES(1, 4);
INSERT INTO book_reader(book_id, reader_id) VALUES(8, 4);
INSERT INTO book_reader(book_id, reader_id) VALUES(6, 5);
INSERT INTO book_reader(book_id, reader_id) VALUES(2, 7);
INSERT INTO book_reader(book_id, reader_id) VALUES(6, 7);
INSERT INTO book_reader(book_id, reader_id) VALUES(8, 7);
INSERT INTO book_reader(book_id, reader_id) VALUES(9, 7);
INSERT INTO book_reader(book_id, reader_id) VALUES(2, 9);
INSERT INTO book_reader(book_id, reader_id) VALUES(3, 9);
INSERT INTO book_reader(book_id, reader_id) VALUES(5, 9);
INSERT INTO book_reader(book_id, reader_id) VALUES(10, 10);