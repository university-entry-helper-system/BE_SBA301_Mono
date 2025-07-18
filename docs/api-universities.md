# API: University

**Base URL:** `/api/v1/universities`

---

## 1. Lấy danh sách tất cả trường đại học

- **Endpoint:** `GET /api/v1/universities`
- **Response:**

```json
{
  "code": 1000,
  "message": "Universities fetched successfully",
  "result": [
    {
      "id": 1,
      "universityCode": "HUST",
      "name": "Đại học Bách Khoa Hà Nội",
      "nameEn": "Hanoi University of Science and Technology",
      "shortName": "HUST",
      "logoUrl": "https://example.com/logo.png",
      "fanpage": "https://facebook.com/hust",
      "foundingYear": 1956,
      "email": "contact@hust.edu.vn",
      "phone": "02438690000",
      "website": "https://hust.edu.vn",
      "description": "Trường đại học kỹ thuật hàng đầu Việt Nam.",
      "status": "active",
      "createdAt": "2025-07-16T03:08:47.324097Z",
      "createdBy": "admin",
      "updatedAt": "2025-07-16T03:08:47.324115Z",
      "updatedBy": "admin",
      "categoryId": 1,
      "category": {
        "id": 1,
        "name": "Đại học công lập",
        "description": "...",
        "status": "active",
        "createdAt": "...",
        "createdBy": "...",
        "updatedAt": "...",
        "updatedBy": "..."
      },
      "admissionMethodIds": [1, 2, 3],
      "campusCount": 2,
      "campuses": [
        // ... danh sách campus nếu includeCampuses=true
      ]
    }
    // ... các trường khác
  ]
}
```

---

## 2. Lấy chi tiết trường đại học theo ID

- **Endpoint:** `GET /api/v1/universities/{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "University fetched successfully",
  "result": {
    "id": 1,
    "universityCode": "HUST",
    "name": "Đại học Bách Khoa Hà Nội",
    "nameEn": "Hanoi University of Science and Technology",
    "shortName": "HUST",
    "logoUrl": "https://example.com/logo.png",
    "fanpage": "https://facebook.com/hust",
    "foundingYear": 1956,
    "email": "contact@hust.edu.vn",
    "phone": "02438690000",
    "website": "https://hust.edu.vn",
    "description": "Trường đại học kỹ thuật hàng đầu Việt Nam.",
    "status": "active",
    "createdAt": "2025-07-16T03:08:47.324097Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T03:08:47.324115Z",
    "updatedBy": "admin",
    "categoryId": 1,
    "category": {
      "id": 1,
      "name": "Đại học công lập",
      "description": "...",
      "status": "active",
      "createdAt": "...",
      "createdBy": "...",
      "updatedAt": "...",
      "updatedBy": "..."
    },
    "admissionMethodIds": [1, 2, 3],
    "campusCount": 2,
    "campuses": [
      // ... danh sách campus nếu includeCampuses=true
    ]
  }
}
```

---

## 3. Tìm kiếm, phân trang, sắp xếp trường đại học

