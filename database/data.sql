BEGIN;

INSERT INTO roles (name)
VALUES
 ('user'),
 ('admin');

INSERT INTO users (name, password, email, role)
VALUES
  ('bone', 'bone', 'tandeey@gmail.com', (SELECT id from roles WHERE name = 'admin')),
  ('gurr', 'gurr', 'sigurd.l@gmail.com', (SELECT id from roles WHERE name = 'admin')),
  ('garr', 'garr', 'pelle@pelle.com', (SELECT id from roles WHERE name = 'user'));

INSERT INTO ingredient(name)
VALUES
  ('Kjøttdeig'),
  ('Melk'),
  ('Vann'),
  ('Lasagne plater'),
  ('Lasagne krydder');

INSERT INTO tags(name)
VALUES
  ('pasta'),
  ('italiensk');

INSERT INTO language(locale, name)
VALUES
  ('NO-no', 'Norwegian'),
  ('EN-en', 'English');

INSERT INTO recipe(name, description, language, calories, procedure, created, modified)
VALUES
  ('Lasser', 'En optimal lasser', (SELECT id from language WHERE name = 'Norwegian'), 1000, 'Hvordan lager man en lasser? Jo.. Nå skal du høre..', Now(), Now());

INSERT INTO ingredient_in_recipe(recipe_id, ingredient_id, amount)
VALUES
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Kjøttdeig'), 400),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Melk'), 300),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Vann'), 200),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Lasagne plater'), 1),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Lasagne krydder'), 1);

INSERT INTO user_star_rate_recipe(user_id, recipe_id, stars)
VALUES
  ((SELECT id from users WHERE name = 'garr'), (SELECT id from recipe WHERE name = 'Lasser'), 5.0);

INSERT INTO user_yes_no_rate_ingredient(user_id, ingredient_id, rating)
VALUES
  ((SELECT id from users WHERE name = 'garr'), (SELECT id from recipe WHERE name = 'Lasser'), true);



END;