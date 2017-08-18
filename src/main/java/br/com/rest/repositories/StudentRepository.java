package br.com.rest.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.rest.models.Student;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long>{	
    
}
