package org.example.bookvexebej2e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.example.bookvexebej2e.repository")
@SpringBootApplication
public class BookvexeBeJ2eApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookvexeBeJ2eApplication.class, args);
    }

}
