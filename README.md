# README #


Active Android Data Persistence and Migration

Whenever your schema changes you need to increment the database version number, either through Configuration or AA_DB_VERSION meta-data. If new classes are added, ActiveAndroid will automatically add them to the database. If you want to change something in an existing table however (e.g. add or delete a column), this is done using sql-scripts named <NewVersion>.sql, where NewVersion is the AA_DB_VERSION, in assets/migrations.

ActiveAndroid will execute a script if its filename is greater than the old database-version and smaller or equal to the new version.

Let’s assume you added a column color to the Items table. You now need to increase AA_DB_VERSION to 2 and provide a script 2.sql.

ALTER TABLE Items ADD COLUMN color INTEGER;