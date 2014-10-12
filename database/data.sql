BEGIN;

INSERT INTO roles (name)
VALUES
 ('user'),
 ('admin');

INSERT INTO users (name, password, email, role)
VALUES
  ('bone', 'bone', 'tandeey@gmail.com', 2),
  ('gurr', 'gurr', 'sigurd.l@gmail.com', 2),
  ('garr', 'garr', 'pelle@pelle.com', 1);

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
  ('Lasser', 'En optimal lasser', 1, 1000, 'Hvordan lager man en lasser? Jo.. Nå skal du høre..', Now(), Now());

INSERT INTO ingredient_in_recipe(recipe_id, ingredient_id, amount)
VALUES
  (1, 1, 400),
  (1, 2, 300),
  (1, 3, 200),
  (1, 4, 1),
  (1, 5, 1);

INSERT INTO user_star_rate_meal(user_id, meal_id, stars)
VALUES
  (3, 1, 5.0);

INSERT INTO user_yes_no_rate_ingredient(user_id, ingredient_id, rating)
VALUES
  (3, 1, true);



END;