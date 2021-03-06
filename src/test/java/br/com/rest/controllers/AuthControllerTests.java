package br.com.rest.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rest.models.User;
import br.com.rest.repositories.UserRepository;
import br.com.rest.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Resource
    private FilterChainProxy filterChainProxy;

    @MockBean
    protected UserRepository userRepository;
    
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    protected DataSource dataSource;

    protected MockMvc mockMvc;
    
    @Autowired 
    protected ObjectMapper mapper;

    protected MvcResult mvcResult;

    @Before
    public void setup() throws Exception {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(filterChainProxy).build();
        signedIn();
    }

    @After
    @Test
    public void logout() throws Exception {
        mvcResult = mockMvc.perform(post("/logout")).andExpect(status().isMovedTemporarily()).andReturn();
    }

    @Test
    public void signedIn() throws Exception {
    	
    	BDDMockito.when(userRepository.findByUsername("lucas")).thenReturn(new User("lucas", "123"));
    	
        mvcResult = mockMvc.perform(post("/login").param("username", "lucas").param("password", "123"))
                .andExpect(status().isOk()).andReturn();

        System.out.println(mvcResult.getResponse().getHeader("Authorization"));

    }

    @Test
    public void signInWithIncorrectUsernameShouldReturnStatus401() throws Exception {

        mockMvc.perform(post("/login").param("username", "tchoris").param("password", "123"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void signInWithIncorrectPasswordShouldReturnStatus401() throws Exception {

        mockMvc.perform(post("/login").param("username", "lucas").param("password", "1234"))
                .andExpect(status().isUnauthorized());

    }

}
