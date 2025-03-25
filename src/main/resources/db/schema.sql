CREATE TABLE kpac
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    title         VARCHAR(250)  NOT NULL,
    description   VARCHAR(2000) NOT NULL,
    creation_date DATE          NOT NULL
);

CREATE TABLE kpac_set
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(250) NOT NULL
);

CREATE TABLE kpac_set_relation
(
    kpac_id INT,
    set_id  INT,
    FOREIGN KEY (kpac_id) REFERENCES kpac (id) ON DELETE CASCADE,
    FOREIGN KEY (set_id) REFERENCES kpac_set (id) ON DELETE CASCADE,
    PRIMARY KEY (kpac_id, set_id)
);
