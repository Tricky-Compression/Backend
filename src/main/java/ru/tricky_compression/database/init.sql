CREATE TABLE IF NOT EXISTS chunks (
    hash TEXT,
    path TEXT
);


INSERT INTO chunks(hash, path) VALUES ('someHash', '/file_storage/some_file');
INSERT INTO chunks(hash, path) VALUES ('someOtherHash', '/file_storage/some_other_file');