# Hướng dẫn sử dụng API Major (Frontend)

**Base URL:** `/api/v1/majors`

---

## 1. Lấy danh sách ngành (có tìm kiếm, phân trang, sắp xếp)

- **Endpoint:** `GET /`
- **Query params:**
  - `search`: Tìm kiếm theo tên ngành (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `name,asc` hoặc `code,desc` (tùy chọn)
- **Response:**

```json
{
  "code": 1000,
  "message": "List of majors fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 3,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "code": "7480201",
        "name": "Công nghệ thông tin",
        "description": "Ngành về CNTT",
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "code": "7340101",
        "name": "Quản trị kinh doanh",
        "description": "Ngành về kinh doanh",
        "status": "ACTIVE"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết ngành theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Major fetched successfully",
  "result": {
    "id": 1,
    "code": "7480201",
    "name": "Công nghệ thông tin",
    "description": "Ngành về CNTT",
    "status": "ACTIVE"
  }
}
```

---

## 3. Tạo ngành mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "code": "7480201",
  "name": "Công nghệ thông tin",
  "description": "Ngành về CNTT"
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Major created successfully",
  "result": {
    "id": 1,
    "code": "7480201",
    "name": "Công nghệ thông tin",
    "description": "Ngành về CNTT",
    "status": "ACTIVE"
  }
}
```

---

## 4. Cập nhật ngành (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới, **có thể thêm trường `status`**

```json
{
  "code": "7480201",
  "name": "Công nghệ thông tin",
  "description": "Ngành về CNTT",
  "status": "ACTIVE"
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "Major updated successfully",
  "result": {
    "id": 1,
    "code": "7480201",
    "name": "Công nghệ thông tin",
    "description": "Ngành về CNTT",
    "status": "ACTIVE"
  }
}
```

---

## 5. Cập nhật trạng thái ngành (ADMIN)

- **Endpoint:** `PATCH /{id}/status`
- **Body (JSON):**

```json
{
  "status": "ACTIVE" // hoặc "DELETED"
}
```

- **Response:**

```json
{
  "code": 1004,
  "message": "Major status updated successfully",
  "result": {
    "id": 1,
    "name": "Công nghệ thông tin",
    "status": "ACTIVE"
  }
}
```

---

## 6. Xóa ngành (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Major deleted successfully"
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

- `code`: Mã ngành (theo quy định của Bộ GD&ĐT)
- `name`: Tên ngành
- `description`: Mô tả ngành
- `status`: Trạng thái (Enum: `ACTIVE`, `DELETED`)

**Lưu ý:**

- Các API trả về theo format trên.
- Các lỗi sẽ trả về code khác và message tương ứng.
- Các API tạo/sửa/xóa/chỉnh trạng thái chỉ dành cho ADMIN (cần accessToken hợp lệ).
- FE phải tự xử lý hiển thị trạng thái ngành.
