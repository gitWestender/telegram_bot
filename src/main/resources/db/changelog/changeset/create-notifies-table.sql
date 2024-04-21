--liquibase formatted sql

--changeset SimakovOleg:1

CREATE TABLE IF NOT EXISTS public.notifications (
    id SERIAL PRIMARY KEY,
    chat_id INTEGER NOT NULL,
    notify_msg VARCHAR(512) NOT NULL,
    date_msg TIMESTAMP
)