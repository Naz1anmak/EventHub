BEGIN;

ALTER TABLE user_metadata
  ALTER COLUMN phone TYPE varchar(15);

ALTER TABLE user_metadata
  ADD CONSTRAINT chk_user_metadata_phone_length CHECK (char_length(coalesce(phone, '')) <= 15);

COMMIT;