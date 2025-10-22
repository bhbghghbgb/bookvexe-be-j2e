package org.example.bookvexebej2e.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;

@Configuration
@EnableJpaRepositories(repositoryBaseClass = QuerydslJpaPredicateExecutor.class)
public class JpaConfig {
}
