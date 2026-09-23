create table tb_roles(
    role_id uuid primary key,
    name varchar(50) not null unique
);

create table tb_user_roles(
    user_id uuid not null,
    role_id uuid not null,
    primary key (user_id, role_id),
    foreign key (user_id) references tb_users(user_id),
    foreign key (role_id) references tb_roles(role_id)
);