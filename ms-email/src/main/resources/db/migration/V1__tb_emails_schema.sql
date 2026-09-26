create table tb_emails(
    email_id uuid primary key,
    user_id uuid not null,
    email_from varchar(150) not null,
    email_to varchar(150) not null,
    subject varchar(80) not null,
    text text not null,
    email_status varchar(30) not null,
    send_date_email date not null
);