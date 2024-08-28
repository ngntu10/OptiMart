# OptiMart

## Requirements

For building and running the application you need:

- [Node 20 & Npm 10](https://nodejs.org/en/download)
- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java21)
- [Maven 3](https://maven.apache.org)

[//]: # ()
[//]: # (## Run the application locally)

[//]: # ()
[//]: # (Install the dependencies:)

[//]: # ()
[//]: # (``` bash)

[//]: # (npm install)

[//]: # (npm run prepare)

[//]: # (```)

[//]: # ()
[//]: # (Make sure to connect to your databse by defining the env file `env.properties` located in `/src/main/resources/`. For example:)

[//]: # ()
[//]: # (``` properties)

[//]: # (# /src/main/resources/env.properties)

[//]: # (DB_DDL_AUTO=update)

[//]: # (DB_URL=jdbc:postgresql://localhost:5432/postgres)

[//]: # (DB_USERNAME=your_username)

[//]: # (DB_PASSWORD=your_password)

[//]: # (```)

[//]: # ()
[//]: # (Run the server:)

[//]: # ()
[//]: # (``` bash)

[//]: # (mvn spring-boot:run)

[//]: # (```)

[//]: # ()
[//]: # (Use a browser to navigate to [http://localhost:8080/swagger-ui/index.html]&#40;http://localhost:8080/api/v1/swagger-ui/index.html&#41;.)

[//]: # ()
[//]: # (## Run tests)

[//]: # ()
[//]: # (``` bash)

[//]: # (mvn test)

[//]: # (```)

[//]: # ()
[//]: # (## Other commands)

[//]: # ()
[//]: # (### Format code)

[//]: # ()
[//]: # (``` bash)

[//]: # (mvn fmt:format)

[//]: # (```)

[//]: # (## How to name a branch?)

[//]: # ()
[//]: # (Branch name pattern:)

[//]: # ()
[//]: # ()
[//]: # (```text)

[//]: # (type/description-in-kebab-case)

[//]: # ()
[//]: # (type/issue-#{issue_number})

[//]: # ()
[//]: # (```)

[//]: # ()
[//]: # (Examples:)

[//]: # ()
[//]: # (```text)

[//]: # (feature/issue-#99)

[//]: # (```)

[//]: # ()
[//]: # (```text)

[//]: # (hotfix/quick-fix-for-an-emergency)

[//]: # (```)

[//]: # ()
[//]: # (Common types according to [simplified convention for naming branches]&#40;https://dev.to/varbsan/a-simplified-convention-for-naming-branches-and-commits-in-git-il4&#41;)

[//]: # (- feature: adding, refactoring or removing a feature)

[//]: # (- bugfix: fixing a bug)

[//]: # (- hotfix: changing code with a temporary solution and/or without following the usual process &#40;usually because of an emergency&#41;)

[//]: # (- test: experimenting outside of an issue/ticket)

[//]: # ()

## How to name a commit message?

**Commitlint** checks if your commit messages meet the [conventional commit format](https://conventionalcommits.org).

Commit message pattern:

```sh
type(scope?): subject  #scope is optional; multiple scopes are supported (current delimiter options: "/", "\" and ",")
```

Examples:

```text
chore: run tests on travis ci
```

```text
fix(server): send cors headers
```

```text
feat(blog): add comment section
```

Common types according to [commitlint-config-conventional (based on the Angular convention)](https://github.com/conventional-changelog/commitlint/tree/master/@commitlint/config-conventional#type-enum) can be:

- build
- chore
- ci
- docs
- feat
- fix
- perf
- refactor
- revert
- style
- test

[//]: # (## References)

[//]: # ()
[//]: # (Read these references if needed:)

[//]: # ()
[//]: # (- [Open api swagger]&#40;https://springdoc.org/&#41;)

[//]: # (- [Lombok]&#40;https://codippa.com/lombok/&#41;)

[//]: # (- [JPA/Hibernate entity relationships]&#40;https://www.baeldung.com/jpa-hibernate-associations&#41;)

[//]: # (- [Hibernate type mappings]&#40;https://vladmihalcea.com/a-beginners-guide-to-hibernate-types/&#41;)