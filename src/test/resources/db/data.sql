INSERT INTO role (id, name)
VALUES (1, 'USER');

INSERT INTO user
    (id, contact_number, email, name, password)
VALUES (2, 123123123, 'Tester@gmail.com', 'Tester', 'Password1');

INSERT INTO user
    (id, contact_number, email, name, password)
VALUES (3, 321321321, 'Tester2@gmail.com', 'Tester', 'Password1');

INSERT INTO users_roles
    (user_id, role_id)
VALUES (2, 1);

INSERT INTO users_roles
    (user_id, role_id)
VALUES (3, 1);