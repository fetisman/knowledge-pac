INSERT INTO kpac (title, description, creation_date)
VALUES ('K-PAC 1', 'Description of K-PAC 1', '2023-10-01'),
       ('K-PAC 2', 'Description of K-PAC 2', '2023-10-02'),
       ('K-PAC 3', 'Description of K-PAC 3', '2024-10-01'),
       ('K-PAC 4', 'Description of K-PAC 4', '2024-10-02');

INSERT INTO kpac_set (title)
VALUES ('Set 1'),
       ('Set 2');

INSERT INTO kpac_set_relation (kpac_id, set_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 2);
