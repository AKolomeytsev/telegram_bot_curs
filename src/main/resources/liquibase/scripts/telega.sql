-- liquibase formatted sql
//-- changeset TelegaBase:1
CREATE TABLE notification_task
(
    id integer
    , chatid integer
    , message character varying(254) COLLATE pg_catalog."default"
    , chat_id bigint NOT NULL
    , date_send timestamp without time zone
    PRIMARY KEY (id)
);