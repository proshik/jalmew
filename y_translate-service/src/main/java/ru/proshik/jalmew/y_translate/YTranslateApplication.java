package ru.proshik.jalmew.y_translate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Created by proshik on 30.07.16.
 */
@EnableOAuth2Client
@EnableFeignClients
@EnableResourceServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class YTranslateApplication {

    public static void main(String[] args) {
        SpringApplication.run(YTranslateApplication.class);
    }

}
