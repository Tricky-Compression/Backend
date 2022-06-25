CREATE TABLE IF NOT EXISTS chunks (
    hash TEXT,
    path TEXT
);


INSERT INTO chunks(hash, path) VALUES ('someHash', '/file_storage/some_file');
INSERT INTO chunks(hash, path) VALUES ('someOtherHash', '/file_storage/some_other_file');

CREATE TABLE IF NOT EXISTS files (
    path TEXT
);

INSERT INTO files(path) VALUES ('some_file');
INSERT INTO files(path) VALUES ('some_other_file');
