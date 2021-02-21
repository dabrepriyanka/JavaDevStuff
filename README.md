# JavaDevStuff
Send Email Service is a RESTful service that sends emails to third-party email service providers which deliver emails to customers. 
There are 2 service providers that it uses - Spendgrid(synchronous) and Snaiilgun(asynchronous).

Technology Stack used for building this service -
Language - Java.
Web Framework - Spring Boot.
Build tool - Maven.

We can send 1 email per request. Currently the service is configured to send email using SpendGrid sender. 
This can be changed to use Snailgun email sender by simply annotating the SnailGunSender class as @Primary.

Example Request:-

```
POST /send_email
Headers:
Content-Type: application/json
Payload:-
{
"to": "susan@abcpreschool.org",
"to_name": "Miss Susan",
"from": "noreply@mybrightwheel.com",
"from_name": "brightwheel",
"subject": "Your Weekly Report",
"body": "<h1>Weekly Report</h1><p>You saved 10 hours this
week!</p>"
}
```

Validation :- 
1. All the above fields are mandatory. 
2. Email needs to be in the correct format.

The above request will first undergo validation and then html in the body will be converted to plain text. 
After this, depending on the email sender configured, it will be forwarded to the correct sender.

Spendgrid sender - This service calls spendgrid sender in a synchronous fashion using Http libraries. It attempts to send the request using POST call and returns the result back to the client.
Snailgun sender - If configured, snailgun sender is called in an asynchronous fashion using a POST call. 
It receives an ID back from the service and returns it back to the client. 

Future work :-
1.  Send email with attachments feature can be provided.
2.  Currently to switch from Spendgrid to Snailgun, we can make a config change(annotate the required class with @Primary). For future, we can configure so that if one service fails, 
it automatically fails over to the different service.
3.  We can support batch requests and throttling can be implemented.

Project Completion status :- 
I was able to get the code done and hit the SpendGrid and SnailGun endpoints using the API_KEYS provided.  

Steps to download and run the project :-
1. How to install basic Spring boot in Eclipse - https://dzone.com/articles/creating-a-spring-boot-project-with-eclipse-and-ma
2. Download the code from https://github.com/dabrepriyanka/JavaDevStuff/tree/master/sendemail.
3. Build and Run using Maven.
4. Start the server and use Postman or CURL to send a POST request to localhost:8182/email





