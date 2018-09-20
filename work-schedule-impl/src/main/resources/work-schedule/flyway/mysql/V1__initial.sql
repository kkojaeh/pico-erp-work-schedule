create table wks_work_schedule (
	id varchar(255) not null,
	category_id varchar(255),
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
	work_day_id varchar(255) not null,
	begin time,
	end time,
	times_order integer not null,
	primary key (work_day_id,times_order)
) engine=InnoDB;

create index WOD_WORK_SCHEDULE_CATEGORY_ID_DATE_IDX
	on wod_work_day (category_id,work_date);

alter table wks_work_schedule_time
	add constraint FKhrlr707swbmdwsdna7eyvnt5f foreign key (work_day_id)
	references wks_work_schedule (id);
