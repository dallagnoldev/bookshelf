alter table tb_emails drop column send_date_email;
alter table tb_emails add column send_date_email timestamp not null default current_timestamp;