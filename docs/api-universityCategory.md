# Hướng dẫn sử dụng API University Category (Frontend)

**Base URL:** `/api/v1/university-categories`

---

## 1. Lấy danh sách loại trường (phân trang)

- **Endpoint:** `GET /paginated`
- **Query params:**
  - `page`: số trang (mặc định 0)
  - `size`: số lượng mỗi trang (mặc định 10)
- **Response:**

```json
{
  "code": 1000,
  "message": "Categories fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 5,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "name": "Công lập",
        "description": "Trường công lập ...",
        "status": "ACTIVE",
        "createdAt": "2024-01-01T00:00:00Z",
        "createdBy": "admin",
        "updatedAt": "2024-01-02T00:00:00Z",
        "updatedBy": "admin"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết loại trường theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Category fetched successfully",
  "result": {
    "id": 1,
    "name": "Công lập",
    "description": "Trường công lập ...",
    "status": "ACTIVE",
    "createdAt": "2024-01-01T00:00:00Z",
    "createdBy": "admin",
    "updatedAt": "2024-01-02T00:00:00Z",
    "updatedBy": "admin"
  }
}
```

---

## 3. Tạo loại trường mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "name": "Công lập",
  "description": "Trường công lập ..."
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Category created successfully",
  "result": {
    /* UniversityCategoryResponse */
  }
}
```

---

## 4. Cập nhật loại trường (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới
- **Response:**

```json
{
  "code": 1002,
  "message": "Category updated successfully",
  "result": {
    /* UniversityCategoryResponse */
  }
}
```

---

## 5. Xóa loại trường (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Category deleted successfully"
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
