BEGIN;

ALTER TABLE categories
  ALTER COLUMN name TYPE varchar(100);

ALTER TABLE categories
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE projects
  ALTER COLUMN name TYPE varchar(100);

ALTER TABLE projects
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE events
  ALTER COLUMN title TYPE varchar(100);

ALTER TABLE events
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE tags
  ALTER COLUMN name TYPE varchar(50);

ALTER TABLE tags
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE users
  ALTER COLUMN username TYPE varchar(50);

ALTER TABLE user_metadata
  ALTER COLUMN phone TYPE varchar(15);

ALTER TABLE categories
  ADD CONSTRAINT chk_categories_name_length CHECK (char_length(name) BETWEEN 3 AND 100);

ALTER TABLE categories
  ADD CONSTRAINT chk_categories_description_length CHECK (char_length(coalesce(description, '')) <= 255);

ALTER TABLE projects
  ADD CONSTRAINT chk_projects_name_length CHECK (char_length(name) BETWEEN 3 AND 100);

ALTER TABLE projects
  ADD CONSTRAINT chk_projects_description_length CHECK (char_length(coalesce(description, '')) <= 255);

ALTER TABLE events
  ADD CONSTRAINT chk_events_title_length CHECK (char_length(title) BETWEEN 3 AND 100);

ALTER TABLE events
  ADD CONSTRAINT chk_events_description_length CHECK (char_length(coalesce(description, '')) <= 255);

ALTER TABLE tags
  ADD CONSTRAINT chk_tags_name_length CHECK (char_length(name) BETWEEN 3 AND 50);

ALTER TABLE tags
  ADD CONSTRAINT chk_tags_description_length CHECK (char_length(coalesce(description, '')) <= 255);

ALTER TABLE users
  ADD CONSTRAINT chk_users_username_length CHECK (char_length(username) BETWEEN 3 AND 50);

ALTER TABLE user_metadata
  ADD CONSTRAINT chk_user_metadata_phone_length CHECK (char_length(coalesce(phone, '')) <= 15);

COMMIT;
