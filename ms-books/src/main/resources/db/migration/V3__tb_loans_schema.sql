alter table tb_loans drop column user_id;
alter table tb_loans add column user_email varchar(150) not null unique default 'NO EMAIL YET';