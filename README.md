[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/9pMJKrGa)

# Backend

1. Navigate to the backend directory

```sh
cd backend
```

2. Create a database instance with Docker (2 alternatives)
   To set up the database, you need docker installed and running. You can use either of the following methods:
    * Alternative 1: Use make  
      Sets up the database and removes any existing instance

       ```sh
       make fresh
       ```

    * Alternative 2: Use Docker compose manually

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

# Frontend

### Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and
disable Vetur).

### Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

### Project Setup

```sh
pnpm install
```

### Compile and Hot-Reload for Development

```sh
pnpm dev
```

### Compile and Minify for Production

```sh
pnpm build
```

### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
pnpm test:unit
```

### Run End-to-End Tests with [Cypress](https://www.cypress.io/)

```sh
pnpm test:e2e:dev
```

This runs the end-to-end tests against the Vite development server.
It is much faster than the production build.

But it's still recommended to test the production build with `test:e2e` before deploying (e.g. in CI environments):

```sh
pnpm build
pnpm test:e2e
```

### Lint with [ESLint](https://eslint.org/)

```sh
pnpm lint
```
