# API News

## Tổng quan

Các API quản lý tin tức (news) cho FE, bao gồm tạo, cập nhật, xóa, tìm kiếm, upload ảnh qua Minio (multipart/form-data).

---

## 1. Lấy danh sách tin tức (phân trang)

- **Endpoint:** `GET /api/v1/news/paginated`
- **Query params:**
  - `page` (int, default=0)
  - `size` (int, default=10)
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
        "title": "...",
        "summary": "...",
        "content": "...",
        "imageUrl": "https://minio.example.com/bucket/abcxyz.jpg",
        "category": "...",
        "university": { ... },
        "viewCount": 0,
        "newsStatus": "PUBLISHED",
        "status": "ACTIVE",
        "createdAt": "...",
        "createdBy": "...",
        "updatedAt": "...",
        "updatedBy": "...",
        "publishedAt": "2024-06-01T08:00:00+07:00",
        "releaseDate": "2024-06-01T08:00:00+07:00"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết tin tức

- **Endpoint:** `GET /api/v1/news/{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "News fetched successfully",
  "result": { ...NewsResponse... }
}
```

---

## 3. Tạo tin tức mới (có upload ảnh)

- **Endpoint:** `POST /api/v1/news`
- **Headers:** `Content-Type: multipart/form-data`
- **Body (form-data):**

  - `universityId` (number, bắt buộc)
  - `title` (string, bắt buộc)
  - `summary` (string, bắt buộc)
  - `content` (string, bắt buộc)
  - `category` (string, optional, enum: [EDUCATION, EVENT, ...])
  - `newsStatus` (string, optional, enum: [PUBLISHED, DRAFT, ...])
  - `releaseDate` (string, optional, ISO 8601)
  - `image` (file, optional)
  - `publishedAt` (string, optional, định dạng ISO 8601, có giờ, timezone GMT+7, ví dụ: `2024-06-01T14:30:00+07:00`)
  - `releaseDate` (string, optional, định dạng ISO 8601, có giờ, timezone GMT+7, ví dụ: `2024-06-01T14:30:00+07:00`)

- **Lưu ý:**

  - **Chỉ upload file ảnh qua trường `image` (multipart/form-data). Không truyền imageUrl từ FE.**
  - `publishedAt` và `releaseDate` là chuỗi ngày giờ ISO 8601, có giờ, timezone GMT+7. Ví dụ: `2024-06-01T14:30:00+07:00`.
  - **Nếu truyền thiếu giờ hoặc thiếu timezone sẽ lỗi hoặc lưu sai giờ.**
  - **BE nhận kiểu Instant, không tự động chuyển timezone, FE phải chủ động format đúng.**
  - **Bắt buộc gửi đủ các trường required, kể cả khi update.**

- **Response:**

```json
{
  "code": 1001,
  "message": "News created successfully",
  "result": { ...NewsResponse... }
}
```

- **Ví dụ request (cURL):**

```bash
curl -X POST http://localhost:8080/api/v1/news \
  -H "Authorization: Bearer <token>" \
  -F "universityId=1" \
  -F "title=Tiêu đề tin" \
  -F "summary=Tóm tắt tin" \
  -F "content=Nội dung tin" \
  -F "category=EDUCATION" \
  -F "newsStatus=PUBLISHED" \
  -F "image=@/path/to/image.jpg"
```

---

## 4. Cập nhật tin tức (có upload ảnh)

- **Endpoint:** `PUT /api/v1/news/{id}`
- **Headers:** `Content-Type: multipart/form-data`
- **Body (form-data):**

  - **Gửi lại đầy đủ các trường bắt buộc như khi tạo:**
    - `universityId`, `title`, `summary`, `content`
  - `category`, `newsStatus`, `releaseDate` (optional)
  - `image` (file, optional, nếu muốn thay ảnh)
  - `publishedAt` (string, optional, định dạng ISO 8601, có giờ, timezone GMT+7, ví dụ: `2024-06-01T14:30:00+07:00`)
  - `releaseDate` (string, optional, định dạng ISO 8601, có giờ, timezone GMT+7, ví dụ: `2024-06-01T14:30:00+07:00`)

