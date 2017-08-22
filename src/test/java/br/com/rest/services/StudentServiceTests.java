package br.com.rest.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.rest.errors.exceptions.ResourceNotFoundException;
import br.com.rest.models.Student;
import br.com.rest.repositories.StudentRepository;

@SpringBootTest
@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTests {
    
    private ExpectedException thrown = ExpectedException.none();
    
    @InjectMocks
    private StudentService studentService;
    
    @Mock
    private Student student;
    
    @Mock
    private StudentRepository studentRepository;
    
    @Before 
    public void setup() {
        when(studentRepository.findOne(1L)).thenReturn(student);
    }
    
    @Test
    public void findShouldReturnStudent() throws Exception{
                        
        Student returnedStudent = studentService.find(1L);

        assertThat(returnedStudent.getId(), equalTo(student.getId()));
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void findStudentNotExistsShouldReturnResourceNotFoundException() throws Exception{
                        
        studentService.find(2L);
    }
    
}
