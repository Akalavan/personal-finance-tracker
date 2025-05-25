CREATE TABLE public.users (
   id BIGSERIAL PRIMARY KEY,
   username VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL UNIQUE,
   created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE public.expenses (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES users(id),
  amount DECIMAL(10, 2),
  stock VARCHAR(50),
  date TIMESTAMP
);
