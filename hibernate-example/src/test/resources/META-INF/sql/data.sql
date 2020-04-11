INSERT INTO person(id, name, email) VALUES (1, 'hyunju', 'hyunjy@hanbat.ac.kr')
INSERT INTO person(id, name, email) VALUES (2, 'hyeonsig', 'hyeonsig@hanbat.ac.kr')
INSERT INTO person(id, name, email) VALUES (3, 'deokgi', 'deokgi@hanbat.ac.kr')

INSERT INTO phone(id, number, type, person_id) VALUES (1, '010-1111-1111', 'MOBILE', 1);
INSERT INTO phone(id, number, type, person_id) VALUES (2, '043-111-1111', 'HOME', 1);
INSERT INTO phone(id, number, type, person_id) VALUES (3, '043-222-2222', 'MOBILE', 1);

INSERT INTO phone(id, number, type, person_id) VALUES (4, '010-3333-3333', 'MOBILE', 2);
INSERT INTO phone(id, number, type, person_id) VALUES (5, '043-333-3333', 'HOME', 2);

INSERT INTO phone(id, number, type, person_id) VALUES (6, '010-444-4444', 'WORK', 3);