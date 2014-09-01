BEGIN;

INSERT INTO roles(name) VALUES ("user");
INSERT INTO roles(name) VALUES ("admin");

INSERT INTO users(name, password, role) VALUES ("bone", "bone", 2);
INSERT INTO users(name, password, role) VALUES ("gurr", "gurr", 2);
INSERT INTO users(name, password, role) VALUES ("garr", "garr", 1);

END;