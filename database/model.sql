BEGIN;

DROP TABLE IF EXISTS user_yes_no_rate_ingredient CASCADE;
DROP TABLE IF EXISTS user_yes_no_rate_recipe CASCADE;
DROP TABLE IF EXISTS user_star_rate_recipe CASCADE;
DROP TABLE IF EXISTS recipe_in_tag CASCADE;
DROP TABLE IF EXISTS ingredient_in_recipe CASCADE;
DROP TABLE IF EXISTS tags CASCADE;
DROP TABLE IF EXISTS ingredient CASCADE;
DROP TABLE IF EXISTS recipe CASCADE;
DROP TABLE IF EXISTS unit CASCADE;
DROP TABLE IF EXISTS user_preferences CASCADE;
DROP TABLE IF EXISTS language CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

CREATE TABLE language (
	id serial8 primary key,
	locale text,
	name text
);

CREATE TABLE recipe (
	id serial8 primary key,
	name text,
	image json,
	description text,
	language int8 references language(id) ON DELETE CASCADE,
	calories real,
	procedure text,
	created timestamptz,
	modified timestamptz
	/* Public rating (matirialized view) */
);

CREATE TABLE tags (
	id serial8 primary key,
	name text
);

CREATE TABLE recipe_in_tag (
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	tag_id int8 references tags(id) ON DELETE CASCADE
);

CREATE TABLE ingredient (
	id serial8 primary key,
	name text,
	image json
);

CREATE TABLE ingredient_in_recipe (
	recipe_id int8 references recipe(id),
	ingredient_id int8 references ingredient(id),
	amount real
);

CREATE TABLE unit (
	id serial8 primary key,
	name text
);

CREATE TABLE user_preferences (
	id serial8 primary key
	/* More definitions of prefs */
);

CREATE TABLE roles (
	id serial8 primary key,
	name text
);

CREATE TABLE users (
	id serial8 primary key,
	role int8 references roles(id),
	name text,
	password text,
	email text,	
	city text,
	age int,
	image text,
	user_preferences int8 references user_preferences(id) ON DELETE CASCADE
);

CREATE TABLE user_star_rate_recipe ( /* id references */
	user_id int8,
	recipe_id int8,
	stars real
);

CREATE TABLE user_yes_no_rate_recipe ( /* id references */
	user_id int8,
	recipe_id int8,
	rating boolean
);

CREATE TABLE user_yes_no_rate_ingredient ( /* id references */
	user_id int8,
	ingredient_id int8,
	rating boolean
);

END;