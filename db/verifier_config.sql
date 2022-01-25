-- DROP USER 'verifieruser'@'localhost';
CREATE USER 'verifieruser'@'localhost' IDENTIFIED BY '12345678';
GRANT ALL PRIVILEGES ON * . * TO 'verifieruser'@'localhost';
FLUSH PRIVILEGES;

-- DROP DATABASE verifier_data;
CREATE DATABASE verifier_data;
