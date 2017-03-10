create table article(
  --  Article identity
  id  not null,
  --  Article title
  title TEXT not null,
  --  Article author
  author TEXT not null,
  --  Article content
  content TEXT not null,
  --  Article submission date
  submitted_at TEXT not null,
  primary key ( id )
);
create table comment(
  --  Comment identity
  id  not null,
  --  Comment article identity
  article_id  not null,
  --  Comment author
  author TEXT not null,
  --  Comment content
  content TEXT not null,
  --  Comment submission date
  submitted_at TEXT not null,
  foreign key ( article_id ) references article( id ),
  primary key ( id )
);
