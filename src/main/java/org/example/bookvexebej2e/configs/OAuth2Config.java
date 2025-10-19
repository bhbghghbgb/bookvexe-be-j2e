package org.example.bookvexebej2e.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.Collections;

public class OAuth2Config {
    @Bean
    @ConditionalOnMissingBean(ClientRegistrationRepository.class)
    public ClientRegistrationRepository emptyClientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(Collections.emptyList());
    }
}
