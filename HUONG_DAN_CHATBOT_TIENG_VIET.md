# Hướng Dẫn Sử Dụng AI Chatbot Tiếng Việt

## 🎯 Tính năng đã cấu hình

AI Chatbot của bạn đã được cấu hình để:

✅ **Trả lời bằng tiếng Việt tự nhiên**  
✅ **Tích hợp dữ liệu thực từ database** (routes, trips, bookings)  
✅ **Hiểu ngữ cảnh Việt Nam** (địa điểm, giá VNĐ, định dạng ngày giờ VN)  
✅ **Tìm kiếm thông minh** (Vector search với embeddings)  
✅ **Trả lời dựa trên dữ liệu thực tế** của hệ thống  

---

## 🚀 Các API Endpoints

### 1. **Hỏi Chatbot AI** (Chính)
```http
POST http://localhost:5181/api/ai/chat
Content-Type: application/json

{
  "prompt": "Có chuyến xe nào từ Hà Nội đến Đà Nẵng không?"
}
```

**Response:**
```json
{
  "success": true,
  "answer": "Có, hệ thống có tuyến Hà Nội → Đà Nẵng với khoảng cách 750km...",
  "timestamp": "2025-10-23T00:30:00",
  "error": null
}
```

### 2. **Hỏi đáp nhanh** (Tiếng Việt thuần túy)
```http
POST http://localhost:5181/api/ai/hoi-dap
Content-Type: application/json

{
  "cauHoi": "Giá vé từ Sài Gòn đi Nha Trang bao nhiêu?"
}
```

### 3. **Xem thông tin tuyến đường**
```http
GET http://localhost:5181/api/ai/routes-info
```

Trả về danh sách tất cả các tuyến đường trong hệ thống.

### 4. **Xem chuyến xe sắp khởi hành**
```http
GET http://localhost:5181/api/ai/trips-info
```

### 5. **Xem thông tin hệ thống**
```http
GET http://localhost:5181/api/ai/system-info
```

### 6. **Thêm kiến thức cho AI**
```http
POST http://localhost:5181/api/ai/knowledge/add
Content-Type: application/json

{
  "title": "Chính sách hoàn vé",
  "content": "Khách hàng có thể hoàn vé trước 24h với phí 10%, trước 12h với phí 20%...",
  "category": "policy"
}
```

---

## 💡 Ví dụ câu hỏi mà AI có thể trả lời

### Về tuyến đường:
- "Có tuyến xe nào từ Hà Nội đến Đà Nẵng không?"
- "Cho tôi biết các tuyến đường hiện có"
- "Từ Sài Gòn đi Nha Trang mất bao lâu?"
- "Khoảng cách từ Hà Nội đến Hải Phòng là bao nhiêu?"

### Về chuyến xe:
- "Có chuyến xe nào khởi hành hôm nay không?"
- "Lịch trình các chuyến xe sắp tới"
- "Chuyến xe nào còn chỗ trống?"

### Về giá vé:
- "Giá vé từ Hà Nội đến Đà Nẵng bao nhiêu?"
- "Cho tôi biết giá các tuyến đường"

### Về hệ thống:
- "Hệ thống có bao nhiêu tuyến đường?"
- "Làm thế nào để đặt vé?"
- "Tôi có thể thanh toán bằng cách nào?"

### Câu hỏi chung:
- "Hướng dẫn đặt vé xe"
- "Chính sách hoàn vé như thế nào?"
- "Tôi cần liên hệ support ở đâu?"

---

## 🎨 Cách AI trả lời

### 1. **Phân tích câu hỏi**
AI sẽ phân tích câu hỏi để hiểu người dùng muốn gì:
- Hỏi về tuyến đường → Lấy dữ liệu từ bảng `routes`
- Hỏi về chuyến xe → Lấy dữ liệu từ bảng `trips`
- Hỏi về giá → Lấy thông tin giá từ database

### 2. **Lấy dữ liệu từ Database**
```java
// Ví dụ: Lấy tất cả tuyến đường
List<RouteDbModel> routes = routeRepository.findAll();

// Lấy các chuyến xe sắp khởi hành
List<TripDbModel> upcomingTrips = tripRepository.findAll()
    .filter(trip -> trip.getDepartureTime().isAfter(now));
```

### 3. **Tạo context tiếng Việt**
```
DANH SÁCH CÁC TUYẾN ĐƯỜNG:
- Tuyến: Hà Nội → Đà Nẵng
  Khoảng cách: 750 km
  Thời gian dự kiến: 720 phút
  
- Tuyến: Sài Gòn → Nha Trang
  Khoảng cách: 450 km
  Thời gian dự kiến: 480 phút
```

### 4. **Gửi prompt đến Ollama**
```
BẠN LÀ TRỢ LÝ ẢO THÔNG MINH CỦA HỆ THỐNG ĐẶT VÉ XE TẠI VIỆT NAM.

NHIỆM VỤ:
- Trả lời bằng tiếng Việt tự nhiên, lịch sự và dễ hiểu
- Sử dụng thông tin từ dữ liệu thực tế của hệ thống
- Định dạng giá tiền theo kiểu Việt Nam (VD: 150.000 VNĐ)

DỮ LIỆU TỪ HỆ THỐNG:
[context từ database]

CÂU HỎI CỦA KHÁCH HÀNG:
"Có tuyến xe nào từ Hà Nội đến Đà Nẵng không?"
```

### 5. **Trả về câu trả lời**
AI sẽ trả lời bằng tiếng Việt tự nhiên, dựa trên dữ liệu thực:

