create table tb_users(
    user_id uuid primary key,
    name varchar(150) not null,
    email varchar(100) not null unique,
    password varchar(255) not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);