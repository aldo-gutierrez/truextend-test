# truextend-test
Truextend Java Test

**Build Requirements**

Gradle 4.10.3 - 5.6.2
  (gradle 6 doesn't support findbugs)
  (gradle 3.5.1  doesn't support junit5 integration)

Java 8

Tomcat 8.5



**Build**

`gradle clean build`

the deploy the war file generated and open

http://localhost:8080/truextend/rest/student
http://localhost:8080/truextend/rest/class

`gradle resolve generateSwaggerDocumentation`

 generates
 
 swagger/TruextendTestAPI.html rest generated documentation
 swagger/TruextendTestAPI.json swagger generated json file that can be used on https://editor.swagger.io/ 

to run unit tests run

`gradle build`

to run api integration test, start tomcat and the run

`gradle build -DincludeTags='integration-test' -DexcludeTags='unit-test' -i`


**Endpoints Examples**

**Insert a Student**

POST
http://localhost:8080/truextend/rest/student

`
{
    "studentId": "7777",
    "lastName" : "Diaz",
    "firstName" : "Batman"
}
`

**Update a Student**

PUT
http://localhost:8080/truextend/rest/student/1

`
{
    "studentId": "7777",
    "lastName" : "Diaz",
    "firstName" : "Batman"
}
`

**List Students**

GET
http://localhost:8080/truextend/rest/student/
example response

`
{
  "total": 1
  "result": [
    {
    "studentId": "7777",
    "lastName" : "Diaz",
    "firstName" : "Batman"
    }
   ] 
}
`

**List Students with order and critera**

GET
http://localhost:8080/truextend/rest/student/?order=firstName:desc,lastName:asc&criteria=firstName:eq:Perez,lastName:like:%25batman%25

**List Students with pagination**
GET
http://localhost:8080/truextend/rest/student/?pageSize=1&pageNumber=1

