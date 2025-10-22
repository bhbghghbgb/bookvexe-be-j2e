# Hướng Dẫn Sử Dụng AI Chatbot

## Tổng quan
Ứng dụng đã được cấu hình để sử dụng **Ollama** - một AI chatbot miễn phí chạy local trên máy tính của bạn.

## Các bước cài đặt và sử dụng

### 1. Cài đặt Ollama

1. Tải Ollama từ: https://ollama.ai/
2. Cài đặt Ollama trên Windows
3. Sau khi cài đặt, Ollama sẽ tự động chạy như một service

### 2. Tải các mô hình AI

Mở Command Prompt hoặc PowerShell và chạy các lệnh sau:

```bash
# Tải mô hình chat (gemma2)
ollama pull gemma2

# Tải mô hình embedding cho vector search
ollama pull nomic-embed-text
```

### 3. Kiểm tra Ollama đang chạy

Chạy lệnh sau để kiểm tra:

```bash
ollama list
```

Bạn sẽ thấy danh sách các mô hình đã tải:
```
NAME                       ID              SIZE      MODIFIED
nomic-embed-text:latest    0a109f422b47    274 MB    4 seconds ago
gemma2:latest              ff02c3702f32    5.4 GB    4 minutes ago
```

### 4. Khởi động ứng dụng

Ollama server sẽ tự động khởi động trên port 11434. Nếu không, chạy:

```bash
ollama serve
```

Sau đó, khởi động ứng dụng Spring Boot của bạn:

```bash
mvnw.cmd spring-boot:run
```

Hoặc chạy từ IDE.

### 5. Sử dụng API Chatbot

Ứng dụng đã có các endpoint chat AI (kiểm tra trong `ChatController`).

#### Endpoint chính:
- **POST** `/api/chat/ask` - Gửi câu hỏi đến AI chatbot

#### Ví dụ request:
```json
POST http://localhost:5181/api/chat/ask
Content-Type: application/json

{
  "message": "Làm thế nào để đặt vé xe?"
}
```

### 6. Tính năng

- ✅ **Chat AI miễn phí**: Sử dụng Ollama, không tốn phí như OpenAI
- ✅ **Chạy local**: Dữ liệu được xử lý trên máy của bạn, bảo mật cao
- ✅ **Tích hợp database**: AI có thể trả lời dựa trên dữ liệu trong database của bạn
- ✅ **Embedding model**: Hỗ trợ vector search và semantic search

## Cấu hình

Cấu hình AI được đặt trong `application.yml`:

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434  # URL của Ollama server
      chat:
        options:
          model: gemma2  # Mô hình chat
          temperature: 0.7  # Độ "sáng tạo" của câu trả lời (0-1)
      embedding:
        options:
          model: nomic-embed-text  # Mô hình embedding
```

## Các mô hình AI khác có thể dùng

Ngoài `gemma2`, bạn có thể thử các mô hình khác:

```bash
# Mô hình nhẹ, nhanh
ollama pull llama3.2

# Mô hình mạnh hơn
ollama pull mistral

# Mô hình code
ollama pull codellama
```

Sau khi tải, cập nhật `model` trong `application.yml`.

## Lưu ý

1. **Yêu cầu hệ thống**: 
   - RAM tối thiểu: 8GB (khuyến nghị 16GB)
   - Dung lượng ổ cứng: ~5-10GB cho mỗi mô hình

2. **Performance**: 
   - Mô hình chạy local nên tốc độ phụ thuộc vào cấu hình máy
   - Lần đầu tiên load mô hình sẽ chậm, sau đó sẽ nhanh hơn

3. **Profile**: 
   - AI chỉ hoạt động khi profile `ai-enabled` được active
   - Kiểm tra trong `application.yml`: `spring.profiles.active: dev, ai-enabled`

## Troubleshooting

### Lỗi: "ChatClient bean not found"
- Kiểm tra Ollama đang chạy: `ollama list`
- Kiểm tra profile: Đảm bảo `ai-enabled` được kích hoạt
- Restart ứng dụng

### Lỗi: "Connection refused to localhost:11434"
- Khởi động Ollama: `ollama serve`
- Kiểm tra port 11434 có đang được sử dụng

### Mô hình quá chậm
- Thử mô hình nhẹ hơn: `ollama pull llama3.2:1b`
- Giảm `temperature` để câu trả lời nhanh hơn

## Liên kết hữu ích

- Ollama Official: https://ollama.ai/
- Danh sách mô hình: https://ollama.ai/library
- Spring AI Documentation: https://docs.spring.io/spring-ai/reference/

