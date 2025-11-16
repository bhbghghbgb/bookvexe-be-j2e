package org.example.bookvexebej2e.helpers.api;

import java.util.UUID;

import org.example.bookvexebej2e.helpers.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentClient {

    private final RestTemplate restTemplate;

    public PaymentClient(RestTemplateBuilder builder,
                         @Value("${services.payment-service.url}") String baseUrl) {
        this.restTemplate = builder.rootUri(baseUrl).build();
    }

    public PaymentDto findById(UUID id) {
        try {
            return restTemplate.getForObject("/admin/payments/{id}", PaymentDto.class, id);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
