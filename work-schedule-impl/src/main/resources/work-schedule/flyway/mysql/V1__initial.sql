create table wks_work_schedule (
	id binary(16) not null,
	category_id varchar(50),
	created_by_id varchar(50),
	created_by_name varchar(50),
	created_date datetime,
	work_date date,
	holiday bit not null,
	last_modified_by_id varchar(50),
	last_modified_by_name varchar(50),
	last_modified_date datetime,
	name varchar(50),
	primary key (id)
) engine=InnoDB;

create table wks_work_schedule_time (
	work_schedule_id binary(16) not null,
	begin time,
	end time,
	times_order integer not null,
	primary key (work_schedule_id,times_order)
) engine=InnoDB;

create index IDX5s8tocbt08tbsak7ry54xgfkj
	on wks_work_schedule (category_id,work_date);

alter table wks_work_schedule_time
	add constraint FKm8pbg9ekv646oovxncv0vu4e5 foreign key (work_schedule_id)
	references wks_work_schedule (id);
