create extension if not exists "pgcrypto";

create table if not exists users (
  id uuid primary key default gen_random_uuid(),
  username varchar(50) not null unique,
  email text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_users_username_length check (char_length(username) between 3 and 50)
);

create table if not exists user_metadata (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null unique references users(id) on delete cascade,
  first_name text,
  last_name text,
  bio text,
  phone varchar(15),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_user_metadata_phone_length check (char_length(coalesce(phone, '')) <= 15)
);

create table if not exists categories (
  id uuid primary key default gen_random_uuid(),
  name varchar(100) not null unique,
  description varchar(255),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_categories_name_length check (char_length(name) between 3 and 100),
    constraint chk_categories_description_length check (char_length(coalesce(description, '')) <= 255)
);

create table if not exists projects (
  id uuid primary key default gen_random_uuid(),
  name varchar(100) not null,
  description varchar(255),
  category_id uuid not null references categories(id),
  owner_id uuid not null references users(id),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_projects_name_length check (char_length(name) between 3 and 100),
    constraint chk_projects_description_length check (char_length(coalesce(description, '')) <= 255)
);
create index if not exists idx_projects_category_id on projects(category_id);

create table if not exists events (
  id uuid primary key default gen_random_uuid(),
  title varchar(100) not null,
  description varchar(255),
  event_date timestamptz,
  location text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_events_title_length check (char_length(title) between 3 and 100),
    constraint chk_events_description_length check (char_length(coalesce(description, '')) <= 255)
);

create table if not exists tags (
  id uuid primary key default gen_random_uuid(),
  name varchar(50) not null unique,
  description varchar(255),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_tags_name_length check (char_length(name) between 3 and 50),
    constraint chk_tags_description_length check (char_length(coalesce(description, '')) <= 255)
);

create table if not exists event_tags (
  event_id uuid not null references events(id) on delete cascade,
  tag_id uuid not null references tags(id) on delete cascade,
  primary key (event_id, tag_id)
);
create index if not exists idx_event_tags_tag_id on event_tags(tag_id);
