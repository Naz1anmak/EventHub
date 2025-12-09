BEGIN;

ALTER TABLE categories
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE categories
  ADD CONSTRAINT chk_categories_description_length CHECK (char_length(coalesce(description, '')) <= 255);

COMMIT;