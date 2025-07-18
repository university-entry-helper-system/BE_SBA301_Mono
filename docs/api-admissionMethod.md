# Hướng dẫn sử dụng API Admission Method (Frontend)

**Base URL:** `/api/v1/admission-methods`

---

## 1. Lấy danh sách phương thức tuyển sinh (có tìm kiếm, phân trang, sắp xếp)

- **Endpoint:** `GET /`
- **Query params:**
  - `search`: Tìm kiếm theo tên phương thức (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `name,asc` hoặc `id,desc` (tùy chọn)
- **Response:**

```json
{
  "code": 1000,
  "message": "List of admission methods fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 3,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "name": "Xét tuyển học bạ",
        "description": "Phương thức xét tuyển dựa trên học bạ THPT",
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "name": "Xét tuyển điểm thi THPT",
        "description": "Phương thức xét tuyển dựa trên điểm thi THPT",
        "status": "ACTIVE"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết phương thức tuyển sinh theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Admission method fetched successfully",
  "result": {
    "id": 1,
    "name": "Xét tuyển học bạ",
    "description": "Phương thức xét tuyển dựa trên học bạ THPT",
    "status": "ACTIVE"
  }
}
```

---

## 3. Tạo phương thức tuyển sinh mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "name": "Xét tuyển học bạ",
  "description": "Phương thức xét tuyển dựa trên học bạ THPT"
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Admission method created successfully",
  "result": {
    "id": 1,
    "name": "Xét tuyển học bạ",
    "description": "Phương thức xét tuyển dựa trên học bạ THPT",
    "status": "ACTIVE"
  }
}
```

---

## 4. Cập nhật phương thức tuyển sinh (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới, **có thể thêm trường `status`**

```json
{
  "name": "Xét tuyển học bạ",
  "description": "Phương thức xét tuyển dựa trên học bạ THPT",
  "status": "ACTIVE"
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "Admission method updated successfully",
  "result": {
    "id": 1,
    "name": "Xét tuyển học bạ",
    "description": "Phương thức xét tuyển dựa trên học bạ THPT",
    "status": "ACTIVE"
  }
}
```

---

## 5. Cập nhật trạng thái phương thức tuyển sinh (ADMIN)

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
  "message": "Admission method status updated successfully",
  "result": {
    "id": 1,
    "name": "Xét tuyển học bạ",
    "status": "ACTIVE"
  }
}
```

---

## 6. Xóa phương thức tuyển sinh (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Admission method deleted successfully"
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

- `name`: Tên phương thức tuyển sinh
- `description`: Mô tả phương thức tuyển sinh
- `status`: Trạng thái (Enum: `ACTIVE`, `DELETED`)

**Lưu ý:**

- Các API trả về theo format trên.
- Các lỗi sẽ trả về code khác và message tương ứng.
- Các API tạo/sửa/xóa/chỉnh trạng thái chỉ dành cho ADMIN (cần accessToken hợp lệ).
- FE phải tự xử lý hiển thị trạng thái phương thức tuyển sinh.
