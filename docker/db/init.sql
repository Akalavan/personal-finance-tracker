CREATE TABLE personal_finance.public.categories (
                                                    id integer NOT NULL,
                                                    name character varying(45) NOT NULL,
                                                    description character varying(255),
                                                    CONSTRAINT categories_name_check CHECK ((length(TRIM(BOTH FROM name)) >= 2)),
                                                    CONSTRAINT categories_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE personal_finance.public.categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE personal_finance.public.categories_id_seq OWNED BY personal_finance.public.categories.id;

ALTER TABLE ONLY personal_finance.public.categories ALTER COLUMN id SET DEFAULT nextval('personal_finance.public.categories_id_seq'::regclass);

CREATE TABLE personal_finance.public.stock (
                                               id integer NOT NULL,
                                               name character varying(45) NOT NULL,
                                               cost numeric(12,2) NOT NULL,
                                               description character varying(255),
                                               categories_id integer NOT NULL,
                                               CONSTRAINT categories_name_check CHECK ((length(TRIM(BOTH FROM name)) >= 2)),
                                               CONSTRAINT stock_pkey PRIMARY KEY (id),
                                               CONSTRAINT stock_categories_id_fkey FOREIGN KEY (categories_id)
                                                   REFERENCES personal_finance.public.categories (id) MATCH SIMPLE
                                                   ON UPDATE NO ACTION
                                                   ON DELETE NO ACTION
);

CREATE SEQUENCE personal_finance.public.stock_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE personal_finance.public.stock_id_seq OWNED BY personal_finance.public.stock.id;

ALTER TABLE ONLY personal_finance.public.stock ALTER COLUMN id SET DEFAULT nextval('personal_finance.public.stock_id_seq'::regclass);
