DROP TABLE IF EXISTS issued_books;
DROP TABLE IF EXISTS readers;
DROP TABLE IF EXISTS books_editions;
DROP TABLE IF EXISTS authors;

CREATE TABLE authors
(
	author_id SERIAL PRIMARY KEY,
	last_name VARCHAR (20),
	first_name VARCHAR (20) NOT NULL,
	middle_name VARCHAR (20) 
);

CREATE TABLE books_editions
(
	book_id SERIAL PRIMARY KEY,
	ISBN VARCHAR (20) NOT NULL,
	title VARCHAR (50) NOT NULL,
	author INTEGER NOT NULL,
	published_year INTEGER NOT NULL,
	language_book VARCHAR (10) NOT NULL,
	count_books INTEGER NOT NULL,
	FOREIGN KEY (author) REFERENCES authors (author_id)
);

CREATE TABLE readers
(
	reader_id SERIAL PRIMARY KEY,
	last_name VARCHAR (20) NOT NULL,
	first_name VARCHAR (20) NOT NULL,
	middle_name VARCHAR (20) NOT NULL,
	address VARCHAR (50) NOT NULL,
	passport_sn VARCHAR (30) NOT NULL
);

CREATE TABLE issued_books
(
	issued_id SERIAL PRIMARY KEY,
	edition INTEGER NOT NULL,
	reader INTEGER NOT NULL,
	issue_date DATE NOT NULL,
	FOREIGN KEY (edition) REFERENCES books_editions (book_id),
	FOREIGN KEY (reader) REFERENCES readers (reader_id)
);


	