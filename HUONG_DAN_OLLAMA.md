# 🚀 Hướng Dẫn Sử Dụng AI với Ollama (Miễn Phí 100%)

## ✅ Đã Chuyển Sang Ollama

Thay vì sử dụng Google Vertex AI (tốn phí), giờ ứng dụng sử dụng **Ollama** - AI chạy local, hoàn toàn miễn phí!

---

## 📦 Cài Đặt Ollama

### **Bước 1: Download & Install Ollama**

1. Truy cập: https://ollama.ai/download
2. Tải bản cho Windows
3. Cài đặt (như cài phần mềm bình thường)
4. Ollama sẽ tự động chạy ở background

### **Bước 2: Pull Mô Hình AI**

Mở **Command Prompt** hoặc **PowerShell** và chạy:

```bash
# Pull mô hình chat (chọn 1 trong các mô hình sau)
ollama pull llama3.2        # Llama 3.2 (4.3GB) - Khuyến nghị
ollama pull mistral         # Mistral (4.1GB) - Nhanh
ollama pull gemma2          # Gemma 2 (5.4GB) - Tốt cho tiếng Việt

# Pull mô hình embedding (cho vector search)
ollama pull nomic-embed-text  # 274MB - Dùng cho tìm kiếm
```

**Lưu ý**: Lần đầu pull sẽ mất 5-10 phút tùy tốc độ mạng.

### **Bước 3: Kiểm Tra Ollama Đang Chạy**

```bash
ollama list  # Xem các mô hình đã cài
```

Kết quả:
```
NAME              ID              SIZE      MODIFIED
llama3.2:latest   f5f50259e1bb    4.3 GB    2 minutes ago
nomic-embed-text  0a109f422b47    274 MB    1 minute ago
```

---

## ⚙️ Cấu Hình Đã Thay Đổi

### **1. pom.xml**
```xml
<!-- ❌ Đã xóa -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-vertex-ai-gemini-spring-boot-starter</artifactId>
</dependency>

<!-- ✅ Thay bằng -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-ollama-spring-boot-starter</artifactId>
</dependency>
```

### **2. application.yml**
```yaml
spring:
  profiles:
    active: dev,ai-enabled  # ✅ AI được kích hoạt
  ai:
    ollama:
      base-url: http://localhost:11434  # Ollama server
      chat:
        options:
          model: llama3.2  # Mô hình chat
          temperature: 0.7
      embedding:
        options:
          model: nomic-embed-text  # Mô hình embedding
```

### **3. Xóa File Google Cloud Credentials**

File `gen-lang-client-0404640351-*.json` **không cần nữa**, có thể xóa!

---

## 🎯 Sử Dụng AI API

### **1. Chat với AI**

**POST** `http://localhost:5181/api/ai/chat`

Request:
```json
{
  "prompt": "Làm thế nào để đặt vé xe?"
}
```

Response:
```json
{
  "answer": "Để đặt vé xe, bạn cần thực hiện các bước sau:\n1. Chọn tuyến xe bạn muốn đi\n2. Chọn ngày giờ xu���t phát\n3. Chọn chỗ ngồi\n4. Thanh toán\n..."
}
```

### **2. Thêm Knowledge vào Database**

**POST** `http://localhost:5181/api/ai/add`

Request:
```json
{
  "title": "Quy trình đặt vé",
  "content": "Bước 1: Chọn tuyến xe. Bước 2: Chọn ghế. Bước 3: Thanh toán bằng VNPay hoặc Momo."
}
```

Response:
```json
{
  "id": 1,
  "title": "Quy trình đặt vé",
  "content": "Bước 1: Chọn tuyến xe...",
  "embedding": [0.123, 0.456, ...],
  "createdAt": "2025-10-22T23:00:00"
}
```

### **3. AI Sẽ Trả Lời Dựa Trên Database**

Cách hoạt động:
1. User hỏi: "Làm sao để thanh toán?"
2. System tìm kiếm trong database các thông tin liên quan (dùng vector search)
3. AI đọc thông tin đó và trả lời câu hỏi

