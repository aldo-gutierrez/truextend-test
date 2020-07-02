package com.truextend.service;

import com.truextend.automation.http.HTTPResponse;
import com.truextend.automation.http.HTTPTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.Path;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class APITest {
    String testUrl = "http://localhost:8080/truextend/rest";
    HTTPTester httpTester;

    @BeforeEach
    public void setUp() throws Exception {
        httpTester = new HTTPTester(new URL(testUrl));
    }

    @Test
    public void testStudentCrud() throws IOException {
        HTTPResponse httpResponse;

        httpTester = new HTTPTester(new URL(testUrl));
        httpTester.addHeader("Accept", "application/json");
        httpTester.addHeader("Content-Type", "application/json");

        Map<String, Object> inputJson;
        Map<String, Object> jsonResponse;
        inputJson = new HashMap<>();

        //validate required studentId
        httpResponse = httpTester.postJson("/student", inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("studentId is required", jsonResponse.get("error"));

        //validate required firstName
        inputJson.put("studentId", "si1");
        httpResponse = httpTester.postJson("/student", inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("firstName is required", jsonResponse.get("error"));

        //validate required lastName
        inputJson.put("firstName", "fn1");
        httpResponse = httpTester.postJson("/student", inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("lastName is required", jsonResponse.get("error"));

        //validate save of parameters
        inputJson.put("lastName", "ln1");
        httpResponse = httpTester.postJson("/student", inputJson);
        assertEquals( 201, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("si1", jsonResponse.get("studentId"));
        assertEquals("fn1", jsonResponse.get("firstName"));
        assertEquals("ln1", jsonResponse.get("lastName"));
        Number id = (Number) jsonResponse.get("id");
        assertNotNull(id, "id is null");

        //read saved
        httpResponse = httpTester.get("/student/"+id);
        assertEquals( 200, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("si1", jsonResponse.get("studentId"));
        assertEquals("fn1", jsonResponse.get("firstName"));
        assertEquals("ln1", jsonResponse.get("lastName"));
        assertEquals(id, jsonResponse.get("id"));

        //change saved
        inputJson.clear();

        //validate required studentId
        inputJson.put("studentId", "");
        httpResponse = httpTester.putJson("/student/"+id, inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("studentId is required", jsonResponse.get("error"));

        //validate required firstName
        inputJson.put("studentId", "si1b");
        inputJson.put("firstName", "");
        httpResponse = httpTester.putJson("/student/"+id, inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("firstName is required", jsonResponse.get("error"));

        //validate required lastName
        inputJson.put("firstName", "fn1b");
        inputJson.put("lastName", "");
        httpResponse = httpTester.putJson("/student/"+id, inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("lastName is required", jsonResponse.get("error"));

        //validate save of parameters
        inputJson.put("lastName", "ln1b");
        httpResponse = httpTester.putJson("/student/"+id, inputJson);
        assertEquals( 200, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("si1b", jsonResponse.get("studentId"));
        assertEquals("fn1b", jsonResponse.get("firstName"));
        assertEquals("ln1b", jsonResponse.get("lastName"));

        //read saved
        httpResponse = httpTester.get("/student/"+id);
        assertEquals( 200, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("si1b", jsonResponse.get("studentId"));
        assertEquals("fn1b", jsonResponse.get("firstName"));
        assertEquals("ln1b", jsonResponse.get("lastName"));

        //read in list with criteria and pagination
        httpResponse = httpTester.get("/student?criteria=studentId:eq:si1b,lastName:like:ln1b&pageNumber=1&pageSize=1");
        assertEquals( 200, httpResponse.getCode());
        Map<String, Object> jsonResponseMap = httpResponse.getJsonBodyAsMap();
        assertEquals(1, jsonResponseMap.get("total"));
        assertEquals(1, ((List)jsonResponseMap.get("results")).size());
        jsonResponse = (Map) ((List)jsonResponseMap.get("results")).get(0);
        assertEquals("si1b", jsonResponse.get("studentId"));
        assertEquals("fn1b", jsonResponse.get("firstName"));
        assertEquals("ln1b", jsonResponse.get("lastName"));


        //delete
        httpResponse = httpTester.delete("/student/"+id);
        assertEquals( 204, httpResponse.getCode());
        httpResponse = httpTester.get("/student/"+id);
        assertEquals( 404, httpResponse.getCode());
    }

    @Test
    public void testClassCrud() throws IOException {
        HTTPResponse httpResponse;

        httpTester = new HTTPTester(new URL(testUrl));
        httpTester.addHeader("Accept", "application/json");
        httpTester.addHeader("Content-Type", "application/json");

        Map<String, Object> inputJson;
        Map<String, Object> jsonResponse;
        inputJson = new HashMap<>();

        //validate required code
        httpResponse = httpTester.postJson("/class", inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("code is required", jsonResponse.get("error"));

        //validate required title
        inputJson.put("code", "c1");
        httpResponse = httpTester.postJson("/class", inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("title is required", jsonResponse.get("error"));

        //validate save of parameters
        inputJson.put("title", "t1");
        inputJson.put("description", "d1");
        httpResponse = httpTester.postJson("/class", inputJson);
        assertEquals( 201, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("c1", jsonResponse.get("code"));
        assertEquals("t1", jsonResponse.get("title"));
        assertEquals("d1", jsonResponse.get("description"));
        Number id = (Number) jsonResponse.get("id");
        assertNotNull(id, "id is null");

        //read saved
        httpResponse = httpTester.get("/class/"+id);
        assertEquals( 200, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("c1", jsonResponse.get("code"));
        assertEquals("t1", jsonResponse.get("title"));
        assertEquals("d1", jsonResponse.get("description"));
        assertEquals(id, jsonResponse.get("id"));

        //change saved
        inputJson.clear();

        //validate required code
        inputJson.put("code", "");
        httpResponse = httpTester.putJson("/class/"+id, inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("code is required", jsonResponse.get("error"));

        //validate required title
        inputJson.put("code", "c1b");
        inputJson.put("title", "");
        httpResponse = httpTester.putJson("/class/"+id, inputJson);
        assertEquals( 400, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("title is required", jsonResponse.get("error"));

        //validate save of parameters
        inputJson.put("title", "t1b");
        inputJson.put("description", "d1b");
        httpResponse = httpTester.putJson("/class/"+id, inputJson);
        assertEquals( 200, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("c1b", jsonResponse.get("code"));
        assertEquals("t1b", jsonResponse.get("title"));
        assertEquals("d1b", jsonResponse.get("description"));

        //read saved
        httpResponse = httpTester.get("/class/"+id);
        assertEquals( 200, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        assertEquals("c1b", jsonResponse.get("code"));
        assertEquals("t1b", jsonResponse.get("title"));
        assertEquals("d1b", jsonResponse.get("description"));

        //read in list
        httpResponse = httpTester.get("/class");
        assertEquals( 200, httpResponse.getCode());
        List<Map<String, Object>> jsonResponseList = httpResponse.getJsonBodyAsList();
        boolean found = false;
        for (Map<String, Object> class0 : jsonResponseList) {
            if (id.equals(class0.get("id"))) {
                found = true;
                assertEquals("c1b", jsonResponse.get("code"));
                assertEquals("t1b", jsonResponse.get("title"));
                assertEquals("d1b", jsonResponse.get("description"));
            }
        }
        assertTrue(found, "class not found in result list");

        //delete
        httpResponse = httpTester.delete("/class/"+id);
        assertEquals( 204, httpResponse.getCode());
        httpResponse = httpTester.get("/class/"+id);
        assertEquals( 404, httpResponse.getCode());
    }

    @Test
    public void testStudentClassAssociation() throws IOException {
        HTTPResponse httpResponse;

        httpTester = new HTTPTester(new URL(testUrl));
        httpTester.addHeader("Accept", "application/json");
        httpTester.addHeader("Content-Type", "application/json");

        Map<String, Object> inputJson;
        Map<String, Object> jsonResponse;
        inputJson = new HashMap<>();

        //insert a student
        inputJson.put("studentId", "si1");
        inputJson.put("firstName", "fn1");
        inputJson.put("lastName", "ln1");
        httpResponse = httpTester.postJson("/student", inputJson);
        assertEquals( 201, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        Number sid = (Number) jsonResponse.get("id");

        //insert a class
        inputJson.clear();
        inputJson.put("code", "c1");
        inputJson.put("title", "t1");
        inputJson.put("description", "d1");
        httpResponse = httpTester.postJson("/class", inputJson);
        assertEquals( 201, httpResponse.getCode());
        jsonResponse = httpResponse.getJsonBodyAsMap();
        Number cid = (Number) jsonResponse.get("id");

        List jsonResponseList;

        //verify that there is no relationship
        httpResponse = httpTester.get("/student/"+sid+"/class");
        assertEquals( 200, httpResponse.getCode());
        jsonResponseList = httpResponse.getJsonBodyAsList();
        assertEquals(0, jsonResponseList.size());

        httpResponse = httpTester.get("/class/"+cid+"/student");
        assertEquals( 200, httpResponse.getCode());
        jsonResponseList = httpResponse.getJsonBodyAsList();
        assertEquals(0, jsonResponseList.size());

        //associate
        httpResponse = httpTester.put("/student/"+sid+"/class/"+cid, null);
        assertEquals( 201, httpResponse.getCode());

        //verify the relationship is added
        httpResponse = httpTester.get("/student/"+sid+"/class");
        assertEquals( 200, httpResponse.getCode());
        jsonResponseList = httpResponse.getJsonBodyAsList();
        assertEquals(1, jsonResponseList.size());
        jsonResponse = (Map) jsonResponseList.get(0);
        assertEquals(cid, jsonResponse.get("id"));
        assertEquals("c1", jsonResponse.get("code"));
        assertEquals("t1", jsonResponse.get("title"));
        assertEquals("d1", jsonResponse.get("description"));

        httpResponse = httpTester.get("/class/"+cid+"/student");
        assertEquals( 200, httpResponse.getCode());
        jsonResponseList = httpResponse.getJsonBodyAsList();
        assertEquals(1, jsonResponseList.size());
        jsonResponse = (Map) jsonResponseList.get(0);
        assertEquals(sid, jsonResponse.get("id"));
        assertEquals("si1", jsonResponse.get("studentId"));
        assertEquals("fn1", jsonResponse.get("firstName"));
        assertEquals("ln1", jsonResponse.get("lastName"));

        //unassociate
        httpResponse = httpTester.delete("/student/"+sid+"/class/"+cid);
        assertEquals( 204, httpResponse.getCode());

        //verify the relationship is deleted
        httpResponse = httpTester.get("/student/"+sid+"/class");
        assertEquals( 200, httpResponse.getCode());
        jsonResponseList = httpResponse.getJsonBodyAsList();
        assertEquals(0, jsonResponseList.size());

        httpResponse = httpTester.get("/class/"+cid+"/student");
        assertEquals( 200, httpResponse.getCode());
        jsonResponseList = httpResponse.getJsonBodyAsList();
        assertEquals(0, jsonResponseList.size());

        //delete
        httpResponse = httpTester.delete("/student/"+sid);
        assertEquals( 204, httpResponse.getCode());
        httpResponse = httpTester.delete("/class/"+cid);
        assertEquals( 204, httpResponse.getCode());

    }

    @AfterEach
    public void tearDown() throws Exception {
        httpTester.close();
    }
}
