package br.com.rest.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "students")
public class Student extends DefaultEntity{ 
	
    @NotEmpty(message = "O Nome é obrigatório")
    private String name;
	
    @NotEmpty
	private String registration;
    
    
    public Student() {
       
    }

    public Student(String name, String registration) {
        this.name = name;
        this.registration = registration;
    }
    
    public Student(Long id, String name, String registration) {
        this.setId(id);
        this.name = name;
        this.registration = registration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }
	
}
