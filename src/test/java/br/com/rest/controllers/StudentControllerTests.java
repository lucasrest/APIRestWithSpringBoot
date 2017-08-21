package br.com.rest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

import br.com.rest.errors.exceptions.ResourceNotFoundException;
import br.com.rest.models.Student;
import br.com.rest.services.StudentService;
import io.jsonwebtoken.MalformedJwtException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTests extends AuthControllerTests {

    @MockBean
    StudentService studentService;
    
    @Test(expected=MalformedJwtException.class)
    public void listStudentsWithInvalidTokenShouldReturnMalformedJwtException() throws Exception {
        mockMvc.perform(get("/students").header("Authorization", "111111"));
    }
    
    @Test
    public void listAllShouldReturnAllStudents() throws Exception {

        List<Student> students = Arrays.asList(new Student(1L, "lucas", "1234567"),
                new Student(2L, "Nigas", "1234567890"));

        BDDMockito.when(studentService.findAll()).thenReturn(students);

        mockMvc.perform(get("/students/all").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("lucas")))
                .andExpect(jsonPath("$[0].registration", is("1234567")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Nigas")))
                .andExpect(jsonPath("$[1].registration", is("1234567890")));
    }     
    
    @Test
    public void findByIdShouldReturnStudentById() throws Exception{
        
        BDDMockito.when(studentService.find(1L)).thenReturn(new Student(1L, "nigas", "1234"));
    
        mockMvc.perform(get("/students/1").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("nigas")))
                .andExpect(jsonPath("$.registration", is("1234")));
    }
    
    @Test
    public void findByIdNoExistShouldReturnStatusCode404() throws Exception{                       
        BDDMockito.when(studentService.find(1L)).thenThrow(new ResourceNotFoundException("user not found"));
        mockMvc.perform(get("/students/1").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))   
                .andExpect(status().isNotFound());
    }

}
