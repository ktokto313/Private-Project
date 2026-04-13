--
-- PostgreSQL database dump
--

\restrict NCRbqtFcljqfxwENxFFEZ2abkMQdZGkH70P0r85HNtNDCob7SacLShCbhF32TFC

-- Dumped from database version 18.3 (Debian 18.3-1.pgdg13+1)
-- Dumped by pg_dump version 18.3

-- Started on 2026-04-13 15:59:16 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3527 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 889 (class 1247 OID 16600)
-- Name: attachmenttype; Type: TYPE; Schema: public; Owner: sa
--

CREATE TYPE public.attachmenttype AS ENUM (
    'TICKET',
    'COMMENT'
);


ALTER TYPE public.attachmenttype OWNER TO sa;

--
-- TOC entry 868 (class 1247 OID 16501)
-- Name: role; Type: TYPE; Schema: public; Owner: sa
--

CREATE TYPE public.role AS ENUM (
    'USER',
    'IT',
    'ADMIN'
);


ALTER TYPE public.role OWNER TO sa;

--
-- TOC entry 880 (class 1247 OID 16542)
-- Name: state; Type: TYPE; Schema: public; Owner: sa
--

CREATE TYPE public.state AS ENUM (
    'CREATED',
    'PROCESSING',
    'RESOLVED',
    'DONE'
);


ALTER TYPE public.state OWNER TO sa;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 232 (class 1259 OID 16606)
-- Name: attachments; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE public.attachments (
    id integer NOT NULL,
    content bytea,
    attachedobjecttype public.attachmenttype,
    contenttype character varying(50),
    attachedobjectid integer
);


ALTER TABLE public.attachments OWNER TO sa;

--
-- TOC entry 231 (class 1259 OID 16605)
-- Name: attachments_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

ALTER TABLE public.attachments ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.attachments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 230 (class 1259 OID 16581)
-- Name: comments; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE public.comments (
    id integer NOT NULL,
    detail character varying(500),
    creator integer,
    ticketid integer,
    timecreated timestamp without time zone
);


ALTER TABLE public.comments OWNER TO sa;

--
-- TOC entry 229 (class 1259 OID 16580)
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

ALTER TABLE public.comments ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 16492)
-- Name: departments; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE public.departments (
    id integer NOT NULL,
    name character varying(50)
);


ALTER TABLE public.departments OWNER TO sa;

--
-- TOC entry 219 (class 1259 OID 16491)
-- Name: departments_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

ALTER TABLE public.departments ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.departments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 16522)
-- Name: priorities; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE public.priorities (
    id integer NOT NULL,
    levelofpriority integer,
    name character varying(50),
    timetorespond interval,
    timetofinish interval
);


ALTER TABLE public.priorities OWNER TO sa;

--
-- TOC entry 223 (class 1259 OID 16521)
-- Name: priorities_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

ALTER TABLE public.priorities ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.priorities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 16552)
-- Name: tickets; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE public.tickets (
    id integer NOT NULL,
    title character varying(100),
    detail character varying(500),
    creator integer,
    state public.state,
    priority integer,
    tickettype integer,
    timecreated timestamp without time zone,
    timeprocessing timestamp without time zone,
    timeresolved timestamp without time zone,
    assignee integer,
    cause character varying(100)
);


ALTER TABLE public.tickets OWNER TO sa;

--
-- TOC entry 227 (class 1259 OID 16551)
-- Name: tickets_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

ALTER TABLE public.tickets ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.tickets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 16533)
-- Name: tickettypes; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE public.tickettypes (
    id integer NOT NULL,
    title character varying(50),
    description character varying(200)
);


ALTER TABLE public.tickettypes OWNER TO sa;

--
-- TOC entry 225 (class 1259 OID 16532)
-- Name: tickettypes_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

ALTER TABLE public.tickettypes ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.tickettypes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 16508)
-- Name: users; Type: TABLE; Schema: public; Owner: sa
--

CREATE TABLE public.users (
    id integer NOT NULL,
    role public.role,
    username character varying(50),
    password character varying(60),
    departmentid integer
);


ALTER TABLE public.users OWNER TO sa;

--
-- TOC entry 221 (class 1259 OID 16507)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: sa
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3521 (class 0 OID 16606)
-- Dependencies: 232
-- Data for Name: attachments; Type: TABLE DATA; Schema: public; Owner: sa
--

INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (1, '\x23848b5f8ebf7d1342f06722f8f73302', 'TICKET', 'text/plain', 1);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (2, '\x29207a3d7f2ce17cd6309d1fc0f5ad7e', 'TICKET', 'image/png', 2);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (3, '\x3a669f9d5ca62b6603825427b165d6b2', 'TICKET', 'application/json', 3);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (4, '\x04a75036e9d520bb983c5ed03b8d0182', 'COMMENT', 'text/plain', 4);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (5, '\x93ec266224f524ba00fb96626d09dd3c', 'TICKET', 'image/png', 5);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (6, '\xe98d2f001da5678b39482efbdf5770dc', 'COMMENT', 'application/pdf', 6);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (7, '\x6a99dc87a303c2ef0f37f52a654435e6', 'TICKET', 'text/plain', 7);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (8, '\x2245023265ae4cf87d02c8b6ba991139', 'COMMENT', 'application/xml', 8);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (9, '\xb9ef165b255673dde47bff07f4390fb1', 'TICKET', 'application/octet-stream', 9);
INSERT INTO public.attachments OVERRIDING SYSTEM VALUE VALUES (10, '\xaad653ca3ee669635f2938b73098b6d7', 'COMMENT', 'text/plain', 10);


--
-- TOC entry 3519 (class 0 OID 16581)
-- Dependencies: 230
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: sa
--

INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (1, 'Can you provide more details?', 2, 2, '2026-04-13 15:55:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (2, 'Issue reproduced successfully.', 3, 3, '2026-04-13 15:54:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (3, 'Fix deployed to staging.', 4, 4, '2026-04-13 15:53:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (4, 'Awaiting user confirmation.', 5, 5, '2026-04-13 15:52:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (5, 'Resolved, please verify.', 6, 6, '2026-04-13 15:51:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (6, 'Escalating to senior team.', 7, 7, '2026-04-13 15:50:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (7, 'Logs indicate possible root cause.', 8, 8, '2026-04-13 15:49:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (8, 'Temporary workaround applied.', 9, 9, '2026-04-13 15:48:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (9, 'Closing ticket due to inactivity.', 10, 10, '2026-04-13 15:47:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (10, 'Looking into this issue.', 11, 11, '2026-04-13 15:46:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (11, 'Can you provide more details?', 12, 12, '2026-04-13 15:45:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (12, 'Issue reproduced successfully.', 13, 13, '2026-04-13 15:44:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (13, 'Fix deployed to staging.', 14, 14, '2026-04-13 15:43:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (14, 'Awaiting user confirmation.', 15, 15, '2026-04-13 15:42:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (15, 'Resolved, please verify.', 16, 16, '2026-04-13 15:41:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (16, 'Escalating to senior team.', 17, 17, '2026-04-13 15:40:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (17, 'Logs indicate possible root cause.', 18, 18, '2026-04-13 15:39:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (18, 'Temporary workaround applied.', 19, 19, '2026-04-13 15:38:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (19, 'Closing ticket due to inactivity.', 20, 20, '2026-04-13 15:37:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (20, 'Looking into this issue.', 21, 21, '2026-04-13 15:36:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (21, 'Can you provide more details?', 22, 22, '2026-04-13 15:35:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (22, 'Issue reproduced successfully.', 23, 23, '2026-04-13 15:34:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (23, 'Fix deployed to staging.', 24, 24, '2026-04-13 15:33:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (24, 'Awaiting user confirmation.', 25, 25, '2026-04-13 15:32:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (25, 'Resolved, please verify.', 26, 26, '2026-04-13 15:31:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (26, 'Escalating to senior team.', 27, 27, '2026-04-13 15:30:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (27, 'Logs indicate possible root cause.', 28, 28, '2026-04-13 15:29:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (28, 'Temporary workaround applied.', 29, 29, '2026-04-13 15:28:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (29, 'Closing ticket due to inactivity.', 30, 30, '2026-04-13 15:27:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (30, 'Looking into this issue.', 31, 31, '2026-04-13 15:26:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (31, 'Can you provide more details?', 32, 32, '2026-04-13 15:25:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (32, 'Issue reproduced successfully.', 33, 33, '2026-04-13 15:24:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (33, 'Fix deployed to staging.', 34, 34, '2026-04-13 15:23:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (34, 'Awaiting user confirmation.', 35, 35, '2026-04-13 15:22:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (35, 'Resolved, please verify.', 36, 36, '2026-04-13 15:21:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (36, 'Escalating to senior team.', 37, 37, '2026-04-13 15:20:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (37, 'Logs indicate possible root cause.', 38, 38, '2026-04-13 15:19:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (38, 'Temporary workaround applied.', 39, 39, '2026-04-13 15:18:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (39, 'Closing ticket due to inactivity.', 40, 40, '2026-04-13 15:17:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (40, 'Looking into this issue.', 41, 41, '2026-04-13 15:16:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (41, 'Can you provide more details?', 42, 42, '2026-04-13 15:15:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (42, 'Issue reproduced successfully.', 43, 43, '2026-04-13 15:14:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (43, 'Fix deployed to staging.', 44, 44, '2026-04-13 15:13:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (44, 'Awaiting user confirmation.', 45, 45, '2026-04-13 15:12:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (45, 'Resolved, please verify.', 46, 46, '2026-04-13 15:11:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (46, 'Escalating to senior team.', 47, 47, '2026-04-13 15:10:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (47, 'Logs indicate possible root cause.', 48, 48, '2026-04-13 15:09:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (48, 'Temporary workaround applied.', 49, 49, '2026-04-13 15:08:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (49, 'Closing ticket due to inactivity.', 50, 50, '2026-04-13 15:07:21.984948');
INSERT INTO public.comments OVERRIDING SYSTEM VALUE VALUES (50, 'Looking into this issue.', 1, 1, '2026-04-13 15:06:21.984948');


--
-- TOC entry 3509 (class 0 OID 16492)
-- Dependencies: 220
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: sa
--

INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (1, 'Human Resources');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (2, 'Finance');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (3, 'IT Support');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (4, 'Software Engineering');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (5, 'Quality Assurance');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (6, 'DevOps');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (7, 'Customer Support');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (8, 'Sales');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (9, 'Marketing');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (10, 'Legal');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (11, 'Procurement');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (12, 'Operations');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (13, 'Logistics');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (14, 'R&D');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (15, 'Product Management');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (16, 'Data Science');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (17, 'Security');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (18, 'Compliance');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (19, 'Training');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (20, 'Administration');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (21, 'Business Intelligence');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (22, 'Infrastructure');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (23, 'Network Operations');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (24, 'Cloud Engineering');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (25, 'Technical Support');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (26, 'Field Services');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (27, 'Customer Success');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (28, 'UX/UI Design');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (29, 'Content Team');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (30, 'Partnerships');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (31, 'Public Relations');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (32, 'Strategy');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (33, 'Risk Management');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (34, 'Audit');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (35, 'Accounting');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (36, 'Payroll');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (37, 'Recruitment');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (38, 'Learning & Development');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (39, 'IT Helpdesk');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (40, 'Database Administration');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (41, 'Mobile Development');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (42, 'Web Development');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (43, 'AI Engineering');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (44, 'Automation');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (45, 'Support Tier 2');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (46, 'Support Tier 3');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (47, 'Vendor Management');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (48, 'Facilities');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (49, 'Internal Tools');
INSERT INTO public.departments OVERRIDING SYSTEM VALUE VALUES (50, 'Platform Engineering');


--
-- TOC entry 3513 (class 0 OID 16522)
-- Dependencies: 224
-- Data for Name: priorities; Type: TABLE DATA; Schema: public; Owner: sa
--

INSERT INTO public.priorities OVERRIDING SYSTEM VALUE VALUES (1, 1, 'Critical', '00:05:00', '01:00:00');
INSERT INTO public.priorities OVERRIDING SYSTEM VALUE VALUES (2, 2, 'High', '00:15:00', '04:00:00');
INSERT INTO public.priorities OVERRIDING SYSTEM VALUE VALUES (3, 3, 'Medium', '01:00:00', '1 day');
INSERT INTO public.priorities OVERRIDING SYSTEM VALUE VALUES (4, 4, 'Low', '04:00:00', '3 days');
INSERT INTO public.priorities OVERRIDING SYSTEM VALUE VALUES (5, 5, 'Very Low', '1 day', '7 days');


--
-- TOC entry 3517 (class 0 OID 16552)
-- Dependencies: 228
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: sa
--

INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (1, 'Page loads very slowly', 'Dashboard takes more than 10 seconds to load.', 2, 'PROCESSING', 2, 2, '2026-04-13 14:56:21.984948', '2026-04-13 15:56:21.984948', '2026-04-13 16:56:21.984948', 7, 'Database performance bottleneck');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (2, 'Error when submitting form', 'Form submission throws unknown server error.', 3, 'RESOLVED', 3, 3, '2026-04-13 13:56:21.984948', '2026-04-13 14:56:21.984948', '2026-04-13 15:56:21.984948', 8, 'Unhandled exception in backend');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (3, 'Request access to dashboard', 'User needs access to analytics dashboard.', 4, 'DONE', 4, 4, '2026-04-13 12:56:21.984948', '2026-04-13 13:56:21.984948', '2026-04-13 14:56:21.984948', 9, 'Missing role assignment');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (4, 'Application crashes on startup', 'App crashes immediately after launch.', 5, 'CREATED', 5, 5, '2026-04-13 11:56:21.984948', '2026-04-13 12:56:21.984948', '2026-04-13 13:56:21.984948', 10, 'Client-side bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (5, 'Data not syncing correctly', 'Records are not updating across services.', 6, 'PROCESSING', 1, 6, '2026-04-13 10:56:21.984948', '2026-04-13 11:56:21.984948', '2026-04-13 12:56:21.984948', 11, 'Synchronization job failure');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (6, 'Email notifications not sent', 'Users are not receiving system emails.', 7, 'RESOLVED', 2, 7, '2026-04-13 09:56:21.984948', '2026-04-13 10:56:21.984948', '2026-04-13 11:56:21.984948', 12, 'SMTP server issue');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (7, 'UI layout broken on mobile', 'Layout overlaps on smaller screens.', 8, 'DONE', 3, 8, '2026-04-13 08:56:21.984948', '2026-04-13 09:56:21.984948', '2026-04-13 10:56:21.984948', 13, 'CSS rendering bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (8, 'API returns 500 error', 'Backend service failing intermittently.', 9, 'CREATED', 4, 9, '2026-04-13 07:56:21.984948', '2026-04-13 08:56:21.984948', '2026-04-13 09:56:21.984948', 14, 'API gateway timeout');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (9, 'Unable to reset password', 'Password reset link not working.', 10, 'PROCESSING', 5, 10, '2026-04-13 06:56:21.984948', '2026-04-13 07:56:21.984948', '2026-04-13 08:56:21.984948', 15, 'Token expiration misconfigured');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (10, 'Cannot login to system', 'User reports invalid credentials error despite correct password.', 11, 'RESOLVED', 1, 1, '2026-04-13 05:56:21.984948', '2026-04-13 06:56:21.984948', '2026-04-13 07:56:21.984948', 16, 'Authentication service misconfiguration');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (11, 'Page loads very slowly', 'Dashboard takes more than 10 seconds to load.', 12, 'DONE', 2, 2, '2026-04-13 04:56:21.984948', '2026-04-13 05:56:21.984948', '2026-04-13 06:56:21.984948', 17, 'Database performance bottleneck');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (12, 'Error when submitting form', 'Form submission throws unknown server error.', 13, 'CREATED', 3, 3, '2026-04-13 03:56:21.984948', '2026-04-13 04:56:21.984948', '2026-04-13 05:56:21.984948', 18, 'Unhandled exception in backend');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (13, 'Request access to dashboard', 'User needs access to analytics dashboard.', 14, 'PROCESSING', 4, 4, '2026-04-13 02:56:21.984948', '2026-04-13 03:56:21.984948', '2026-04-13 04:56:21.984948', 19, 'Missing role assignment');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (14, 'Application crashes on startup', 'App crashes immediately after launch.', 15, 'RESOLVED', 5, 5, '2026-04-13 01:56:21.984948', '2026-04-13 02:56:21.984948', '2026-04-13 03:56:21.984948', 20, 'Client-side bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (15, 'Data not syncing correctly', 'Records are not updating across services.', 16, 'DONE', 1, 6, '2026-04-13 00:56:21.984948', '2026-04-13 01:56:21.984948', '2026-04-13 02:56:21.984948', 21, 'Synchronization job failure');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (16, 'Email notifications not sent', 'Users are not receiving system emails.', 17, 'CREATED', 2, 7, '2026-04-12 23:56:21.984948', '2026-04-13 00:56:21.984948', '2026-04-13 01:56:21.984948', 22, 'SMTP server issue');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (17, 'UI layout broken on mobile', 'Layout overlaps on smaller screens.', 18, 'PROCESSING', 3, 8, '2026-04-12 22:56:21.984948', '2026-04-12 23:56:21.984948', '2026-04-13 00:56:21.984948', 23, 'CSS rendering bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (18, 'API returns 500 error', 'Backend service failing intermittently.', 19, 'RESOLVED', 4, 9, '2026-04-12 21:56:21.984948', '2026-04-12 22:56:21.984948', '2026-04-12 23:56:21.984948', 24, 'API gateway timeout');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (19, 'Unable to reset password', 'Password reset link not working.', 20, 'DONE', 5, 10, '2026-04-12 20:56:21.984948', '2026-04-12 21:56:21.984948', '2026-04-12 22:56:21.984948', 25, 'Token expiration misconfigured');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (20, 'Cannot login to system', 'User reports invalid credentials error despite correct password.', 21, 'CREATED', 1, 1, '2026-04-12 19:56:21.984948', '2026-04-12 20:56:21.984948', '2026-04-12 21:56:21.984948', 26, 'Authentication service misconfiguration');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (21, 'Page loads very slowly', 'Dashboard takes more than 10 seconds to load.', 22, 'PROCESSING', 2, 2, '2026-04-12 18:56:21.984948', '2026-04-12 19:56:21.984948', '2026-04-12 20:56:21.984948', 27, 'Database performance bottleneck');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (22, 'Error when submitting form', 'Form submission throws unknown server error.', 23, 'RESOLVED', 3, 3, '2026-04-12 17:56:21.984948', '2026-04-12 18:56:21.984948', '2026-04-12 19:56:21.984948', 28, 'Unhandled exception in backend');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (23, 'Request access to dashboard', 'User needs access to analytics dashboard.', 24, 'DONE', 4, 4, '2026-04-12 16:56:21.984948', '2026-04-12 17:56:21.984948', '2026-04-12 18:56:21.984948', 29, 'Missing role assignment');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (24, 'Application crashes on startup', 'App crashes immediately after launch.', 25, 'CREATED', 5, 5, '2026-04-12 15:56:21.984948', '2026-04-12 16:56:21.984948', '2026-04-12 17:56:21.984948', 30, 'Client-side bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (25, 'Data not syncing correctly', 'Records are not updating across services.', 26, 'PROCESSING', 1, 6, '2026-04-12 14:56:21.984948', '2026-04-12 15:56:21.984948', '2026-04-12 16:56:21.984948', 31, 'Synchronization job failure');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (26, 'Email notifications not sent', 'Users are not receiving system emails.', 27, 'RESOLVED', 2, 7, '2026-04-12 13:56:21.984948', '2026-04-12 14:56:21.984948', '2026-04-12 15:56:21.984948', 32, 'SMTP server issue');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (27, 'UI layout broken on mobile', 'Layout overlaps on smaller screens.', 28, 'DONE', 3, 8, '2026-04-12 12:56:21.984948', '2026-04-12 13:56:21.984948', '2026-04-12 14:56:21.984948', 33, 'CSS rendering bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (28, 'API returns 500 error', 'Backend service failing intermittently.', 29, 'CREATED', 4, 9, '2026-04-12 11:56:21.984948', '2026-04-12 12:56:21.984948', '2026-04-12 13:56:21.984948', 34, 'API gateway timeout');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (29, 'Unable to reset password', 'Password reset link not working.', 30, 'PROCESSING', 5, 10, '2026-04-12 10:56:21.984948', '2026-04-12 11:56:21.984948', '2026-04-12 12:56:21.984948', 35, 'Token expiration misconfigured');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (30, 'Cannot login to system', 'User reports invalid credentials error despite correct password.', 31, 'RESOLVED', 1, 1, '2026-04-12 09:56:21.984948', '2026-04-12 10:56:21.984948', '2026-04-12 11:56:21.984948', 36, 'Authentication service misconfiguration');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (31, 'Page loads very slowly', 'Dashboard takes more than 10 seconds to load.', 32, 'DONE', 2, 2, '2026-04-12 08:56:21.984948', '2026-04-12 09:56:21.984948', '2026-04-12 10:56:21.984948', 37, 'Database performance bottleneck');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (32, 'Error when submitting form', 'Form submission throws unknown server error.', 33, 'CREATED', 3, 3, '2026-04-12 07:56:21.984948', '2026-04-12 08:56:21.984948', '2026-04-12 09:56:21.984948', 38, 'Unhandled exception in backend');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (33, 'Request access to dashboard', 'User needs access to analytics dashboard.', 34, 'PROCESSING', 4, 4, '2026-04-12 06:56:21.984948', '2026-04-12 07:56:21.984948', '2026-04-12 08:56:21.984948', 39, 'Missing role assignment');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (34, 'Application crashes on startup', 'App crashes immediately after launch.', 35, 'RESOLVED', 5, 5, '2026-04-12 05:56:21.984948', '2026-04-12 06:56:21.984948', '2026-04-12 07:56:21.984948', 40, 'Client-side bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (35, 'Data not syncing correctly', 'Records are not updating across services.', 36, 'DONE', 1, 6, '2026-04-12 04:56:21.984948', '2026-04-12 05:56:21.984948', '2026-04-12 06:56:21.984948', 41, 'Synchronization job failure');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (36, 'Email notifications not sent', 'Users are not receiving system emails.', 37, 'CREATED', 2, 7, '2026-04-12 03:56:21.984948', '2026-04-12 04:56:21.984948', '2026-04-12 05:56:21.984948', 42, 'SMTP server issue');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (37, 'UI layout broken on mobile', 'Layout overlaps on smaller screens.', 38, 'PROCESSING', 3, 8, '2026-04-12 02:56:21.984948', '2026-04-12 03:56:21.984948', '2026-04-12 04:56:21.984948', 43, 'CSS rendering bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (38, 'API returns 500 error', 'Backend service failing intermittently.', 39, 'RESOLVED', 4, 9, '2026-04-12 01:56:21.984948', '2026-04-12 02:56:21.984948', '2026-04-12 03:56:21.984948', 44, 'API gateway timeout');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (39, 'Unable to reset password', 'Password reset link not working.', 40, 'DONE', 5, 10, '2026-04-12 00:56:21.984948', '2026-04-12 01:56:21.984948', '2026-04-12 02:56:21.984948', 45, 'Token expiration misconfigured');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (40, 'Cannot login to system', 'User reports invalid credentials error despite correct password.', 41, 'CREATED', 1, 1, '2026-04-11 23:56:21.984948', '2026-04-12 00:56:21.984948', '2026-04-12 01:56:21.984948', 46, 'Authentication service misconfiguration');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (41, 'Page loads very slowly', 'Dashboard takes more than 10 seconds to load.', 42, 'PROCESSING', 2, 2, '2026-04-11 22:56:21.984948', '2026-04-11 23:56:21.984948', '2026-04-12 00:56:21.984948', 47, 'Database performance bottleneck');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (42, 'Error when submitting form', 'Form submission throws unknown server error.', 43, 'RESOLVED', 3, 3, '2026-04-11 21:56:21.984948', '2026-04-11 22:56:21.984948', '2026-04-11 23:56:21.984948', 48, 'Unhandled exception in backend');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (43, 'Request access to dashboard', 'User needs access to analytics dashboard.', 44, 'DONE', 4, 4, '2026-04-11 20:56:21.984948', '2026-04-11 21:56:21.984948', '2026-04-11 22:56:21.984948', 49, 'Missing role assignment');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (44, 'Application crashes on startup', 'App crashes immediately after launch.', 45, 'CREATED', 5, 5, '2026-04-11 19:56:21.984948', '2026-04-11 20:56:21.984948', '2026-04-11 21:56:21.984948', 50, 'Client-side bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (45, 'Data not syncing correctly', 'Records are not updating across services.', 46, 'PROCESSING', 1, 6, '2026-04-11 18:56:21.984948', '2026-04-11 19:56:21.984948', '2026-04-11 20:56:21.984948', 1, 'Synchronization job failure');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (46, 'Email notifications not sent', 'Users are not receiving system emails.', 47, 'RESOLVED', 2, 7, '2026-04-11 17:56:21.984948', '2026-04-11 18:56:21.984948', '2026-04-11 19:56:21.984948', 2, 'SMTP server issue');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (47, 'UI layout broken on mobile', 'Layout overlaps on smaller screens.', 48, 'DONE', 3, 8, '2026-04-11 16:56:21.984948', '2026-04-11 17:56:21.984948', '2026-04-11 18:56:21.984948', 3, 'CSS rendering bug');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (48, 'API returns 500 error', 'Backend service failing intermittently.', 49, 'CREATED', 4, 9, '2026-04-11 15:56:21.984948', '2026-04-11 16:56:21.984948', '2026-04-11 17:56:21.984948', 4, 'API gateway timeout');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (49, 'Unable to reset password', 'Password reset link not working.', 50, 'PROCESSING', 5, 10, '2026-04-11 14:56:21.984948', '2026-04-11 15:56:21.984948', '2026-04-11 16:56:21.984948', 5, 'Token expiration misconfigured');
INSERT INTO public.tickets OVERRIDING SYSTEM VALUE VALUES (50, 'Cannot login to system', 'User reports invalid credentials error despite correct password.', 1, 'RESOLVED', 1, 1, '2026-04-11 13:56:21.984948', '2026-04-11 14:56:21.984948', '2026-04-11 15:56:21.984948', 6, 'Authentication service misconfiguration');


--
-- TOC entry 3515 (class 0 OID 16533)
-- Dependencies: 226
-- Data for Name: tickettypes; Type: TABLE DATA; Schema: public; Owner: sa
--

INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (1, 'Bug', 'Software defect or unexpected behavior');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (2, 'Feature Request', 'Request for new functionality');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (3, 'Incident', 'System outage or critical issue');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (4, 'Service Request', 'General support request');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (5, 'Access Request', 'Permission or account related');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (6, 'Performance Issue', 'Slow or degraded system');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (7, 'Security Issue', 'Potential vulnerability or breach');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (8, 'Data Issue', 'Incorrect or missing data');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (9, 'UI/UX Issue', 'Interface or usability problem');
INSERT INTO public.tickettypes OVERRIDING SYSTEM VALUE VALUES (10, 'Integration Issue', 'Third-party or API problem');


--
-- TOC entry 3511 (class 0 OID 16508)
-- Dependencies: 222
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: sa
--

INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (1, 'IT', 'binh.tran1', '$2b$10$examplehashedpasswordstring1234567890abcd', 2);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (2, 'ADMIN', 'chi.le2', '$2b$10$examplehashedpasswordstring1234567890abcd', 3);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (3, 'USER', 'duc.pham3', '$2b$10$examplehashedpasswordstring1234567890abcd', 4);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (4, 'IT', 'huy.hoang4', '$2b$10$examplehashedpasswordstring1234567890abcd', 5);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (5, 'ADMIN', 'khanh.vu5', '$2b$10$examplehashedpasswordstring1234567890abcd', 6);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (6, 'USER', 'linh.phan6', '$2b$10$examplehashedpasswordstring1234567890abcd', 7);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (7, 'IT', 'minh.dang7', '$2b$10$examplehashedpasswordstring1234567890abcd', 8);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (8, 'ADMIN', 'nam.bui8', '$2b$10$examplehashedpasswordstring1234567890abcd', 9);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (9, 'USER', 'phuong.do9', '$2b$10$examplehashedpasswordstring1234567890abcd', 10);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (10, 'IT', 'quang.nguyen10', '$2b$10$examplehashedpasswordstring1234567890abcd', 11);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (11, 'ADMIN', 'trang.tran11', '$2b$10$examplehashedpasswordstring1234567890abcd', 12);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (12, 'USER', 'tuan.le12', '$2b$10$examplehashedpasswordstring1234567890abcd', 13);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (13, 'IT', 'vy.pham13', '$2b$10$examplehashedpasswordstring1234567890abcd', 14);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (14, 'ADMIN', 'yen.hoang14', '$2b$10$examplehashedpasswordstring1234567890abcd', 15);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (15, 'USER', 'long.vu15', '$2b$10$examplehashedpasswordstring1234567890abcd', 16);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (16, 'IT', 'hai.phan16', '$2b$10$examplehashedpasswordstring1234567890abcd', 17);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (17, 'ADMIN', 'son.dang17', '$2b$10$examplehashedpasswordstring1234567890abcd', 18);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (18, 'USER', 'thao.bui18', '$2b$10$examplehashedpasswordstring1234567890abcd', 19);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (19, 'IT', 'hoa.do19', '$2b$10$examplehashedpasswordstring1234567890abcd', 20);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (20, 'ADMIN', 'an.nguyen20', '$2b$10$examplehashedpasswordstring1234567890abcd', 21);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (21, 'USER', 'binh.tran21', '$2b$10$examplehashedpasswordstring1234567890abcd', 22);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (22, 'IT', 'chi.le22', '$2b$10$examplehashedpasswordstring1234567890abcd', 23);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (23, 'ADMIN', 'duc.pham23', '$2b$10$examplehashedpasswordstring1234567890abcd', 24);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (24, 'USER', 'huy.hoang24', '$2b$10$examplehashedpasswordstring1234567890abcd', 25);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (25, 'IT', 'khanh.vu25', '$2b$10$examplehashedpasswordstring1234567890abcd', 26);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (26, 'ADMIN', 'linh.phan26', '$2b$10$examplehashedpasswordstring1234567890abcd', 27);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (27, 'USER', 'minh.dang27', '$2b$10$examplehashedpasswordstring1234567890abcd', 28);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (28, 'IT', 'nam.bui28', '$2b$10$examplehashedpasswordstring1234567890abcd', 29);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (29, 'ADMIN', 'phuong.do29', '$2b$10$examplehashedpasswordstring1234567890abcd', 30);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (30, 'USER', 'quang.nguyen30', '$2b$10$examplehashedpasswordstring1234567890abcd', 31);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (31, 'IT', 'trang.tran31', '$2b$10$examplehashedpasswordstring1234567890abcd', 32);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (32, 'ADMIN', 'tuan.le32', '$2b$10$examplehashedpasswordstring1234567890abcd', 33);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (33, 'USER', 'vy.pham33', '$2b$10$examplehashedpasswordstring1234567890abcd', 34);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (34, 'IT', 'yen.hoang34', '$2b$10$examplehashedpasswordstring1234567890abcd', 35);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (35, 'ADMIN', 'long.vu35', '$2b$10$examplehashedpasswordstring1234567890abcd', 36);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (36, 'USER', 'hai.phan36', '$2b$10$examplehashedpasswordstring1234567890abcd', 37);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (37, 'IT', 'son.dang37', '$2b$10$examplehashedpasswordstring1234567890abcd', 38);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (38, 'ADMIN', 'thao.bui38', '$2b$10$examplehashedpasswordstring1234567890abcd', 39);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (39, 'USER', 'hoa.do39', '$2b$10$examplehashedpasswordstring1234567890abcd', 40);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (40, 'IT', 'an.nguyen40', '$2b$10$examplehashedpasswordstring1234567890abcd', 41);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (41, 'ADMIN', 'binh.tran41', '$2b$10$examplehashedpasswordstring1234567890abcd', 42);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (42, 'USER', 'chi.le42', '$2b$10$examplehashedpasswordstring1234567890abcd', 43);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (43, 'IT', 'duc.pham43', '$2b$10$examplehashedpasswordstring1234567890abcd', 44);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (44, 'ADMIN', 'huy.hoang44', '$2b$10$examplehashedpasswordstring1234567890abcd', 45);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (45, 'USER', 'khanh.vu45', '$2b$10$examplehashedpasswordstring1234567890abcd', 46);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (46, 'IT', 'linh.phan46', '$2b$10$examplehashedpasswordstring1234567890abcd', 47);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (47, 'ADMIN', 'minh.dang47', '$2b$10$examplehashedpasswordstring1234567890abcd', 48);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (48, 'USER', 'nam.bui48', '$2b$10$examplehashedpasswordstring1234567890abcd', 49);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (49, 'IT', 'phuong.do49', '$2b$10$examplehashedpasswordstring1234567890abcd', 50);
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (50, 'ADMIN', 'quang.nguyen50', '$2b$10$examplehashedpasswordstring1234567890abcd', 1);


--
-- TOC entry 3528 (class 0 OID 0)
-- Dependencies: 231
-- Name: attachments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('public.attachments_id_seq', 10, true);


--
-- TOC entry 3529 (class 0 OID 0)
-- Dependencies: 229
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('public.comments_id_seq', 50, true);


--
-- TOC entry 3530 (class 0 OID 0)
-- Dependencies: 219
-- Name: departments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('public.departments_id_seq', 50, true);


--
-- TOC entry 3531 (class 0 OID 0)
-- Dependencies: 223
-- Name: priorities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('public.priorities_id_seq', 5, true);


--
-- TOC entry 3532 (class 0 OID 0)
-- Dependencies: 227
-- Name: tickets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('public.tickets_id_seq', 50, true);


--
-- TOC entry 3533 (class 0 OID 0)
-- Dependencies: 225
-- Name: tickettypes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('public.tickettypes_id_seq', 10, true);


--
-- TOC entry 3534 (class 0 OID 0)
-- Dependencies: 221
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: sa
--

SELECT pg_catalog.setval('public.users_id_seq', 50, true);


--
-- TOC entry 3351 (class 2606 OID 16615)
-- Name: attachments attachments_attachedobjecttype_attachedobjectid_key; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_attachedobjecttype_attachedobjectid_key UNIQUE (attachedobjecttype, attachedobjectid);


--
-- TOC entry 3353 (class 2606 OID 16613)
-- Name: attachments attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.attachments
    ADD CONSTRAINT attachments_pkey PRIMARY KEY (id);


--
-- TOC entry 3349 (class 2606 OID 16588)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- TOC entry 3329 (class 2606 OID 16499)
-- Name: departments departments_name_key; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_name_key UNIQUE (name);


--
-- TOC entry 3331 (class 2606 OID 16497)
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (id);


--
-- TOC entry 3337 (class 2606 OID 16529)
-- Name: priorities priorities_levelofpriority_key; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.priorities
    ADD CONSTRAINT priorities_levelofpriority_key UNIQUE (levelofpriority);


--
-- TOC entry 3339 (class 2606 OID 16531)
-- Name: priorities priorities_name_key; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.priorities
    ADD CONSTRAINT priorities_name_key UNIQUE (name);


--
-- TOC entry 3341 (class 2606 OID 16527)
-- Name: priorities priorities_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.priorities
    ADD CONSTRAINT priorities_pkey PRIMARY KEY (id);


--
-- TOC entry 3347 (class 2606 OID 16559)
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- TOC entry 3343 (class 2606 OID 16538)
-- Name: tickettypes tickettypes_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.tickettypes
    ADD CONSTRAINT tickettypes_pkey PRIMARY KEY (id);


--
-- TOC entry 3345 (class 2606 OID 16540)
-- Name: tickettypes tickettypes_title_key; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.tickettypes
    ADD CONSTRAINT tickettypes_title_key UNIQUE (title);


--
-- TOC entry 3333 (class 2606 OID 16513)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3335 (class 2606 OID 16515)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3359 (class 2606 OID 16589)
-- Name: comments comments_creator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_creator_fkey FOREIGN KEY (creator) REFERENCES public.users(id);


--
-- TOC entry 3360 (class 2606 OID 16594)
-- Name: comments comments_ticketid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_ticketid_fkey FOREIGN KEY (ticketid) REFERENCES public.tickets(id);


--
-- TOC entry 3355 (class 2606 OID 16575)
-- Name: tickets tickets_assignee_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_assignee_fkey FOREIGN KEY (assignee) REFERENCES public.users(id);


--
-- TOC entry 3356 (class 2606 OID 16560)
-- Name: tickets tickets_creator_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_creator_fkey FOREIGN KEY (creator) REFERENCES public.users(id);


--
-- TOC entry 3357 (class 2606 OID 16565)
-- Name: tickets tickets_priority_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_priority_fkey FOREIGN KEY (priority) REFERENCES public.priorities(id);


--
-- TOC entry 3358 (class 2606 OID 16570)
-- Name: tickets tickets_tickettype_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_tickettype_fkey FOREIGN KEY (tickettype) REFERENCES public.tickettypes(id);


--
-- TOC entry 3354 (class 2606 OID 16516)
-- Name: users users_departmentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: sa
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_departmentid_fkey FOREIGN KEY (departmentid) REFERENCES public.departments(id);


-- Completed on 2026-04-13 15:59:16 UTC

--
-- PostgreSQL database dump complete
--

\unrestrict NCRbqtFcljqfxwENxFFEZ2abkMQdZGkH70P0r85HNtNDCob7SacLShCbhF32TFC