**Ví dụ**:
```bash
# Thêm knowledge
curl -X POST http://localhost:5181/api/ai/add \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Thanh toán",
    "content": "Hệ thống hỗ trợ thanh toán qua VNPay, Momo và chuyển khoản ngân hàng"
  }'

# Hỏi AI
curl -X POST http://localhost:5181/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "Tôi có thể thanh toán bằng cách nào?"
  }'

# AI sẽ trả lời:
# "Bạn có thể thanh toán qua VNPay, Momo hoặc chuyển khoản ngân hàng."
```

---

## 🔧 Troubleshooting

### **Lỗi: "Connection refused" hoặc "Ollama not found"**

**Nguyên nhân**: Ollama chưa chạy hoặc chưa cài đặt

**Giải pháp**:
```bash
# Kiểm tra Ollama có chạy không
curl http://localhost:11434/api/tags

# Nếu lỗi, khởi động Ollama
ollama serve
```

### **Lỗi: "Model not found"**

**Nguyên nhân**: Chưa pull mô hình

**Giải pháp**:
```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```

### **Lỗi: "ChatClient bean not found"**

**Nguyên nhân**: Profile `ai-enabled` chưa được kích hoạt

**Giải pháp**: Kiểm tra `application.yml`:
```yaml
spring:
  profiles:
    active: dev,ai-enabled  # ← Phải có 'ai-enabled'
```

---

## 🆚 So Sánh: Vertex AI vs Ollama

| Tiêu chí | Vertex AI (Google) | Ollama (Local) |
|----------|-------------------|----------------|
| **Chi phí** | $$$ Tính phí theo request | ✅ Miễn phí 100% |
| **Tốc độ** | Nhanh (cloud) | Tùy máy tính |
| **Cài đặt** | Phức tạp (cần Google Cloud) | ✅ Đơn giản |
| **Privacy** | Dữ liệu gửi lên cloud | ✅ Dữ liệu ở local |
| **Internet** | Cần internet | ✅ Không cần (sau khi pull) |
| **Mô hình** | Gemini | Llama, Mistral, Gemma |

---

## 📊 Các Mô Hình AI Khuyến Nghị

### **Chat Models**

| Mô hình | Kích thước | Ưu điểm |
|---------|-----------|---------|
| `llama3.2` | 4.3 GB | Cân bằng tốt, khuyến nghị |
| `mistral` | 4.1 GB | Nhanh, code tốt |
| `gemma2` | 5.4 GB | Tốt cho tiếng Việt |
| `qwen2.5` | 4.7 GB | Tốt cho châu Á |

### **Embedding Models**

| Mô hình | Kích thước | Ưu điểm |
|---------|-----------|---------|
| `nomic-embed-text` | 274 MB | Nhanh, chất lượng tốt |
| `all-minilm` | 45 MB | Rất nhẹ, nhanh |

### **Đổi Mô Hình**

Trong `application.yml`:
```yaml
spring:
  ai:
    ollama:
      chat:
        options:
          model: mistral  # ← Đổi thành mistral
```

Sau đó pull mô hình:
```bash
ollama pull mistral
```

---

## ✨ Tính Năng

✅ **Chat AI miễn phí 100%**  
✅ **Vector search** - Tìm kiếm thông tin trong database  
✅ **RAG (Retrieval-Augmented Generation)** - AI trả lời dựa trên dữ liệu thực  
✅ **Chạy offline** - Không cần internet (sau khi pull model)  
✅ **Privacy** - Dữ liệu không rời khỏi máy bạn  

---

## 🚀 Bắt Đầu Ngay

1. **Cài Ollama**: https://ollama.ai/download
2. **Pull mô hình**:
   ```bash
   ollama pull llama3.2
   ollama pull nomic-embed-text
   ```
3. **Khởi động ứng dụng**: `mvn spring-boot:run`
4. **Test API**: http://localhost:5181/swagger-ui.html

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề:
1. Kiểm tra Ollama đang chạy: `ollama list`
2. Kiểm tra log ứng dụng
3. Kiểm tra profile `ai-enabled` đã được kích hoạt

**Ollama Documentation**: https://github.com/ollama/ollama

