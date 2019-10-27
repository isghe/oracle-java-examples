# Oracle Java Examples

# Install

Download `ojdbc7.jar` from [jdbc-drivers-12c-downloads](https://www.oracle.com/database/technologies/jdbc-drivers-12c-downloads.html) and copy it in `lib/` folder.

To run the examples `WrongWay`, `CorrectWay`, `GoodWay`, `BetterWay`, run the script in `sql/create-table_test_insert.sql`; it will create the table `TEST_INSERT` in your DB.

# Configure

Copy `Configure/Credentials-template.java` to `Configure/Credentials.java` and modify the credentials you need (url, user and password).

# Compile

`$ cd examples/HelloWorld`

`$ . ../../script/compile.sh`

# Run
`. ../../script/run.sh`
