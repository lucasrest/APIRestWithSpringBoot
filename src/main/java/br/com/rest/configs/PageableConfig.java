package br.com.rest.configs;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class PageableConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentsResolver) {        
        PageableHandlerMethodArgumentResolver phma = new PageableHandlerMethodArgumentResolver();
        phma.setFallbackPageable(new PageRequest(0, 3));
        argumentsResolver.add(phma);
    }
    
}
