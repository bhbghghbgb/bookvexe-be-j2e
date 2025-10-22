package org.example.bookvexebej2e.controllers.chat;

import org.example.bookvexebej2e.models.db.Knowledge;
import org.example.bookvexebej2e.repositories.chat.KnowledgeRepository;
import org.example.bookvexebej2e.services.chat.AiChatService;
import org.example.bookvexebej2e.services.chat.EmbeddingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class ChatController {

    private final AiChatService aiChatService;
    private final KnowledgeRepository knowledgeRepository;
    private final EmbeddingService embeddingService;

    public ChatController(AiChatService aiChatService, KnowledgeRepository knowledgeRepository, EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
        this.aiChatService = aiChatService;
        this.knowledgeRepository = knowledgeRepository;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest req) {
        return new ChatResponse(aiChatService.ask(req.prompt()));
    }

    public record ChatRequest(String prompt) {}
    public record ChatResponse(String answer) {}

    @PostMapping("/add")
    public Knowledge add(@RequestBody Knowledge req) {
        float[] vector = embeddingService.embed(req.getContent());
        req.setEmbedding(vector);
        return knowledgeRepository.save(req);
    }
}
