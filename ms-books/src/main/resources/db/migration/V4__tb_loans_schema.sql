alter table tb_loans drop constraint if exists tb_loans_user_email_key;
alter table tb_loans alter column user_email drop default;