- **Endpoint:** `GET /api/v1/universities`
- **Query params:**
  - `search`: Tìm kiếm theo tên, mã trường, viết tắt (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `id,asc` hoặc `name,desc` (mặc định: `id,asc`)
  - `categoryId`: Lọc theo loại trường (tùy chọn)
  - `provinceId`: Lọc theo tỉnh (tùy chọn)
  - `includeCampuses`: true/false (có trả về danh sách campus không)
- **Response:**

```json
{
  "code": 1000,
  "message": "Universities fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "universityCode": "HUST",
        "name": "Đại học Bách Khoa Hà Nội",
        "nameEn": "Hanoi University of Science and Technology",
        "shortName": "HUST",
        "logoUrl": "https://example.com/logo.png",
        "fanpage": "https://facebook.com/hust",
        "foundingYear": 1956,
        "email": "contact@hust.edu.vn",
        "phone": "02438690000",
        "website": "https://hust.edu.vn",
        "description": "Trường đại học kỹ thuật hàng đầu Việt Nam.",
        "status": "active",
        "createdAt": "2025-07-16T03:08:47.324097Z",
        "createdBy": "admin",
        "updatedAt": "2025-07-16T03:08:47.324115Z",
        "updatedBy": "admin",
        "categoryId": 1,
        "category": {
          "id": 1,
          "name": "Đại học công lập",
          "description": "...",
          "status": "active",
          "createdAt": "...",
          "createdBy": "...",
          "updatedAt": "...",
          "updatedBy": "..."
        },
        "admissionMethodIds": [1, 2, 3],
        "campusCount": 2,
        "campuses": [
          // ... danh sách campus nếu includeCampuses=true
        ]
      }
      // ... các trường khác
    ]
  }
}
```

---

## 4. Tạo mới trường đại học (ADMIN)

- **Endpoint:** `POST /api/v1/universities`
- **Request body:**

```json
{
  "universityCode": "FPTU",
  "name": "Đại học FPT",
  "nameEn": "FPT University",
  "shortName": "FPT",
  "logoUrl": "https://example.com/fpt-logo.png",
  "fanpage": "https://facebook.com/fptu",
  "foundingYear": 2006,
  "email": "contact@fpt.edu.vn",
  "phone": "02473001866",
  "website": "https://fpt.edu.vn",
  "description": "Trường đại học FPT",
  "categoryId": 2
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "University created successfully",
  "result": {
    "id": 2,
    "universityCode": "FPTU",
    "name": "Đại học FPT",
    "nameEn": "FPT University",
    "shortName": "FPT",
    "logoUrl": "https://example.com/fpt-logo.png",
    "fanpage": "https://facebook.com/fptu",
    "foundingYear": 2006,
    "email": "contact@fpt.edu.vn",
    "phone": "02473001866",
    "website": "https://fpt.edu.vn",
    "description": "Trường đại học FPT",
    "status": "active",
    "createdAt": "2025-07-16T10:00:00.000Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T10:00:00.000Z",
    "updatedBy": "admin",
    "categoryId": 2,
    "category": {
      "id": 2,
      "name": "Đại học tư thục",
      "description": "...",
      "status": "active",
      "createdAt": "...",
      "createdBy": "...",
      "updatedAt": "...",
      "updatedBy": "..."
    },
    "admissionMethodIds": [],
    "campusCount": 0,
    "campuses": []
  }
}
```

---

## 5. Cập nhật trường đại học (ADMIN)

- **Endpoint:** `PUT /api/v1/universities/{id}`
- **Request body:**

```json
{
  "universityCode": "FPTU",
  "name": "Đại học FPT Hà Nội",
  "nameEn": "FPT University Hanoi",
  "shortName": "FPT",
  "logoUrl": "https://example.com/fpt-logo.png",
  "fanpage": "https://facebook.com/fptu",
  "foundingYear": 2006,
  "email": "contact@fpt.edu.vn",
  "phone": "02473001866",
  "website": "https://fpt.edu.vn",
  "description": "Trường đại học FPT cơ sở Hà Nội",
  "categoryId": 2
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "University updated successfully",
  "result": {
    "id": 2,
    "universityCode": "FPTU",
    "name": "Đại học FPT Hà Nội",
    "nameEn": "FPT University Hanoi",
    "shortName": "FPT",
    "logoUrl": "https://example.com/fpt-logo.png",
    "fanpage": "https://facebook.com/fptu",
    "foundingYear": 2006,
    "email": "contact@fpt.edu.vn",
    "phone": "02473001866",
    "website": "https://fpt.edu.vn",
    "description": "Trường đại học FPT cơ sở Hà Nội",
    "status": "active",
    "createdAt": "2025-07-16T10:00:00.000Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T10:05:00.000Z",
    "updatedBy": "admin",
    "categoryId": 2,
    "category": {
      "id": 2,
      "name": "Đại học tư thục",
      "description": "...",
      "status": "active",
      "createdAt": "...",
      "createdBy": "...",
      "updatedAt": "...",
      "updatedBy": "..."
    },
    "admissionMethodIds": [],
    "campusCount": 0,
    "campuses": []
  }
}
```

---

## 6. Xóa trường đại học (ADMIN)

- **Endpoint:** `DELETE /api/v1/universities/{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "University deleted successfully"
}
```

---

## 7. Cập nhật trạng thái trường đại học (ADMIN)

- **Endpoint:** `PATCH /api/v1/universities/{id}/status`
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
  "message": "University status updated successfully",
  "result": {
    "id": 2,
    "universityCode": "FPTU",
    "name": "Đại học FPT Hà Nội",
    "nameEn": "FPT University Hanoi",
    "shortName": "FPT",
    "logoUrl": "https://example.com/fpt-logo.png",
    "fanpage": "https://facebook.com/fptu",
    "foundingYear": 2006,
    "email": "contact@fpt.edu.vn",
    "phone": "02473001866",
    "website": "https://fpt.edu.vn",
    "description": "Trường đại học FPT cơ sở Hà Nội",
    "status": "inactive",
    "createdAt": "2025-07-16T10:00:00.000Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T10:10:00.000Z",
    "updatedBy": "admin",
    "categoryId": 2,
    "category": {
      "id": 2,
      "name": "Đại học tư thục",
      "description": "...",
      "status": "active",
      "createdAt": "...",
      "createdBy": "...",
      "updatedAt": "...",
      "updatedBy": "..."
    },
    "admissionMethodIds": [],
    "campusCount": 0,
    "campuses": []
  }
}
```

---

## 8. Model dữ liệu University (chuẩn BE trả về)

```json
{
  "id": 1,
  "universityCode": "HUST",
  "name": "Đại học Bách Khoa Hà Nội",
  "nameEn": "Hanoi University of Science and Technology",
  "shortName": "HUST",
  "logoUrl": "https://example.com/logo.png",
  "fanpage": "https://facebook.com/hust",
  "foundingYear": 1956,
  "email": "contact@hust.edu.vn",
  "phone": "02438690000",
  "website": "https://hust.edu.vn",
  "description": "Trường đại học kỹ thuật hàng đầu Việt Nam.",
  "status": "active",
  "createdAt": "2025-07-16T03:08:47.324097Z",
  "createdBy": "admin",
  "updatedAt": "2025-07-16T03:08:47.324115Z",
  "updatedBy": "admin",
  "categoryId": 1,
  "category": {
    "id": 1,
    "name": "Đại học công lập",
    "description": "...",
    "status": "active",
    "createdAt": "...",
    "createdBy": "...",
    "updatedAt": "...",
    "updatedBy": "..."
  },
  "admissionMethodIds": [1, 2, 3],
  "campusCount": 2,
  "campuses": [
    // ... danh sách campus nếu includeCampuses=true
  ]
}
```

---

**Lưu ý:**

- Các API tạo, cập nhật, xóa, cập nhật trạng thái chỉ dành cho ADMIN.
- FE cần lấy đúng các trường: `id`, `universityCode`, `name`, `nameEn`, `shortName`, `logoUrl`, `fanpage`, `foundingYear`, `email`, `phone`, `website`, `description`, `status`, `categoryId`, `category`, `admissionMethodIds`, `campusCount`, `campuses`.
- Trường `status` có thể là: `"active"`, `"inactive"`, `"deleted"` (tùy vào logic backend).
- Nếu muốn lấy danh sách campus, truyền thêm param `includeCampuses=true` khi gọi API.
