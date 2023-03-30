# Task Manager Service
This is a Java service that offers a REST API for a task manager. 
The API allows you to add tasks and retrieve a list of all tasks.
### Requirements
To build and run this service, you will need:

* Java 11 or higher
* Gradle

### Building and Running the Service
To build and run this service:

1. Download the zip file from the email
2. Change to the project directory:
```bash 
cd taskmanager
```
3. Build the project
```
gradle build
```
4. Run the service:
```
java -jar build/libs/taskmanager-0.0.1-SNAPSHOT.jar
```

The service should now be running on http://localhost:8080

### API Endpoints
`POST /api/tasks`

Adds a new task to the task list.

#### Request Body:
```json
{
  "title": "Task1",
  "description": "This is task 1",
  "finished": false
}
```

#### Response Body:
```
Task added successfully!
```

`GET /api/tasks`

Retrieves a list of all tasks.

#### Response Body:
```json
[
  {
    "title": "Task1",
    "description": "This is task1",
    "finished": false
  }
]
```

#### Health monitor actuator
`/actuator/health`
