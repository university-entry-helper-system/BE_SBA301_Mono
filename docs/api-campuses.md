# API: Campus

**Base URL:** `/api/v1/campuses`

---

## 1. Lấy danh sách tất cả cơ sở (campus)

- **Endpoint:** `GET /api/v1/campuses`
- **Query params:**

  - `search`: Tìm kiếm theo tên cơ sở (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `id,asc` hoặc `campusName,desc` (mặc định: `id,asc`)
  - `universityId`: Lọc theo trường đại học (tùy chọn)
  - `provinceId`: Lọc theo tỉnh/thành (tùy chọn)
  - `campusType`: Lọc theo loại cơ sở (tùy chọn, ví dụ: MAIN, BRANCH, ...)
  - `isMainCampus`: Lọc cơ sở chính (true/false, tùy chọn)

- **Response:**

```json
{
  "code": 1000,
  "message": "Campuses fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "campusName": "Cơ sở chính Xuân Thủy",
        "campusCode": "MAIN",
        "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
        "phone": "024-37547460",
        "email": "info@vnu.edu.vn",
        "website": "https://vnu.edu.vn",
        "isMainCampus": true,
        "campusType": {
          "id": 1,
          "name": "MAIN",
          "description": "Cơ sở chính",
          "status": "active"
        },
        "description": "Cơ sở chính của ĐHQGHN",
        "establishedYear": 1993,
        "areaHectares": 50.5,
        "university": {
          "id": 1,
          "universityCode": "VNU_HN",
          "name": "Đại học Quốc gia Hà Nội",
          "shortName": "ĐHQGHN"
        },
        "province": {
          "id": 1,
          "name": "Hà Nội",
          "region": "BAC"
        },
        "status": "active",
        "createdAt": "2025-07-16T03:08:47.324097Z",
        "createdBy": "admin",
        "updatedAt": "2025-07-16T03:08:47.324115Z",
        "updatedBy": "admin"
      }
      // ... các campus khác
    ]
  }
}
```

---

## 2. Lấy chi tiết cơ sở theo ID

- **Endpoint:** `GET /api/v1/campuses/{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Campus fetched successfully",
  "result": {
    "id": 1,
    "campusName": "Cơ sở chính Xuân Thủy",
    "campusCode": "MAIN",
    "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
    "phone": "024-37547460",
    "email": "info@vnu.edu.vn",
    "website": "https://vnu.edu.vn",
    "isMainCampus": true,
    "campusType": {
      "id": 1,
      "name": "MAIN",
      "description": "Cơ sở chính",
      "status": "active"
    },
    "description": "Cơ sở chính của ĐHQGHN",
    "establishedYear": 1993,
    "areaHectares": 50.5,
    "university": {
      "id": 1,
      "universityCode": "VNU_HN",
      "name": "Đại học Quốc gia Hà Nội",
      "shortName": "ĐHQGHN"
    },
    "province": {
      "id": 1,
      "name": "Hà Nội",
      "region": "BAC"
    },
    "status": "active",
    "createdAt": "2025-07-16T03:08:47.324097Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T03:08:47.324115Z",
    "updatedBy": "admin"
  }
}
```

---

## 3. Tạo mới cơ sở (ADMIN)

- **Endpoint:** `POST /api/v1/campuses`
- **Content-Type:** `application/json`
- **Request body:**

```json
{
  "universityId": 1,
  "provinceId": 1,
  "campusName": "Cơ sở chính Xuân Thủy",
  "campusCode": "MAIN",
  "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
  "phone": "024-37547460",
  "email": "info@vnu.edu.vn",
  "website": "https://vnu.edu.vn",
  "isMainCampus": true,
  "campusTypeId": 1,
  "description": "Cơ sở chính của ĐHQGHN",
  "establishedYear": 1993,
  "areaHectares": 50.5
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Campus created successfully",
  "result": {
    "id": 10,
    "campusName": "Cơ sở chính Xuân Thủy",
    "campusCode": "MAIN",
    "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
    "phone": "024-37547460",
    "email": "info@vnu.edu.vn",
    "website": "https://vnu.edu.vn",
    "isMainCampus": true,
    "campusType": {
      "id": 1,
      "name": "MAIN",
      "description": "Cơ sở chính",
      "status": "active"
    },
    "description": "Cơ sở chính của ĐHQGHN",
    "establishedYear": 1993,
    "areaHectares": 50.5,
    "university": {
      "id": 1,
      "universityCode": "VNU_HN",
      "name": "Đại học Quốc gia Hà Nội",
      "shortName": "ĐHQGHN"
    },
    "province": {
      "id": 1,
      "name": "Hà Nội",
      "region": "BAC"
    },
    "status": "active",
    "createdAt": "2025-07-16T10:00:00.000Z",
    "createdBy": "admin",
    "updatedAt": "2025-07-16T10:00:00.000Z",
    "updatedBy": "admin"
  }
}
```

---

## 4. Cập nhật cơ sở (ADMIN)

- **Endpoint:** `PUT /api/v1/campuses/{id}`
- **Content-Type:** `application/json`
- **Request body:** (giống như tạo mới, chỉ cần truyền các trường muốn cập nhật)

```json
{
  "universityId": 1,
  "provinceId": 1,
  "campusName": "Cơ sở chính Xuân Thủy",
  "campusCode": "MAIN",
  "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
  "phone": "024-37547460",
  "email": "info@vnu.edu.vn",
  "website": "https://vnu.edu.vn",
  "isMainCampus": true,
  "campusTypeId": 1,
  "description": "Cơ sở chính của ĐHQGHN",
  "establishedYear": 1993,
  "areaHectares": 50.5
}
```

- **Response:** (giống response tạo mới, chỉ khác message và code)

```json
{
  "code": 1002,
  "message": "Campus updated successfully",
  "result": { ... }
}
```

---

## 5. Xóa cơ sở (ADMIN)

- **Endpoint:** `DELETE /api/v1/campuses/{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Campus deleted successfully"
}
```

---

## 6. Cập nhật trạng thái cơ sở (ADMIN)

- **Endpoint:** `PATCH /api/v1/campuses/{id}/status`
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
  "message": "Campus status updated successfully",
  "result": {
    // ... thông tin campus sau khi cập nhật trạng thái
  }
}
```

---

## 7. Model dữ liệu Campus (chuẩn BE trả về)

```json
{
  "id": 1,
  "campusName": "Cơ sở chính Xuân Thủy",
  "campusCode": "MAIN",
  "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
  "phone": "024-37547460",
  "email": "info@vnu.edu.vn",
  "website": "https://vnu.edu.vn",
  "isMainCampus": true,
  "campusType": {
    "id": 1,
    "name": "MAIN",
    "description": "Cơ sở chính",
    "status": "active"
  },
  "description": "Cơ sở chính của ĐHQGHN",
  "establishedYear": 1993,
  "areaHectares": 50.5,
  "university": {
    "id": 1,
    "universityCode": "VNU_HN",
    "name": "Đại học Quốc gia Hà Nội",
    "shortName": "ĐHQGHN"
  },
  "province": {
    "id": 1,
    "name": "Hà Nội",
    "region": "BAC"
  },
  "status": "active",
  "createdAt": "2025-07-16T03:08:47.324097Z",
  "createdBy": "admin",
  "updatedAt": "2025-07-16T03:08:47.324115Z",
  "updatedBy": "admin"
}
```

---

## **Lưu ý cho FE**

- **Tạo/sửa campus:** Gửi request dạng `application/json`, không cần multipart.
- **Các trường bắt buộc:** `universityId`, `provinceId`, `campusName`, `campusTypeId`, `address`.
- **Trường `isMainCampus`**: true/false, mỗi trường đại học phải có ít nhất 1 campus chính.
- **Khi filter:** Có thể filter theo trường, tỉnh, loại cơ sở, cơ sở chính/phân hiệu.
- **Trường `campusType`** là object, FE lấy danh sách loại cơ sở qua API `/api/v1/campus-types`.
- **Trường `province`** là object, FE lấy danh sách tỉnh qua API `/api/v1/provinces`.
- **Không có upload file cho campus.**
- **Khi xóa campus:** BE sẽ soft-delete (status = deleted).
