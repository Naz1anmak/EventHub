-- Добавление ON UPDATE CASCADE для всех внешних ключей с ON DELETE CASCADE
-- Создание таблицы compensation_actions для хранения информации о компенсационных действиях

-- 1. projects.category_id: изменение с RESTRICT на CASCADE с обновлением
alter table projects drop constraint if exists projects_category_id_fkey;
alter table projects
  add constraint projects_category_id_fkey
  foreign key (category_id)
  references categories(id)
  on delete cascade
  on update cascade;

-- 2. user_metadata.user_id: добавление ON UPDATE CASCADE
alter table user_metadata drop constraint if exists user_metadata_user_id_fkey;
alter table user_metadata
  add constraint user_metadata_user_id_fkey
  foreign key (user_id)
  references users(id)
  on delete cascade
  on update cascade;

-- 3. event_tags.event_id: добавление ON UPDATE CASCADE
alter table event_tags drop constraint if exists event_tags_event_id_fkey;
alter table event_tags
  add constraint event_tags_event_id_fkey
  foreign key (event_id)
  references events(id)
  on delete cascade
  on update cascade;

-- 4. event_tags.tag_id: добавление ON UPDATE CASCADE
alter table event_tags drop constraint if exists event_tags_tag_id_fkey;
alter table event_tags
  add constraint event_tags_tag_id_fkey
  foreign key (tag_id)
  references tags(id)
  on delete cascade
  on update cascade;

CREATE TABLE compensation_actions (
    id UUID PRIMARY KEY default gen_random_uuid(),
    action_type VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    retries INT DEFAULT 0,
    last_attempt timestamptz,
    created_at timestamptz not null default now()
);
