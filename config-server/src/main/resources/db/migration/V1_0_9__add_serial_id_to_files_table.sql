ALTER TABLE files
DROP CONSTRAINT pk_file;

ALTER TABLE files
ADD COLUMN id SERIAL PRIMARY KEY;