-- liquibase formatted sql

-- changeset zhevalentin:1

CREATE TABLE Person (
    id SERIAL,
    email TEXT
)