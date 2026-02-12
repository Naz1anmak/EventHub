create extension if not exists "pgcrypto";

create table if not exists users (
  id uuid primary key default gen_random_uuid(),
  version bigint not null,
  username varchar(50) not null unique,
  email text not null unique,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_users_username_length check (char_length(username) between 3 and 50)
);
create index if not exists idx_users_username on users(username);
create index if not exists idx_users_email on users(email);

create table if not exists user_metadata (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null unique references users(id) on delete cascade,
  first_name varchar(30) not null,
  last_name varchar(30) not null,
  address text,
  phone varchar(16) not null unique,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_user_metadata_first_name_length check (char_length(first_name) between 2 and 30),
    constraint chk_user_metadata_last_name_length check (char_length(last_name) between 2 and 30),
    constraint chk_user_metadata_phone_length check (char_length(coalesce(phone, '')) <= 16)
);
create index if not exists idx_user_metadata_phone on user_metadata(phone);

create table if not exists categories (
  id uuid primary key default gen_random_uuid(),
  version bigint not null,
  name varchar(100) not null unique,
  description varchar(255),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_categories_name_length check (char_length(name) between 3 and 100),
    constraint chk_categories_description_length check (char_length(coalesce(description, '')) <= 255)
);
create index if not exists idx_categories_name on categories(name);

create table if not exists projects (
  id uuid primary key default gen_random_uuid(),
  version bigint not null,
  name varchar(100) not null unique,
  description varchar(255),
  category_id uuid not null references categories(id) on delete cascade,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_projects_name_length check (char_length(name) between 3 and 100),
    constraint chk_projects_description_length check (char_length(coalesce(description, '')) <= 255)
);
create index if not exists idx_projects_name on projects(name);
create index if not exists idx_projects_category_id on projects(category_id);

create table if not exists events (
  id uuid primary key default gen_random_uuid(),
  version bigint not null,
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
  version bigint not null,
  name varchar(50) not null unique,
  description varchar(255),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
    constraint chk_tags_name_length check (char_length(name) between 3 and 50),
    constraint chk_tags_description_length check (char_length(coalesce(description, '')) <= 255)
);
create index if not exists idx_tags_name on tags(name);

create table if not exists event_tags (
  event_id uuid not null references events(id) on delete cascade,
  tag_id uuid not null references tags(id) on delete cascade,
  primary key (event_id, tag_id)
);
create index if not exists idx_event_tags_tag_id on event_tags(tag_id);
