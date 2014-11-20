BEGIN;

INSERT INTO roles (name)
VALUES
 ('user'),
 ('admin');

INSERT INTO users (id, first_name, last_name, email, role)
VALUES
  ('1','bone', 'bone', 'tandeey@gmail.com', (SELECT id from roles WHERE name = 'admin')),
  ('2','gurr', 'gurr', 'sigurd.l@gmail.com', (SELECT id from roles WHERE name = 'admin')),
  ('3','garr', 'garr', 'pelle@pelle.com', (SELECT id from roles WHERE name = 'user'));

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

INSERT INTO recipe(name, description, language, calories, procedure, spicy, created, modified, created_by)
VALUES
  ('Lasser', 'En optimal lasser', (SELECT id from language WHERE name = 'Norwegian'), 
    1000, 'Hvordan lager man en lasser? Jo.. Nå skal du høre..', 1, Now(), Now(), (SELECT id from users WHERE first_name = 'garr')),
  ('Taccer', 'En optimal taccer', (SELECT id from language WHERE name = 'Norwegian'), 
    1000, 'Hvordan lager man en taccer? Jo.. Nå skal du høre..', 2, Now(), Now(), (SELECT id from users WHERE first_name = 'garr'));

INSERT INTO recipe_in_tag (recipe_id, tag_id)
VALUES
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id FROM tags WHERE name = 'italiensk')),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id FROM tags WHERE name = 'pasta'));

INSERT INTO ingredient_in_recipe(recipe_id, ingredient_id, amount)
VALUES
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Kjøttdeig'), 400),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Melk'), 300),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Vann'), 200),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Lasagne plater'), 1),
  ((SELECT id from recipe WHERE name = 'Lasser'), (SELECT id from ingredient WHERE name = 'Lasagne krydder'), 1);

INSERT INTO user_star_rate_recipe(user_id, recipe_id, stars)
VALUES
  ((SELECT id from users WHERE first_name = 'garr'), (SELECT id from recipe WHERE name = 'Lasser'), 5.0);

INSERT INTO user_yes_no_rate_ingredient(user_id, ingredient_id, rating)
VALUES
  ((SELECT id from users WHERE first_name = 'garr'), (SELECT id from recipe WHERE name = 'Lasser'), true);



END;