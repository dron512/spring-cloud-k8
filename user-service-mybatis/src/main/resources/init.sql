create table users
(
    user_idx      int primary key auto_increment,
    user_email    varchar(255) unique,
    user_id       varchar(255),
    user_password varchar(255),
    user_name     varchar(255),
    write_date    datetime default now(),
    update_date   datetime default now()
);

drop table users;