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
1. <b>How do you handle versions of external APIs - are some versions better suited to solve our problem?</b><br/>
I used version 2.0 for bitbucket and 3.0 for github. Bitbucket's v2.0 is the only available API I could find, which is
set through the URI path. Attempting to downgrade to 1.0 provides the response that the API is no longer supported. 
Unfortunately, this API did cause some performance problems with the deep linked resources, discussed in more depth in
the `Performance` section below. GitHub's API is defaulted to version 3.0, but is able to be modified by passing in the
appropriate `Accept` header. In my opinion, GitHub's API was easier to work with, so I did not investigate any other
previous versions. In general, I try to use the most recent API versions, as I believe that is what the API designers
intended.

2. <b>How do you/would you handle a failed network call to Github/Bitbucket?</b><br/>
This is currently handled by the GlobalExceptionHandler class. If one of the underlying calls to github or bitbucket
fail, the exception is bubbled up to the controller, which is then translated to a response object by the
GlobalExceptionHandler. A human readable message and an appropriate HTTP status code are returned to the user.

3. <b>Which REST verbs and URI structure makes the most sense?</b><br/>
GET makes the most sense since the purpose of this application is to retrieve a repository overview for the given 
organization name. `/repository` is the controller path, since this controller could be expanded in the future to
contain other endpoints also related to a repository, such as creating or updating. `/overview/{name}` is the specific 
path for retrieving repository summary for the given name. I chose to use a path variable because this is a GET 
endpoint and the word 'overview' since the response object is a summation of information across an organization's
repositories.

4. <b>How efficient is your code?</b><br/>
Explained in detail in the `Performance` section below, but I believe that this application is nearly as performant as
possible given the API's being consumed. One future improvement could be to parallelize the calls to github and
bitbucket, but since this is not the bottleneck, I have chosen to leave then synchronous.

## What I'd like to improve on...
1. <b>Performance</b><br/>
Unfortunately the bitbucket api requires traversing multiple deep links to retrieve complete information for a 
repository such as fork and watcher count. As the number of repositories owned by an organization grows, the count of
requests required to build the overview grows at twice the rate. For example, 1 request can be used to retrieve a count
of 10 repositories, but then 20 additional requests must be made to calculate the total fork and watcher counts for
those 10 repositories. This, in combination with bitbucket's rate-limited API, could prove troublesome for either real
world use of this application or organizations with many repositories.
<br/><br/>
I have parallelized the deep linked calls to bitbucket to improve end user performance, but this unfortunately means 
that the rate limit is also approached more quickly. Setting the thread pool size to 3 seems to be the best but may
need to be tweaked if the repository count for an organization is larger than the provided examples.

2. <b>Exception handling</b><br/>
In the current implementation, if a downstream exception is encountered, no data will be returned to the user. This
means that if any calls to either github or bitbucket fail, the exception is bubbled up to the end user, asking them
to try again. If this were a real world application, it might make more sense to respond with partial information or
with a more specific error message to indicate which downstream service failed.