package com.mlv.dreamshop.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SHopConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
