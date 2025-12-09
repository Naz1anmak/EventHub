BEGIN;

ALTER TABLE projects
  ALTER COLUMN description TYPE varchar(255);

ALTER TABLE projects
  ADD CONSTRAINT chk_projects_description_length CHECK (char_length(coalesce(description, '')) <= 255);

COMMIT;