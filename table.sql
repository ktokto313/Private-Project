-- Database: app

-- DROP DATABASE IF EXISTS app;

create table Departments (
	id int generated always as identity primary key,
	name character varying(50) unique
);

create type role as ENUM ('USER', 'IT', 'ADMIN');

create table Users (
	id int generated always as identity primary key,
	role role,
	username character varying(50) unique,
	password character varying(60),
	departmentID int references Departments(id)
);

create table Priorities (
	id int generated always as identity primary key,
	levelOfPriority int unique,
	name character varying(50) unique,
	timeToRespond interval,
	timeToFinish interval
);

create table TicketTypes (
	id int generated always as identity primary key,
	title character varying(50) unique,
	description character varying(200)
);

create type state as ENUM('CREATED', 'PROCESSING', 'RESOLVED', 'DONE');

create table Tickets (
	id int generated always as identity primary key,
	title character varying(100),
	detail character varying(500),
	creator int references Users(id),
	state state,
	priority int references Priorities(id),
	ticketType int references TicketTypes(id),
	timeCreated timestamp,
	timeProcessing timestamp,
	timeResolved timestamp,
	assignee int references Users(id),
	cause character varying(100)
);

create table Comments (
	id int generated always as identity primary key,
	detail character varying(500),
	creator int references Users(id),
	ticketID int references Tickets(id),
	timeCreated timestamp
);

create type attachmentType as ENUM('TICKET', 'COMMENT');

create table Attachments (
	id int generated always as identity primary key,
	content bytea,
	attachedObjectType attachmentType,
	contentType character varying(50),
	attachedObjectID int,
	unique(attachedObjectType, attachedObjectID)
);
