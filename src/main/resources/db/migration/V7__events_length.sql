BEGIN;

ALTER TABLE events
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE events
  ADD CONSTRAINT chk_events_description_length CHECK (char_length(coalesce(description, '')) <= 255);

COMMIT;