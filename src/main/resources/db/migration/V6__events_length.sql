BEGIN;

ALTER TABLE events
  ALTER COLUMN title TYPE varchar(100);

ALTER TABLE events
  ADD CONSTRAINT chk_events_title_length CHECK (char_length(title) BETWEEN 3 AND 100);

COMMIT;