
INSERT INTO employees (`id`,`business_mobile`,`contract_type`,`department`,`email`,`first_name`,`fte`,`grade`,`hire_date`,`last_name`,`profession`,`salary`,`system_employee_id`)
VALUES (1,"123-456-654","EMPLOYMENT",NULL,"mgoreckl@anythos.com","marcin", NULL,NULL,NULL,NULL,NULL,52345,NULL),
        (2,"456-987-987","EMPLOYMENT",NULL,"kamil@anythos.com","kamil",NULL,NULL,NULL,NULL,NULL,52345,NULL),
        (3,"456-654-654","EMPLOYMENT",NULL,"hanna@anythos.com","Hanna",NULL,NULL,NULL,NULL,NULL,52345,NULL);
insert into users
VALUES (1, 'password1', 'mgoreck', 1),
       (1, 'Catalina27', 'kamil', 2 ),
       (1, 'Catalina27', 'hanna', 3 );
insert into roles
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'MANAGER');
insert into users_roles
values (1, 1),
       (2, 1),
       (2, 3);
