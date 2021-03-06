DROP TABLE IF EXISTS person;

CREATE TABLE person (
  id VARCHAR(250) PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  cpf VARCHAR(250) NOT NULL,
  birthDate Date NOT NULL,
  birthCountry VARCHAR(250) NOT NULL,
  birthState VARCHAR(250) NOT NULL,
  birthCity VARCHAR(250) NOT NULL,
  fatherName VARCHAR(250),
  motherName VARCHAR(250),
  email VARCHAR(250) NOT NULL
);
