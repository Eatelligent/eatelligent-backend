BEGIN;

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

CREATE TABLE tag (
	id serial8 primary key,
	name text
);

CREATE TABLE recipe_in_tag (
	recipe_id int8 references recipe(id) ON DELETE CASCADE,
	tag_id int8 references tag(id) ON DELETE CASCADE
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
	city text,
	age int,
	image text,
	user_preferences int8 references user_preferences(id) ON DELETE CASCADE
);

CREATE TABLE user_star_rate_meal ( /* id references */
	user_id int8,
	meal_id int8,
	stars int
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