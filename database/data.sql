BEGIN;

INSERT INTO language(locale, name)
VALUES
  ('NO-no', 'Norwegian'),
  ('EN-en', 'English');

INSERT INTO users(first_name, last_name, email, image, role, created)
VALUES
  ('admin', 'admin', 'admin@admin.com', null, 'admin', NOW()),
  ('gurr', 'gurr', 'gurr@admin.com', null, 'user', NOW()),
  ('bone', 'bone', 'bone@admin.com', null, 'user', NOW()),
  ('garr', 'garr', 'garr@admin.com', null, 'user', NOW());

INSERT INTO logininfo(provider_id, provider_key)
VALUES
  ('credentials', 'admin@admin.com'),
  ('credentials', 'gurr@admin.com'),
  ('credentials', 'bone@admin.com'),
  ('credentials', 'garr@admin.com');

INSERT INTO userlogininfo(user_id, login_info_id)
VALUES
  ((SELECT id FROM users WHERE role = 'admin'), (SELECT id FROM logininfo WHERE provider_key = 'admin@admin.com')),
  ((SELECT id FROM users WHERE email = 'gurr@admin.com'), (SELECT id FROM logininfo WHERE provider_key = 'gurr@admin.com')),
  ((SELECT id FROM users WHERE email = 'bone@admin.com'), (SELECT id FROM logininfo WHERE provider_key = 'bone@admin.com')),
  ((SELECT id FROM users WHERE email = 'garr@admin.com'), (SELECT id FROM logininfo WHERE provider_key = 'garr@admin.com'));

INSERT INTO passwordinfo(hasher, password, login_info_id)
VALUES
  ('bcrypt', crypt('admin', gen_salt('bf')), (SELECT id FROM logininfo WHERE provider_key = 'admin@admin.com')),
  ('bcrypt', crypt('admin', gen_salt('bf')), (SELECT id FROM logininfo WHERE provider_key = 'gurr@admin.com')),
  ('bcrypt', crypt('admin', gen_salt('bf')), (SELECT id FROM logininfo WHERE provider_key = 'bone@admin.com')),
  ('bcrypt', crypt('admin', gen_salt('bf')), (SELECT id FROM logininfo WHERE provider_key = 'garr@admin.com'));

INSERT INTO unit(name)
VALUES
  ('liter'),
  ('gram'),
  ('kilogram'),
  ('teaspoon'),
  ('tablespoon'),
  ('piece'),
  ('deciliter');

INSERT INTO ingredient(name, default_unit)
VALUES
  ('lasagnekrydder', (SELECT id FROM unit WHERE name = 'piece')),
  ('ost', (SELECT id FROM unit WHERE name = 'gram')),
  ('lasagneplater', (SELECT id FROM unit WHERE name = 'piece')),
  ('kjøttdeig', (SELECT id FROM unit WHERE name = 'gram'));

INSERT INTO recipe(name, language, created, modified, created_by)
VALUES
  ('lasser', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr')),
  ('taccer', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr')),
  ('suppe', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr')),
  ('ribbe', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr'));

INSERT INTO ingredient_in_recipe(recipe_id, ingredient_id, unit_id, amount)
VALUES
  ((SELECT id FROM recipe WHERE name = 'lasser'), (SELECT id FROM ingredient WHERE name = 'lasagnekrydder'), (SELECT id FROM unit WHERE name = 'piece'), 1.0),
  ((SELECT id FROM recipe WHERE name = 'lasser'), (SELECT id FROM ingredient WHERE name = 'lasagneplater'), (SELECT id FROM unit WHERE name = 'piece'), 1.0),
  ((SELECT id FROM recipe WHERE name = 'lasser'), (SELECT id FROM ingredient WHERE name = 'ost'), (SELECT id FROM unit WHERE name = 'gram'), 200.0),
  ((SELECT id FROM recipe WHERE name = 'lasser'), (SELECT id FROM ingredient WHERE name = 'kjøttdeig'), (SELECT id FROM unit WHERE name = 'gram'), 400.0);

INSERT INTO user_star_rate_recipe
VALUES
  ((SELECT id FROM users WHERE first_name = 'garr'), (SELECT id FROM recipe WHERE name = 'lasser'), 3.0, NOW(), 0),
  ((SELECT id FROM users WHERE first_name = 'garr'), (SELECT id FROM recipe WHERE name = 'taccer'), 4.0, NOW(), 0),
  ((SELECT id FROM users WHERE first_name = 'gurr'), (SELECT id FROM recipe WHERE name = 'taccer'), 4.0, NOW(), 0),
  ((SELECT id FROM users WHERE first_name = 'gurr'), (SELECT id FROM recipe WHERE name = 'ribbe'), 3.0, NOW(), 0),
  ((SELECT id FROM users WHERE first_name = 'gurr'), (SELECT id FROM recipe WHERE name = 'suppe'), 5.0, NOW(), 0),
  ((SELECT id FROM users WHERE first_name = 'bone'), (SELECT id FROM recipe WHERE name = 'lasser'), 3.0, NOW(), 0),
  ((SELECT id FROM users WHERE first_name = 'bone'), (SELECT id FROM recipe WHERE name = 'suppe'), 5.0, NOW(), 0),
  ((SELECT id FROM users WHERE first_name = 'bone'), (SELECT id FROM recipe WHERE name = 'ribbe'), 3.0, NOW(), 0);


END;