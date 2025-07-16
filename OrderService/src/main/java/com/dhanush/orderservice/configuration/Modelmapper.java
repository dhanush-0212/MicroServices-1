package com.dhanush.orderservice.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Modelmapper {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
