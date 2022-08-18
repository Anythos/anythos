insert into roles
VALUES (1,'USER'),
       (2,'ADMIN'),
       (3,'MANAGER');
insert into users
VALUES (1,'54Dumbo', 'ACTIVE', 'mgoreck'),
       (2,'Catalina27', 'ACTIVE', 'bsabala');
insert into user_role values (1,1), (2,1), (2,3);