```
Có ạ! Hệ thống hiện có tuyến xe từ Hà Nội đến Đà Nẵng với thông tin như sau:

- Khoảng cách: 750 km
- Thời gian di chuyển: khoảng 12 giờ
- Giá vé trung bình: 300.000 - 500.000 VNĐ tùy loại xe

Hiện có 3 chuyến xe sắp khởi hành trong tuần này. 
Anh/chị có muốn xem lịch trình chi tiết không?
```

---

## 🔧 Cấu hình trong application.yml

```yaml
spring:
  profiles:
    active: dev, ai-enabled  # Bật AI
    
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: gemma2          # Mô hình AI tiếng Việt tốt
          temperature: 0.7       # Độ sáng tạo (0-1)
      embedding:
        options:
          model: nomic-embed-text  # Mô hình embedding
```

---

## 📊 Vector Search với Embeddings

Hệ thống sử dụng **embeddings** để tìm kiếm thông tin liên quan:

### 1. **Thêm kiến thức**
```java
// Khi thêm kiến thức mới
float[] vector = embeddingService.embed("Nội dung kiến thức");
knowledge.setEmbedding(vector);
knowledgeRepository.save(knowledge);
```

### 2. **Tìm kiếm**
```java
// Khi có câu hỏi
float[] queryVector = embeddingService.embed("Câu hỏi của user");
List<Knowledge> similar = knowledgeRepository.findSimilar(queryVector, 5);
```

### 3. **Lợi ích**
- ✅ Tìm kiếm theo **ý nghĩa**, không chỉ từ khóa
- ✅ Hiểu được câu hỏi tương tự
- ✅ Trả lời chính xác hơn

---

## 🎯 Ví dụ thực tế

### Scenario 1: Hỏi về tuyến đường
**User:** "Từ Hà Nội đi Đà Nẵng mất bao lâu?"

**AI xử lý:**
1. Phát hiện pattern "từ X đến Y"
2. Gọi `findRoutesByLocations("Hà Nội", "Đà Nẵng")`
3. Lấy dữ liệu từ database
4. Format thành câu trả lời tiếng Việt

**Response:**
```
Tuyến Hà Nội → Đà Nẵng có khoảng cách 750km, 
thời gian di chuyển dự kiến là 12 giờ (720 phút).

Hiện có giá vé từ 300.000 đến 500.000 VNĐ tùy loại xe.
```

### Scenario 2: Hỏi về chuyến xe
**User:** "Có chuyến nào khởi hành hôm nay không?"

**AI xử lý:**
1. Phát hiện từ khóa "chuyến", "khởi hành"
2. Gọi `getUpcomingTripsContext()`
3. Lọc các chuyến trong ngày hôm nay
4. Trả về danh sách

**Response:**
```
Hôm nay có 5 chuyến xe khởi hành:

1. Hà Nội → Hải Phòng - 08:00 - Còn 15 chỗ
2. Sài Gòn → Nha Trang - 09:30 - Còn 8 chỗ
3. Đà Nẵng → Huế - 14:00 - Còn 20 chỗ
...

Anh/chị muốn đặt chuyến nào ạ?
```

---

## 🧪 Testing

### Test với Postman hoặc cURL:

```bash
# Test hỏi chatbot
curl -X POST http://localhost:5181/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Có tuyến xe nào từ Hà Nội đến Đà Nẵng không?"}'

# Test xem routes
curl http://localhost:5181/api/ai/routes-info

# Test xem trips
curl http://localhost:5181/api/ai/trips-info

# Test thêm knowledge
curl -X POST http://localhost:5181/api/ai/knowledge/add \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Chính sách hoàn vé",
    "content": "Khách hàng có thể hoàn vé trước 24h với phí 10%",
    "category": "policy"
  }'
```

---

## 🔐 Swagger UI

Truy cập Swagger để test API trực quan:
```
http://localhost:5181/swagger-ui.html
```

Tìm section **"AI Chatbot"** để test các endpoint.

---

## 📝 Lưu ý quan trọng

1. **Ollama phải đang chạy**
   ```bash
   # Kiểm tra
   ollama list
   
   # Nếu không chạy
   ollama serve
   ```

2. **Profile ai-enabled phải được bật**
   ```yaml
   spring:
     profiles:
       active: dev, ai-enabled
   ```

3. **Database phải có dữ liệu**
   - Cần có dữ liệu trong bảng `routes` và `trips`
   - Nếu chưa có, AI sẽ trả lời "Chưa có dữ liệu"

4. **Mô hình gemma2 hỗ trợ tiếng Việt tốt**
   - Nếu muốn thử mô hình khác: `llama3.2`, `mistral`

---

## 🚀 Tips nâng cao

### 1. Thêm nhiều kiến thức cho AI
```bash
POST /api/ai/knowledge/add
{
  "title": "Quy định hành lý",
  "content": "Mỗi khách được mang 2 túi xách tay, không quá 20kg",
  "category": "rules"
}
```

### 2. Train AI với FAQ
Thêm các câu hỏi thường gặp vào knowledge base để AI trả lời tốt hơn.

### 3. Tùy chỉnh temperature
- `0.3` - Trả lời chính xác, ít sáng tạo
- `0.7` - Cân bằng (mặc định)
- `0.9` - Sáng tạo, đa dạng hơn

---

## 🎉 Kết luận

Hệ thống AI Chatbot của bạn đã sẵn sàng:
- ✅ Trả lời tiếng Việt tự nhiên
- ✅ Tích hợp dữ liệu thực từ database
- ✅ Hiểu ngữ cảnh Việt Nam
- ✅ Miễn phí 100% với Ollama
- ✅ Chạy local, bảo mật

Hãy thử hỏi AI các câu hỏi về hệ thống đặt vé xe của bạn! 🚌💬

