package com.fptaptech.s4.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name", "dsdqoutbx");
        config.put("api_key", "343796844766731");
        config.put("api_secret", "tOMVm8cXHzTUSPIdAMz0crlSSNM");
        config.put("secure", true);
        return new Cloudinary(config);
    }

}
