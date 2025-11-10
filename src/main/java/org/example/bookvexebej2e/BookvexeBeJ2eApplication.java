package org.example.bookvexebej2e;

import org.example.bookvexebej2e.repositories.base.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableJpaRepositories(basePackages = "org.example.bookvexebej2e.repositories", repositoryBaseClass = BaseRepositoryImpl.class)
@SpringBootApplication
@EnableScheduling
@EnableFeignClients(basePackages = "org.example.bookvexebej2e.helpers.api")
public class BookvexeBeJ2eApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookvexeBeJ2eApplication.class, args);
    }

}
