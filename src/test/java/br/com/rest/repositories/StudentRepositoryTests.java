package br.com.rest.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.rest.models.Student;
import br.com.rest.repositories.StudentRepository;
import br.com.rest.services.StudentService;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepositoryTests {
    
    @Autowired
    private StudentRepository studentRepository;  
    
    @Autowired
    private TestEntityManager manager; 
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
           
    @Test
    public void saveShouldPersistData() {
        Student student = new Student("nigas", "123456");
        studentRepository.save(student);
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo("nigas");
        assertThat(student.getRegistration()).isEqualTo("123456");
    }
    
    @Test
    public void updateShouldChangeAndPersistData() {
        Student student = new Student("nigas", "123456");
        manager.persist(student);
        student.setName("nigas2");
        student.setRegistration("1234567");
        studentRepository.save(student);
        Student studentGet = manager.find(Student.class, student.getId());
        assertThat(studentGet.getId()).isEqualTo(student.getId());
        assertThat(studentGet.getName()).isEqualTo("nigas2");
        assertThat(studentGet.getRegistration()).isEqualTo("1234567");
    }
    
    @Test
    public void deleteShouldRemoveData() {
        Student student = new Student("nigas", "123456");
        manager.persist(student);
        studentRepository.delete(student.getId());
        Student studentGet = manager.find(Student.class, student.getId());
        assertThat(studentGet).isNull();
    }
    
    @Test
    public void findByIdShouldReturnStudentById() {
        Student student = new Student("nigas", "123456");
        manager.persist(student);
        Student studentGet = studentRepository.findOne(student.getId());
        assertThat(studentGet.getId()).isEqualTo(student.getId());
        assertThat(studentGet.getName()).isEqualTo("nigas");
        assertThat(studentGet.getRegistration()).isEqualTo("123456");
    }
}
