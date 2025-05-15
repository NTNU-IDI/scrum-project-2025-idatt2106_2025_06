# Backend

1. Navigate to the backend directory

```sh
cd backend
```

2. Create a database instance with Docker
    * Make  
      Sets up the database and removes any existing instance

       ```sh
       make fresh
       ```

    * Docker compose

      Sets up the database

       ```sh 
       docker compose up -d mysql
       ```

      Tears down the database

       ```sh
       docker compose down
       ```


3. Build the project with Maven. (The -DskipTests flag skips running the tests. This is due to a container error
   that occurs when multiple test classes are run. If you still want to run the tests, see step 5.)

   ```sh 
   mvn clean install -DskipTests
   ```


4. Run the backend application with the following command.

   ```sh
   mvn spring-boot:run
   ```

5. Run backend tests

   Due to the previously mentioned container error, test classes must be run individually. You can run individual
   test classes using Maven's -Dtest flag like this:

   ```sh
   mvn test -Dtest=NavnPåTestKlasse
   ```

   Example with one of the project's test classes:

   ```sh 
   mvn test -Dtest=EventServiceTest
   ```