# Hướng dẫn sử dụng API Province (Frontend)

**Base URL:** `/api/v1/provinces`

---

## 1. Lấy danh sách tỉnh/thành

- **Endpoint:** `GET /`
- **Response:**

```json
{
  "code": 1000,
  "message": "List of provinces fetched successfully",
  "result": [
    {
      "id": 1,
      "name": "Hà Nội",
      "description": "Thủ đô",
      "region": "BAC",
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "name": "Đà Nẵng",
      "description": "Thành phố biển",
      "region": "TRUNG",
      "status": "ACTIVE"
    },
    {
      "id": 3,
      "name": "TP. Hồ Chí Minh",
      "description": "Đô thị lớn nhất",
      "region": "NAM",
      "status": "ACTIVE"
    }
  ]
}
```

---

## 2. Lấy chi tiết tỉnh/thành theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Province fetched successfully",
  "result": {
    "id": 1,
    "name": "Hà Nội",
    "description": "Thủ đô",
    "region": "BAC",
    "status": "ACTIVE"
  }
}
```

---

## 3. Tạo tỉnh/thành mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "name": "Hà Nội",
  "description": "Thủ đô",
  "region": "BAC"
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Province created successfully",
  "result": {
    "id": 1,
    "name": "Hà Nội",
    "description": "Thủ đô",
    "region": "BAC",
    "status": "ACTIVE"
  }
}
```

---

## 4. Cập nhật tỉnh/thành (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới
- **Response:**

```json
{
  "code": 1002,
  "message": "Province updated successfully",
  "result": {
    "id": 1,
    "name": "Hà Nội",
    "description": "Thủ đô",
    "region": "BAC",
    "status": "ACTIVE"
  }
}
```

---

## 5. Xóa tỉnh/thành (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Province deleted successfully"
}
```

---

## Format chung của response

```json
{
  "code": number,
  "message": "string",
  "result": ... // tuỳ API
}
```

**Giải thích các trường:**

- `name`: Tên tỉnh/thành
- `description`: Mô tả tỉnh/thành
- `region`: Vùng miền (Enum: `BAC` - Bắc, `TRUNG` - Trung, `NAM` - Nam)
- `status`: Trạng thái (Enum: `ACTIVE`, `DELETED`)

**Lưu ý:**

- Các API trả về theo format trên.
- Các lỗi sẽ trả về code khác và message tương ứng.
- Các API tạo/sửa/xóa chỉ dành cho ADMIN (cần accessToken hợp lệ).
