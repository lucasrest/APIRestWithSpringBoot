package br.com.rest.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.rest.models.User;

public interface UserRepository extends CrudRepository<User, Long>{

    public User findByUsername(String username);
    
    public User findByUsernameAndPassword(String username, String password);
}
