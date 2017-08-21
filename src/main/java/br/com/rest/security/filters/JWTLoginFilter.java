package br.com.rest.security.filters;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rest.models.User;
import br.com.rest.security.services.TokenAuthenticationService;
import br.com.rest.services.UserService;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{    
    
    public JWTLoginFilter(String url, AuthenticationManager manager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(manager);        
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        User user = null;
        if((request.getParameter("username") != null) && (request.getParameter("password") != null)) {
            user = new User(request.getParameter("username"), request.getParameter("password"));
        }else {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);               
        }
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                                                                            user.getUsername(),
                                                                            user.getPassword(),
                                                                            Collections.emptyList()));        
    }
    
    @Override
    public void successfulAuthentication(
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication auth) {
        TokenAuthenticationService.addAuthentication(response, auth.getName());
    }
    
}
