CREATE DATABASE IF NOT EXISTS db_lavacao_01;
USE db_lavacao_01;

CREATE TABLE cor(
   id int NOT NULL auto_increment,
   nome  varchar(30) NOT NULL,
   CONSTRAINT pk_cor
      PRIMARY KEY(id)
) engine=InnoDB;


INSERT INTO cor(nome) VALUES('AZUL');
INSERT INTO cor(nome) VALUES('PRETO');
INSERT INTO cor(nome) VALUES('BRANCO');

