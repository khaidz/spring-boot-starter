package net.khaibq.springbootstater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = {"net.khaibq.springbootstater"})
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableWebSecurity
@EnableMethodSecurity
@EnableAsync
@EnableScheduling
public class SpringBootStaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStaterApplication.class, args);
    }

}
