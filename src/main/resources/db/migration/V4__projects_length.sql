BEGIN;

ALTER TABLE projects
  ALTER COLUMN name TYPE varchar(100);

ALTER TABLE projects
  ADD CONSTRAINT chk_projects_name_length CHECK (char_length(name) BETWEEN 3 AND 100);

COMMIT;