-- liquibase formatted sql
CREATE TABLE IF NOT EXISTS public.notification_task
(
    id integer
    , chatid integer
    , message character varying(254) COLLATE pg_catalog."default"
    , chat_id bigint NOT NULL
    , date_send timestamp without time zone
    PRIMARY KEY (id)
);