package ru.proshik.jalmew.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by proshik on 20.08.16.
 */
@EnableOAuth2Client
@EnableFeignClients
@EnableResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class LearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnApplication.class);
    }

}
