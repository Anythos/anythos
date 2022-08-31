insert into users
VALUES (1, 1, 'password1', 'mgoreck', null ),
       (2, 1, 'Catalina27', 'kamil', null ),
       (3, 1, 'Catalina27', 'hanna', null );
insert into roles
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'MANAGER');
insert into users_roles
values (1, 1),
       (2, 1),
       (2, 3);
