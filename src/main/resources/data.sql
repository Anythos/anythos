insert into users
VALUES (1, '54Dumbo', 'ACTIVE', 'mgoreck'),
       (2, 'Catalina27', 'ACTIVE', 'bsabala');
insert into roles
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN'),
       (3, 'ROLE_MANAGER');
insert into users_roles
values (1, 1),
       (2, 1),
       (2, 3);
