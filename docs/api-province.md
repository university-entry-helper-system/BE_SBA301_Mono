# Hướng dẫn sử dụng API Province (Frontend)

**Base URL:** `/api/v1/provinces`

---

## 1. Lấy danh sách tỉnh/thành (có tìm kiếm, phân trang, sắp xếp)

- **Endpoint:** `GET /`
- **Query params:**
  - `search`: Tìm kiếm theo tên tỉnh/thành phố (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `region,asc` hoặc `name,desc` (tùy chọn)
- **Response:**

```json
{
  "code": 1000,
  "message": "List of provinces fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 3,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "name": "Hà Nội",
        "description": "Thủ đô",
        "region": "BAC",
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "name": "Đà Nẵng",
        "description": "Thành phố biển",
        "region": "TRUNG",
        "status": "ACTIVE"
      },
      {
        "id": 3,
        "name": "TP. Hồ Chí Minh",
        "description": "Đô thị lớn nhất",
        "region": "NAM",
        "status": "ACTIVE"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết tỉnh/thành theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Province fetched successfully",
  "result": {
    "id": 1,
    "name": "Hà Nội",
    "description": "Thủ đô",
    "region": "BAC",
    "status": "ACTIVE"
  }
}
```

---

## 3. Tạo tỉnh/thành mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "name": "Hà Nội",
  "description": "Thủ đô",
  "region": "BAC"
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Province created successfully",
  "result": {
    "id": 1,
    "name": "Hà Nội",
    "description": "Thủ đô",
    "region": "BAC",
    "status": "ACTIVE"
  }
}
```

---

## 4. Cập nhật tỉnh/thành (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới, **có thể thêm trường `status`**

```json
{
  "name": "Hà Nội",
  "description": "Thủ đô",
  "region": "BAC",
  "status": "ACTIVE"
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "Province updated successfully",
  "result": {
    "id": 1,
    "name": "Hà Nội",
    "description": "Thủ đô",
    "region": "BAC",
    "status": "ACTIVE"
  }
}
```

---

## 5. Xóa tỉnh/thành (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Province deleted successfully"
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

- `name`: Tên tỉnh/thành
- `description`: Mô tả tỉnh/thành
- `region`: Vùng miền (Enum: `BAC` - Bắc, `TRUNG` - Trung, `NAM` - Nam)
- `status`: Trạng thái (Enum: `ACTIVE`, `DELETED`)

**Yêu cầu convert region (FE phải xử lý):**

- **Khi gửi lên API:** Convert tiếng Việt → Enum
  - `"Bắc"` → `"BAC"`
  - `"Trung"` → `"TRUNG"`
  - `"Nam"` → `"NAM"`
- **Khi nhận từ API:** Convert Enum → tiếng Việt
  - `"BAC"` → `"Bắc"`
  - `"TRUNG"` → `"Trung"`
  - `"NAM"` → `"Nam"`

**Ví dụ code convert (JavaScript):**

```javascript
// Convert tiếng Việt → Enum (khi gửi lên API)
const regionToEnum = {
  Bắc: "BAC",
  Trung: "TRUNG",
  Nam: "NAM",
};

// Convert Enum → tiếng Việt (khi nhận từ API)
const enumToRegion = {
  BAC: "Bắc",
  TRUNG: "Trung",
  NAM: "Nam",
};
```

**Lưu ý:**

- Các API trả về theo format trên.
- Các lỗi sẽ trả về code khác và message tương ứng.
- Các API tạo/sửa/xóa chỉ dành cho ADMIN (cần accessToken hợp lệ).
- FE phải tự xử lý convert region giữa tiếng Việt và Enum.
