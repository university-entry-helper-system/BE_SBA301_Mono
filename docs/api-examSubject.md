# Hướng dẫn sử dụng API Exam Subject (Frontend)

**Base URL:** `/api/v1/exam-subjects`

---

## 1. Lấy danh sách môn thi (có tìm kiếm, phân trang, sắp xếp)

- **Endpoint:** `GET /`
- **Query params:**
  - `search`: Tìm kiếm theo tên môn thi (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `name,asc` hoặc `id,desc` (tùy chọn)
- **Response:**

```json
{
  "code": 1000,
  "message": "List of exam subjects fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 3,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "name": "Toán học",
        "shortName": "Toán",
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "name": "Vật lý",
        "shortName": "Lý",
        "status": "ACTIVE"
      },
      {
        "id": 3,
        "name": "Hóa học",
        "shortName": "Hóa",
        "status": "DELETED"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết môn thi theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Exam subject fetched successfully",
  "result": {
    "id": 1,
    "name": "Toán học",
    "shortName": "Toán",
    "status": "ACTIVE"
  }
}
```

---

## 3. Tạo môn thi mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "name": "Toán học",
  "shortName": "Toán"
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Exam subject created successfully",
  "result": {
    "id": 1,
    "name": "Toán học",
    "shortName": "Toán",
    "status": "ACTIVE"
  }
}
```

---

## 4. Cập nhật môn thi (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới, **có thể thêm trường `status`**

```json
{
  "name": "Toán học",
  "shortName": "Toán",
  "status": "ACTIVE"
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "Exam subject updated successfully",
  "result": {
    "id": 1,
    "name": "Toán học",
    "shortName": "Toán",
    "status": "ACTIVE"
  }
}
```

---

## 5. Cập nhật trạng thái môn thi (ADMIN)

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
  "message": "Exam subject status updated successfully",
  "result": {
    "id": 1,
    "name": "Toán học",
    "status": "ACTIVE"
  }
}
```

---

## 6. Xóa môn thi (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Exam subject deleted successfully"
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

- `name`: Tên môn thi
- `shortName`: Tên viết tắt của môn thi
- `status`: Trạng thái (Enum: `ACTIVE`, `DELETED`)

**Lưu ý:**

- Các API trả về theo format trên.
- Các lỗi sẽ trả về code khác và message tương ứng.
- Các API tạo/sửa/xóa/chỉnh trạng thái chỉ dành cho ADMIN (cần accessToken hợp lệ).
- FE phải tự xử lý hiển thị trạng thái môn thi.
