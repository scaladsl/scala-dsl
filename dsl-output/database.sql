create table article(
    id  not null,
    title TEXT not null,
    author TEXT not null,
    content TEXT not null,
    submitted_at TEXT not null,
    primary key ( id )
);
create table comment(
    id  not null,
    article_id  not null,
    author TEXT not null,
    content TEXT not null,
    submitted_at TEXT not null,
    primary key ( id )
    FOREIGN KEY(article_id) REFERENCES article(id)
);
