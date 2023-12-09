package com.theonewhocode.eazyschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
@EnableJpaRepositories("com.theonewhocode.eazyschool.repository")
@EntityScan("com.theonewhocode.eazyschool.model")
public class EazySchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(EazySchoolApplication.class, args);
    }

}
