package com.truextend.service;

import com.truextend.dao.StudentDAO;
import com.truextend.exception.BusinessException;
import com.truextend.model.Student;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import org.mockito.Mockito;

public class StudentServiceTest {


    @BeforeAll
    static void initAll() {
        System.out.println("@BeforeAll");
    }

    @BeforeEach
    void init() {
        System.out.println("@BeforeEach");
    }

    @Test
    public void testInsert(){
        StudentDAO studentDAO = Mockito.mock(StudentDAO.class);
        StudentService studentService = new StudentService();
        studentService.setStudentDAO(studentDAO);

        Student student = new Student();
        try {
            studentService.insert(student);
            fail("studentId should be required");
        } catch (BusinessException ex) {
            Assertions.assertEquals("studentId is required", ex.getMessage());
        }
        student.setStudentId("44668877");
        try {
            studentService.insert(student);
            fail("firstName should be required");
        } catch (BusinessException ex) {
            Assertions.assertEquals("firstName is required", ex.getMessage());
        }
        student.setFirstName("Jose");
        try {
            studentService.insert(student);
            fail("lastName should be required.");
        } catch (BusinessException ex) {
            Assertions.assertEquals("lastName is required", ex.getMessage());
        }
        student.setLastName("Perez");
        studentService.insert(student);


        Student student2 = new Student();
        student2.setStudentId("44668877");
        student2.setFirstName("Jose");
        student2.setLastName("Perez");
        try {
            Mockito.when(studentDAO.selectBy("studentId", "44668877")).thenReturn(student);
            studentService.insert(student2);
            fail("A student with the same studentId can't be insert");
        } catch (BusinessException ex) {
            Assertions.assertEquals("studentId is taken", ex.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        System.out.println("@AfterEach");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("@AfterAll");
    }

}
