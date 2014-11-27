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
DROP TABLE IF EXISTS oauth2info CASCADE;
DROP TABLE IF EXISTS oauth1info CASCADE;
DROP TABLE IF EXISTS passwordinfo CASCADE;
DROP TABLE IF EXISTS userlogininfo CASCADE;
DROP TABLE IF EXISTS logininfo;

CREATE TABLE language (
	id serial8 primary key,
	locale text,
	name text
);

CREATE TABLE tags (
	id serial8 primary key,
	name text
);

CREATE TABLE ingredient (
	id serial8 primary key,
	name text,
	image json
);

CREATE TABLE unit (
	id serial8 primary key,
	name text
);

CREATE TABLE user_preferences (
	id serial8 primary key
	/* More definitions of prefs */
);

CREATE TABLE users (
	id text primary key,
	first_name text,
	last_name text,
	/*password text,*/
	email text,	
	/*city text,*/
	/*age int,*/
	image text,
	role text
	/* user_preferences int8 references user_preferences(id) ON DELETE CASCADE*/
);

CREATE TABLE recipe (
	id serial8 primary key,
	name text,
	image text,
	description text,
	language int8 references language(id) ON DELETE CASCADE,
	calories real,
	procedure text,
	spicy int,
	created timestamptz,
	modified timestamptz,
	created_by text references users(id) ON DELETE CASCADE
	/* Public rating (matirialized view) */
);

CREATE TABLE recipe_in_tag (
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	tag_id int8 references tags(id) ON DELETE CASCADE
);

CREATE TABLE ingredient_in_recipe (
	recipe_id int8 references recipe(id),
	ingredient_id int8 references ingredient(id),
	amount real
);

CREATE TABLE user_star_rate_recipe ( /* id references */
	user_id text,
	recipe_id int8,
	stars real
);

CREATE TABLE user_yes_no_rate_recipe ( /* id references */
	user_id text,
	recipe_id int8,
	rating boolean
);

CREATE TABLE user_yes_no_rate_ingredient ( /* id references */
	user_id text,
	ingredient_id int8,
	rating boolean
);

CREATE TABLE logininfo (
	id serial8 primary key,
	provider_id text,
	provider_key text
);

CREATE TABLE userlogininfo (
	user_id text NOT NULL,
	login_info_id int8 NOT NULL
);

CREATE TABLE passwordinfo (
	hasher text,
	password text,
	salt text,
	login_info_id int8
);

CREATE TABLE oauth1info (
	id serial8 primary key,
	token text,
	secret text,
	login_info_id int8
);

CREATE TABLE oauth2info (
	id serial8 primary key,
	accesstoken text,
	tokentype text,
	expiresin int8,
	refreshtoken text,
	login_info_id int8
);

END;