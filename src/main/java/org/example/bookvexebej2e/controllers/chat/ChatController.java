package org.example.bookvexebej2e.controllers.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.bookvexebej2e.models.db.Knowledge;
import org.example.bookvexebej2e.repositories.chat.KnowledgeRepository;
import org.example.bookvexebej2e.services.chat.AiChatService;
import org.example.bookvexebej2e.services.chat.DatabaseContextService;
import org.example.bookvexebej2e.services.chat.EmbeddingService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ai")
@Profile("ai-enabled")  // Only enable when "ai-enabled" profile is active
@Tag(name = "AI Chatbot", description = "API chatbot AI hỗ trợ khách hàng (tiếng Việt)")
public class ChatController {

    private final AiChatService aiChatService;
    private final KnowledgeRepository knowledgeRepository;
    private final EmbeddingService embeddingService;
    private final DatabaseContextService databaseContextService;

    public ChatController(AiChatService aiChatService,
                         KnowledgeRepository knowledgeRepository,
                         EmbeddingService embeddingService,
                         DatabaseContextService databaseContextService) {
        this.embeddingService = embeddingService;
        this.aiChatService = aiChatService;
        this.knowledgeRepository = knowledgeRepository;
        this.databaseContextService = databaseContextService;
    }

    @PostMapping("/chat")
    @Operation(summary = "Hỏi chatbot AI", description = "Gửi câu hỏi đến AI chatbot và nhận câu trả lời bằng tiếng Việt")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest req) {
        try {
            String answer = aiChatService.ask(req.prompt());
            return ResponseEntity.ok(new ChatResponse(
                true,
                answer,
                LocalDateTime.now(),
                null
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(new ChatResponse(
                false,
                "Xin lỗi, hệ thống đang gặp sự cố. Vui lòng thử lại sau.",
                LocalDateTime.now(),
                e.getMessage()
            ));
        }
    }

    @PostMapping("/hoi-dap")
    @Operation(summary = "Hỏi đáp nhanh", description = "Endpoint tiếng Việt để hỏi chatbot")
    public ResponseEntity<ChatResponse> hoiDap(@RequestBody HoiDapRequest req) {
        return chat(new ChatRequest(req.cauHoi()));
    }

    @PostMapping("/knowledge/add")
    @Operation(summary = "Thêm kiến thức", description = "Thêm tài liệu vào knowledge base của AI")
    public ResponseEntity<Knowledge> addKnowledge(@RequestBody KnowledgeRequest req) {
        try {
            Knowledge knowledge = new Knowledge();
            knowledge.setTitle(req.title());
            knowledge.setContent(req.content());

            // Tạo embedding vector
            float[] vector = embeddingService.embed(req.content());
            knowledge.setEmbedding(vector);

            Knowledge saved = knowledgeRepository.save(knowledge);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/knowledge/list")
    @Operation(summary = "Danh sách kiến thức", description = "Xem tất cả tài liệu trong knowledge base")
    public ResponseEntity<Iterable<Knowledge>> listKnowledge() {
        return ResponseEntity.ok(knowledgeRepository.findAll());
    }

    @GetMapping("/routes-info")
    @Operation(summary = "Thông tin tuyến đường", description = "Xem thông tin các tuyến đường có trong hệ thống")
    public ResponseEntity<String> getRoutesInfo() {
        return ResponseEntity.ok(databaseContextService.getRoutesContext());
    }

    @GetMapping("/trips-info")
    @Operation(summary = "Thông tin chuyến xe", description = "Xem thông tin các chuyến xe sắp khởi hành")
    public ResponseEntity<String> getTripsInfo() {
        return ResponseEntity.ok(databaseContextService.getUpcomingTripsContext());
    }

    @GetMapping("/system-info")
    @Operation(summary = "Thông tin hệ thống", description = "Xem thông tin tổng quan về hệ thống")
    public ResponseEntity<String> getSystemInfo() {
        return ResponseEntity.ok(databaseContextService.getSystemOverview());
    }

    // ===== DTOs =====

    public record ChatRequest(String prompt) {}

    public record HoiDapRequest(String cauHoi) {}

    public record ChatResponse(
        boolean success,
        String answer,
        LocalDateTime timestamp,
        String error
    ) {}

    public record KnowledgeRequest(
        String title,
        String content,
        String category
    ) {}
}
