# davon-library-backend

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/davon-library-backend-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- ArC ([guide](https://quarkus.io/guides/cdi-reference)): Build time CDI dependency injection
- JDBC Driver - Microsoft SQL Server ([guide](https://quarkus.io/guides/datasource)): Connect to the Microsoft SQL Server database via JDBC
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Agroal - DB connection pool ([guide](https://quarkus.io/guides/datasource)): JDBC Datasources and connection pooling

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

## Project structure

- `com.devon.library.backend.model`: `Author`, `Book`, `User`, `Loan`
- `com.devon.library.backend.repository`: DAO interfaces and in-memory implementations
- `com.devon.library.backend.service`: `UserService`, `BookService`, `LoanService` business logic
- `com.devon.library.backend.resource`: `HealthResource` + sample `GreetingResource`

## Business logic implemented

- Checkout: decrements `availableCopies`, sets due date (14 days <=300 pages, 21 days >300)
- Return: marks `returnDate`, increments `availableCopies`
- Search: title/author substring search
- User management: create and lookup by email

## Tests

Unit tests cover services with in-memory repositories: `BookServiceTest`, `UserServiceTest`, `LoanServiceTest`.

## Cursor IDE features leveraged

- Multi-file generation and refactor tools
- Code actions for imports and Lombok annotations
- Test discovery and running via Maven integration
