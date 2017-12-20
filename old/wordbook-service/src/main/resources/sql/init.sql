BEGIN TRANSACTION;

DROP TABLE IF EXISTS "wordbook" CASCADE;
DROP SEQUENCE IF EXISTS "wordbook_seq";

DROP TABLE IF EXISTS "wordbook_statistic" CASCADE;
DROP SEQUENCE IF EXISTS "wordbook_statistic_seq";

---------------------------------------------------------

CREATE SEQUENCE "wordbook_statistic_seq";
CREATE TABLE "wordbook_statistic" (

  "id"             BIGINT PRIMARY KEY       DEFAULT "nextval"('"wordbook_statistic_seq"'),
  "created_date"   TIMESTAMP WITH TIME ZONE DEFAULT "now"(),
  "progress_level" BIGINT NOT NULL

);

CREATE SEQUENCE "wordbook_seq";
CREATE TABLE "wordbook" (
  "id"                    BIGINT PRIMARY KEY       DEFAULT "nextval"('"wordbook_seq"'),
  "created_date"          TIMESTAMP WITH TIME ZONE DEFAULT "now"(),
  "username"              TEXT                                        NOT NULL,
  "word_id"               TEXT                                        NOT NULL,
  "wordbook_statistic_id" BIGINT REFERENCES "wordbook_statistic" (id) NOT NULL,
  CONSTRAINT "wordBook_unique" UNIQUE (username, word_id, wordbook_statistic_id)
);


END TRANSACTION;