package br.com.rest.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "students")
public class Student extends DefaultEntity{ 
	
    @NotEmpty
    private String name;
	
    @NotEmpty
	private String registration;
    

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