- **Lưu ý:**

  - Nếu không upload ảnh mới, có thể bỏ qua trường `image`, backend sẽ giữ ảnh cũ.
  - `publishedAt` và `releaseDate` nếu truyền lên phải đúng định dạng ISO 8601, timezone GMT+7. Ví dụ: `2024-06-01T14:30:00+07:00`.
  - **Nếu truyền thiếu giờ hoặc thiếu timezone sẽ lỗi hoặc lưu sai giờ.**
  - **BE nhận kiểu Instant, không tự động chuyển timezone, FE phải chủ động format đúng.**
  - Không truyền imageUrl từ FE.
  - Nếu thiếu trường bắt buộc sẽ trả về 400 Bad Request.

- **Ví dụ request (cURL):**

```bash
curl -X PUT http://localhost:8080/api/v1/news/19 \
  -H "Authorization: Bearer <token>" \
  -F "universityId=1" \
  -F "title=Tiêu đề mới" \
  -F "summary=Tóm tắt mới" \
  -F "content=Nội dung mới" \
  -F "category=EDUCATION" \
  -F "newsStatus=PUBLISHED" \
  -F "image=@/path/to/new-image.jpg"
```

---

## 5. Xóa tin tức

- **Endpoint:** `DELETE /api/v1/news/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Response:**

```json
{
  "code": 1003,
  "message": "News deleted successfully"
}
```

---

## 6. Tìm kiếm, lọc, top hot news

- `GET /api/v1/news/search?query=...&page=0&size=10`
- `GET /api/v1/news/hot`
- `GET /api/v1/news?category=...&search=...&fromDate=...&toDate=...&minViews=...&maxViews=...&newsStatus=...&page=0&size=10`

---

## 7. Các lỗi thường gặp

- **400 Bad Request:** Thiếu trường bắt buộc (universityId, title, summary, content) hoặc kiểu dữ liệu không hợp lệ.
- **401 Unauthorized:** Thiếu hoặc sai token.
- **403 Forbidden:** Không có quyền admin.

---

## 8. Ghi chú

- Khi update/tạo, luôn gửi đủ các trường bắt buộc.
- Nếu chỉ muốn đổi ảnh, vẫn phải gửi lại các trường required.
- Nếu không upload ảnh mới, backend sẽ giữ ảnh cũ.
- Nếu cần hỗ trợ thêm, liên hệ backend.

## 9. Ví dụ format ngày giờ đúng trong JavaScript (GMT+7)

```js
// Lấy giờ hiện tại theo GMT+7, format ISO 8601
function toGmt7ISOString(date = new Date()) {
  const tzOffset = 7 * 60; // phút
  const localISO = new Date(
    date.getTime() - (date.getTimezoneOffset() - tzOffset) * 60000
  )
    .toISOString()
    .replace("Z", "+07:00");
  return localISO;
}
// Ví dụ:
const publishedAt = toGmt7ISOString(); // "2024-06-01T14:30:00+07:00"
```

## 10. Lưu ý khi gửi form-data từ FE

- **Không append field nếu giá trị là chuỗi rỗng hoặc null.**
- **universityId phải là số, không phải chuỗi.**
- **Các trường @NotBlank (title, summary, content, ...) không được truyền "" (chuỗi rỗng).**
- Nếu truyền "" hoặc sai kiểu, backend sẽ trả về lỗi thiếu field.

### Ví dụ tạo FormData đúng chuẩn:

```js
const formData = new FormData();
if (values.universityId)
  formData.append("universityId", String(values.universityId));
if (values.title) formData.append("title", values.title);
if (values.summary) formData.append("summary", values.summary);
if (values.content) formData.append("content", values.content);
if (values.category) formData.append("category", values.category);
if (values.newsStatus) formData.append("newsStatus", values.newsStatus);
const publishedAtStr = toGmt7ISOString(values.publishedAt);
if (publishedAtStr) formData.append("publishedAt", publishedAtStr);
const releaseDateStr = toGmt7ISOString(values.releaseDate);
if (releaseDateStr) formData.append("releaseDate", releaseDateStr);
if (values.image) formData.append("image", values.image);
```

### Ví dụ fix cho Select trường (ép value về số):

```jsx
<Select
  labelId="universityId-label"
  name="universityId"
  value={formik.values.universityId}
  onChange={(e) => formik.setFieldValue("universityId", Number(e.target.value))}
  // ... các prop khác
>
  <MenuItem value={0}>-- Chọn trường --</MenuItem>
  {universities.map((u) => (
    <MenuItem key={u.id} value={u.id}>
      {u.shortName || u.name}
    </MenuItem>
  ))}
</Select>
```

**Luôn kiểm tra lại FormData trước khi gửi:**

```js
for (let [k, v] of formData.entries()) {
  console.log(k, v);
}
```
