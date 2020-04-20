package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ImportResource(value = {"classpath:spring-conf-interceptor.xml"})
public class AutoConfig implements WebMvcConfigurer {
    // @Bean
    // Interceptor interceptor() {
    //     return new Interceptor();
    // }
    //
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(interceptor()).addPathPatterns("/**");
    // }
}
