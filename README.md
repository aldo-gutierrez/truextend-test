# truextend-test
Truextend Java Test

**Build Requirements**

Gradle 3.5.1 - 5.6

Java 8



**Build**

`gradle clean build`

the deploy the war file generated and open

http://localhost:8080/truextend/rest/student
http://localhost:8080/truextend/rest/class

`gradle resolve generateSwaggerDocumentation`

 generates
 
 swagger/TruextendTestAPI.html rest documentation
 swagger/TruextendTestAPI.json swagger json file that can be used on https://editor.swagger.io/ 

