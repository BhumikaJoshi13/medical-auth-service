package com.medical.admin.client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import feign.Logger;
import feign.RequestInterceptor;
public class ReceptionistServiceClientConfig {
	@Bean
    public RequestInterceptor receptionistTokenInterceptor() {  // unique bean name
        return requestTemplate -> {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getCredentials() != null) {
                    String token = auth.getCredentials().toString();
                    requestTemplate.header("Authorization", "Bearer " + token);
                }
            } catch (Exception e) {
                System.err.println("Token forwarding failed: " + e.getMessage());
            }
        };
    }

    @Bean
    public Logger.Level receptionistFeignLoggerLevel() {  
        return Logger.Level.FULL;
    }
}
