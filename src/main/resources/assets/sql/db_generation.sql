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
CREATE TABLE token(
  idToken INTEGER NOT NULL CONSTRAINT AUTO_INCREMENT PRIMARY KEY,
  username varchar(255),
  value varchar(255),
  ip VARCHAR(36)
);
CREATE TABLE config(
  idConfig INTEGER NOT NULL CONSTRAINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  portable BOOLEAN
)
--pas mettre de point virgule à la fin