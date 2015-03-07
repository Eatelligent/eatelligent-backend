BEGIN;

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
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS ingredient_in_tag;
DROP TABLE IF EXISTS ingredient_tag;
DROP TABLE IF EXISTS cold_start;
DROP TABLE IF EXISTS user_cold_start;
DROP TABLE IF EXISTS about;
DROP TABLE IF EXISTS cold_start;
DROP TABLE IF EXISTS user_viewed_recipe;


CREATE TABLE language (
	id serial8 primary key,
	locale text NOT NULL UNIQUE,
	name text NOT NULL UNIQUE
);

CREATE TABLE tags (
	id serial8 primary key,
	name text NOT NULL UNIQUE
);

CREATE TABLE unit (
	id serial8 primary key,
	name text NOT NULL UNIQUE
);

CREATE TABLE ingredient (
	id serial8 primary key,
	name text NOT NULL UNIQUE,
	image json,
	default_unit int8 references unit(id) ON DELETE CASCADE,
	netto real,
	water real,
	calories real,
	proteins real,
	fat real,
	saturated_fat real,
	trans_fat real,
	mono_fat real,
	poly_fat real,
	cholesterol real,
	carbs real,
	starch real,
	mono_di real,
	sugar real,
	fiber real,
	nacl real,
	alcho real,
	retinol real,
	b_caro real,
	vit_a real,
	vit_d real,
	vit_e real,
	vit_b1 real,
	vit_b2 real,
	niacin real,
	niaekv real,
	vit_b6 real,
	folat real,
	vit_b12 real,
	vit_c real,
	ca real,
	fe real,
	na real,
	potassium real,
	mg real,
	zn real,
	iodine real,
	se real,
	cu real,
	phosphorus real
);	

CREATE TABLE users (
	id serial8 primary key,
	first_name text,
	last_name text,
	email text NOT NULL UNIQUE,
	image text,
	role text,
	created timestamp,
	recipe_language int8 references language(id),
	app_language int8 references language(id),
	city text,
	country text,
	sex text,
	year_born int CONSTRAINT age_check CHECK (year_born > 1900 AND year_born < 2016),
	enrolled boolean,
	metric_system boolean
);

CREATE TABLE user_preferences (
	user_id int8 references users(id) ON DELETE CASCADE
	/* More definitions of prefs */
);

CREATE TABLE recipe (
	id serial8 primary key,
	name text NOT NULL,
	image text,
	description text,
	language int8 NOT NULL references language(id) ON DELETE CASCADE,
	calories real,
	procedure text,
	spicy int CONSTRAINT spicy_check CHECK (spicy > 0 AND spicy < 4),
	time int CONSTRAINT time_check CHECK (time > 0),
	difficulty text,
	source text,
	created timestamp NOT NULL,
	modified timestamp NOT NULL,
	published timestamp,
	deleted timestamp,
	created_by int8 NOT NULL references users(id) ON DELETE CASCADE
	/* Public rating (matirialized view) */
);

CREATE TABLE favorites (
	user_id int8 references users(id) ON DELETE CASCADE,
	recipe_id int8 references recipe(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX favorites_idx
ON favorites(user_id, recipe_id);

CREATE TABLE recipe_in_tag (
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	tag_id int8 references tags(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX recipe_in_tag_idx
ON recipe_in_tag(recipe_id, tag_id);

CREATE TABLE ingredient_tag (
	id serial8 primary key,
	name text NOT NULL UNIQUE
);

CREATE TABLE ingredient_in_tag (
	ingredient_id int8 references ingredient(id) ON DELETE CASCADE,
	tag_id int8 references ingredient_tag(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX ingredient_in_tag_idx
ON ingredient_in_tag(ingredient_id, tag_id);

CREATE TABLE ingredient_in_recipe (
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	ingredient_id int8 references ingredient(id) ON DELETE CASCADE,
	unit_id int8 references unit(id) ON DELETE CASCADE,
	amount real
);

CREATE TABLE user_star_rate_recipe ( /* id references */
	user_id int8 references users(id) ON DELETE CASCADE,
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	rating real CONSTRAINT rating_check CHECK (rating >= 0.0 AND rating <= 5.0),
	created timestamp,
	created_long int8
);

CREATE UNIQUE INDEX user_star_rate_recipe_idx 
ON user_star_rate_recipe (user_id, recipe_id);

CREATE TABLE user_yes_no_rate_recipe ( /* id references */
	user_id int8 references users(id) ON DELETE CASCADE,
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	rating int,
	last_seen timestamp
);

CREATE UNIQUE INDEX user_yes_no_rate_recipe_idx
ON user_yes_no_rate_recipe(user_id, recipe_id);

CREATE TABLE user_viewed_recipe(
	user_id int8 NOT NULL references users(id) ON DELETE CASCADE,
	recipe_id int8 NOT NULL references recipe(id) ON DELETE CASCADE,
	duration int8 NOT NULL,
	last_seen timestamp NOT NULL
);

CREATE UNIQUE INDEX user_viewed_recipe_idx
ON user_viewed_recipe(user_id, recipe_id);

CREATE TABLE logininfo (
	id serial8 primary key,
	provider_id text,
	provider_key text
);

CREATE TABLE userlogininfo (
	user_id int8 NOT NULL,
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

CREATE TABLE cold_start (
	id serial8 primary key,
	image text NOT NULL,
	identifier text UNIQUE NOT NULL,
	description text NOT NULL,
	ingredient_tags hstore,
	recipe_tags hstore
);

CREATE TABLE user_cold_start (
	user_id int8 NOT NULL references users(id) ON DELETE CASCADE,
	cold_start_id int8 NOT NULL references cold_start(id) ON DELETE CASCADE,
	answer boolean,
	answer_time timestamp,
);


-- Tables for storing values for a user to different tags
CREATE TABLE user_ingredient_tag (
	user_id int8 references users(id) ON DELETE CASCADE,
	ingredient_tag_id int8 references ingredient_tag(id) ON DELETE CASCADE,
	value real
);

CREATE TABLE user_recipe_tag (
	user_id int8 references users(id) ON DELETE CASCADE,
	recipe_tag_id int8 references tags(id) ON DELETE CASCADE,
	value real
);

CREATE TABLE given_recommendation (
	user_id int8 references users(id) ON DELETE CASCADE,
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	type text,
	created timestamp,
	data json
);

CREATE TABLE measure_rating (
	user_id int8 references users(id) ON DELETE CASCADE,
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	created timestamp,
	rating real,
	source text,
	data json
);



END;
