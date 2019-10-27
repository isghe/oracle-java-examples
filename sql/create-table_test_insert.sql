create table TEST_INSERT
(
  id               NUMBER(6) not null,
  insert_timestamp DATE default sysdate not null,
  description      VARCHAR2(256) not null
)
;
alter table TEST_INSERT
  add constraint PK_TEST_INSERT primary key (ID);

