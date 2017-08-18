package br.com.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rest.models.Student;
import br.com.rest.services.StudentService;

@RestController
@RequestMapping("students")
public class StudentController {
	
    @Autowired
    StudentService studentService;
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    
	@GetMapping
	public ResponseEntity<?> findAll(Pageable pageable) {			    
		return new ResponseEntity<>(studentService.findAll(pageable), HttpStatus.OK) ;
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody Student student){
	    return new ResponseEntity<>(studentService.save(student), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable Long id){
        return new ResponseEntity<>(studentService.find(id), HttpStatus.OK);
    }
	
	@PutMapping
    public ResponseEntity<?> update(@RequestBody Student student){
        return new ResponseEntity<>(studentService.update(student), HttpStatus.OK);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return new ResponseEntity<>(studentService.delete(id));
    }
	
	
	
}