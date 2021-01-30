 drop database jobportaldb;
 drop user jobportal;
 create user jobportal with password 'password';
 create database jobportaldb with template=template0 owner=jobportal;
 \connect jobportaldb;

  alter default privileges grant all on tables to jobportal;
  alter default privileges grant all on sequences to jobportal;

  create table jp_users(
  user_id integer primary key not null,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  email varchar(30) not null,
  password text not null,
  user_type varchar(20)
  );

create table jp_jobs(
job_id integer primary key not null,
user_id integer not null,
job_title varchar(20) not null,
job_description varchar(50)not null
);
alter table jp_jobs add constraint zan_users_fk
foreign key(user_id) references jp_users(user_id);

create sequence jp_users_seq increment 1 start 1;
create sequence jp_jobs_seq increment 1 start 1;
