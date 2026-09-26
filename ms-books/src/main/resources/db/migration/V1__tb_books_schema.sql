create table tb_books(
    book_id uuid primary key,
    title varchar(150) not null,
    author varchar(150) not null,
    isbn varchar(13) not null unique,
    publisher varchar(150) not null,
    publication_year int not null,
    total_copies int not null,
    available_copies int not null,
    category varchar(80) not null default 'UNSPECIFIED',
    language varchar(80) not null default 'UNSPECIFIED',
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);