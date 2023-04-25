# task-report-service

A simple task-reporting service to keep track of generic backend tasks metrics

# API documentation

Two resources are available tot the API consumer:

1. `/task-execution-reports` to operate on the `taskExecutionReports`
2. `/task-step-execution-reports` to operate on the `taskStepExecutionReports`

The swagger is available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

## /task-execution-reports

### `GET` getAll

The method returns all the `taskExecutionReports` present on the persistence layer

### `GET` getAllByStatus `/status/{status}`

The method returns all the `taskExecutionReports` with status equal to the path variable value `status` present on the
persistence layer

### `GET` getAllOrderByExecutionTime `/execution-time`

The method returns all the `taskExecutionReports` present on the persistence layer ordered by
ascending `executionTimeSeconds`

### `GET` getById `/{id}`

The method returns the `taskExecutionReport` present on the persistence layer with id equal to the path variable
value `id`

### `POST` create

The method allows the creation and the update of the `taskExecutionReport` following the request json model:

TaskExecutionReportRequest:

```
{
  "id": number,
  "taskId": non-null number,
  "taskStepExecutionReports": List<TaskStepExecutionReportRequest>,
  "startDateTime": non-null "yyyy-MM-ddThh:mm:ss",
  "endDateTime": "yyyy-MM-ddThh:mm:ss",
  "errorMessage": string
}
```

TaskStepExecutionReportRequest:

```
{
  "id": number,
  "taskExecutionId": non-null number,
  "stepName": non-null string,
  "status": status
  "startDateTime": non-null "yyyy-MM-ddThh:mm:ss",
  "endDateTime": "yyyy-MM-ddThh:mm:ss",
  "errorMessage": string
}
```

To create a new entity on the persistence layer the `id` field must be left unspecified. A request with a specific `id`
will overwrite the pre-existing one forcing an `UPDATE` of the resource. The field `status` must have a value between
the following allowed:

```
{
  "SUCCESS",
  "FAILURE",
  "RUNNING"
}
```

### `DELETE` delete `/{id}`

The method deletes the `taskExecutionReport` and all the associated `taskStepExecutionReports` present on the
persistence layer respectively with id and taskExecutionId equal to the path variable value `id`

## /task-step-execution-reports

### `GET` getAll

The method returns all the `taskStepExecutionReports` present on the persistence layer

### `GET` getById `/{id}`

The method returns the `taskStepExecutionReport` present on the persistence layer with id equal to the path variable
value `id`

### `POST` create

The method allows the creation and the update of the `taskStepExecutionReport` following the request json model:

TaskStepExecutionReportRequest:

```
{
  "id": number,
  "taskExecutionId": non-null number,
  "stepName": non-null string,
  "status": status
  "startDateTime": non-null "yyyy-MM-ddThh:mm:ss",
  "endDateTime": "yyyy-MM-ddThh:mm:ss",
  "errorMessage": string
}
```

To create a new entity on the persistence layer the `id` field must be left unspecified. A request with a specific `id`
will overwrite the pre-existing one forcing an `UPDATE` of the resource. The field `status` must have a value between
the following allowed:

```
{
  "SUCCESS",
  "FAILURE",
  "RUNNING"
}
```

### `DELETE` delete `/{id}`

The method deletes the `taskStepExecutionReport` present on the persistence layer respectively with taskExecutionId
equal to the path variable value `id`

### `GET` getByTaskExecutionId `/task-execution-id/{task_execution_id}`

The method returns the `taskStepExecutionReports` present on the persistence layer with taskExecutionId equal to the
path variable value `task_execution_id`. It is possible to sort the resulting list accordingly to the following request
params:

1. `direction` with possible values `ASC,DESC`
2. `columnName` with possible
   values `id,taskExecutionId,stepName,status,startDateTime,endDateTime,executionTimeSeconds,errorMessage,insertDate,updateDate`

## Responses

### Positive response

On all methods positive responses always return HttpStatus `200 OK`. Some methods output a response body using the following json
response models.

TaskExecutionReportResponse:

```
{
  "id": number,
  "taskId": number,
  "taskStepExecutionReports": List<TaskStepExecutionReportRequest>,
  "startDateTime": "yyyy-MM-ddThh:mm:ss",
  "endDateTime": "yyyy-MM-ddThh:mm:ss",
  "executionTimeSeconds": number,
  "errorMessage": string,
  "status": status
}
```

TaskStepExecutionReportResponse:

```
{
  "id": number,
  "taskExecutionId": number,
  "stepName": string,
  "status": status
  "startDateTime": "yyyy-MM-ddThh:mm:ss",
  "endDateTime": "yyyy-MM-ddThh:mm:ss",
  "executionTimeSeconds": number,
  "errorMessage": string
}
```

### Validation failed response

On some methods a thrown validation failed exception returns HttpStatus `400 BAD REQUEST`. In this case the produced
response body uses the following json response models.

```
{
    "message": "Field error",
    "code": "field_error",
    "detailErrorList": [
        {
            "field": "{field_name}",
            "message": "{field_name} should not be null"
        }
    ],
    "dateTime": "yyyy-MM-dd hh:mm:ss+hh:mm"
}
```

### Not found response

On search methods the absence of the searched resource will result in a HttpStatus `404 NOT FOUND`.
The body will be a simple string following one of the two patterns:
1. "TaskExecutionReport for id [`id`] not found"
2. "TaskStepExecutionReport for id [`id`] not found"