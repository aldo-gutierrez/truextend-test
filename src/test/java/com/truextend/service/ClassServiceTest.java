package com.truextend.service;

import com.truextend.dao.ClassDAO;
import com.truextend.exception.BusinessException;
import com.truextend.model.Class0;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.fail;

public class ClassServiceTest {

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
        ClassDAO classDAO = Mockito.mock(ClassDAO.class);
        ClassService classService = new ClassService();
        classService.setClassDAO(classDAO);

        Class0 class01 = new Class0();
        try {
            classService.insert(class01);
            fail("code should be required");
        } catch (BusinessException ex) {
            Assertions.assertEquals("code is required", ex.getMessage());
        }
        class01.setCode("MAT-101");
        try {
            classService.insert(class01);
            fail("title should be required");
        } catch (BusinessException ex) {
            Assertions.assertEquals("title is required", ex.getMessage());
        }
        class01.setTitle("Algebra 1");
        class01.setDescription("Algebra Basica y Matrices");
        classService.insert(class01);


        Class0 class02 = new Class0();
        class02.setCode("MAT-101");
        class02.setTitle("Algebra de Baldor");
        try {
            Mockito.when(classDAO.selectBy("code", "MAT-101")).thenReturn(class01);
            classService.insert(class02);
            fail("A class with the same code can't be insert");
        } catch (BusinessException ex) {
            Assertions.assertEquals("code is taken", ex.getMessage());
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
