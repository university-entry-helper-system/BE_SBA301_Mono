# Hướng dẫn sử dụng API News (Frontend)

**Base URL:** `/api/v1/news`

---

## 1. Lấy danh sách tin tức phân trang

- **Endpoint:** `GET /paginated`
- **Query params:**
  - `page`: số trang (mặc định 0)
  - `size`: số lượng mỗi trang (mặc định 10)
- **Response:**

```json
{
  "code": 1000,
  "message": "News fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "items": [
      {
        "id": 1,
        "university": {
          /* UniversityResponse */
        },
        "title": "Tiêu đề",
        "summary": "Tóm tắt",
        "content": "Nội dung chi tiết",
        "imageUrl": "http://...",
        "category": "Tin tức",
        "viewCount": 123,
        "newsStatus": "Published",
        "publishedAt": "2024-05-01T12:00:00Z",
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

## 2. Lấy chi tiết tin tức theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "News fetched successfully",
  "result": {
    "id": 1,
    "university": {
      /* UniversityResponse */
    },
    "title": "Tiêu đề",
    "summary": "Tóm tắt",
    "content": "Nội dung chi tiết",
    "imageUrl": "http://...",
    "category": "Tin tức",
    "viewCount": 123,
    "newsStatus": "Published",
    "publishedAt": "2024-05-01T12:00:00Z",
    "status": "ACTIVE",
    "createdAt": "2024-05-01T10:00:00Z",
    "createdBy": "admin",
    "updatedAt": "2024-05-01T11:00:00Z",
    "updatedBy": "admin"
  }
}
```

---

## 3. Tạo tin tức mới (ADMIN)

- **Endpoint:** `POST /`
- **Content-Type:** `multipart/form-data`
- **Body:**
  - `universityId`: số (bắt buộc)
  - `title`: chuỗi (bắt buộc, tối đa 255 ký tự)
  - `summary`: chuỗi (bắt buộc, tối đa 500 ký tự)
  - `content`: chuỗi (bắt buộc)
  - `category`: chuỗi (bắt buộc)
  - `image`: file ảnh (tùy chọn)
  - `imageUrl`: chuỗi (tùy chọn)
  - `newsStatus`: chuỗi (mặc định "Published")
  - `publishedAt`: ISO date (tùy chọn)
- **Response:**

```json
{
  "code": 1001,
  "message": "News created successfully",
  "result": {
    /* NewsResponse */
  }
}
```

---

## 4. Cập nhật tin tức (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Content-Type:** `multipart/form-data`
- **Body:**
  - Giống như tạo mới
- **Response:**

```json
{
  "code": 1002,
  "message": "News updated successfully",
  "result": {
    /* NewsResponse */
  }
}
```

---

## 5. Xóa tin tức (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "News deleted successfully"
}
```

---

## 6. Tìm kiếm tin tức

- **Endpoint:** `GET /search`
- **Query params:**
  - `query`: từ khóa tìm kiếm (bắt buộc)
  - `page`: số trang (mặc định 0)
  - `size`: số lượng mỗi trang (mặc định 10)
- **Response:**

```json
{
  "code": 1000,
  "message": "News search successful",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 5,
    "totalPages": 1,
    "items": [
      /* NewsResponse[] */
    ]
  }
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
- Trường `university` trong NewsResponse là object chi tiết trường (xem UniversityResponse).
- Trường `status` có thể là `ACTIVE` hoặc `DELETED`.
- Các API tạo/sửa/xóa chỉ dành cho ADMIN (cần accessToken hợp lệ).
