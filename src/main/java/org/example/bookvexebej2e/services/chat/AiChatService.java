package org.example.bookvexebej2e.services.chat;

import org.example.bookvexebej2e.models.db.Knowledge;
import org.example.bookvexebej2e.repositories.chat.KnowledgeRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("ai-enabled")  // Only enable when "ai-enabled" profile is active
public class AiChatService {

    private final ChatClient chatClient;
    private final EmbeddingService embeddingService;
    private final KnowledgeRepository knowledgeRepository;
    private final DatabaseContextService databaseContextService;

    public AiChatService(ChatClient chatClient,
                        EmbeddingService embeddingService,
                        KnowledgeRepository knowledgeRepository,
                        DatabaseContextService databaseContextService) {
        this.chatClient = chatClient;
        this.embeddingService = embeddingService;
        this.knowledgeRepository = knowledgeRepository;
        this.databaseContextService = databaseContextService;
    }

    public String ask(String userPrompt) {
        // 1 Phân tích câu hỏi để lấy context phù hợp
        String databaseContext = buildDatabaseContext(userPrompt);

        // 2 Tạo embedding cho câu hỏi (tìm kiếm trong knowledge base)
        float[] queryVector = embeddingService.embed(userPrompt);

        // 3 Tìm các tài liệu liên quan trong knowledge base
        List<Knowledge> relevantDocs = knowledgeRepository.findSimilar(
                embeddingService.toPgVectorLiteral(queryVector), 5
        );

        // 4 Ghép context từ knowledge base
        String knowledgeContext = relevantDocs.stream()
                .map(k -> "• " + k.getTitle() + ": " + k.getContent())
                .collect(Collectors.joining("\n"));

        // 5 Tạo prompt tiếng Việt tối ưu
        String prompt = buildVietnamesePrompt(userPrompt, databaseContext, knowledgeContext);

        // 6 Gọi Gemini/Ollama
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    /**
     * Xây dựng context từ database dựa trên câu hỏi
     */
    private String buildDatabaseContext(String userPrompt) {
        String promptLower = userPrompt.toLowerCase();
        StringBuilder context = new StringBuilder();

        // Thêm thông tin tổng quan hệ thống
        context.append(databaseContextService.getSystemOverview()).append("\n\n");

        // Nếu hỏi về tuyến đường, giá vé
        if (promptLower.contains("tuyến") ||
            promptLower.contains("đường") ||
            promptLower.contains("giá") ||
            promptLower.contains("từ") && promptLower.contains("đến")) {
            context.append(databaseContextService.getRoutesContext()).append("\n\n");
        }

        // Nếu hỏi về chuyến xe, lịch trình
        if (promptLower.contains("chuyến") ||
            promptLower.contains("lịch") ||
            promptLower.contains("khởi hành") ||
            promptLower.contains("giờ")) {
            context.append(databaseContextService.getUpcomingTripsContext()).append("\n\n");
        }

        // Phát hiện điểm đi và điểm đến trong câu hỏi
        String routeInfo = extractRouteQuery(userPrompt);
        if (routeInfo != null) {
            context.append(routeInfo).append("\n\n");
        }

        return context.toString();
    }

    /**
     * Trích xuất thông tin tuyến đường từ câu hỏi
     */
    private String extractRouteQuery(String userPrompt) {
        // Tìm pattern "từ X đến Y" hoặc "X - Y" hoặc "X Y"
        String[] commonPatterns = {
            "từ (\\w+) đến (\\w+)",
            "từ (\\w+) tới (\\w+)",
            "(\\w+) - (\\w+)",
            "(\\w+) đến (\\w+)"
        };

        for (String pattern : commonPatterns) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern, java.util.regex.Pattern.CASE_INSENSITIVE);
            java.util.regex.Matcher m = p.matcher(userPrompt);
            if (m.find()) {
                String departure = m.group(1);
                String arrival = m.group(2);
                return databaseContextService.findRoutesByLocations(departure, arrival);
            }
        }

        return null;
    }

    /**
     * Xây dựng prompt tối ưu cho tiếng Việt và ngữ cảnh Việt Nam
     */
    private String buildVietnamesePrompt(String userPrompt, String databaseContext, String knowledgeContext) {
        return """
                BẠN LÀ TRỢ LÝ ẢO THÔNG MINH CỦA HỆ THỐNG ĐẶT VÉ XE TẠI VIỆT NAM.
                
                NHIỆM VỤ:
                - Trả lời bằng tiếng Việt tự nhiên, lịch sự và dễ hiểu
                - Sử dụng thông tin từ dữ liệu thực tế của hệ thống để trả lời chính xác
                - Nếu không có thông tin, hãy thành thật nói rằng bạn cần kiểm tra lại
                - Định dạng giá tiền theo kiểu Việt Nam (VD: 150.000 VNĐ)
                - Định dạng thời gian theo kiểu Việt Nam (VD: 08:30 ngày 23/10/2025)
                - Gợi ý các bước tiếp theo nếu khách hàng muốn đặt vé
                
                DỮ LIỆU TỪ HỆ THỐNG:
                %s
                
                KIẾN THỨC BỔ SUNG:
                %s
                
                CÂU HỎI CỦA KHÁCH HÀNG:
                "%s"
                
                HÃY TRẢ LỜI:
                (Trả lời ngắn gọn, rõ ràng, thân thiện. Nếu có nhiều lựa chọn, hãy liệt kê và giải thích từng cái.)
                """.formatted(
                    databaseContext.isEmpty() ? "Không có dữ liệu liên quan" : databaseContext,
                    knowledgeContext.isEmpty() ? "Không có kiến thức bổ sung" : knowledgeContext,
                    userPrompt
                );
    }
}
