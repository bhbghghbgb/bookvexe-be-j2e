package org.example.bookvexebej2e.services.chat;

import org.example.bookvexebej2e.models.db.Knowledge;
import org.example.bookvexebej2e.repositories.chat.KnowledgeRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiChatService {

    private final ChatClient chatClient;
    private final EmbeddingService embeddingService;
    private final KnowledgeRepository knowledgeRepository;

    public AiChatService(ChatClient chatClient, EmbeddingService embeddingService, KnowledgeRepository knowledgeRepository) {
        this.chatClient = chatClient;
        this.embeddingService = embeddingService;
        this.knowledgeRepository = knowledgeRepository;
    }

    public String ask(String userPrompt) {
        // 1️⃣ Tạo embedding cho câu hỏi
        float[] queryVector = embeddingService.embed(userPrompt);

        // 2️⃣ Tìm các doc tương tự trong DB
        List<Knowledge> relevantDocs = knowledgeRepository.findSimilar(
                embeddingService.toPgVectorLiteral(queryVector), 5
        );

        // 3️⃣ Ghép context từ DB
        String context = relevantDocs.stream()
                .map(k -> "• " + k.getTitle() + ": " + k.getContent())
                .collect(Collectors.joining("\n"));

        // 4️⃣ Prompt cho Gemini
        String prompt = """
                Bạn là trợ lý của hệ thống đặt vé xe.
                Hãy sử dụng thông tin sau (nếu liên quan) để trả lời câu hỏi.

                DỮ LIỆU LIÊN QUAN:
                %s

                CÂU HỎI NGƯỜI DÙNG:
                %s
                """.formatted(context, userPrompt);

        // 5️⃣ Gọi Gemini
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
