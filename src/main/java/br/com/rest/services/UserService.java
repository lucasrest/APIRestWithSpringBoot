package br.com.rest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.rest.models.User;
import br.com.rest.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public User find(Long id) {
        return userRepository.findOne(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public HttpStatus delete(Long id) {
        userRepository.delete(id);
        return HttpStatus.OK;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public boolean authenticated(String username, String password) {
        if(userRepository.findByUsernameAndPassword(username, password) != null)
            return true;
        else
            return false;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        List<GrantedAuthority> authorityListAdmin = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
        List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");        
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorityListAdmin);
    }

}
