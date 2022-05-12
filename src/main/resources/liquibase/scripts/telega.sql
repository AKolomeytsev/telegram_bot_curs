-- liquibase formatted sql
-- changeset TelegaBase:1
CREATE TABLE notification_task
(
    id integer PRIMARY KEY
    , message character varying(254)
    , chat_id bigint NOT NULL
    , date_send timestamp without time zone
);