package br.com.rest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.isNotNull;

import br.com.rest.errors.exceptions.ResourceNotFoundException;
import br.com.rest.models.Student;
import br.com.rest.repositories.StudentRepository;
import io.jsonwebtoken.MalformedJwtException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerTests extends AuthControllerTests {

    @MockBean
    StudentRepository studentRepository;
    
    private String invalidToken = "111111";
    
    @Before
    @Override
    public void setup() throws Exception {
    	super.setup();
    	BDDMockito.when(studentRepository.findOne(1L)).thenReturn(new Student(1L, "lucas", "123"));
    }
    
    @Test(expected=MalformedJwtException.class)
    public void listStudentsWithInvalidTokenShouldReturnMalformedJwtException() throws Exception {
        mockMvc.perform(get("/students").header("Authorization", invalidToken));
    }
    
    @Test
    public void listStudentsWithoutTokenShouldReturnStatusCode403() throws Exception {
        mockMvc.perform(get("/students")).andExpect(status().isForbidden());
    }
    
    @Test
    public void listAllShouldReturnAllStudents() throws Exception {

        List<Student> students = Arrays.asList(new Student(1L, "lucas", "123"),
                new Student(2L, "Nigas", "1234567890"));

        BDDMockito.when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(get("/students/all").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("lucas")))
                .andExpect(jsonPath("$[0].registration", is("123")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Nigas")))
                .andExpect(jsonPath("$[1].registration", is("1234567890")));
    }     
    
    @Test(expected=MalformedJwtException.class)
    public void findByIdWithInvalidTokenShouldReturnMalformedJwtException() throws Exception {
        mockMvc.perform(get("/students/1").header("Authorization", invalidToken));
    }
    
    @Test
    public void findByIdWithoutTokenShouldReturnStatusCode403() throws Exception {
        mockMvc.perform(get("/students/1")).andExpect(status().isForbidden());
    }
    
    @Test
    public void findByIdShouldReturnStudentById() throws Exception{
                    
        mockMvc.perform(get("/students/1").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("lucas")))
                .andExpect(jsonPath("$.registration", is("123")));
    }
    
    @Test
    public void findByIdNoExistShouldReturnStatusCode404() throws Exception{                       
        BDDMockito.when(studentRepository.findOne(1L)).thenReturn(null);
        mockMvc.perform(get("/students/1").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))         
        .andExpect(status().isNotFound());
    }
    
    @Test(expected=MalformedJwtException.class)
    public void deleteWithInvalidTokenShouldReturnMalformedJwtException() throws Exception {
        mockMvc.perform(delete("/students/1").header("Authorization", invalidToken));
    }
    
    @Test
    public void deleteWithoutTokenShouldReturnStatusCode403() throws Exception {
        mockMvc.perform(delete("/students/1")).andExpect(status().isForbidden());
    }
    
    @Test
    public void deleteShouldReturnStatusCode200() throws Exception{
    	
    	BDDMockito.doNothing().when(studentRepository).delete(1L);
    	
    	mockMvc.perform(delete("/students/1").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
    			.andExpect(status().isOk());
    }
    
    @Test
    public void deleteStudentNotFoundShouldReturnStatusCode404() throws Exception{
    	
    	BDDMockito.when(studentRepository.findOne(1L)).thenReturn(null);
    	
    	mockMvc.perform(delete("/students/1").header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
    			.andExpect(status().isNotFound());
    }
    
    @Test(expected=MalformedJwtException.class)
    public void updateWithInvalidTokenShouldReturnMalformedJwtException() throws Exception {
           	    	
    	mockMvc.perform(put("/students").header("Authorization", invalidToken));
    }
    
    @Test
    public void updateWithoutTokenShouldReturnStatusCode403() throws Exception {
        mockMvc.perform(delete("/students")).andExpect(status().isForbidden());
    }
    
    @Test
    public void updateWithNameNullShouldReturnStatusCode400() throws Exception {
        Student student = new Student(1L, null, "123");
        
        String json = mapper.writeValueAsString(student);
        
        mockMvc.perform(put("/students")
                   .header("Authorization", mvcResult.getResponse().getHeader("Authorization"))
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(json))
                        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void updateWithRegistrationNullShouldReturnStatusCode400() throws Exception {
        Student student = new Student(1L, "nigas", null);
        
        String json = mapper.writeValueAsString(student);
        
        mockMvc.perform(put("/students")
                   .header("Authorization", mvcResult.getResponse().getHeader("Authorization"))
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(json))
                        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void updateShouldReturnPersistChangedStudentAndStatusCode200() throws Exception {
        Student student = new Student(1L, "nigas", "123");
        
        String json = mapper.writeValueAsString(student);
        
        mockMvc.perform(put("/students")
                   .header("Authorization", mvcResult.getResponse().getHeader("Authorization"))
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(json))
                        .andDo(print())
                        .andExpect(status().isOk());
    }
    
    @Test(expected=MalformedJwtException.class)
    public void saveWithInvalidTokenShouldReturnMalformedJwtException() throws Exception {
        mockMvc.perform(post("/students").header("Authorization", invalidToken));
    }
    
    @Test
    public void saveWithoutTokenShouldReturnStatusCode403() throws Exception {
        mockMvc.perform(post("/students")).andExpect(status().isForbidden());
    }
    
    @Test
    public void saveWithNameNullShouldReturnStatusCode400() throws Exception{
    	
    	Student student = new Student(3L, null, "1234");
    	
    	BDDMockito.when(studentRepository.save(student)).thenReturn(student);
    	
    	String json = mapper.writeValueAsString(student);
    	
    	mockMvc.perform(post("/students")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)	
    			.header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
    	            .andExpect(jsonPath("$.title", is("Erro ao validar os Campos")))
    				.andExpect(status().isBadRequest());
    	            
    }
    
    @Test
    public void saveWithRegistrationNullShouldReturnStatusCode400() throws Exception{
    	
    	Student student = new Student(3L, "nigas", null);
    	
    	BDDMockito.when(studentRepository.save(student)).thenReturn(student);
    	
    	String json = mapper.writeValueAsString(student);
    	
    	mockMvc.perform(post("/students")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
    	            .andExpect(jsonPath("$.title", is("Erro ao validar os Campos")))	
    	            .andExpect(status().isBadRequest());
    }
    
    @Test
    public void saveShouldReturnPersistDataAndStatusCode200() throws Exception{
    	
    	Student student = new Student(2L, "nigas", "1234567");
    	
    	BDDMockito.when(studentRepository.save(student)).thenReturn(student);  
    	
    	String json = mapper.writeValueAsString(student);
    	
    	mockMvc.perform(post("/students")    					
    					.contentType(MediaType.APPLICATION_JSON)
    					.content(json)
    					.header("Authorization", mvcResult.getResponse().getHeader("Authorization")))
    					.andExpect(jsonPath("$.id", notNullValue()))
    					.andExpect(jsonPath("$.name", is("nigas")))
    					.andExpect(jsonPath("$.registration", is("1234567")))
    					.andExpect(status().isOk());
    }
}
