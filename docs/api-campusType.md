# API: CampusType

**Base URL:** `/api/v1/campus-types`

---

## 1. Lấy danh sách tất cả loại cơ sở

- **Endpoint:** `GET /api/v1/campus-types`
- **Response:**

```json
{
  "code": 1000,
  "message": "Campus types fetched successfully",
  "result": [
    {
      "id": 1,
      "name": "MAIN",
      "description": "Cơ sở chính, nơi đặt trụ sở chính của trường đại học hoặc học viện.",
      "status": "active",
      "createdAt": "2025-07-16T03:08:47.324097Z",
      "createdBy": "admin3",
      "updatedAt": "2025-07-16T03:08:47.324115Z",
      "updatedBy": "admin3"
    },
    {
      "id": 2,
      "name": "BRANCH",
      "description": "Cơ sở phân hiệu.",
      "status": "active",
      "createdAt": "...",
      "createdBy": "...",
      "updatedAt": "...",
      "updatedBy": "..."
    }
  ]
}
```

---

## 2. Lấy chi tiết loại cơ sở theo ID

- **Endpoint:** `GET /api/v1/campus-types/{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Campus type fetched successfully",
  "result": {
    "id": 1,
    "name": "MAIN",
    "description": "Cơ sở chính, nơi đặt trụ sở chính của trường đại học hoặc học viện.",
    "status": "active",
    "createdAt": "2025-07-16T03:08:47.324097Z",
    "createdBy": "admin3",
    "updatedAt": "2025-07-16T03:08:47.324115Z",
    "updatedBy": "admin3"
  }
}
```

---

## 3. Tìm kiếm, phân trang, sắp xếp loại cơ sở

- **Endpoint:** `GET /api/v1/campus-types/search`
- **Query params:**
  - `search`: Tìm kiếm theo tên loại cơ sở (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `id,asc` hoặc `name,desc` (mặc định: `id,asc`)
- **Response:**

```json
{
  "code": 1000,
  "message": "Campus types fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "name": "MAIN",
        "description": "Cơ sở chính, nơi đặt trụ sở chính của trường đại học hoặc học viện.",
        "status": "active",
        "createdAt": "2025-07-16T03:08:47.324097Z",
        "createdBy": "admin3",
        "updatedAt": "2025-07-16T03:08:47.324115Z",
        "updatedBy": "admin3"
      }
      // ... các loại khác
    ]
  }
}
```

---

## 4. Tạo mới loại cơ sở (ADMIN)

- **Endpoint:** `POST /api/v1/campus-types`
- **Request body:**

```json
{
  "name": "TRAINING_CENTER",
  "description": "Trung tâm đào tạo"
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Campus type created successfully",
  "result": {
    "id": 3,
    "name": "TRAINING_CENTER",
    "description": "Trung tâm đào tạo",
    "status": "active",
    "createdAt": "2025-07-16T10:00:00.000Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T10:00:00.000Z",
    "updatedBy": "admin"
  }
}
```

---

## 5. Cập nhật loại cơ sở (ADMIN)

- **Endpoint:** `PUT /api/v1/campus-types/{id}`
- **Request body:**

```json
{
  "name": "RESEARCH_CENTER",
  "description": "Trung tâm nghiên cứu"
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "Campus type updated successfully",
  "result": {
    "id": 3,
    "name": "RESEARCH_CENTER",
    "description": "Trung tâm nghiên cứu",
    "status": "active",
    "createdAt": "2025-07-16T10:00:00.000Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T10:05:00.000Z",
    "updatedBy": "admin"
  }
}
```

---

## 6. Xóa loại cơ sở (ADMIN)

- **Endpoint:** `DELETE /api/v1/campus-types/{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Campus type deleted successfully"
}
```

---

## 7. Cập nhật trạng thái loại cơ sở (ADMIN)

- **Endpoint:** `PATCH /api/v1/campus-types/{id}/status`
- **Request body:**

```json
{
  "status": "INACTIVE"
}
```

- **Response:**

```json
{
  "code": 1004,
  "message": "Campus type status updated successfully",
  "result": {
    "id": 3,
    "name": "RESEARCH_CENTER",
    "description": "Trung tâm nghiên cứu",
    "status": "inactive",
    "createdAt": "2025-07-16T10:00:00.000Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T10:10:00.000Z",
    "updatedBy": "admin"
  }
}
```

---

## 8. Model dữ liệu CampusType

```json
{
  "id": 1,
  "name": "MAIN",
  "description": "Cơ sở chính, nơi đặt trụ sở chính của trường đại học hoặc học viện.",
  "status": "active",
  "createdAt": "2025-07-16T03:08:47.324097Z",
  "createdBy": "admin3",
  "updatedAt": "2025-07-16T03:08:47.324115Z",
  "updatedBy": "admin3"
}
```

---

**Lưu ý:**

- Các API tạo, cập nhật, xóa, cập nhật trạng thái chỉ dành cho ADMIN.
- FE chỉ cần quan tâm các trường: `id`, `name`, `description`, `status` để hiển thị dropdown hoặc filter.
- Trường `status` có thể là: `"active"`, `"inactive"`, `"deleted"` (tùy vào logic backend).
