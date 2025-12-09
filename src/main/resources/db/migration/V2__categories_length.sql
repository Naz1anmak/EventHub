BEGIN;

ALTER TABLE categories
  ALTER COLUMN name TYPE varchar(100);

ALTER TABLE categories
ADD CONSTRAINT chk_categories_name_length CHECK (char_length(name) BETWEEN 3 AND 100);

COMMIT;