BEGIN;

ALTER TABLE tags
  ALTER COLUMN name TYPE varchar(50);

ALTER TABLE tags
  ADD CONSTRAINT chk_tags_name_length CHECK (char_length(name) BETWEEN 3 AND 50);

COMMIT;