# Demo API cho việc tạo Trip với nhiều xe

## 1. Tạo Trip với danh sách carIds đơn giản

```http
POST /admin/trips
Content-Type: application/json

{
  "routeId": "123e4567-e89b-12d3-a456-426614174000",
  "departureTime": "2024-01-01T10:00:00",
  "price": 250000,
  "availableSeats": 45,
  "carIds": [
    "123e4567-e89b-12d3-a456-426614174001",
    "123e4567-e89b-12d3-a456-426614174002",
    "123e4567-e89b-12d3-a456-426614174003"
  ]
}
```

## 2. Tạo Trip với thông tin xe chi tiết

```http
POST /admin/trips
Content-Type: application/json

{
  "routeId": "123e4567-e89b-12d3-a456-426614174000",
  "departureTime": "2024-01-01T10:00:00",
  "price": 250000,
  "availableSeats": 45,
  "tripCars": [
    {
      "carId": "123e4567-e89b-12d3-a456-426614174001",
      "price": 250000,
      "availableSeats": 16
    },
    {
      "carId": "123e4567-e89b-12d3-a456-426614174002",
      "price": 280000,
      "availableSeats": 29
    },
    {
      "carId": "123e4567-e89b-12d3-a456-426614174003"
      // Sử dụng giá và số ghế mặc định từ trip
    }
  ]
}
```

## 3. Cập nhật Trip và thay đổi danh sách xe

```http
PUT /admin/trips/{tripId}
Content-Type: application/json

{
  "routeId": "123e4567-e89b-12d3-a456-426614174000",
  "departureTime": "2024-01-01T11:00:00",
  "price": 270000,
  "availableSeats": 50,
  "carIds": [
    "123e4567-e89b-12d3-a456-426614174001",
    "123e4567-e89b-12d3-a456-426614174004" // Thêm xe mới, bỏ xe cũ
  ]
}
```

## 4. Lấy danh sách xe của một trip

```http
GET /admin/trip-cars/trip/{tripId}
```

## Cách hoạt động:

1. **Khi tạo Trip**: 
   - Tạo bản ghi Trip trước
   - Với mỗi xe được chọn, tạo bản ghi TripCar tương ứng
   - Lưu vào bảng `tripCars` với `tripId` và `carId`

2. **Khi cập nhật Trip**:
   - Cập nhật thông tin Trip
   - Nếu có thay đổi danh sách xe:
     - Xóa mềm tất cả TripCar cũ của trip này
     - Tạo lại TripCar mới theo danh sách mới

3. **Hai cách chọn xe**:
   - **Cách 1**: `carIds` - Danh sách ID xe đơn giản, sử dụng giá và số ghế từ trip
   - **Cách 2**: `tripCars` - Thông tin chi tiết, có thể set giá và số ghế riêng cho từng xe

4. **API endpoints có sẵn**:
   - `GET /admin/trip-cars/trip/{tripId}` - Lấy danh sách xe của trip
   - `GET /admin/trip-cars` - Lấy tất cả TripCar
   - `POST /admin/trip-cars` - Tạo TripCar riêng lẻ
   - `PUT /admin/trip-cars/{id}` - Cập nhật TripCar riêng lẻ