--Run this script on the local database 'postgres' as 'postgres'/'postgres'
--If not exists
CREATE ROLE test_owner LOGIN PASSWORD 'sadx12dsadascxz442w' CREATEDB CREATEROLE;
--If not exists
CREATE ROLE test_user LOGIN PASSWORD 'czxfdqwd213dsa';
CREATE DATABASE test_db WITH ENCODING = 'UTF8';
ALTER DATABASE test_db OWNER TO test_owner;
GRANT CONNECT ON DATABASE test_db TO test_user;
