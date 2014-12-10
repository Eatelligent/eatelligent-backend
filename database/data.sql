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
  ('credentials', 'admin@admin.com');

INSERT INTO userlogininfo(user_id, login_info_id)
VALUES
  ((SELECT id FROM users WHERE role = 'admin'), (SELECT id FROM logininfo WHERE provider_key = 'admin@admin.com'));

INSERT INTO passwordinfo(hasher, password, login_info_id)
VALUES
  (
    'bcrypt',
    crypt('admin', gen_salt('bf')),
    (SELECT id FROM logininfo WHERE provider_key = 'admin@admin.com')
    );

INSERT INTO recipe(name, language, created, modified, created_by)
VALUES
  ('lasser', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr')),
  ('taccer', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr')),
  ('suppe', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr')),
  ('ribbe', (SELECT id FROM language WHERE name = 'Norwegian'), NOW(), NOW(), (SELECT id FROM users WHERE first_name = 'garr'));

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