BEGIN;

INSERT INTO language(locale, name)
VALUES
  ('NO-no', 'Norwegian'),
  ('EN-en', 'English');

INSERT INTO users(first_name, last_name, email, image, role, created, enrolled, metric_system)
VALUES
  ('admin', 'admin', 'admin@admin.com', null, 'admin', NOW(), false, true);

INSERT INTO logininfo(provider_id, provider_key)
VALUES
  ('credentials', 'admin@admin.com');

INSERT INTO userlogininfo(user_id, login_info_id)
VALUES
  ((SELECT id FROM users WHERE role = 'admin'), (SELECT id FROM logininfo WHERE provider_key = 'admin@admin.com'));

INSERT INTO passwordinfo(hasher, password, login_info_id)
VALUES
  ('bcrypt', crypt('admin', gen_salt('bf')), (SELECT id FROM logininfo WHERE provider_key = 'admin@admin.com'));

INSERT INTO unit(name)
VALUES
  ('liter'),
  ('gram'),
  ('kilogram'),
  ('teaspoon'),
  ('tablespoon'),
  ('pcs'),
  ('deciliter'),
  ('bag'),
  ('can'),
  ('clove'),
  ('slice'),
  ('cup'),
  ('leafs'),
  ('pinch');

INSERT INTO ingredient_tag(name)
VALUES
  ('Meat'),
  ('Fish'),
  ('Seafood'),
  ('Spice'),
  ('Spicy'),
  ('Salty'),
  ('Flour'),
  ('Extras'),
  ('Fruit'),
  ('Berries'),
  ('Cheese'),
  ('Milk product'),
  ('Pasta'),
  ('Potatoes'),
  ('Green');

END;