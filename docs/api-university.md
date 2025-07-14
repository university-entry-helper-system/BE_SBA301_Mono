# Hướng dẫn sử dụng API University (Frontend)

**Base URL:** `/api/v1/universities`

---

## 1. Lấy danh sách trường (phân trang)

- **Endpoint:** `GET /`
- **Query params:**
  - `page`: số trang (mặc định 0)
  - `size`: số lượng mỗi trang (mặc định 10)
- **Response:**

```json
{
  "code": 1000,
  "message": "Universities fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "items": [
      {
        "id": 1,
        "categoryId": 2,
        "name": "Đại học A",
        "shortName": "DHA",
        "logoUrl": "http://...",
        "foundingYear": 2000,
        "province": { "id": 1, "name": "Hà Nội", "region": "North" },
        "address": "123 Đường ABC",
        "email": "info@dha.edu.vn",
        "phone": "0123456789",
        "website": "https://dha.edu.vn",
        "description": "Trường đại học A ...",
        "status": "ACTIVE",
        "createdAt": "2024-05-01T10:00:00Z",
        "createdBy": "admin",
        "updatedAt": "2024-05-01T11:00:00Z",
        "updatedBy": "admin"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết trường theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "University fetched successfully",
  "result": {
    "id": 1,
    "category": {
      "id": 2,
      "name": "Công lập",
      "description": "Trường công lập ...",
      "status": "ACTIVE",
      "createdAt": "2024-01-01T00:00:00Z",
      "createdBy": "admin",
      "updatedAt": "2024-01-02T00:00:00Z",
      "updatedBy": "admin"
    },
    "name": "Đại học A",
    "shortName": "DHA",
    "logoUrl": "http://...",
    "foundingYear": 2000,
    "province": { "id": 1, "name": "Hà Nội", "region": "North" },
    "address": "123 Đường ABC",
    "email": "info@dha.edu.vn",
    "phone": "0123456789",
    "website": "https://dha.edu.vn",
    "description": "Trường đại học A ...",
    "status": "ACTIVE",
    "createdAt": "2024-05-01T10:00:00Z",
    "createdBy": "admin",
    "updatedAt": "2024-05-01T11:00:00Z",
    "updatedBy": "admin"
  }
}
```

---

## 3. Tạo trường mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "categoryId": 2,
  "name": "Đại học A",
  "shortName": "DHA",
  "logoUrl": "http://...",
  "foundingYear": 2000,
  "provinceId": 1,
  "type": "Công lập",
  "address": "123 Đường ABC",
  "email": "info@dha.edu.vn",
  "phone": "0123456789",
  "website": "https://dha.edu.vn",
  "description": "Trường đại học A ...",
  "admissionMethodIds": [1, 2, 3]
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "University created successfully",
  "result": {
    /* UniversityResponse */
  }
}
```

---

## 4. Cập nhật trường (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới
- **Response:**

```json
{
  "code": 1002,
  "message": "University updated successfully",
  "result": {
    /* UniversityResponse */
  }
}
```

---

## 5. Xóa trường (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "University deleted successfully"
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

**Lưu ý:**

- Các API trả về theo format trên.
- Các lỗi sẽ trả về code khác và message tương ứng.
- Các API tạo/sửa/xóa chỉ dành cho ADMIN (cần accessToken hợp lệ).
- Trường `status` có thể là `ACTIVE` hoặc `DELETED`.
- Trường `province` là object chi tiết tỉnh/thành.
- Trường `category` là object chi tiết loại trường (ở API chi tiết).
