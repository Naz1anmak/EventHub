BEGIN;

ALTER TABLE users
  ALTER COLUMN username TYPE varchar(50);

ALTER TABLE users
  ADD CONSTRAINT chk_users_username_length CHECK (char_length(username) BETWEEN 3 AND 50);

COMMIT;