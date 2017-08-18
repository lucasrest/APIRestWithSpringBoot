package br.com.rest.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.rest.errors.exceptions.ResourceNotFoundException;
import br.com.rest.models.Student;
import br.com.rest.repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;
    
    public StudentService(StudentRepository studentRespository) {
        this.studentRepository = studentRespository;
    }
    
    public Iterable<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
    
    public Student find(Long id) {
        verifyIfStudentExist(id);
        return studentRepository.findOne(id);
    }
    
    public Student save(Student student) {        
        return studentRepository.save(student);
    }
    
    public Student update(Student student) {  
        verifyIfStudentExist(student.getId());
        return studentRepository.save(student);
    }
    
    public HttpStatus delete(Long id) {
        verifyIfStudentExist(id);
        studentRepository.delete(id);
        return HttpStatus.OK;
    }
    
    private void verifyIfStudentExist(Long id) {
        if(studentRepository.findOne(id) == null)
            throw new ResourceNotFoundException("Student not found!");
    }
    
}
