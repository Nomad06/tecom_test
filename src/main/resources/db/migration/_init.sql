--Run this script on the newly created local database 'test_db' with test_owner credentials
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE SCHEMA test AUTHORIZATION test_owner;
GRANT USAGE ON SCHEMA test TO test_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA test TO test_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA test to test_user;
