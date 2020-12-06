# Coding Challenge App

A Java Spring Boot app to retrieve general repository overview information from both github.com and bitbucket.com

## Requirements:

1. Java 11 or later
2. Maven 3.6 or later

## Running the code

### Spin up the service

```
mvnw spring-boot:run
```

### Making Requests
Health Check:
```
curl -i "http://127.0.0.1:8080/health"
```
Repository overview:
```
curl -i "http://127.0.0.1:8080/repository/overview/{name}"
```

## Considerations
1. How do you/would you handle a failed network call to Github/Bitbucket?<br/>
This is currently handled by the GlobalExceptionHandler class. If one of the underlying calls to github or bitbucket
fail, the exception is bubbled up to the controller, which is then translated to a response object by the
GlobalExceptionHandler. A human readable message and an appropriate HTTP status code are returned to the user.

2. Which REST verbs and URI structure makes the most sense?<br/>
GET makes the most sense since the purpose of this application is to retrieve a repository overview for the given 
organization name. `/repository` is the controller path, since this controller could be expanded in the future to
contain other endpoints also related to a repository, such as creating or updating. `/overview/{name}` is the specific 
path for retrieving repository summary for the given name. I chose to use a path variable because this is a GET 
endpoint and the word 'overview' since the response object is a summation of information across an organization's
repositories.


## What I'd like to improve on...
1. Performance<br/>
Unfortunately the bitbucket api requires traversing multiple deep links to retrieve complete information for a 
repository such as fork and watcher count. As the number of repositories owned by an organization grows, the count of
requests required to build the overview grows at twice the rate. For example, 1 request can be used to retrieve a count
of 10 repositories, but then 20 additional requests must be made to calculate the total fork and watcher counts for
those 10 repositories. This, in combination with bitbucket's rate-limited API, could prove troublesome for either real
world use of this application or organizations with many repositories.

2. Exception handling<br/>
In the current implementation, if a downstream exception is encountered, no data will be returned to the user. This
means that if any calls to either github or bitbucket fail, the exception is bubbled up to the end user, asking them
to try again. If this were a real world application, it might make more sense to respond with partial information or
with a more specific error message to indicate which downstream service failed.