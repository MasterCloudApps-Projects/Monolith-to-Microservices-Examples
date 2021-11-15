-- TABLES
create table loyalty
(
    id       bigint       not null primary key,
    customer_id     bigint null,
    loyalty_account varchar(255)   null
);