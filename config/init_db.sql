create table if not exists resume
(
    uuid      char(36) primary key not null,
    full_name text                 not null
);

alter table resume
    owner to postgres;

create table if not exists contact
(
    id          serial,
    resume_uuid char(36) not null references resume (uuid) on delete cascade,
    type        text     not null,
    value       text     not null
);

alter table contact
    owner to postgres;

create unique index if not exists contact_uuid_type_index
    on contact (resume_uuid, type);

create table if not exists section
(
    id serial,
    resume_uuid char(36) not null references resume (uuid) on delete cascade,
    type        text,
    content     text     not null
);

alter table contact
    owner to postgres;

create unique index if not exists section_uuid_type_index
    on section (resume_uuid, type);