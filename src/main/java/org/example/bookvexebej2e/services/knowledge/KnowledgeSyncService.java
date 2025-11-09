package org.example.bookvexebej2e.services.knowledge;

import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.dto.knowledge.KnowledgeSyncRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KnowledgeSyncService {

    private final RestTemplate restTemplate;
    
    @Value("${chat.service.url}")
    private String chatServiceUrl;

    public KnowledgeSyncService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sync knowledge to chat service
     */
    public void syncKnowledge(KnowledgeSyncRequest request) {
        try {
            String url = chatServiceUrl + "/api/knowledge/sync";
            
            log.info("Syncing knowledge to chat service: entityType={}, operation={}, entityId={}", 
                    request.getEntityType(), request.getOperation(), request.getEntityId());
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Knowledge sync successful for entityId: {}", request.getEntityId());
            } else {
                log.warn("Knowledge sync returned non-success status: {} - Response: {}", 
                        response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Failed to sync knowledge to chat service for entityId: {}. Error: {}", 
                    request.getEntityId(), e.getMessage(), e);
            // Không throw exception để không ảnh hưởng đến main flow
        }
    }

    /**
     * Sync Trip knowledge
     */
    public void syncTrip(String tripId, String operation, String title, String content) {
        KnowledgeSyncRequest request = KnowledgeSyncRequest.builder()
                .entityId(tripId)
                .entityType("TRIP")
                .operation(operation)
                .title(title)
                .content(content)
                .category("TRIP_INFO")
                .build();
        syncKnowledge(request);
    }

    /**
     * Sync Route knowledge
     */
    public void syncRoute(String routeId, String operation, String title, String content) {
        KnowledgeSyncRequest request = KnowledgeSyncRequest.builder()
                .entityId(routeId)
                .entityType("ROUTE")
                .operation(operation)
                .title(title)
                .content(content)
                .category("ROUTE_INFO")
                .build();
        syncKnowledge(request);
    }

    /**
     * Sync Car knowledge
     */
    public void syncCar(String carId, String operation, String title, String content) {
        KnowledgeSyncRequest request = KnowledgeSyncRequest.builder()
                .entityId(carId)
                .entityType("CAR")
                .operation(operation)
                .title(title)
                .content(content)
                .category("CAR_INFO")
                .build();
        syncKnowledge(request);
    }
}
