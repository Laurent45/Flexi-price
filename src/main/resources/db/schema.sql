CREATE TYPE "product_type" AS ENUM (
  'high_end_phone',
  'mid_range_phone',
  'laptop'
);

CREATE TYPE "client_type" AS ENUM (
  'personal',
  'professional_revenue_gte_10m',
  'professional_revenue_lt_10m'
);

CREATE TYPE "cart_status" AS ENUM (
  'active',
  'checked_out',
  'abandoned'
);

CREATE TABLE "client" (
                          "id" bigint PRIMARY KEY,
                          "type" client_type NOT NULL
);

CREATE TABLE "personal_client" (
                                   "id" bigint PRIMARY KEY,
                                   "user_name" varchar UNIQUE NOT NULL,
                                   "first_name" varchar NOT NULL,
                                   "last_name" varchar NOT NULL
);

CREATE TABLE "professional_client" (
                                       "id" bigint PRIMARY KEY,
                                       "siren" varchar UNIQUE NOT NULL,
                                       "legal_name" varchar NOT NULL,
                                       "vat_number" varchar,
                                       "annual_revenue" bigint NOT NULL
);

CREATE TABLE "cart" (
                        "id" bigint PRIMARY KEY,
                        "client_id" bigint NOT NULL,
                        "status" cart_status NOT NULL DEFAULT 'active'
);

CREATE TABLE "product" (
                           "id" bigint PRIMARY KEY,
                           "type" product_type NOT NULL,
                           "name" varchar NOT NULL,
                           "product_price_id" bigint
);

CREATE TABLE "product_price" (
                                 "id" bigint PRIMARY KEY,
                                 "product_id" bigint NOT NULL,
                                 "client_type" client_type NOT NULL,
                                 "price" bigint NOT NULL
);

CREATE TABLE "cart_item" (
                             "cart_id" bigint NOT NULL,
                             "product_price_id" bigint NOT NULL,
                             "quantity" int NOT NULL,
                             PRIMARY KEY ("cart_id", "product_price_id")
);

CREATE UNIQUE INDEX one_active_cart_per_client
    ON cart(client_id)
    WHERE status = 'active';

ALTER TABLE "personal_client" ADD FOREIGN KEY ("id") REFERENCES "client" ("id");

ALTER TABLE "professional_client" ADD FOREIGN KEY ("id") REFERENCES "client" ("id");

ALTER TABLE "cart" ADD FOREIGN KEY ("client_id") REFERENCES "client" ("id");

ALTER TABLE "product_price" ADD FOREIGN KEY ("id") REFERENCES "product" ("product_price_id");

ALTER TABLE "product_price" ADD FOREIGN KEY ("product_id") REFERENCES "product" ("id");

ALTER TABLE "cart_item" ADD FOREIGN KEY ("cart_id") REFERENCES "cart" ("id");

ALTER TABLE "cart_item" ADD FOREIGN KEY ("product_price_id") REFERENCES "product_price" ("id");
