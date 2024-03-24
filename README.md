## Building the app

```bash
./mvnw clean package
```
will compile the app, run the unit tests and produce an uber-jar in the target folder.

## Running with maven and testcontainers

The following command will run a version of the application with a mongodb as a testcontainer.
It will have sample entities created in the db, so that one can easily experiment with the api.

```bash
./mvnw spring-boot:test-run
```

## Running from jar
You will need a running mongodb instance that you can spin up with docker-compose.

```bash
docker compose -f docker/docker-compose.yml up -d
```

Once mongodb is up, run the app itself. It will not have any data init.
See the previous section for info on how to spin up the app with some data in it.

```bash
java -jar target/task-tracker-0.0.1-SNAPSHOT.jar
```

You may stop the instance once you're finished.
```bash
docker compose -f docker/docker-compose.yml down
```

## Available commands

The REST API covers CRUD operations of the user and task entities.
For the task entity specifically, one can add an observer user by its id.

Get the list of all users and tasks

```bash
curl http://localhost:8080/api/v1/users
curl http://localhost:8080/api/v1/tasks
```

Replace the task and user id in the following request to add an observer

```bash
curl -d '{"id":"3a704760-692a-47ca-96e3-973ca459b759"}' -H "Content-Type: application/json" \
-X POST http://localhost:8080/api/v1/tasks/886a3e58-e5c6-4e0b-ad41-e5fb91b2fa1e/observers
```

## Configuration

The app reads its configuration from the `src/main/resources/application.yml` file.
You may override any of the parameters from the command line using the `-D` flag.
