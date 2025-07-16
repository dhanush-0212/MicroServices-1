package com.dhanush.productservice.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class modelmapping {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
