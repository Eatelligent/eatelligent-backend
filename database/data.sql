BEGIN;

INSERT INTO language(locale, name)
VALUES
  ('NO-no', 'Norwegian'),
  ('EN-en', 'English');

INSERT INTO users(id, first_name, last_name, email, image, role, created)
VALUES
  ('a8535a42-761f-11e4-b116-123b93f75cba', 'admin', 'admin', 'admin@admin.com', null, 'admin', NOW());

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


END;