BEGIN TRANSACTION;

DROP TABLE IF EXISTS "users" CASCADE;
DROP SEQUENCE IF EXISTS "users_seq";

---------------------------------------------------------

CREATE SEQUENCE "users_seq";
CREATE TABLE "users" (
  "id"            BIGINT PRIMARY KEY       DEFAULT "nextval"('"users_seq"'),
  "created_date"  TIMESTAMP WITH TIME ZONE DEFAULT "now"(),
  "username"      TEXT    NOT NULL,
  "password"      TEXT    NOT NULL,
  "enabled"       BOOLEAN NOT NULL         DEFAULT TRUE,
  "email"         TEXT,
  "confirm_email" BOOLEAN,
  CONSTRAINT "username_unique" UNIQUE (username),
  CONSTRAINT "email_unique" UNIQUE (email)
);

END TRANSACTION;