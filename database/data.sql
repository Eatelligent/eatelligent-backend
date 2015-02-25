--
-- PostgreSQL database dump
--
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


SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cold_start; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE cold_start (
    id bigint NOT NULL,
    image text NOT NULL,
    identifier text NOT NULL,
    description text NOT NULL
);


ALTER TABLE public.cold_start OWNER TO mealchooser;

--
-- Name: cold_start_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE cold_start_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.cold_start_id_seq OWNER TO mealchooser;

--
-- Name: cold_start_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE cold_start_id_seq OWNED BY cold_start.id;


--
-- Name: favorites; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE favorites (
    user_id bigint,
    recipe_id bigint
);


ALTER TABLE public.favorites OWNER TO mealchooser;

--
-- Name: ingredient; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE ingredient (
    id bigint NOT NULL,
    name text NOT NULL,
    image json,
    default_unit bigint,
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


ALTER TABLE public.ingredient OWNER TO mealchooser;

--
-- Name: ingredient_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE ingredient_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ingredient_id_seq OWNER TO mealchooser;

--
-- Name: ingredient_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE ingredient_id_seq OWNED BY ingredient.id;


--
-- Name: ingredient_in_recipe; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE ingredient_in_recipe (
    recipe_id bigint,
    ingredient_id bigint,
    unit_id bigint,
    amount real
);


ALTER TABLE public.ingredient_in_recipe OWNER TO mealchooser;

--
-- Name: ingredient_in_tag; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE ingredient_in_tag (
    ingredient_id bigint,
    tag_id bigint
);


ALTER TABLE public.ingredient_in_tag OWNER TO mealchooser;

--
-- Name: ingredient_tag; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE ingredient_tag (
    id bigint NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.ingredient_tag OWNER TO mealchooser;

--
-- Name: ingredient_tag_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE ingredient_tag_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ingredient_tag_id_seq OWNER TO mealchooser;

--
-- Name: ingredient_tag_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE ingredient_tag_id_seq OWNED BY ingredient_tag.id;


--
-- Name: language; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE language (
    id bigint NOT NULL,
    locale text NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.language OWNER TO mealchooser;

--
-- Name: language_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE language_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.language_id_seq OWNER TO mealchooser;

--
-- Name: language_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE language_id_seq OWNED BY language.id;


--
-- Name: logininfo; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE logininfo (
    id bigint NOT NULL,
    provider_id text,
    provider_key text
);


ALTER TABLE public.logininfo OWNER TO mealchooser;

--
-- Name: logininfo_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE logininfo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.logininfo_id_seq OWNER TO mealchooser;

--
-- Name: logininfo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE logininfo_id_seq OWNED BY logininfo.id;


--
-- Name: oauth1info; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE oauth1info (
    id bigint NOT NULL,
    token text,
    secret text,
    login_info_id bigint
);


ALTER TABLE public.oauth1info OWNER TO mealchooser;

--
-- Name: oauth1info_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE oauth1info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.oauth1info_id_seq OWNER TO mealchooser;

--
-- Name: oauth1info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE oauth1info_id_seq OWNED BY oauth1info.id;


--
-- Name: oauth2info; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE oauth2info (
    id bigint NOT NULL,
    accesstoken text,
    tokentype text,
    expiresin bigint,
    refreshtoken text,
    login_info_id bigint
);


ALTER TABLE public.oauth2info OWNER TO mealchooser;

--
-- Name: oauth2info_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE oauth2info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.oauth2info_id_seq OWNER TO mealchooser;

--
-- Name: oauth2info_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE oauth2info_id_seq OWNED BY oauth2info.id;


--
-- Name: passwordinfo; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE passwordinfo (
    hasher text,
    password text,
    salt text,
    login_info_id bigint
);


ALTER TABLE public.passwordinfo OWNER TO mealchooser;

--
-- Name: recipe; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE recipe (
    id bigint NOT NULL,
    name text NOT NULL,
    image text,
    description text,
    language bigint NOT NULL,
    calories real,
    procedure text,
    spicy integer,
    "time" integer,
    difficulty text,
    created timestamp without time zone NOT NULL,
    modified timestamp without time zone NOT NULL,
    published timestamp without time zone,
    deleted timestamp without time zone,
    created_by bigint NOT NULL,
    source text,
    CONSTRAINT spicy_check CHECK (((spicy > 0) AND (spicy < 4))),
    CONSTRAINT time_check CHECK (("time" > 0))
);


ALTER TABLE public.recipe OWNER TO mealchooser;

--
-- Name: recipe_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE recipe_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.recipe_id_seq OWNER TO mealchooser;

--
-- Name: recipe_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE recipe_id_seq OWNED BY recipe.id;


--
-- Name: recipe_in_tag; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE recipe_in_tag (
    recipe_id bigint,
    tag_id bigint
);


ALTER TABLE public.recipe_in_tag OWNER TO mealchooser;

--
-- Name: tags; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE tags (
    id bigint NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.tags OWNER TO mealchooser;

--
-- Name: tags_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE tags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tags_id_seq OWNER TO mealchooser;

--
-- Name: tags_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE tags_id_seq OWNED BY tags.id;


--
-- Name: unit; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE unit (
    id bigint NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.unit OWNER TO mealchooser;

--
-- Name: unit_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE unit_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.unit_id_seq OWNER TO mealchooser;

--
-- Name: unit_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE unit_id_seq OWNED BY unit.id;


--
-- Name: user_cold_start; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE user_cold_start (
    user_id bigint NOT NULL,
    cold_start_id bigint NOT NULL,
    answer boolean,
    answer_time timestamp without time zone
);


ALTER TABLE public.user_cold_start OWNER TO mealchooser;

--
-- Name: user_preferences; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE user_preferences (
    user_id bigint
);


ALTER TABLE public.user_preferences OWNER TO mealchooser;

--
-- Name: user_star_rate_recipe; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE user_star_rate_recipe (
    user_id bigint,
    recipe_id bigint,
    rating real,
    created timestamp without time zone,
    created_long bigint,
    CONSTRAINT rating_check CHECK (((rating >= (0.0)::double precision) AND (rating <= (5.0)::double precision)))
);


ALTER TABLE public.user_star_rate_recipe OWNER TO mealchooser;

--
-- Name: user_viewed_recipe; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE user_viewed_recipe (
    user_id bigint NOT NULL,
    recipe_id bigint NOT NULL,
    duration bigint NOT NULL,
    last_seen timestamp without time zone NOT NULL
);


ALTER TABLE public.user_viewed_recipe OWNER TO mealchooser;

--
-- Name: user_yes_no_rate_recipe; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE user_yes_no_rate_recipe (
    user_id bigint,
    recipe_id bigint,
    rating integer,
    last_seen timestamp without time zone
);


ALTER TABLE public.user_yes_no_rate_recipe OWNER TO mealchooser;

--
-- Name: userlogininfo; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE userlogininfo (
    user_id bigint NOT NULL,
    login_info_id bigint NOT NULL
);


ALTER TABLE public.userlogininfo OWNER TO mealchooser;

--
-- Name: users; Type: TABLE; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE TABLE users (
    id bigint NOT NULL,
    first_name text,
    last_name text,
    email text NOT NULL,
    image text,
    role text,
    created timestamp without time zone,
    recipe_language bigint,
    app_language bigint,
    city text,
    country text,
    sex text,
    year_born integer,
    enrolled boolean,
    metric_system boolean,
    CONSTRAINT age_check CHECK (((year_born > 1900) AND (year_born < 2016)))
);


ALTER TABLE public.users OWNER TO mealchooser;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: mealchooser
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO mealchooser;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mealchooser
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY cold_start ALTER COLUMN id SET DEFAULT nextval('cold_start_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY ingredient ALTER COLUMN id SET DEFAULT nextval('ingredient_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY ingredient_tag ALTER COLUMN id SET DEFAULT nextval('ingredient_tag_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY language ALTER COLUMN id SET DEFAULT nextval('language_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY logininfo ALTER COLUMN id SET DEFAULT nextval('logininfo_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY oauth1info ALTER COLUMN id SET DEFAULT nextval('oauth1info_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY oauth2info ALTER COLUMN id SET DEFAULT nextval('oauth2info_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY recipe ALTER COLUMN id SET DEFAULT nextval('recipe_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY tags ALTER COLUMN id SET DEFAULT nextval('tags_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY unit ALTER COLUMN id SET DEFAULT nextval('unit_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Data for Name: cold_start; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY cold_start (id, image, identifier, description) FROM stdin;
1	http://res.cloudinary.com/hnjelkrui/image/upload/v1423661777/meat-hi_copy_bs47gz.jpg	meat	Do you like meat?
2	http://res.cloudinary.com/hnjelkrui/image/upload/v1423661876/spicy_bdur6z.jpg	spicy	Do you like spicy food?
3	http://res.cloudinary.com/hnjelkrui/image/upload/v1423661797/chef_owzejt.jpg	skills	Do you view yourself as a good chef?
4	http://res.cloudinary.com/hnjelkrui/image/upload/v1423661835/fish_q3ga7g.png	fish	Do you like fish?
5	http://res.cloudinary.com/hnjelkrui/image/upload/v1423667343/chicken_copy_wthzdg.jpg	chicken	Do you like chicken?
\.


--
-- Name: cold_start_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('cold_start_id_seq', 5, true);


--
-- Data for Name: favorites; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY favorites (user_id, recipe_id) FROM stdin;
1	23
1	42
11	23
\.


--
-- Data for Name: ingredient; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY ingredient (id, name, image, default_unit, netto, water, calories, proteins, fat, saturated_fat, trans_fat, mono_fat, poly_fat, cholesterol, carbs, starch, mono_di, sugar, fiber, nacl, alcho, retinol, b_caro, vit_a, vit_d, vit_e, vit_b1, vit_b2, niacin, niaekv, vit_b6, folat, vit_b12, vit_c, ca, fe, na, potassium, mg, zn, iodine, se, cu, phosphorus) FROM stdin;
13	Margarin eller olje til steking	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
30	Olje	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
36	Vann	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
57	Margarin	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
91	Honning	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
119	Balsamicoeddik	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
120	Olivenolje	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
131	Smør til steking	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
2	Tomat	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
3	Pizzabunn	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
4	Creme fraiche	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
9	Wokgrønnsaker	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
7	Salt	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
8	Pepper	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
12	Kjøttdeig av svin	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
10	Grønnsaksbulljong	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
11	Villris	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
32	Laksefilet	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
14	Finhakket løk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
15	Gulrot	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
17	Maiskorn	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
18	Sukker	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
19	Potet	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
20	Smør	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
21	Melk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
22	Muskatnøtt	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
97	Sukkererter	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
24	Grillet kylling	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
25	Rød paprika	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
27	Rødløk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
26	Grønn frisk asparges	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
28	Stangselleri	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
29	Grønt eple	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
50	Torskefilet	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
37	Ferdigkokte poteter	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
38	Purre	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
40	Couscous	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
39	Tandoorikrydder	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
41	Hønsebuljongterning	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
5	Pesto	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
31	Nøtte- og frøblanding til salater	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
34	Hodekål	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
46	Grønn Paprika	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
47	Løk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
49	Hvetemel	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
51	Grønne erter	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
54	Bacon	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
52	Kremfløte	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
48	Fiskebuljong (utblandet)	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
55	Sjampinjong i skiver	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
113	Røkt skinke	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
56	Purre i ringer	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
58	Brokkoli	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
61	Sopp i skiver	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
59	Strimlet kokt skinke	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
60	Hvitløk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
62	Vårløk i skiver	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
63	Nudler med krydder	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
64	Svinekjøtt i strimler	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
67	Avokado	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
65	Isbergsalat	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
66	Ruccula	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
42	Sitron	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
44	Tzatziki	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
130	Smårettsbacon	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
79	Eple	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
70	Kjøttdeig	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
69	Pinjekjerner	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
84	Buljongpulver	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
83	Wienerpølser	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
73	Lime	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
71	Rød chili	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
72	Frisk ingefær	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
90	Salat	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
74	Frisk koriander	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
75	Gresk yoghurt	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
98	Spinat	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
103	Pære	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
101	Rødkål	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
109	Matfløte	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
110	Kinakål	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
112	Fullkornsris	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
114	Pasta	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
78	Babysalat	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
116	Soltørkede tomater	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
121	Rømme	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
124	Vårløk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
123	Malt spisskummen	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
122	Chilipulver	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
126	Matyoghurt	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
127	Salatblad	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
133	Tacokrydder	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
81	Lasagne krydder	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
86	Hermetiske tomater	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
80	Lasagne plater	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
135	Tacosaus	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
85	Spaghetti	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
118	Friske urter	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
106	Kjøttpølse	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
99	Kyllingfilet	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
87	Tørkede urter	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
88	Roastbiff	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
89	Plomme	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
95	Frisk kruspersille	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
94	Chilibønner	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
92	Nakkekoteletter av svin	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
96	Hermetiske hvite bønner	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
23	Revet hvitost	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
129	Medisterkake	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
100	Fennikel	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
102	Rosmarin	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
105	Hvit pepper	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
35	Egg	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
104	Frisk basilikum	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
117	Revet Parmesan	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
107	Dypfryst lapskausblanding	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
111	Woksaus	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
108	Kjøttbuiljongterning	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
125	Pitabrød	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
132	Dypfryste erter	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
128	Tikka masala krydder	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
82	Grøtris	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
6	Kotelett	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
134	Hermetiske ananasbiter	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
77	Hamburgerbrød	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
68	Sitronsaft	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
150	Hvitvin eller cider	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
154	Olje til smøring av form	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
160	Eddik	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
16	Tomatpuré	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
33	Sitronpepper	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
53	Frisk gressløk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
159	Karri	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
43	Bladpersille	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
76	Agurk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
115	Finhakket hvitløk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
93	Gul paprika	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
153	Grønnsaksblanding	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
145	Sellerirot	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
146	Aspargesbønner	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
147	Nudler	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
138	Paprika	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
156	Seifilet	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
157	Cherrytomat	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
139	Fiskesaus	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
45	Ferdig asian soup	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
152	Hvit saus	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
158	Ketchup	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
148	Grønn squash	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
149	Sorte oliven	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
142	Kokosmelk	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
137	Kyllingstrimler	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
144	Hakkede pinjekjerner	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
1	Falukorv	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
162	Hakkede hermetiske tomater	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
151	Smårettskinke	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
143	Biff av ytrefilet	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
161	Byggris	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
141	Hønsebuljong utblandet	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
136	Pastasaus carbonara	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
140	Soyasaus	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
155	Brødrasp	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
\.


--
-- Name: ingredient_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('ingredient_id_seq', 162, true);


--
-- Data for Name: ingredient_in_recipe; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY ingredient_in_recipe (recipe_id, ingredient_id, unit_id, amount) FROM stdin;
29	70	2	100
29	7	4	0.125
29	123	4	0.125
29	122	4	0.5
29	13	5	0.200000003
29	46	6	0.25
29	25	6	0.25
3	7	4	1
3	8	4	1
3	12	2	150
3	13	5	1
3	14	6	0.125
3	15	6	1
3	16	9	0.25
3	17	12	0.25
29	71	6	0.25
29	47	6	0.25
29	94	9	0.25
29	86	9	0.25
22	6	6	1
22	7	4	0.125
22	8	4	0.125
22	13	5	0.5
3	18	4	0.25
3	19	2	300
3	20	2	30
3	21	7	0.5
3	22	4	0.25
3	23	7	1
22	38	6	0.25
22	101	8	0.25
22	102	4	0.25
22	103	6	0.25
24	13	5	0.5
24	7	4	0.125
24	8	4	0.125
24	106	2	100
24	107	6	0.25
13	64	2	150
13	68	4	0.5
13	7	4	0.5
13	13	5	1
13	66	6	0.25
13	65	6	1
13	67	6	0.5
13	27	6	0.5
13	69	7	0.25
10	13	5	0.5
10	12	2	100
10	54	2	25
10	55	2	75
10	56	2	25
10	23	7	0.5
8	13	5	0.5
8	12	2	100
8	46	6	0.25
8	47	6	1
8	45	8	0.5
1	2	6	0.5
1	3	2	170
1	4	2	100
1	5	5	0.75
1	1	6	0.25
30	128	5	4
30	13	5	0.5
30	93	6	0.25
30	125	6	1
30	99	6	1
30	127	6	1
30	124	6	1
30	25	6	0.25
30	126	7	0.5
28	47	2	0.125
28	114	2	100
28	70	2	100
28	8	4	0.125
28	7	4	0.125
28	121	5	1
28	120	5	0.5
28	119	5	0.5
28	118	5	0.5
28	13	5	0.5
28	2	6	0.75
25	36	1	0.300000012
25	64	2	75
25	8	4	0.125
25	7	4	0.125
25	13	5	0.5
25	108	6	0.5
25	47	6	0.25
25	19	6	0.5
25	58	6	0.25
25	109	7	0.5
19	92	6	1
19	7	4	0.125
19	13	5	0.5
19	47	6	0.25
19	93	6	0.5
19	28	6	1
19	94	9	0.5
19	95	5	0.5
16	36	7	1
16	7	4	0.25
16	21	1	0.25
16	82	7	0.5
7	44	2	100
7	32	2	150
7	43	5	1
7	30	5	0
7	13	5	1
7	39	5	1
7	42	6	0
7	41	6	0.125
7	36	7	1
7	40	7	1
26	13	5	0.5
26	15	6	0.5
26	46	6	0.25
26	64	2	150
26	110	6	0.25
26	111	8	0.25
26	112	2	50
20	92	6	1
20	7	4	0.125
20	13	5	0.5
20	96	9	0.25
20	97	8	0.25
20	98	2	60
20	8	4	0.125
18	88	2	100
18	89	6	1.5
18	20	5	0.25
18	90	8	0.25
18	91	5	0.5
18	73	6	1
18	27	6	0.25
18	36	7	0.125
17	36	1	0.5
17	83	2	150
17	84	2	12
17	87	4	0.25
17	7	4	0.125
17	8	4	0.125
17	16	5	0.5
17	85	6	0.25
17	47	6	0.25
17	86	9	0.25
17	60	10	0
11	59	2	20
11	58	2	25
11	57	2	50
11	8	4	0.5
11	7	4	0.5
11	38	6	0.25
11	35	6	2
11	23	7	0.75
11	49	7	1
11	60	10	0.5
9	32	2	50
9	50	2	50
9	7	4	0.25
9	8	4	0.25
9	49	5	0.5
9	53	5	0.25
9	15	6	0.5
9	48	7	1
9	21	7	0.5
9	51	7	0.25
9	52	7	0.25
4	24	6	0.330000013
4	25	6	0.330000013
4	26	6	2
4	27	6	0.330000013
4	28	6	0.670000017
6	2	6	0.5
6	8	4	0.5
6	12	2	200
6	13	5	0.5
6	35	6	2.5
6	36	7	2
6	37	2	100
6	38	6	0.125
23	70	2	100
23	13	5	0.5
23	47	6	0.25
23	60	10	0.5
23	16	5	0.5
23	8	4	0.125
23	7	4	0.25
23	104	5	0.25
23	36	7	0.25
23	20	5	0.5
23	49	5	0.75
23	21	7	1
23	23	7	1
23	105	5	1
23	22	4	0.125
23	80	6	2
5	7	4	0.5
5	13	5	0.5
5	19	6	2
5	20	5	0.5
5	32	2	150
5	33	4	0.5
5	34	6	0.125
42	8	4	0.125
42	7	4	0.125
42	18	5	0.125
42	16	5	0.5
42	10	6	0.25
42	47	6	0.125
42	35	6	1
42	36	7	1
42	162	9	0.25
42	60	10	0.25
42	104	14	1
40	156	2	200
40	19	2	200
40	8	4	0.25
40	7	4	0.5
40	49	5	1
40	13	5	0.5
40	157	6	3
40	58	6	0.25
40	47	6	0.5
35	8	4	0.25
35	140	4	0.5
35	18	4	1
35	139	5	1.5
35	72	5	0.5
35	138	6	0.5
35	47	6	0.5
35	137	6	1
35	142	7	0.5
35	141	7	1
35	60	10	1
41	8	4	0.125
41	7	4	0.125
41	160	4	0.25
41	159	4	0.5
41	158	5	1.5
41	13	5	0.5
41	25	6	0.25
41	47	6	0.25
41	99	6	1
41	52	7	1
41	78	8	0.25
41	161	8	0.5
39	151	2	75
39	152	8	0.25
39	153	2	75
39	35	6	0.5
39	23	7	0.25
39	154	5	0.5
39	155	5	0.5
37	12	2	100
37	140	5	1
37	72	5	0.5
37	13	5	0.5
37	147	6	1
37	145	6	0.125
37	58	6	0.25
37	15	6	0.5
37	146	9	0.125
37	60	10	0.25
36	5	5	1.5
36	68	5	0.5
36	144	5	1
36	143	2	200
38	8	4	0.125
38	7	4	0.25
38	149	5	0.75
38	13	5	0.5
38	71	6	0.25
38	148	6	0.25
38	93	6	0.25
38	25	6	0.25
38	6	6	1
38	150	7	0.5
38	60	10	1
34	13	5	0.5
34	27	6	0.25
34	114	2	90
34	1	6	0.25
34	136	8	0.5
33	12	2	150
33	13	5	0.5
33	133	8	0.25
33	134	9	0.25
33	2	6	0.75
33	135	9	0.25
33	23	2	31.25
27	114	2	75
27	113	2	100
27	8	4	0.125
27	7	4	0.125
27	117	5	0
27	13	5	0.5
27	116	6	3
27	38	6	0.25
27	35	6	0.5
27	115	10	0.5
31	129	6	3
31	19	6	1
31	21	7	0.5
31	7	4	0.125
31	8	4	0.125
31	130	2	40
31	131	5	0.25
31	132	2	50
32	70	2	100
32	8	4	0.125
32	7	4	0.125
32	13	5	0.5
32	38	6	0.125
32	15	6	1.25
32	2	6	0.25
32	10	7	0.5
32	115	10	0.25
21	25	6	0.5
21	47	6	0.5
21	69	5	1
21	99	6	1
21	100	6	0.25
14	70	2	125
14	71	6	0.25
14	60	10	0.25
14	72	4	0.25
14	73	6	0.25
14	74	5	0.25
14	7	4	0.125
14	8	4	0.125
14	13	5	0.25
14	30	5	0.25
14	75	7	0.5
14	76	6	0.125
14	77	6	2
14	78	8	0.25
14	79	6	0.25
12	12	2	100
12	13	5	0.5
12	25	6	0.25
12	61	2	40
12	62	6	1
12	63	6	0
12	36	7	3
4	29	6	0.330000013
4	30	5	0.330000013
4	31	5	0.670000017
2	9	8	0.5
2	7	4	0.25
2	8	4	0.25
2	10	7	0.25
2	11	2	60
2	6	6	1
\.


--
-- Data for Name: ingredient_in_tag; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY ingredient_in_tag (ingredient_id, tag_id) FROM stdin;
161	8
2	15
3	7
4	12
141	16
141	4
140	20
155	20
7	5
7	6
7	4
8	4
155	7
10	4
11	8
68	15
14	15
15	15
16	15
17	15
18	4
68	20
19	14
19	15
19	8
20	12
21	12
22	4
24	1
25	15
26	15
26	8
27	15
28	15
29	15
33	4
37	14
38	15
39	4
39	5
40	8
41	4
5	4
31	8
34	15
46	15
47	15
49	7
51	15
52	12
53	15
53	4
159	4
159	5
48	2
55	15
56	15
58	15
59	1
59	17
60	15
60	4
61	15
62	15
64	17
64	1
63	8
65	15
65	8
66	15
66	8
67	15
42	15
43	4
44	8
79	16
79	15
69	4
69	15
70	1
84	4
71	4
71	5
71	15
90	15
90	8
72	5
72	15
73	15
74	4
75	12
98	15
76	15
101	15
109	12
110	15
112	8
78	15
114	8
115	4
115	15
116	15
121	12
124	15
122	4
122	5
123	4
126	12
127	15
133	4
135	4
135	5
80	13
80	8
81	4
118	15
118	4
85	13
87	4
89	9
93	15
94	5
94	15
95	4
92	17
153	15
96	15
23	12
23	11
97	15
100	15
145	15
146	15
102	4
147	8
105	4
138	15
156	2
156	3
157	15
139	3
104	5
104	15
117	12
117	11
45	20
35	19
107	20
108	7
108	4
111	20
125	7
128	4
128	5
132	15
134	20
134	15
152	7
152	3
158	20
158	15
148	15
149	15
9	15
12	1
32	2
113	1
113	17
113	19
142	15
6	1
6	19
88	1
88	19
88	18
77	8
77	7
9	20
86	15
86	20
136	20
12	19
106	1
106	19
106	18
82	8
103	9
32	19
50	3
50	2
50	19
54	1
54	17
54	19
83	1
83	19
83	18
130	1
130	17
130	19
99	16
99	19
129	1
129	18
129	19
137	19
137	16
144	4
144	15
1	1
1	19
1	18
1	17
162	20
162	15
151	1
151	17
151	19
143	1
143	19
143	18
\.


--
-- Data for Name: ingredient_tag; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY ingredient_tag (id, name) FROM stdin;
1	Meat
2	Fish
3	Seafood
4	Spice
5	Spicy
6	Salty
7	Flour
8	Extras
9	Fruit
10	Berries
11	Cheese
12	Milk product
13	Pasta
14	Potatoes
15	Green
16	Chicken
17	Pork
18	Beef
19	Animal product
20	Composed
\.


--
-- Name: ingredient_tag_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('ingredient_tag_id_seq', 20, true);


--
-- Data for Name: language; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY language (id, locale, name) FROM stdin;
1	NO-no	Norwegian
2	EN-en	English
\.


--
-- Name: language_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('language_id_seq', 2, true);


--
-- Data for Name: logininfo; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY logininfo (id, provider_id, provider_key) FROM stdin;
1	credentials	admin@admin.com
2	credentials	asd@asd.asd
3	credentials	fdg@dfgdf.sdf
4	credentials	tandeey@gmail.com
5	credentials	tandeey+test@gmail.com
6	credentials	e.Lisleby@Gmail.com
7	credentials	e.lislebo@gmail.com
8	credentials	kjsletten@gmail.com
9	credentials	Kjsletten@gmail.com
10	credentials	ha@ha.no
11	credentials	a@b.no
12	credentials	tandee.y@gmail.com
\.


--
-- Name: logininfo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('logininfo_id_seq', 12, true);


--
-- Data for Name: oauth1info; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY oauth1info (id, token, secret, login_info_id) FROM stdin;
\.


--
-- Name: oauth1info_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('oauth1info_id_seq', 1, false);


--
-- Data for Name: oauth2info; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY oauth2info (id, accesstoken, tokentype, expiresin, refreshtoken, login_info_id) FROM stdin;
\.


--
-- Name: oauth2info_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('oauth2info_id_seq', 1, false);


--
-- Data for Name: passwordinfo; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY passwordinfo (hasher, password, salt, login_info_id) FROM stdin;
bcrypt	$2a$06$pp7fqRfInhFI3GTBIcoTuePLHVwPn.7jkYAHxgWd/tUcwXvVmaC02	\N	1
bcrypt	$2a$10$Ej5wVLTWU3H1cY16PMNiRu9WHfp1wp1bFXX21N5Y.imNM0oNB4Hv.	\N	2
bcrypt	$2a$10$dqTAWcW0.auLCAJBlzIa3.JoH2qyyoV9jZigudPy31bE2U1p.vQvC	\N	3
bcrypt	$2a$10$z0XIqfLqGtJZfJzW71SeoOmjm4gYTGhFOmtXKblZOxV8Y9FEazZSa	\N	4
bcrypt	$2a$10$gU6SytA5yQg4x/wbbNUVv.ZmWm1qVM5PLMLbOtvAUPX8uEiEQ8v6.	\N	4
bcrypt	$2a$10$3F5spUK6UcP79w85b47ad.Ti6LCl9Fqha0v5HjG6vD304WpFUIRQW	\N	5
bcrypt	$2a$10$yjSakiSTMby.o40lmQ8SvOD4x.i2DQUWTBeAFSqUpXON0x3xxwUBS	\N	6
bcrypt	$2a$10$DfvZlyrSXBsk/NYGwwn3iOBtAp9w0cC/PGPnkwpc.GyQ4Nj0zVekG	\N	7
bcrypt	$2a$10$LdMEi6ba0V7RdOMG.rdSjuXDGAS0DSUlu7CNrmccR5O9uGBWf/WxC	\N	8
bcrypt	$2a$10$.tkdUT2WPoCuYq35kotF3u4A/mr.Vj1pslDTU43dH/DSlv4osHMKS	\N	9
bcrypt	$2a$10$H6F/R0GXPaD6X.kjt6obJukohnpxR/3VjMP.gf5VVevrYhb9paKd2	\N	10
bcrypt	$2a$10$0Fe1IKz6v5Wn.ArGiJiWCe9KaWuoI6WwWAKHo/eWCU0.PnbQVGg1K	\N	11
bcrypt	$2a$10$n29I0Ymyjs8DoMcUSZfmZ.pbadmwyz.Lm.bvlJCnBfCMClvlbSzzW	\N	12
\.


--
-- Data for Name: recipe; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY recipe (id, name, image, description, language, calories, procedure, spicy, "time", difficulty, created, modified, published, deleted, created_by, source) FROM stdin;
1	Pai med falukorv	http://res.cloudinary.com/hnjelkrui/image/upload/v1423559303/x9feiprpkdvptyinxky2.jpg	Herlige små paier som smaker godt og er enkle å lage. Restene fra middagen er også supre å ha i matpakka. I denne oppskriften har vi beregnet to paier til hver.	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>1.</strong>&nbsp;Forvarm stekeovn til&nbsp;225 °C.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>2.</strong>&nbsp;Skjær falukorv og tomat i skiver.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>3.</strong>&nbsp;Rull ut pizzadeig og del den i 8 firkanter, ca. 14 x 14 cm. Rull kantene innover på hver firkant.&nbsp;</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>4.</strong>&nbsp;Bland crème fraîche&nbsp;og pesto. Legg 1 ss av blandingen i hver firkant, smør det litt utover.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>5.</strong>&nbsp;Legg falukorv og tomat annenhver gang i takstenmønster over crème fraîche-&nbsp;og pestoblandingen. Pensle kantene med egg.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>6.</strong>&nbsp;Stek midt i ovnen i ca. 15 minutter.&nbsp;</p>	1	20	Enkel	2015-02-10 09:07:57.323	2015-02-17 12:31:25.894	2015-02-17 12:31:25.894	\N	1	http://www.matprat.no/oppskrifter/familien/pai-med-falukorv/
41	Kyllingfilet i form	http://res.cloudinary.com/hnjelkrui/image/upload/v1423578742/a10jangcgs6nnfbwc2v5.jpg	Denne kyllingformen med smak av karri er en sikker vinner på middagsbordet for både store og små. Og det beste er at den lager seg nærmest selv. 	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Brun kyllingfilet raskt på begge sider i en stekepanne med litt margarin eller olje, legg filetene over i en ildfast form sammen med løk og paprika.&nbsp;</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Visp sammen ingredienser til sausen, og hell det over i formen.&nbsp;</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Sett formen i stekeovn på 200 °C i 20-25 minutter til kyllingen er gjennomstekt.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">4.</span>&nbsp;Kok byggris etter anvisning på pakken.&nbsp;</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server saftige kyllingfileter i karrisaus med byggris og frisk salat.&nbsp;</p>	1	30	Middels	2015-02-10 14:32:12.543	2015-02-12 20:53:14.816	2015-02-12 20:53:14.816	\N	1	http://www.matprat.no/oppskrifter/familien/kyllingfilet-i-form/
3	Kjøttform med potetmoslokk	http://res.cloudinary.com/hnjelkrui/image/upload/v1423559737/psibaw439ukiq8gqjffi.jpg	Her er en magrere variant av den engelske Sheperd's Pie, som ofte ble laget med malt lammekjøtt. Potetmos ble lagt som et lokk på toppen, og servert som en alt i én form. Kjempegod hverdagsmat både for liten og stor!	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Skrell potet, og del dem i grove terninger. Kok potetterninger møre i en kjele med vann. Hell av kokevannet. Mos poteter og rør inn melk og smør. Smak til med salt, pepper og eventuelt litt muskat.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Stek kjøttdeig i to omganger i en stekepanne med margarin eller olje, sett kjøttdeigen til side. Fres løk og gulrot til grønnsakene er gylne, ha i tomatpuré, og fres ytterligere et par minutter. Tilsett hakkede tomater, kjøttdeig og mais. Bland godt og smak til med krydder.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Ha kjøttsausen i en dyp ildfast form, eventuelt i små porsjonsformer. Legg &nbsp;potetmos som et lokk over kjøttsausen og dryss ost på toppen.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">4.</span>&nbsp;Sett den ildfaste formen i stekeovn på 180 °C og stek&nbsp;i 30-40 minutter til osten er smeltet og toppen har fått en gyllen farge.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server kjøttformen med en frisk salat og godt brød.</p>	1	50	Middels	2015-02-10 09:15:25.059	2015-02-17 12:31:21.536	2015-02-17 12:31:21.536	\N	1	http://www.matprat.no/oppskrifter/familien/kjottform-med-potetmoslokk/
6	Bondeomelett	http://res.cloudinary.com/hnjelkrui/image/upload/v1423561818/iu1zlltbov59yk9rwydg.jpg	Omelett, eggepanne, frittata eller tortilla – kall det hva du vil! Fellesnevneren er at det er rask, sunn og smakfull mat som kan varieres i det uendelige og som smaker nesten like godt kald som varm. Har du rester igjen har du en super lunsj i morgen. 	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;"><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px;">Stek kjøttdeig i en stekepanne med margarin eller olje.<br></p><ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Pisk sammen egg, vann, salt og pepper.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Skjær potet, tomat og purre i skiver.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Legg kjøttdeig, potetskiver og purre lagvis i en ildfast form. Hell over eggeblandingen. Legg tomatskivene på toppen. Dekk til formen med aluminiumsfolie og stek bondeomeletten i stekeovnen på 220 °C i ca. 20 minutter.</li></ol></li></ol>	1	20	Enkel	2015-02-10 09:27:19.483	2015-02-11 08:54:36.644	2015-02-11 08:54:36.644	\N	1	http://www.matprat.no/oppskrifter/rask/bondeomelett1/
5	Ovnsbakt laksefilet	http://res.cloudinary.com/hnjelkrui/image/upload/v1423560381/h1yw3zgwwc6z3h6rpirz.jpg	Ovnsbakt laksefilet med kål og poteter er enkelt å lage og passer perfekt i en travel hverdag. Baker du fisken i stekeovnen kan du raskt tilberede mat til mange, med et flott resultat. 	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Kok potet i en kjele med lettsaltet vann til de er møre.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Del laksefilet i porsjonsstykker. Pensle filetene med litt margarin eller olje. Krydre med salt og sitronpepper.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Legg filetene på et bakepapirkledd stekebrett. Bak filetene på 180 °C i 10-15 minutter avhengig av tykkelse.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">4.</span>&nbsp;Del kål i båter og kok dem møre i en kjele med lettsaltet vann. Hell av vannet og ha på smør.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server saftig og god laksefilet med nykokt kål og potet.</p>	1	40	Enkel	2015-02-10 09:26:12.071	2015-02-12 08:03:21.425	2015-02-12 08:03:21.425	\N	1	http://www.matprat.no/oppskrifter/familien/ovnsbakt-laksefilet/
4	Grillet kylling med lun salat	http://res.cloudinary.com/hnjelkrui/image/upload/v1423561992/jf5tvd36wck1umf7ewpw.jpg	Lag en lun salat med paprika, løk, asparges, stilkselleri og eple. Salatblanding med nøtter og frø setter en ekstra spiss på den lune salaten. Supert tilbehør til grillet kylling.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Varm kyllingbitene i stekeovn ved 200 °C i 15 minutter.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Del grønnsakene i passe store biter og stek dem i stekepanne med olje i 2-3 minutter. Dryss over nøttemiks og flaksalt. Grønnsakene smaker best når de fortsatt er sprø.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server kyllingretten&nbsp;med kokt ris og søt chilisaus eller pesto.</p>	1	20	Enkel	2015-02-10 09:20:11.062	2015-02-17 12:31:17.01	2015-02-17 12:31:17.01	\N	1	http://www.matprat.no/oppskrifter/rask/grillet-kylling-med-lun-salat/
12	Rask og digg nudelsuppe	http://res.cloudinary.com/hnjelkrui/image/upload/v1423562601/g3jacg23mwswfcb95gos.jpg	Det er utrolig lite som skal til før en pakke nudler blir mer spennende! Med litt kjøttdeig og strimlede grønnsaker blir det faktisk et ordentlig godt måltid!	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Stek kjøttdeig i en stekepanne med olje. Tilsett paprika og sopp og fres videre i 2-3 minutter.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Ha i vårløk, nudler, krydder og vann. La det koke i 2-3 minutter.</li></ol><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server med godt brød.</p>	1	10	Enkel	2015-02-10 10:03:00.4	2015-02-17 12:31:06.823	2015-02-17 12:31:06.823	\N	1	http://www.matprat.no/oppskrifter/rask/rask-og-digg-nudelsuppe/
11	Skinkepai	http://res.cloudinary.com/hnjelkrui/image/upload/v1423562584/cboyrt9pguo0ruoxeidn.jpg	Når du har lært deg å lage en paibunn og hvor mange egg du trenger til fyllet, finnes det nesten ikke grenser for hva du kan ha i en pai. Pai er kjempedeilig mat å servere til middag, og restene er supre å ha i matpakka.	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">1. Lag paideig først. Smuldre margarin og mel til en grynete masse uten klumper. Tilsett vann og kna deigen raskt sammen. Bruk litt mer mel om nødvendig. Pakk deigen i litt plastfolie og legg den i kjøleskapet mens du forbereder resten.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">2. Forvarm stekeovnen til 200 °C.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">3. Del brokkoli i små buketter. Kok opp en kjele med vann og forvell brokkolibukettene (kok dem i ca. 1 minutt). Hell av kokevannet og skyll med kaldt vann.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">4. Visp sammen egg, melk, salt og pepper. Sett til side.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">5. Trykk deigen ut i en paiform slik at den er like tykk over alt. Bruk en gaffel til å prikke den med (stikke hull i bunnen).</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">6. Sett paiformen midt i stekeovnen og forstek paiskallet i 12-15 minutter, til den begynner å bli gyllen.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">7. Ta paiformen ut av stekeovnen. Ha skinke, hvitløk, purre og brokkolibuketter over bunnen. Hell eggeblandingen over og dryss ost på toppen.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">9. Senk varmen i stekeovnen til 180 °C og stek paien midt i ovnen i 25-30 minutter, eller til eggeblandingen har stivnet og paien har fått en fin, gyllenbrun farge.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">10. Server paien nystekt eller lun, gjerne med en god, frisk salat ved siden av.</p>	1	90	Vanskelig	2015-02-10 10:02:46.331	2015-02-17 12:31:10.857	2015-02-17 12:31:10.857	\N	1	http://www.matprat.no/oppskrifter/familien/skinkepai/
14	Burger med asiatisk smak	http://res.cloudinary.com/hnjelkrui/image/upload/v1423563013/jmdigci5h3k7si69e7wz.jpg	Små, spicy burgere med smak av chili, friske urter og krydder. Godt for store og små!	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>1.</strong>&nbsp;Ha kjøttdeig i en bolle. Ha i chili, hvitløk, ingefær, limesaft, koriander, salt og pepper. Rør deigen godt sammen, men ikke for mye da den blir seig.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>2.</strong>&nbsp;Klapp deigen til en stor rund kake, og del den i seks like store deler. Form hver del til en rund hamburger.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>3.</strong>&nbsp;Pensle hamburgerne med litt olje og stek på middels varme i 4-5 minutter på hver side, avhengig av tykkelse.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>4.</strong>&nbsp;Kjør koriander og olje sammen med en stavmikser. Vend inn gresk yoghurt, og smak til med salt og pepper.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>5.</strong>&nbsp;Skrell agurk og del den i to på langs. Skrap ut kjernen, og kutt agurken i tynne skiver. Bland agurkskiver sammen med dressingen.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>6.&nbsp;</strong>Varm hamburgerbrød i stekeovn på 225 °C i 2-3 minutter.&nbsp;<br>Del det varme hamburgerbrødet i to. Legg salat på den ene halvdelen av brødet, ha på dressing, salat, epleskiver og burger, og legg på toppbrødet.</p>	2	30	Enkel	2015-02-10 10:09:51.636	2015-02-17 12:30:59.735	2015-02-17 12:30:59.735	\N	1	http://www.matprat.no/oppskrifter/familien/burger-med-asiatisk-smak/
10	Svinnende kjærlighet	http://res.cloudinary.com/hnjelkrui/image/upload/v1423562403/fizejass2xz2qtmzrlbg.jpg	Her er en variant av brennende kjærlighet. Denne består av potetmos med revet ost, servert med stekt bacon, sopp og kjøttdeig av svin. Raskt og enkelt.	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Stek bacon, sopp og kjøttdeig i en stekepanne med margarin/olje til kjøttdeigen er gjennomstekt. Ha i purre og stek videre til purren har blitt blank (ca. 2 minutter).</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Lag potetmos etter anvisning på posen. Rør inn revet ost.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Fordel potetmos på talerken med kjøttdeig og bacon på toppen.</li></ol>	1	20	Enkel	2015-02-10 09:59:41.708	2015-02-17 12:31:08.853	2015-02-17 12:31:08.853	\N	1	http://www.matprat.no/oppskrifter/rask/svinnende-kjarlighet/
7	Ovnsbakt laks med tandoorikrydder, couscous og tzatziki	http://res.cloudinary.com/hnjelkrui/image/upload/v1423562007/qbv1nifsupoeglq7fxb4.jpg	Denne gode hverdagsretten med stekt laks, couscous og tzatziki har eksotiske smaker som både store og små setter pris på.  	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Kok opp vann, buljong og olje i en kjele. Trekk kjelen til side, og ha i coucous og dekk til med plastfolie. La couscousen svelle til all væske er borte.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Krydre laksefilet med tandoorikrydder. Ha margarin eller olje i en varm stekepanne, og stek laksefileter på middels varme i 3-5 minutter på hver side avhengig av tykkelse.&nbsp;</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Vend inn revet sitronskall og hakket persille i couscousen.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server stekt laks med coucous og tzatziki. Pynt eventuelt med sitronbåter.</p>	1	40	Enkel	2015-02-10 09:53:17.45	2015-02-17 12:31:14.955	2015-02-17 12:31:14.955	\N	1	http://www.matprat.no/oppskrifter/familien/ovnsbakt-laks-med-tandoorikrydder-couscous-og-tzatziki/
18	Roastbiffsalat med plommer	http://res.cloudinary.com/hnjelkrui/image/upload/v1423570318/oy2bv5wxzprezxhsmkn9.jpg	Roastbiff er perfekt i salat. Sammen med rødløk og det søte og syrlige i plommene, er dette en litt annerledes salat. 	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Del plommene i to og fjern steinen. Ha smør i en varm stekepanne og stek de halve plommene til de blir gylne. Legg dem til side.&nbsp;</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Kutt løken i små terninger og stek i en panne til løken er blank. Skru ned temperaturen. Tilsett resten av ingrediensene til dressingen. Sil av løken, og sett den til side.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Del plommene i båter. Legg salaten i en bolle eller på et fat. Legg roastbiffen og plommebåtene over salaten.&nbsp;</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">5.</span>&nbsp;Drypp litt dressing over salaten. Pynt med litt av den stekte løken på roastbiffen, og server resten av dressingen ved siden av.</p>	1	20	Enkel	2015-02-10 12:11:43.262	2015-02-17 12:30:55.331	2015-02-17 12:30:55.331	\N	1	http://www.matprat.no/oppskrifter/familien/roastbiffsalat-med-plommer/
17	Pølser og spagetti med pizzaiola	http://res.cloudinary.com/hnjelkrui/image/upload/v1423570247/ugimnapyy6nfy56zgm5k.jpg	Dette er en utrolig enkel, men kjempegod middag. Med hjemmelaget pastasaus, pizzaiola, er det ikke rart resultatet blir bra.	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Kok opp vannet med buljongpulver. Ta kjelen av platen og legg i pølsene. La de trekke i ca. 10 minutter.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Lag spagetti etter anvisning på pakken.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Kutt opp løk og hvitløk og stek i en gryte til det blir blankt. Hell over tomater og ha i tomatpuré. Kok opp og la det småkoke i 5 minutter. Smak til med urter og krydder.</li></ol><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Retten smaker kjempegodt sammen med en grønn salat med cherrytomater og marinerte hvitløksbåter.</p>	1	30	Enkel	2015-02-10 12:10:28.519	2015-02-17 12:30:57.397	2015-02-17 12:30:57.397	\N	1	http://www.matprat.no/oppskrifter/rask/polser-og-spagetti-med-pizzaiola/
16	Risgrøt	http://res.cloudinary.com/hnjelkrui/image/upload/v1423570083/qzoouhexptgu4yeqsngd.jpg	Risgrøt med smør, sukker og kanel kan serveres hele året, men er kanskje vanligst i jula. Da vil mange ha en skåldet mandel i grøten, og en liten premie til den som finner mandelen.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Kok opp grøtris og vann i en gryte, og la det koke i 10 minutter på middels varme til vannet er nesten kokt inn.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Hell i melk, kok opp og senk varmen. La grøten småkoke på svak varme i ca. 1 time, eller til den er passe tykk. Rør om av og til slik at det ikke svir seg.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Smak til med salt.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server risgrøt med “smørøye”, sukker og kanel.</p>	1	60	Middels	2015-02-10 12:07:29.96	2015-02-17 12:31:04.622	2015-02-17 12:31:04.622	\N	1	http://www.matprat.no/oppskrifter/familien/risgrot/
22	Svinekoteletter med pære og rødkål	http://res.cloudinary.com/hnjelkrui/image/upload/v1423572621/fss2iracyjo8mqz87c8u.jpg	Enkelt og godt! Server kotelettene med lune pærer og gjør gjerne rødkålen litt mer spennende ved å smaksette den med purre og krydder.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Skjær noen hakk i fettranden på kotelettene, da blir det lettere å steke dem jevnt. Krydre kotelettene med salt og pepper.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Brun kotelettene på sterk varme i en stekepanne med margarin, 2-3 minutter på hver side. La kotelettene ettersteke på svakere varme i ca. 5 minutter.&nbsp;</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Del pære i fire, og ta ut kjernehuset. Legg bitene i pannen sammen med kotelettene de siste 5 minuttene.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">4.</span>&nbsp;Fres purre i en liten kjele. Tilsett rosmarin og ha i godt avrent rødkål. Varm opp og krydre eventuelt med salt og pepper.&nbsp;</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server kotelettene med varme pærer og rødkål.</p>	1	20	Enkel	2015-02-10 12:49:33.474	2015-02-17 12:30:34.997	2015-02-17 12:30:34.997	\N	1	http://www.matprat.no/oppskrifter/familien/svinekoteletter-med-pare-og-rodkal/
20	Nakkekoteletter med spinat og hvite bønner	http://res.cloudinary.com/hnjelkrui/image/upload/v1423572761/ivlu7rqonc4549ohdjpk.jpg	Nakkekoteletter er takknemlig mat å ty til i en travel hverdag. Raskt, smakfullt og rimelig - stort bedre kan det ikke bli. Og her er tilbehøret ferdig før du får telt til tre, spinaten skal bare så vidt i den varme pannen før den er klar til servering.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Rens hvitløk. Press med knivflaten til hvitløken presses sammen. Skjær den i skiver, og finhakk den.&nbsp;Da kommer smakene ekstra godt frem.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Lag små snitt i fettranden, slik at koteletten steker jevnt. Brun kotelettene 2-3 minutter på begge sider i en varm stekepanne med litt olje. La kotelettene ettersteke på lav varme i ca. 5 minutter.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Surr hvitløk, bønner og sukkererter raskt i en stekepanne med litt olje. Ha i spinat, og stek til den har falt sammen og alt er gjennomvarmt. Smak til med salt og pepper.&nbsp;</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server de saftige nakkekotelettene med stekt spinat og bønner.&nbsp;</p>	1	20	Enkel	2015-02-10 12:45:02.207	2015-02-17 12:30:32.747	2015-02-17 12:30:32.747	\N	1	http://www.matprat.no/oppskrifter/familien/nakkekoteletter-med-spinat-og-hvite-bonner/
25	Brokkolisuppe med strimlet svinekjøtt	http://res.cloudinary.com/hnjelkrui/image/upload/v1423573616/ochiji89xta8dympcf3q.jpg	En varmende suppe blir alltid godt mottatt! Toppet med stekte strimler av svinekjøtt gir den varme og energi til vinterkalde kvelder.	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">1. Vask brokkoli og del den i grove biter, også stilken. Skrell potet og løk, og kutt i terninger.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">2. Kok opp vann med buljongterninger og fløte, tilsett grønnsaker. Kok i ca. 10 minutter til grønnsakene er.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">3. Legg til side noen små buketter brokkoli. Kjør med en stavmikser eller en blender til en jevn suppe. La suppen få et oppkok.&nbsp;</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">4. Fres kjøttet raskt i en varm stekepanne med margarin, 2-4 minutter. Krydre med salt og pepper.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">5. Legg stekte strimler av svinekjøtt og brokkolibuketter i suppen rett før servering.</p>	1	20	Enkel	2015-02-10 13:06:36.755	2015-02-17 12:30:30.369	2015-02-17 12:30:30.369	\N	1	http://www.matprat.no/oppskrifter/familien/brokkolisuppe-med-strimlet-svinekjott/
9	Kremet fiskesuppe	http://res.cloudinary.com/hnjelkrui/image/upload/v1423562314/ujjajavub7ypziztf4vv.jpg	Her er en rask og enkel fiskesuppe som varmer godt og smaker nydelig. Om du er glad i reker eller blåskjell, er det kjempegodt i suppa også!	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Kok opp fiskebuljongen i en gryte og legg i gulrotbitene. La dem koke i 3-4 minutter.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Bland melk og hvetemel i et glass med skrulokk, og rist godt. Hell blandingen i fiskebuljongen mens du visper hele tiden. La suppen småkoke i ca. 5 minutter.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Senk temperaturen og legg i fiskebiter og erter. La suppa trekke i ca. 5 minutter. Rør inn fløten og smak til med salt og pepper. Dryss over finklippet gressløk.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server gjerne suppen med brød eller flatbrød.</p>	1	40	Middels	2015-02-10 09:58:21.664	2015-02-17 12:31:12.787	2015-02-17 12:31:12.787	\N	1	http://www.matprat.no/oppskrifter/familien/kremet-fiskesuppe/
27	Pasta med røkt skinke og soltørkede tomater	http://res.cloudinary.com/hnjelkrui/image/upload/v1423574053/kiqztqfwintqfkdovq9p.jpg	En enkel pastarett med spennende smaker – denne liker de fleste!	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Kok pasta som annvisning på pakken. Vask og del purre i ringer. Del soltørked tomate og røkt skinke i tynne strimler.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Stek purre, hvitløk, soltørkede tomater og skinke i litt olje. Tilsett kokt pasta og rør det hele godt sammen.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">&nbsp;Pisk egg og rør det inn i pastablandingen. La egget stivne litt i pastaen og dryss over parmesanost.</li></ol><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Serveres med en frisk grønn salat og grovt brød.</p>	1	20	Enkel	2015-02-10 13:13:41.903	2015-02-17 12:30:10.918	2015-02-17 12:30:10.918	\N	1	http://www.matprat.no/oppskrifter/rask/pasta-med-rokt-skinke-og-soltorkede-tomater/
21	Kyllingfilet med wokket grønnsaksmiks	http://res.cloudinary.com/hnjelkrui/image/upload/v1423572592/c7mdojyelywzylvdezha.jpg	Wokkede grønnsaker blir man aldri lei. Dessuten blir det saftig og godt, selv uten saus. Kyllingen kan gjerne smaksettes med honning.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Skjær kyllingfilet i store terninger og grønnsakene i passe store biter. Fennikel bør finsnittes.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Fres kjøttet i pannen til det er så vidt gjennomstekt. Ha på salt og pepper og la det hvile mens grønnsakene wokkes. Smak til grønnsakene med salt og pepper og anrett dem på en tallerken.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Tre kjøttet på spyd og legg det over grønnsakene. Dryss ristede pinjekjerner over før servering.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span style="color: inherit;">Tips 1:&nbsp;Ha litt honning i pannen på slutten av steketiden på kyllingbitene. Den søte smaken "kler" kyllingen godt, og bitene får dessuten en fin gyllen farge.</span></p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span style="color: inherit;">Tips 2: Stek kyllingbitene sammen med urtekrydder for å få ekstra god smak. Urter som passer godt er tiamin, rosmarin eller litt løpestikke.</span><br></p>	1	20	Enkel	2015-02-10 12:49:19.339	2015-02-17 12:30:27.177	2015-02-17 12:30:27.177	\N	1	http://www.matprat.no/oppskrifter/rask/kyllingfilet-med-wokket-gronnsaksmiks/
29	Chili con carne	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577955/fvkecg2ssbt3gswvqm3r.jpg	Chili con carne betyr chili med kjøtt. Chili con carne er ikke meksikansk mat. Dette er en rett som har basis i det meksikanske kjøkken, men som er ekte amerikansk, trolig fra Texas.	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>1.</strong>&nbsp;Ha margarin eller olje i en varm gryte. Vent til margarinen slutter å bruse, og brun kjøttdeig i to omganger sammen med&nbsp;løk, hvitløk og chili.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>2.</strong>&nbsp;Hell over hermetisk tomat og la det surre i 3-4 minutter.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>3.</strong>&nbsp;Bland inn chilibønner, paprika og krydder. La det koke et par minutter og smak til med salt.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server gjerne gryteretten&nbsp;med salat og brød.</p>	2	20	Enkel	2015-02-10 13:59:40.999	2015-02-17 12:30:18.009	2015-02-17 12:30:18.009	\N	1	http://www.matprat.no/oppskrifter/rask/chili-con-carne/
19	Nakkekoteletter med chilibønnegryte	http://res.cloudinary.com/hnjelkrui/image/upload/v1423570625/d60edmejopatvlfmzpit.jpg	Glad i koteletter, men lei surkål? Klar for å prøve noe nytt? Her er den nye middagsfavoritten din!	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">1. Stek løk, paprika og selleri i en høy stekepanne/gryte med litt olje til grønnsakene er såvidt møre.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">2. Hell på chilibønner. La gryten småkoke mens du steker kotelettene. Smak eventuelt til med salt og pepper. Dryss over finhakket persille før servering.&nbsp;</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">3. Lag små snitt i fettranden, slik at koteletten steker jevnt. Brun kotelettene 2-3 minutter på begge sider i en varm stekepanne med olje. La kotelettene ettersteke på lav varme i ca. 5 minutter.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server saftige nakkekoteletter med chilibønnegryte og godt brød.</p>	2	20	Enkel	2015-02-10 12:16:51.02	2015-02-17 12:30:52.924	2015-02-17 12:30:52.924	\N	1	http://www.matprat.no/oppskrifter/familien/nakkekoteletter-med-chilibonnegryte/
31	Medisterkaker med potetstappe, erter og bacon	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577931/y0q1pzshw9ycj1npnftj.jpg	Medisterkaker er knallgod førjulsmat. Med potetstappe, erter og bacon blir dette en rask og smakfull middag som også de minste i familien kommer til å elske. 	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>1.&nbsp;</strong>Forvarm stekeovn til 180 C°.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>2.</strong>&nbsp;Legg medisterkaker i en ildfast form. Stek midt i stekeovn i ca. 15 minutter, eller til de er gjennomvarme.</p><div class="highlight infobox big " style="margin: 20px 0px; position: relative; padding: 15px 15px 15px 60px; min-height: 45px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; background-color: rgb(242, 240, 236);"><div class="rich-text" style="padding: 10px 0px 30px; clear: both;">Kjøp gjerne medisterdeig og lag dine egne medisterkaker. Form kakene i håndflaten ved hjelp av en skje, beregn ca. 40 g per kake. Brun kakene i en varm stekepanne med smør til de har fått en fin farge rundt det hele, legg dem over i en ildfast form og stek dem ferdige i stekeovn på 180 C° i ca. 10 minutter.</div></div><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>3.</strong>&nbsp;Skrell potet og skjær i terninger. Ha terningene over i en kjele med vann. Kok potetterningene til de er møre, ca. 10 minutter.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"></p><div class="highlight infobox big " style="margin: 20px 0px; position: relative; padding: 15px 15px 15px 60px; min-height: 45px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; background-color: rgb(242, 240, 236);"><div class="rich-text" style="padding: 10px 0px 30px; clear: both;">Har du ekstra dårlig tid, kan du bruke ferdig potetmos.</div></div><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>4.</strong>&nbsp;Hell av kokevannet og mos potetene med en stamper. Spe med melk, og smak til med salt og pepper.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>5.</strong>&nbsp;Stek bacon i en stekepanne med smør til det har fått en fin farge. Ha i erter mot slutten av steketiden og la det surre et par minutter til ertene er varme.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server medisterkakene med potetmos toppet med sprøstekt bacon og erter. En skje tyttebærsyltetøy smaker også godt til.</p>	1	30	Middels	2015-02-10 14:03:37.426	2015-02-17 12:30:13.296	2015-02-17 12:30:13.296	\N	1	http://www.matprat.no/oppskrifter/rask/medisterkaker-med-potetstappe-erter-og-bacon/
32	Hverdagsgryte	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577089/hr0e4rlbjcidedq1hdey.jpg	En favoritt i vårt ungdommelige "testpanel" med barn i en alder fra 6-10 år. Denne gryten kan kanskje også være en god introduksjon av gryterett for kresne. Små biter kan virke positivt inn på barna. De revne gulrøttene er supre.	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Ha margarin eller olje i en varm kjele. Vent til maragrinen slutter å bruse. Stek kjøttdeig i flere omganger.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Ha purre, hvitløk, gulrot og tomat i gryten.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Tilsett buljong og la det koke i noen minutter. Smak til med salt og pepper.</li></ol><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Serveres gjerne med poteter eller potetstappe.</p>	1	20	Enkel	2015-02-10 14:04:38.33	2015-02-17 12:30:15.715	2015-02-17 12:30:15.715	\N	1	http://www.matprat.no/oppskrifter/familien/hverdagsgryte/
33	Tacograteng	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577281/zsmw8w3niferm5rg0sib.jpg	Tacokrydder kan brukes til mer enn fredagstaco med tortillaslefser eller tacoskjell. En tacograteng med kjøttdeig av svin og tacosmak er kjapp hverdagsmat for hele familien.	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Stek kjøttdeig i margarin i en stekepanne. Ha på tacokrydder og følg anvisning på posen.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Fordel kjøttdeig, ananas og tomat i en ildfast formen.&nbsp;Hell over tacosaus og dryss over revet ost.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Stek gratengen midt i ovnen på 225&nbsp;ºC i 15 minutter til osten har fått gyllen farge.&nbsp;</li></ol><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server gratengen med salat og créme fraîche.</p>	1	30	Enkel	2015-02-10 14:07:48.661	2015-02-17 12:30:07.88	2015-02-17 12:30:07.88	\N	1	http://www.matprat.no/oppskrifter/familien/tacograteng/
37	Nudler med kjøttdeig	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577914/hrlj7d5fcagbqw0gjsk6.jpg	Denne retten er laget med nudler som de fleste barn er veldig glad i. Vær obs på at nudler kan inneholde mye salt og fett. Sjekk bak på pakkene og sammenlign næringsinnholdet. Spagetti eller ris kan brukes som en erstatning for nudler i denne oppskriften.	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Ha margarin eller olje i en varm stekepanne. Vent til margarinen slutter å bruse, og stek kjøttdeig i flere omganger. Bruk en stekespade til å dele den i mindre biter.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Vask, skrell og kutt gulrot og sellerirot i strimler. Del brokkoli i mindre biter. Stek alle grønnsakene med finhakket hvitløk.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Kok nudler etter anvisning på pakken.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Bland kjøttdeig, grønnsaker og nudler, tilsett soyasaus og kraften fra aspargesbønneboksen.</li></ol>	1	30	Enkel	2015-02-10 14:18:26.397	2015-02-17 12:29:58.614	2015-02-17 12:29:58.614	\N	1	http://www.matprat.no/oppskrifter/familien/nudler-med-kjottdeig/
36	Pestomarinert ytrefilet	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577860/umndjpidci5ukulvbvwl.jpg	Pesto er blitt manges favoritt. Blandingen av basilikum, parmesan, pinjekjerner og olje frister flere og flere. Pesto kan brukes til mye, også marinade. Her er det ytrefilet som har fått smak av pesto.	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Bland pesto og sitronsaft i en plastpose. Legg i&nbsp;biffene, press ut luften og fordel marinaden rundt biffene. La dem marinere i 2-3 timer.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Enten du vil grille eller steke biffene, stek på sterk varme til kjøttsaften pipler ut på overflaten. Snu biffene, når det pipler på den andre siden er biffene medium stekt.</li></ol><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server med pastasalat med pesto, og dryss så finhakkede pinjekjerner over retten.</p>	1	80	Enkel	2015-02-10 14:17:18.694	2015-02-17 12:30:00.938	2015-02-17 12:30:00.938	\N	1	http://www.matprat.no/oppskrifter/kos/pestomarinert-ytrefilet/
34	Kremet pasta med falukorv	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577519/lylnwkcgysfz87dgofts.jpg	En rask og enkel middag. Perfekt for travle hverdager.	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>1.&nbsp;</strong>Skjær falukorv i halve skiver og løk i halve ringer. Stek&nbsp;pølseskivene og løk gyllent i en stekepanne med litt margarin eller olje.&nbsp;</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>2.</strong>&nbsp;Kok pasta etter anvisning på pakken. Hell av vannet.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>3.&nbsp;</strong>Lag pastasaus etter anvisning på posen.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><strong>4.</strong>&nbsp;Rør sausen inn i pastaen og ha på stekt falukorv og løk.&nbsp;</p>	1	10	Enkel	2015-02-10 14:11:45.891	2015-02-17 12:30:05.726	2015-02-17 12:30:05.726	\N	1	http://www.matprat.no/oppskrifter/familien/kremet-pasta-med-falukorv/
39	Gullgravergrateng	http://res.cloudinary.com/hnjelkrui/image/upload/v1423578445/lk9pxg1wtmrnn8h3ed8y.jpg	Det som er supert med denne gullgravergratengen er at den er litt klebrig, og derfor ikke faller av skjea så lett når de minste lærer seg å spise selv. Dessuten er alle ingrediensene blandet, slik at barna får litt av alt - også grønnsaker - for hver munnfull.	1	0	<ol style="padding: 0px; margin-right: 0px; margin-bottom: 0.75em; margin-left: 0px; list-style: none; counter-reset: li 0; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;"><li style="margin: 5px 0px 15px; counter-increment: li 1;">Kok makaroni som anvist på pakken.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Lag hvit saus etter anvisning på posen.&nbsp;Avkjøl sausen litt, og rør inn egg og ost.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Skjær skinken i biter. Bland hvit saus, makaroni, skinke og grønnsakblanding. Hell dette i en smurt ildfast form. Strø over brødrasp.</li><li style="margin: 5px 0px 15px; counter-increment: li 1;">Stek gratengen på 175 grader i stekeovn 30-40 minutter.</li></ol>	1	50	Middels	2015-02-10 14:27:17.248	2015-02-17 12:29:55.983	2015-02-17 12:29:55.983	\N	1	http://www.matprat.no/oppskrifter/familien/gullgravergrateng/
38	Koteletter med spansk vri	http://res.cloudinary.com/hnjelkrui/image/upload/v1423578308/jmc3cvxelnvcat9ecb7l.jpg	Kun koteletter med brun saus? Nei, her skal vi vise deg at koteletter kan brukes sammen med mange spennende ingredienser. Fiks ferdig på noen minutter.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Brun kotelettene raskt og dryss over litt salt og grovmalt pepper. Legg dem til side og hold varmt.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Del grønnsakene i strimler, pepper/chili i biter. Fres grønnsakene raskt i panna og tilsett cider/hvitvin.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Legg kotelettene over og legg på lokk. La det hele brassere (trekke) i ca. 10 minutter.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server med brød.</p>	1	30	Enkel	2015-02-10 14:24:37.539	2015-02-17 12:30:03.114	2015-02-17 12:30:03.114	\N	1	http://www.matprat.no/oppskrifter/kos/koteletter-med-spansk-vri/
23	Lasagne	http://res.cloudinary.com/hnjelkrui/image/upload/v1423573371/jbbjknf4sfwysv12cmjj.jpg	Nordmenn har mange favoritter fra det italienske kjøkken, deriblant lasagne. Ovnsretten består av kjøttsaus og ostesaus som legges lagvis med pastaplater.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Brun kjøttdeig i olje på sterk varme i to omganger. Ha all kjøttdeigen tilbake i stekepanna.&nbsp;Tilsett hakket løk, hvitløk, tomater, tomatpuré, salt, pepper og vann.&nbsp;La sausen småkoke i ca. 10 minutter til den begynner å tykne. Dryss over basilikum.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Smelt smør i en kjele og rør inn mel. Spe med melk under omrøring og la sausen koke i ca. 10 minutter. Den skal være forholdsvis tykk.&nbsp;Ha i parmesan og la osten smelte. Smak til med krydder.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Legg lasagneplater, kjøttsaus og ostesaus lagvis&nbsp;i en ildfast form. Start og avslutt med&nbsp;ostesaus. Dryss over revet ost til slutt.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">4.</span>&nbsp;Sett formen i stekeovn på 225° C og stek i 30-40 minutter. Kjenn etter med en pinne eller spiss kniv om pastaen er mør. La lasagnen hvile noen minutter før servering.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server med baguette og salat ved siden av.</p>	1	80	Middels	2015-02-10 13:02:39.06	2015-02-11 11:29:35.061	2015-02-11 11:29:35.061	\N	1	http://www.matprat.no/oppskrifter/familien/lasagne/
42	Tomatsuppe med kokt egg	http://res.cloudinary.com/hnjelkrui/image/upload/v1423579508/sl58r65mczxvgbr2t7vu.jpg	Hva er suppefavoritten i Norge? Kan det være tomatsuppe, mon tro? Uansett – enten du lager suppen fra pose eller tar deg tid til å lage den fra bunnen, så smaker den enda bedre med et hardkokt egg oppi!	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">1. Kok egg i 10 minutter. Avkjøl i kaldt vann og skrell dem.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">2. Fres løk og hvitløk myk og blank i en kjele med litt olje.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">3. Tilsett tomatpuré, og la det surre ett minutt. Ha deretter i hakket tomat, vann, buljong og sukker. Kok opp og la det småkoke i ca. 10 minutter.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">4. Kjør suppen til ønsket konsistens med stavmikser eller i hurtigmikser. Tilsett hakket basilikum og smak til med salt og pepper.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server suppen med kokte egg.</p>	1	30	Enkel	2015-02-10 14:44:54.274	2015-02-12 13:45:48.981	2015-02-12 13:45:48.981	\N	1	http://www.matprat.no/oppskrifter/familien/tomatsuppe-med-kokt-egg/
40	Seibiff med løk	http://res.cloudinary.com/hnjelkrui/image/upload/v1423578639/wp1gtfpul5b66mxy4ep0.jpg	Sei er en ypperlig matfisk, og når vi snakker om smak stiller seien i en helt egen klasse. Seibiff med løk er god norsk hverdagsklassiker.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Kok poteter.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Rens løk, del den i to og skjær den i tynne skiver. Smelt margarin og stek løken på middels varme til den er gyllen og har karamellisert seg. Smak til med salt og pepper.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Rens brokkoli og skjær den i fine buketter. Kok i lettsaltet vann, ca. 3 minutter.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">4.</span>&nbsp;Skjær fisken i serveringsstykker og vend dem i melblandingen. Stek fisken i panne med margarin, ca. to minutter på hver side.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">Server</span>&nbsp;med cherrytomater, brokkoli og kokte poteter.</p>	1	30	Enkel	2015-02-10 14:30:26.969	2015-02-12 13:45:51.557	2015-02-12 13:45:51.557	\N	1	http://www.matprat.no/oppskrifter/tradisjon/seibiff-med-lok/
35	Thai-wok med kylling	http://res.cloudinary.com/hnjelkrui/image/upload/v1423577898/lwujpzitgcagyw0io0xt.jpg	I Thailand brukes fiskesaus på samme måte som vi bruker salt. Får du ikke tak i thailandsk fiskesaus, så går det bra å smake seg til med salt i stedet. Prøv vår thai-wok med kylling	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Stek kyllingstrimlene i smør i en panne til det er jevnt hvitt over det hele.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Ha i oppdelt løk, paprika, hvitløk og ingefær og stek litt videre.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Tilsett sukker, fiskesaus (salt), soyasaus og pepper. Spe med hønsebuljong, vann og kokosmelk. Smak til retten.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server med jasminris. Pynt gjerne med strimlet vårløk.</p>	1	30	Middels	2015-02-10 14:14:27.841	2015-02-12 13:45:54.078	2015-02-12 13:45:54.078	\N	1	http://www.matprat.no/oppskrifter/kos/thai-wok-med-kylling/
30	Kylling i pita	http://res.cloudinary.com/hnjelkrui/image/upload/v1423576838/ix5o9rtpcrjeoleko349.jpg	Kylling i pita er raskt og enkelt, populært for både store og små er det også. Når du vil ha en forandring på retten kan du gjerne lage den med med strimlet svinekjøtt.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Skjær kyllingfilet i små terninger, ca. 1 x 1 cm. Skjær paprika i biter og vårløk i ringer.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Stek&nbsp;kyllingbitene på middels varme i en stekepanne med litt olje. Ha i paprika og vårløk, dryss over tikka masala-krydder og rør godt. Vend inn matyoghurt. Bland alt godt sammen og la det bli gjennomvarmt.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Fyll varme pitabrød med kyllingblandingen og friske salatblad.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server gjerne mer salat ved siden av.</p>	1	20	Enkel	2015-02-10 14:00:12.156	2015-02-17 12:30:20.087	2015-02-17 12:30:20.087	\N	1	http://www.matprat.no/oppskrifter/familien/kylling-i-pita/
28	Pasta med kjøttdeig og tomatsalsa	http://res.cloudinary.com/hnjelkrui/image/upload/v1423576097/n6oedbv6v2yimzamwh5b.jpg	Du kommer hjem fra jobb og det haster som vanlig med maten. Barna venter og om en time skal dere av gårde på trening. Pasta og kjøttsaus, det er enkelt. På med kokeplatene, koke opp pastavannet og fram med kjøttdeigen fra kjøleskapet...	1	0	<p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">1. Ha margarin eller olje i en varm stekepanne. Vent til margarinen slutter å bruse, og stek kjøttdeigen i flere omganger. Bruk en stekespade til å dele den i mindre biter.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">2. Kok pasta etter anvisning på pakken.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">2. Hakk løk og skjær tomatene i små biter. Tilsett&nbsp;finhakkede friske krydderurter. Bland alt sammen med balsamicoeddik, olivenolje og smak til med salt og pepper.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">3. Legg pasta på en tallerken, ha over kjøttdeigen. Hell over litt rømme og legg tomatsalsaen på toppen.</p>	1	30	Enkel	2015-02-10 13:48:03.05	2015-02-17 12:30:22.266	2015-02-17 12:30:22.266	\N	1	http://www.matprat.no/oppskrifter/familien/pasta-med-kjottdeig-og-tomatsalsa/
26	Kickwok	http://res.cloudinary.com/hnjelkrui/image/upload/v1423573983/ggb5dbjcevk7uc5pz4co.jpg	Det er nesten ingen vits å spise usunt, når det finnes så mye sunt og godt.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Kok fullkornsris som anvist på pakken.&nbsp;</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Skjær kinakål og paprika i tynne strimler. Skrell gulrot og skjær dem i tynne skiver med en ostehøvel.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Varm litt olje i en wok, og brun kjøttet i små porsjoner. Sett det til side.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">4.</span>&nbsp;Ha i litt olje, og stek grønnsakene raskt i mindre porsjoner.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">5.</span>&nbsp;Ha kjøttet og grønnsakene tilbake i wokken og rør inn sausen. Varm opp.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server wokken med fullkornsris og du har et enkelt og raskt hverdagsmåltid.&nbsp;</p>	1	20	Enkel	2015-02-10 13:12:44.138	2015-02-17 12:30:24.9	2015-02-17 12:30:24.9	\N	1	http://www.matprat.no/oppskrifter/familien/kickwok/
24	Pytt i panne	http://res.cloudinary.com/hnjelkrui/image/upload/v1423573497/fqas6poq2nbsxnezb3nl.jpg	Rester i kjøleskapet kan bli et skikkelig godt måltid. Her er en oppskrift på pytt i panne, men du behøver ikke følge oppskriften. Bruk de restene du har og stek dem i panna. Server gjerne med stekt egg og brød.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Skjær pølse i terninger og stek dem lett i en stekepanne.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Ha i&nbsp;lapskausblanding i små porsjoner, la det ettersteke sammen på svakeste varme, til grønnsakene er møre. Smak til med salt og pepper.</p>	1	10	Enkel	2015-02-10 13:04:25.531	2015-02-17 12:30:49.598	2015-02-17 12:30:49.598	\N	1	http://www.matprat.no/oppskrifter/rask/pytt-i-panne/
13	Avokadosalat med strimlet svinekjøtt	http://res.cloudinary.com/hnjelkrui/image/upload/v1423562923/qlzfv85nqrnmpshjxofo.jpg	Mettende salat med mye godt i! Denne kan du trygt servere til middag.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Fres kjøttet et par minutter på sterk varme i litt olje. Stek det i to omganger. Dryss over salt og pepper.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Skyll salatene og skjær issalaten i strimler. Ruccula kan brukes som den er eller i biter. Legg salaten i en bolle sammen med løken. Avokado skjæres i båter og dynkes med litt sitronsaft slik at den holder fargen.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Pinjekjernene blir ekstra gode hvis de ristes i tørr stekepanne til de får en gyllen farge. Dryss pinjekjernene over salaten.</p>	1	20	Enkel	2015-02-10 10:08:25.271	2015-02-17 12:31:02.39	2015-02-17 12:31:02.39	\N	1	http://www.matprat.no/oppskrifter/rask/avokadosalat-med-strimlet-svinekjott/
8	Enkel asiatisk gryte med svinekjøttdeig	http://res.cloudinary.com/hnjelkrui/image/upload/v1423562194/okdbhbmzzkwks4l0cwyn.jpg	Med utgangspunkt i et par suppeposer kan du trylle frem en herlig og velsmakende gryterett i løpet av få minutter. 	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Kok opp nudlene etter anvisning på pakken. Skjær løken i båter og paprikaen i biter.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Stek kjøttdeig i en stekepanne med margarin/olje. Fres grønnsakene et par minutter sammen med kjøttet.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Hell den ferdige suppen over kjøttet. Ha nudlene i sammen med resten av retten.</p><p style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;">Server med godt brød.</p>	1	20	Enkel	2015-02-10 09:56:07.721	2015-02-17 12:31:19.257	2015-02-17 12:31:19.257	\N	1	http://www.matprat.no/oppskrifter/rask/enkel-asiatisk-gryte-med-svinekjottdeig/
2	Koteletter med wokgrønnsaker	http://res.cloudinary.com/hnjelkrui/image/upload/v1423561942/mapqp2nbzhvuphasaxb7.jpg	Enkelt, raskt og billig! Kan man ønske seg mer? Jo, godt og sunt! Denne retten fyller alle kriteriene for en rask hverdagsmiddag.	1	0	<p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">1.</span>&nbsp;Sett over ris til koking.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">2.</span>&nbsp;Brun kotelettene på god varme i 2­-3 minutter på hver side. Ha på krydder, og etterstek i ca. 5 minutter på svak varme.</p><p class="mp_order" style="margin-top: 5px; margin-bottom: 15px; line-height: 24px; color: rgb(0, 0, 0); font-family: Neutra2Text, 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 16px;"><span class="recipe-step" style="font-weight: bold; font-size: 20px; margin: 0px;">3.</span>&nbsp;Ta kotelettene ut av panna, og fres wokgrønnsakene i litt olje på god varme. Tilsett buljong og la det koke i 1 minutt.</p>	1	20	Enkel	2015-02-10 09:14:20.076	2015-02-17 12:31:23.717	2015-02-17 12:31:23.717	\N	1	http://www.matprat.no/oppskrifter/rask/koteletter-med-wokgronnsaker/
\.


--
-- Name: recipe_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('recipe_id_seq', 42, true);


--
-- Data for Name: recipe_in_tag; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY recipe_in_tag (recipe_id, tag_id) FROM stdin;
8	2
3	1
2	2
1	1
6	2
23	1
5	1
42	1
41	1
39	1
37	1
34	1
33	1
27	2
32	1
30	1
28	1
26	1
21	5
25	1
20	1
22	1
24	2
19	1
18	1
17	2
14	1
13	2
12	2
10	2
11	1
9	1
7	1
4	2
\.


--
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY tags (id, name) FROM stdin;
1	Familien
2	Rask
3	indisk
4	norsk
5	Rasj
\.


--
-- Name: tags_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('tags_id_seq', 5, true);


--
-- Data for Name: unit; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY unit (id, name) FROM stdin;
1	liter
2	gram
3	kilogram
4	teaspoon
5	tablespoon
6	pcs
7	deciliter
8	bag
9	can
10	clove
11	slice
12	cup
13	leafs
14	pinch
\.


--
-- Name: unit_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('unit_id_seq', 14, true);


--
-- Data for Name: user_cold_start; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY user_cold_start (user_id, cold_start_id, answer, answer_time) FROM stdin;
1	3	t	2015-02-13 09:21:25.851
1	4	t	2015-02-13 09:21:26.279
1	5	t	2015-02-13 09:21:26.751
6	2	t	2015-02-17 12:27:47.195
6	3	t	2015-02-17 12:27:48.809
6	4	t	2015-02-17 12:27:50.381
6	5	t	2015-02-17 12:27:51.517
7	4	t	2015-02-17 13:30:12.407
7	5	t	2015-02-17 13:30:13.213
8	5	t	2015-02-17 13:43:07.075
9	2	t	2015-02-17 13:53:43.377
9	3	t	2015-02-17 13:53:43.9
9	4	t	2015-02-17 13:53:44.309
9	5	t	2015-02-17 13:53:44.934
10	1	t	2015-02-17 14:14:01.067
10	2	f	2015-02-17 14:14:02.054
10	3	t	2015-02-17 14:14:03.094
10	4	t	2015-02-17 14:14:04.042
10	5	t	2015-02-17 14:14:05.194
1	1	t	2015-02-18 13:23:44.361
1	2	t	2015-02-18 13:23:44.998
5	1	t	2015-02-12 08:03:56.432
5	2	t	2015-02-12 08:03:57.58
5	3	t	2015-02-12 08:03:58.313
5	4	t	2015-02-12 08:03:59.128
5	5	t	2015-02-12 08:04:00.822
\.


--
-- Data for Name: user_preferences; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY user_preferences (user_id) FROM stdin;
\.


--
-- Data for Name: user_star_rate_recipe; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY user_star_rate_recipe (user_id, recipe_id, rating, created, created_long) FROM stdin;
1	6	5	2015-02-11 14:31:54.152	1423665114152
1	23	5	2015-02-11 14:40:35.521	1423665635521
1	5	4	2015-02-12 08:57:23.383	1423731443383
5	42	4	2015-02-12 13:46:38.097	1423748798097
5	40	3	2015-02-12 13:46:43.301	1423748803301
1	40	4	2015-02-12 14:58:27.664	1423753107664
7	23	5	2015-02-17 13:34:31.586	1424180071586
8	23	5	2015-02-17 13:43:41.05	1424180621050
1	36	4	2015-02-20 15:10:05.564	1424445005564
1	42	4	2015-02-24 08:48:22.232	1424767702232
1	41	4	2015-02-24 09:50:10.452	1424771410452
1	39	3	2015-02-24 10:46:06.36	1424774766360
1	35	3	2015-02-24 10:48:01.541	1424774881541
11	23	2	2015-02-24 11:48:37.102	1424778517102
12	23	5	2015-02-24 13:39:40.343	1424785180343
\.


--
-- Data for Name: user_viewed_recipe; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY user_viewed_recipe (user_id, recipe_id, duration, last_seen) FROM stdin;
11	23	97	2015-02-24 11:24:05.636
11	6	23	2015-02-24 11:47:39.849
1	35	7	2015-02-24 13:20:10.588
1	5	7	2015-02-24 13:21:06.093
1	36	99	2015-02-20 15:11:41.87
12	23	176	2015-02-24 13:55:43.795
5	42	4	2015-02-12 13:46:40.404
5	40	4	2015-02-12 13:46:46.159
12	42	7	2015-02-24 13:55:51.608
1	41	1033	2015-02-24 09:50:52.307
1	40	345	2015-02-24 10:23:38.126
1	6	23	2015-02-24 10:24:27.513
7	6	7	2015-02-17 13:33:35.579
8	23	18	2015-02-17 13:43:48.468
8	6	3	2015-02-17 13:43:59.373
8	5	30	2015-02-17 13:44:54.923
9	42	3	2015-02-17 13:56:47.301
9	23	103	2015-02-17 13:57:07.665
1	38	77	2015-02-17 14:11:09.312
1	39	352	2015-02-24 10:54:17.115
1	42	715	2015-02-24 11:00:14.415
1	23	99	2015-02-24 11:00:29.51
\.


--
-- Data for Name: user_yes_no_rate_recipe; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY user_yes_no_rate_recipe (user_id, recipe_id, rating, last_seen) FROM stdin;
1	40	0	2015-02-12 14:59:18.036
6	6	1	2015-02-17 12:30:15.586
7	23	1	2015-02-17 13:34:28.997
7	6	1	2015-02-17 13:34:36.866
7	5	-1	2015-02-17 13:34:38.568
7	42	-1	2015-02-17 13:34:39.83
7	40	-1	2015-02-17 13:34:41.24
8	23	1	2015-02-17 13:43:29.796
8	6	0	2015-02-17 13:44:00.916
8	5	1	2015-02-17 13:44:24.595
9	23	1	2015-02-17 13:54:43.411
9	6	-1	2015-02-17 13:54:44.452
9	5	-1	2015-02-17 13:54:45.409
9	42	-1	2015-02-17 13:54:46.488
9	40	-1	2015-02-17 13:54:47.398
1	42	13	2015-02-24 08:48:17.121
11	23	1	2015-02-24 11:05:00.479
11	6	1	2015-02-24 11:47:16.075
12	41	-1	2015-02-24 13:27:33.06
12	42	-1	2015-02-24 13:27:34.704
12	36	-1	2015-02-24 13:27:39.458
12	5	-1	2015-02-24 13:27:41.161
12	40	-1	2015-02-24 13:27:42.719
12	39	-1	2015-02-24 13:27:44.318
12	35	-1	2015-02-24 13:27:47.55
12	6	-7	2015-02-24 13:39:18.328
12	23	5	2015-02-24 13:39:32.561
\.


--
-- Data for Name: userlogininfo; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY userlogininfo (user_id, login_info_id) FROM stdin;
1	1
2	2
3	3
4	4
5	5
6	6
7	7
8	8
9	9
10	10
11	11
12	12
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: mealchooser
--

COPY users (id, first_name, last_name, email, image, role, created, recipe_language, app_language, city, country, sex, year_born, enrolled, metric_system) FROM stdin;
2	asd	asd	asd@asd.asd	\N	user	2015-02-11 13:59:25.471	\N	\N	\N	\N	\N	\N	f	t
3	fsdf	sdgfdh	fdg@dfgdf.sdf	\N	user	2015-02-11 14:01:31.602	\N	\N	\N	\N	\N	\N	f	t
4	yes	tes	tandeey@gmail.com	http://www.gravatar.com/avatar/e0683c8de64a56e070b0823902a9b9ad?d=404	user	2015-02-11 15:14:05.733	\N	\N	\N	\N	\N	\N	f	t
6	Even	Lisletass	e.Lisleby@Gmail.com	\N	user	2015-02-17 12:27:39.404	\N	\N	\N	\N	\N	\N	f	t
7	Even	Lislebø	e.lislebo@gmail.com	\N	user	2015-02-17 13:30:03.347	\N	\N	\N	\N	\N	\N	f	t
8	Kjell	Propell	kjsletten@gmail.com	\N	user	2015-02-17 13:42:30.195	\N	\N	Disen	\N	\N	1920	f	t
9	Kjell	Propell	Kjsletten@gmail.com	\N	user	2015-02-17 13:53:40.069	\N	\N	\N	\N	\N	\N	f	t
5	test	testesen	tandeey+test@gmail.com	\N	user	2015-02-12 08:03:53.201	\N	\N	\N	\N	male	\N	f	t
10	Hei	Og	ha@ha.no	\N	user	2015-02-17 14:13:58.298	\N	\N	\N	\N	\N	\N	f	t
1	admin	admin	admin@admin.com	\N	admin	2015-02-10 09:02:18.686	\N	\N	Hamar	\N	male	1992	f	t
11	Pelle	Krogstad	a@b.no	\N	user	2015-02-24 11:04:33.346	\N	\N	\N	\N	\N	1922	f	t
12	testorama	testolini	tandee.y@gmail.com	\N	user	2015-02-24 13:16:18.623	\N	\N	s	\N	\N	\N	f	t
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mealchooser
--

SELECT pg_catalog.setval('users_id_seq', 12, true);


--
-- Name: cold_start_identifier_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY cold_start
    ADD CONSTRAINT cold_start_identifier_key UNIQUE (identifier);


--
-- Name: cold_start_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY cold_start
    ADD CONSTRAINT cold_start_pkey PRIMARY KEY (id);


--
-- Name: ingredient_name_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY ingredient
    ADD CONSTRAINT ingredient_name_key UNIQUE (name);


--
-- Name: ingredient_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY ingredient
    ADD CONSTRAINT ingredient_pkey PRIMARY KEY (id);


--
-- Name: ingredient_tag_name_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY ingredient_tag
    ADD CONSTRAINT ingredient_tag_name_key UNIQUE (name);


--
-- Name: ingredient_tag_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY ingredient_tag
    ADD CONSTRAINT ingredient_tag_pkey PRIMARY KEY (id);


--
-- Name: language_locale_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY language
    ADD CONSTRAINT language_locale_key UNIQUE (locale);


--
-- Name: language_name_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY language
    ADD CONSTRAINT language_name_key UNIQUE (name);


--
-- Name: language_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY language
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- Name: logininfo_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY logininfo
    ADD CONSTRAINT logininfo_pkey PRIMARY KEY (id);


--
-- Name: oauth1info_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY oauth1info
    ADD CONSTRAINT oauth1info_pkey PRIMARY KEY (id);


--
-- Name: oauth2info_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY oauth2info
    ADD CONSTRAINT oauth2info_pkey PRIMARY KEY (id);


--
-- Name: recipe_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY recipe
    ADD CONSTRAINT recipe_pkey PRIMARY KEY (id);


--
-- Name: tags_name_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_name_key UNIQUE (name);


--
-- Name: tags_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- Name: unit_name_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY unit
    ADD CONSTRAINT unit_name_key UNIQUE (name);


--
-- Name: unit_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY unit
    ADD CONSTRAINT unit_pkey PRIMARY KEY (id);


--
-- Name: users_email_key; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: mealchooser; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: favorites_idx; Type: INDEX; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE UNIQUE INDEX favorites_idx ON favorites USING btree (user_id, recipe_id);


--
-- Name: ingredient_in_tag_idx; Type: INDEX; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE UNIQUE INDEX ingredient_in_tag_idx ON ingredient_in_tag USING btree (ingredient_id, tag_id);


--
-- Name: recipe_in_tag_idx; Type: INDEX; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE UNIQUE INDEX recipe_in_tag_idx ON recipe_in_tag USING btree (recipe_id, tag_id);


--
-- Name: user_star_rate_recipe_idx; Type: INDEX; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE UNIQUE INDEX user_star_rate_recipe_idx ON user_star_rate_recipe USING btree (user_id, recipe_id);


--
-- Name: user_viewed_recipe_idx; Type: INDEX; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE UNIQUE INDEX user_viewed_recipe_idx ON user_viewed_recipe USING btree (user_id, recipe_id);


--
-- Name: user_yes_no_rate_recipe_idx; Type: INDEX; Schema: public; Owner: mealchooser; Tablespace: 
--

CREATE UNIQUE INDEX user_yes_no_rate_recipe_idx ON user_yes_no_rate_recipe USING btree (user_id, recipe_id);


--
-- Name: favorites_recipe_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY favorites
    ADD CONSTRAINT favorites_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE;


--
-- Name: favorites_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY favorites
    ADD CONSTRAINT favorites_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: ingredient_default_unit_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY ingredient
    ADD CONSTRAINT ingredient_default_unit_fkey FOREIGN KEY (default_unit) REFERENCES unit(id) ON DELETE CASCADE;


--
-- Name: ingredient_in_recipe_ingredient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY ingredient_in_recipe
    ADD CONSTRAINT ingredient_in_recipe_ingredient_id_fkey FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE;


--
-- Name: ingredient_in_recipe_recipe_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY ingredient_in_recipe
    ADD CONSTRAINT ingredient_in_recipe_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE;


--
-- Name: ingredient_in_tag_ingredient_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY ingredient_in_tag
    ADD CONSTRAINT ingredient_in_tag_ingredient_id_fkey FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE;


--
-- Name: ingredient_in_tag_tag_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY ingredient_in_tag
    ADD CONSTRAINT ingredient_in_tag_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES ingredient_tag(id) ON DELETE CASCADE;


--
-- Name: recipe_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY recipe
    ADD CONSTRAINT recipe_created_by_fkey FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: recipe_in_tag_recipe_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY recipe_in_tag
    ADD CONSTRAINT recipe_in_tag_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE;


--
-- Name: recipe_in_tag_tag_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY recipe_in_tag
    ADD CONSTRAINT recipe_in_tag_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE;


--
-- Name: recipe_language_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY recipe
    ADD CONSTRAINT recipe_language_fkey FOREIGN KEY (language) REFERENCES language(id) ON DELETE CASCADE;


--
-- Name: user_cold_start_cold_start_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_cold_start
    ADD CONSTRAINT user_cold_start_cold_start_id_fkey FOREIGN KEY (cold_start_id) REFERENCES cold_start(id) ON DELETE CASCADE;


--
-- Name: user_cold_start_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_cold_start
    ADD CONSTRAINT user_cold_start_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: user_preferences_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_preferences
    ADD CONSTRAINT user_preferences_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: user_star_rate_recipe_recipe_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_star_rate_recipe
    ADD CONSTRAINT user_star_rate_recipe_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE;


--
-- Name: user_star_rate_recipe_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_star_rate_recipe
    ADD CONSTRAINT user_star_rate_recipe_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: user_viewed_recipe_recipe_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_viewed_recipe
    ADD CONSTRAINT user_viewed_recipe_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE;


--
-- Name: user_viewed_recipe_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_viewed_recipe
    ADD CONSTRAINT user_viewed_recipe_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: user_yes_no_rate_recipe_recipe_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_yes_no_rate_recipe
    ADD CONSTRAINT user_yes_no_rate_recipe_recipe_id_fkey FOREIGN KEY (recipe_id) REFERENCES recipe(id) ON DELETE CASCADE;


--
-- Name: user_yes_no_rate_recipe_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY user_yes_no_rate_recipe
    ADD CONSTRAINT user_yes_no_rate_recipe_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


--
-- Name: users_app_language_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_app_language_fkey FOREIGN KEY (app_language) REFERENCES language(id);


--
-- Name: users_recipe_language_fkey; Type: FK CONSTRAINT; Schema: public; Owner: mealchooser
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_recipe_language_fkey FOREIGN KEY (recipe_language) REFERENCES language(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: mealchooser
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM mealchooser;
GRANT ALL ON SCHEMA public TO mealchooser;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

END;