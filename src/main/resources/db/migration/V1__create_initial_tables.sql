create extension if not exists "pgcrypto";

create table if not exists users (
  id UUID primary key default gen_random_uuid(),
  username text not null unique
)
