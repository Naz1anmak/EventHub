BEGIN;

ALTER TABLE tags
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE tags
  ADD CONSTRAINT chk_tags_description_length CHECK (char_length(coalesce(description, '')) <= 255);

COMMIT;