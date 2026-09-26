drop table if exists tb_user_roles;

create table tb_user_roles(
    user_id uuid not null,
    role_id uuid not null,
    primary key(user_id, role_id),
    constraint fk_user_roles_user
        foreign key (user_id) references tb_users(user_id) on delete cascade,
    constraint fk_user_roles_role
        foreign key (role_id) references tb_roles(role_id)
);