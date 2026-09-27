create table tb_loans(
    loan_id uuid primary key,
    user_id uuid not null,
    book_id uuid not null,
    loan_date date not null,
    return_loan_date date not null,
    loan_status varchar(20) not null,
    created_at timestamp not null default current_timestamp,
    constraint fk_loans_book foreign key (book_id) references tb_books(book_id)
);