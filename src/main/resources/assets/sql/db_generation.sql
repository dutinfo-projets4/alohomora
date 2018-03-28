 CREATE TABLE  directory (
  idGroupe INTEGER NOT NULL PRIMARY KEY,
  content LONGTEXT,
  parent_grp INTEGER
);
CREATE TABLE element(
  idElement INTEGER NOT NULL CONSTRAINT AUTO_INCREMENT PRIMARY KEY,
  content LONGTEXT,
  idGroupe INTEGER,
  FOREIGN KEY (idGroupe) REFERENCES directory(idGroupe)
  ON DELETE CASCADE ON UPDATE NO ACTION
);
CREATE TABLE config(
  idConfig INTEGER NOT NULL CONSTRAINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR (255),
  name VARCHAR(255),
  portable BOOLEAN,
  token VARCHAR (255),
  RequestID INTEGER
)
--pas mettre de point virgule à la fin