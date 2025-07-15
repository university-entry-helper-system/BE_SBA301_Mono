# API: University

## 🆕 **Các thay đổi quan trọng (2025-07)**

- **logoUrl:**
  - FE **không gửi URL** logo, chỉ upload file ảnh qua trường `logoFile` (multipart/form-data).
  - BE sẽ upload lên Minio và trả về trường `logoUrl` là URL public ảnh logo (FE chỉ cần dùng URL này để hiển thị).
- **fanpage:**
  - Đã bổ sung field `fanpage` (link Facebook) cho trường đại học.
  - FE có thể gửi thêm trường này khi tạo/cập nhật, BE sẽ trả về trong response.

---

## ⚠️ **HƯỚNG DẪN FE VỀ UPLOAD ẢNH LOGO**

- Khi tạo/cập nhật trường, **FE phải gửi file ảnh logo qua trường `logoFile`** (kiểu `multipart/form-data`).
- Không gửi trường `logoUrl` kiểu string trong request.
- Sau khi upload thành công, **BE sẽ trả về trường `logoUrl` là URL public ảnh logo** (FE chỉ cần dùng URL này để hiển thị ảnh).
- Nếu muốn đổi ảnh, FE gửi file mới qua trường `logoFile`. Nếu không đổi ảnh, FE không cần gửi trường này khi update.
- **Ví dụ gửi form-data:**
  - Key: `logoFile` (type: File)
  - Các key khác: `categoryId`, `name`, ... (type: Text)

**Ví dụ sử dụng với Postman:**

- Chọn Body > form-data
- Thêm trường `logoFile` (type: File), chọn file ảnh
- Thêm các trường khác như bình thường

**Hiển thị ảnh:**

- Lấy trường `logoUrl` từ response, dùng làm src cho thẻ `<img>`
- Ví dụ: `<img src={university.logoUrl} alt="Logo trường" />`

---

## ⚡️ **HƯỚNG DẪN FE HIỂN THỊ ẢNH LOGO TỪ MINIO (BUCKET PUBLIC)**

Khi bucket MinIO đã cấp quyền public (anonymous download), FE chỉ cần build URL public để hiển thị ảnh logo trường đại học.

### 1. Khi tạo/cập nhật trường

- FE upload file logo qua trường `logoFile` (multipart/form-data).
- BE trả về trường `logoUrl` (có thể là tên file hoặc đường dẫn file trên MinIO, ví dụ: `logo-NEU.png` hoặc `folder/logo-NEU.png`).

### 2. Cách build URL public để hiển thị ảnh

#### A. Nếu BE trả về full URL

- FE dùng trực tiếp:
  ```jsx
  <img src={university.logoUrl} alt="Logo trường" />
  ```

#### B. Nếu BE chỉ trả về tên file (ví dụ: `logo-NEU.png`)

- FE build URL:
  ```js
  // Giả sử MinIO chạy ở http://localhost:9000, bucket là mybucket
  const minioBaseUrl = "http://localhost:9000";
  const bucketName = "mybucket";
  const logoUrl = `${minioBaseUrl}/${bucketName}/${university.logoUrl}`;
  ```
  ```jsx
  <img src={logoUrl} alt="Logo trường" />
  ```

#### C. Nếu BE trả về đường dẫn con (folder/file.png)

- FE vẫn build như trên:
  ```js
  const logoUrl = `${minioBaseUrl}/${bucketName}/${university.logoUrl}`;
  ```

### 3. Xử lý trường hợp không có logo

```jsx
<img
  src={logoUrl || "/default-logo.png"}
  alt="Logo trường"
  style={{ width: 120, height: 120 }}
/>
```

### 4. Lưu ý cho FE

- Không cần token, không cần presigned URL: chỉ cần build đúng URL là truy cập được ảnh.
- Nếu đổi host MinIO hoặc bucket, FE phải cập nhật lại base URL/bucket cho đúng.
- Có thể cache ảnh logo để tăng tốc độ hiển thị.
- Nếu logoUrl null hoặc rỗng, hiển thị ảnh mặc định.

---

## 🛡️ Quy tắc validate dữ liệu & lỗi thường gặp

### 1. Lỗi thường gặp khi gửi dữ liệu

| Lỗi                                                               | Nguyên nhân                                                                                                         | Cách khắc phục                                           |
| ----------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------- |
| **HttpMediaTypeNotSupportedException**                            | FE gửi Content-Type `multipart/form-data` nhưng BE chưa hỗ trợ (phải dùng @ModelAttribute, không dùng @RequestBody) | Đã fix, FE gửi form-data, BE nhận @ModelAttribute        |
| **DataIntegrityViolationException**                               | Vi phạm ràng buộc DB (null trường bắt buộc, trùng unique, v.v.)                                                     | Kiểm tra dữ liệu gửi lên, đảm bảo đúng quy tắc           |
| **PSQLException: value too long for type character varying(255)** | Gửi chuỗi quá dài cho trường chỉ cho phép 255 ký tự                                                                 | FE phải validate độ dài trước khi gửi                    |
| **org.hibernate.exception.DataException**                         | Dữ liệu vượt quá giới hạn khai báo trong entity                                                                     | FE phải validate độ dài, BE nên dùng TEXT cho trường dài |

### 2. Quy tắc validate từng trường (FE cần biết)

| Field              | Bắt buộc | Kiểu          | Độ dài tối đa  | Ghi chú                             |
| ------------------ | -------- | ------------- | -------------- | ----------------------------------- |
| name               | ✔️       | String        | 255            | Tên trường                          |
| shortName          |          | String        | 50             | Tên viết tắt                        |
| logoFile           |          | File          | 5MB            | Ảnh logo, chỉ nhận file ảnh         |
| fanpage            |          | String        | 255            | Link Facebook                       |
| foundingYear       | ✔️       | Integer       | 4              | Năm thành lập                       |
| provinceId         | ✔️       | Integer       |                | ID tỉnh/thành                       |
| categoryId         | ✔️       | Integer       |                | ID loại trường                      |
| address            |          | String        | Không giới hạn | Đã chuyển sang TEXT, nhập thoải mái |
| email              |          | String        | 255            | Email liên hệ                       |
| phone              |          | String        | 20             | Số điện thoại                       |
| website            |          | String        | 255            | Website                             |
| description        |          | String        | Không giới hạn | TEXT, nhập thoải mái                |
| admissionMethodIds |          | List<Integer> |                | Danh sách ID phương thức tuyển sinh |

**Lưu ý:**

- Các trường TEXT (address, description) không giới hạn 255 ký tự, FE có thể nhập dài.
- Các trường String khác (name, email, website, fanpage, ...) phải kiểm tra độ dài trước khi gửi (không vượt quá 255 ký tự).
- Nếu gửi file ảnh, chỉ gửi file ảnh (jpg/png/webp), dung lượng tối đa 5MB (hoặc theo cấu hình BE).
- Nếu gửi nhiều admissionMethodIds, gửi nhiều trường cùng tên trong form-data.

---

## 1. Lấy danh sách trường (search, pagination, sort, filter)

- **Endpoint:** `GET /`
- **Query params:**
  - `search`: Tìm kiếm theo tên trường hoặc tên viết tắt (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `name,asc` hoặc `id,desc` (tùy chọn)
  - `categoryId`: Lọc theo ID loại trường (tùy chọn)
  - `provinceId`: Lọc theo ID tỉnh/thành (tùy chọn)
- **Response:**

```json
{
  "code": 1000,
  "message": "Universities fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "items": [
      {
        "id": 1,
        "categoryId": 2,
        "admissionMethodIds": [1, 2, 3],
        "name": "Đại học A",
        "shortName": "DHA",
        "logoUrl": "logo-dha.png", // Tên file ảnh trên Minio
        "fanpage": "https://facebook.com/dha.edu.vn",
        "foundingYear": 2000,
        "province": {
          "id": 1,
          "name": "Hà Nội",
          "description": "Thủ đô Hà Nội",
          "region": "BAC"
        },
        "address": "123 Đường ABC",
        "email": "info@dha.edu.vn",
        "phone": "0123456789",
        "website": "https://dha.edu.vn",
        "description": "Trường đại học A ...",
        "status": "active",
        "createdAt": "2024-05-01T10:00:00Z",
        "createdBy": "admin",
        "updatedAt": "2024-05-01T11:00:00Z",
        "updatedBy": "admin"
      }
    ]
  }
}
```

### Ví dụ sử dụng:

- Lấy tất cả trường:

  ```http
  GET /api/v1/universities?page=0&size=10
  ```

- Tìm kiếm theo tên:

  ```http
  GET /api/v1/universities?search=Đại học&page=0&size=10
  ```

- Sắp xếp theo tên tăng dần:

  ```http
  GET /api/v1/universities?sort=name,asc&page=0&size=10
  ```

- Lọc theo loại trường:

  ```http
  GET /api/v1/universities?categoryId=2&page=0&size=10
  ```

- Lọc theo tỉnh/thành:

  ```http
  GET /api/v1/universities?provinceId=1&page=0&size=10
  ```

- Kết hợp nhiều filter:
  ```http
  GET /api/v1/universities?search=Đại học&categoryId=2&provinceId=1&sort=name,asc&page=0&size=10
  ```

---

## 2. Lấy chi tiết trường theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "University fetched successfully",
  "result": {
    "id": 1,
    "categoryId": 2,
    "category": {
      "id": 2,
      "name": "Công lập",
      "description": "Trường công lập ...",
      "status": "active",
      "createdAt": "2024-01-01T00:00:00Z",
      "createdBy": "admin",
      "updatedAt": "2024-01-02T00:00:00Z",
      "updatedBy": "admin"
    },
    "admissionMethodIds": [1, 2, 3],
    "name": "Đại học A",
    "shortName": "DHA",
    "logoUrl": "logo-dha.png", // Tên file ảnh trên Minio
    "fanpage": "https://facebook.com/dha.edu.vn",
    "foundingYear": 2000,
    "province": {
      "id": 1,
      "name": "Hà Nội",
      "description": "Thủ đô Hà Nội",
      "region": "BAC"
    },
    "address": "123 Đường ABC",
    "email": "info@dha.edu.vn",
    "phone": "0123456789",
    "website": "https://dha.edu.vn",
    "description": "Trường đại học A ...",
    "status": "active",
    "createdAt": "2024-05-01T10:00:00Z",
    "createdBy": "admin",
    "updatedAt": "2024-05-01T11:00:00Z",
    "updatedBy": "admin"
  }
}
```

### 📋 **Thông tin chi tiết trong view detail:**

#### **Thông tin cơ bản:**

- `id`: ID trường
- `name`: Tên trường
- `shortName`: Tên viết tắt
- `logoUrl`: Tên file ảnh logo (lưu trên Minio, FE tự lấy URL)
- `fanpage`: Link fanpage Facebook của trường
- `foundingYear`: Năm thành lập
- `description`: Mô tả chi tiết
- `admissionMethodIds`: Danh sách ID phương thức tuyển sinh

#### **Thông tin liên hệ:**

- `address`: Địa chỉ trường
- `email`: Email liên hệ
- `phone`: Số điện thoại
- `website`: Website chính thức

#### **Thông tin phân loại:**

- `categoryId`: ID loại trường
- `category`: Object chi tiết loại trường (chỉ có trong API detail)
  - `id`: ID loại trường
  - `name`: Tên loại (Công lập, Tư thục, ...)
  - `description`: Mô tả loại trường
  - `status`: Trạng thái loại trường
- `province`: Object chi tiết tỉnh/thành
  - `id`: ID tỉnh/thành
  - `name`: Tên tỉnh/thành
  - `description`: Mô tả tỉnh/thành
  - `region`: Vùng miền (BAC, TRUNG, NAM)

#### **Thông tin hệ thống:**

- `status`: Trạng thái trường (active/deleted)
- `createdAt`: Thời gian tạo
- `createdBy`: Người tạo
- `updatedAt`: Thời gian cập nhật cuối
- `updatedBy`: Người cập nhật cuối

### 🎯 **Sử dụng cho Frontend:**

**Hiển thị thông tin trường:**

- Logo, tên, tên viết tắt
- Năm thành lập, địa chỉ
- Thông tin liên hệ (email, phone, website)
- Mô tả chi tiết
- Danh sách phương thức tuyển sinh (admissionMethodIds)

**Hiển thị phân loại:**

- Loại trường (công lập/tư thục) qua categoryId hoặc category
- Tỉnh/thành và vùng miền

**Hiển thị trạng thái:**

- Trạng thái hoạt động
- Thông tin audit (tạo/sửa)

---

## 3. Tạo trường mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "categoryId": 2,
  "name": "Đại học A",
  "shortName": "DHA",
  "logoUrl": "logo-dha.png", // Tên file ảnh trên Minio
  "fanpage": "https://facebook.com/dha.edu.vn",
  "foundingYear": 2000,
  "provinceId": 1,
  "address": "123 Đường ABC",
  "email": "info@dha.edu.vn",
  "phone": "0123456789",
  "website": "https://dha.edu.vn",
  "description": "Trường đại học A ...",
  "admissionMethodIds": [1, 2, 3]
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "University created successfully",
  "result": {
    "id": 1,
    "categoryId": 2,
    "admissionMethodIds": [1, 2, 3]
    // ... các trường khác như API detail ...
  }
}
```

---

## 4. Cập nhật trường (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới
- **Các trường có thể update:**
  - `categoryId`: Cập nhật loại trường (UniversityCategory)
  - `provinceId`: Cập nhật tỉnh/thành (Province) - khi update province, region sẽ được update gián tiếp theo province
  - `name`: Tên trường
  - `shortName`: Tên viết tắt
  - `logoUrl`: URL logo
  - `foundingYear`: Năm thành lập
  - `address`: Địa chỉ
  - `email`: Email
  - `phone`: Số điện thoại
  - `website`: Website
  - `description`: Mô tả
  - `admissionMethodIds`: Danh sách ID phương thức tuyển sinh
- **Response:**

```json
{
  "code": 1002,
  "message": "University updated successfully",
  "result": {
    "id": 1,
    "categoryId": 2,
    "admissionMethodIds": [1, 2, 3]
    // ... các trường khác như API detail ...
  }
}
```

---

## 5. Xóa trường (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "University deleted successfully"
}
```

---

## 6. Cập nhật trạng thái trường (ADMIN)

- **Endpoint:** `PATCH /{id}/status`
- **Body (JSON):**

```json
{
  "status": "active" // hoặc "deleted"
}
```

- **Response:**

```json
{
  "code": 1004,
  "message": "University status updated successfully",
  "result": {
    "id": 1,
    "name": "Đại học A",
    "status": "active",
    "categoryId": 2,
    "admissionMethodIds": [1, 2, 3]
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
- Các API tạo/sửa/xóa/cập nhật trạng thái chỉ dành cho ADMIN (cần accessToken hợp lệ).
- Trường `status` có thể là `active` hoặc `deleted` (không phải ACTIVE/DELETED).
- Trường `province` là object chi tiết tỉnh/thành (bao gồm region).
- Trường `categoryId` là ID loại trường, `category` là object chi tiết loại trường (ở API detail).
- Trường `admissionMethodIds` là mảng id phương thức tuyển sinh liên kết với trường.
- **Update Province/Category/Region:**
  - ✅ `categoryId`: Có thể update trực tiếp
  - ✅ `provinceId`: Có thể update trực tiếp
  - ✅ `region`: Được update gián tiếp khi thay đổi `provinceId` (region thuộc về province)
- Search tìm kiếm theo cả `name` và `shortName` của trường.
- Sort hỗ trợ các trường: `id`, `name`, `shortName`, `foundingYear`, `createdAt`, `updatedAt`.
- Filter theo `categoryId` và `provinceId` sử dụng ID thay vì tên để tối ưu performance.
- **Khác biệt giữa API list và detail:**
  - API list trả về `categoryId` (số), `admissionMethodIds` (mảng id)
  - API detail trả về `categoryId`, `category` (object), `admissionMethodIds`

---

## API hỗ trợ cho Frontend

### Lấy danh sách loại trường (dropdown)

- **Endpoint:** `GET /api/v1/university-categories/paginated`
- **Dùng cho:** Dropdown chọn loại trường

### Lấy danh sách tỉnh/thành (dropdown)

- **Endpoint:** `GET /api/v1/provinces`
- **Dùng cho:** Dropdown chọn tỉnh/thành

### Lấy danh sách phương thức tuyển sinh (dropdown)

- **Endpoint:** `GET /api/v1/admission-methods`
- **Dùng cho:** Dropdown chọn phương thức tuyển sinh

---

## 🎨 **HƯỚNG DẪN FRONTEND - CHUYỂN ĐỔI ID SANG NAME**

### 📋 **Tổng quan về việc hiển thị dropdown**

Trong hệ thống, categoryId chính là loại trường (type), không cần trường type riêng biệt.

### 🔄 **1. Các trường cần chuyển đổi ID → Name**

#### **A. University Category (Loại trường)**

- **API lấy danh sách:** `GET /api/v1/university-categories/paginated`
- **Response:**

```json
{
  "code": 1000,
  "message": "University categories fetched successfully",
  "result": {
    "items": [
      {
        "id": 1,
        "name": "Công lập",
        "description": "Trường công lập",
        "status": "active"
      },
      {
        "id": 2,
        "name": "Tư thục",
        "description": "Trường tư thục",
        "status": "active"
      }
    ]
  }
}
```

#### **B. Province (Tỉnh/Thành)**

- **API lấy danh sách:** `GET /api/v1/provinces`
- **Response:**

```json
{
  "code": 1000,
  "message": "Provinces fetched successfully",
  "result": [
    {
      "id": 1,
      "name": "Hà Nội",
      "description": "Thủ đô Hà Nội",
      "region": "BAC"
    },
    {
      "id": 2,
      "name": "TP. Hồ Chí Minh",
      "description": "Thành phố Hồ Chí Minh",
      "region": "NAM"
    }
  ]
}
```

#### **C. Admission Methods (Phương thức tuyển sinh)**

- **API lấy danh sách:** `GET /api/v1/admission-methods`
- **Response:**

```json
{
  "code": 1000,
  "message": "Admission methods fetched successfully",
  "result": [
    {
      "id": 1,
      "name": "Xét tuyển học bạ",
      "description": "Xét tuyển dựa trên học bạ THPT"
    },
    {
      "id": 2,
      "name": "Thi THPT Quốc gia",
      "description": "Xét tuyển dựa trên kết quả thi THPT"
    }
  ]
}
```

### 💻 **2. Implementation trên Frontend**

#### **A. Tạo Store/State Management**

```javascript
// stores/dropdownData.js
class DropdownDataStore {
  constructor() {
    this.categories = [];
    this.provinces = [];
    this.admissionMethods = [];
    this.loading = false;
  }

  // Load tất cả dropdown data khi khởi tạo app
  async loadAllDropdownData() {
    this.loading = true;
    try {
      await Promise.all([
        this.loadCategories(),
        this.loadProvinces(),
        this.loadAdmissionMethods(),
      ]);
    } catch (error) {
      console.error("Error loading dropdown data:", error);
    } finally {
      this.loading = false;
    }
  }

  async loadCategories() {
    const response = await fetch(
      "/api/v1/university-categories/paginated?size=100"
    );
    const data = await response.json();
    this.categories = data.result.items;
  }

  async loadProvinces() {
    const response = await fetch("/api/v1/provinces");
    const data = await response.json();
    this.provinces = data.result;
  }

  async loadAdmissionMethods() {
    const response = await fetch("/api/v1/admission-methods");
    const data = await response.json();
    this.admissionMethods = data.result;
  }

  // Helper methods để chuyển đổi ID → Name
  getCategoryName(categoryId) {
    const category = this.categories.find((cat) => cat.id === categoryId);
    return category ? category.name : "N/A";
  }

  getProvinceName(provinceId) {
    const province = this.provinces.find((prov) => prov.id === provinceId);
    return province ? province.name : "N/A";
  }

  getAdmissionMethodNames(admissionMethodIds) {
    if (!admissionMethodIds || admissionMethodIds.length === 0) return "N/A";
    return admissionMethodIds
      .map((id) => {
        const method = this.admissionMethods.find((m) => m.id === id);
        return method ? method.name : "N/A";
      })
      .join(", ");
  }
}

export default new DropdownDataStore();
```

#### **B. Component hiển thị University List**

```jsx
// components/UniversityList.jsx
import React, { useState, useEffect } from "react";
import dropdownStore from "../stores/dropdownData";

const UniversityList = () => {
  const [universities, setUniversities] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadUniversities();
    // Load dropdown data khi component mount
    dropdownStore.loadAllDropdownData();
  }, []);

  const loadUniversities = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/v1/universities?page=0&size=10");
      const data = await response.json();
      setUniversities(data.result.items);
    } catch (error) {
      console.error("Error loading universities:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="university-list">
      <h2>Danh sách trường đại học</h2>

      {loading ? (
        <div>Loading...</div>
      ) : (
        <table className="university-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tên trường</th>
              <th>Loại trường</th>
              <th>Tỉnh/Thành</th>
              <th>Vùng miền</th>
              <th>Trạng thái</th>
            </tr>
          </thead>
          <tbody>
            {universities.map((university) => (
              <tr key={university.id}>
                <td>{university.id}</td>
                <td>{university.name}</td>
                <td>
                  {/* Chuyển đổi categoryId → categoryName */}
                  {dropdownStore.getCategoryName(university.categoryId)}
                </td>
                <td>
                  {/* Hiển thị province name từ province object */}
                  {university.province?.name || "N/A"}
                </td>
                <td>
                  {/* Hiển thị region từ province object */}
                  {university.province?.region || "N/A"}
                </td>
                <td>
                  <span className={`status ${university.status}`}>
                    {university.status === "active" ? "Hoạt động" : "Đã xóa"}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default UniversityList;
```

#### **C. Component Form tạo/sửa University**

```jsx
// components/UniversityForm.jsx
import React, { useState, useEffect } from "react";
import dropdownStore from "../stores/dropdownData";

const UniversityForm = ({ university, onSubmit }) => {
  const [formData, setFormData] = useState({
    categoryId: "",
    name: "",
    shortName: "",
    logoUrl: "",
    foundingYear: "",
    provinceId: "",
    type: "public",
    address: "",
    email: "",
    phone: "",
    website: "",
    description: "",
    admissionMethodIds: [],
  });

  useEffect(() => {
    // Load dropdown data
    dropdownStore.loadAllDropdownData();

    // Pre-fill form nếu là edit mode
    if (university) {
      setFormData({
        categoryId: university.categoryId || "",
        name: university.name || "",
        shortName: university.shortName || "",
        logoUrl: university.logoUrl || "",
        foundingYear: university.foundingYear || "",
        provinceId: university.province?.id || "",
        type: university.type || "public",
        address: university.address || "",
        email: university.email || "",
        phone: university.phone || "",
        website: university.website || "",
        description: university.description || "",
        admissionMethodIds: university.admissionMethodIds || [],
      });
    }
  }, [university]);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="university-form">
      <div className="form-group">
        <label>Loại trường *</label>
        <select
          value={formData.categoryId}
          onChange={(e) =>
            setFormData({ ...formData, categoryId: parseInt(e.target.value) })
          }
          required
        >
          <option value="">Chọn loại trường</option>
          {dropdownStore.categories.map((category) => (
            <option key={category.id} value={category.id}>
              {category.name}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label>Tỉnh/Thành *</label>
        <select
          value={formData.provinceId}
          onChange={(e) =>
            setFormData({ ...formData, provinceId: parseInt(e.target.value) })
          }
          required
        >
          <option value="">Chọn tỉnh/thành</option>
          {dropdownStore.provinces.map((province) => (
            <option key={province.id} value={province.id}>
              {province.name} ({province.region})
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label>Phương thức tuyển sinh</label>
        <select
          multiple
          value={formData.admissionMethodIds}
          onChange={(e) => {
            const selectedOptions = Array.from(
              e.target.selectedOptions,
              (option) => parseInt(option.value)
            );
            setFormData({ ...formData, admissionMethodIds: selectedOptions });
          }}
        >
          {dropdownStore.admissionMethods.map((method) => (
            <option key={method.id} value={method.id}>
              {method.name}
            </option>
          ))}
        </select>
        <small>Giữ Ctrl để chọn nhiều</small>
      </div>

      {/* Các trường khác... */}
      <div className="form-group">
        <label>Tên trường *</label>
        <input
          type="text"
          value={formData.name}
          onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          required
        />
      </div>

      <div className="form-group">
        <label>Tên viết tắt</label>
        <input
          type="text"
          value={formData.shortName}
          onChange={(e) =>
            setFormData({ ...formData, shortName: e.target.value })
          }
        />
      </div>

      <button type="submit">{university ? "Cập nhật" : "Tạo mới"}</button>
    </form>
  );
};

export default UniversityForm;
```

#### **D. Component hiển thị University Detail**

```jsx
// components/UniversityDetail.jsx
import React, { useState, useEffect } from "react";

const UniversityDetail = ({ universityId }) => {
  const [university, setUniversity] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadUniversityDetail();
  }, [universityId]);

  const loadUniversityDetail = async () => {
    setLoading(true);
    try {
      const response = await fetch(`/api/v1/universities/${universityId}`);
      const data = await response.json();
      setUniversity(data.result);
    } catch (error) {
      console.error("Error loading university detail:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (!university) return <div>Không tìm thấy trường</div>;

  return (
    <div className="university-detail">
      <h2>Chi tiết trường: {university.name}</h2>

      <div className="detail-section">
        <h3>Thông tin cơ bản</h3>
        <div className="info-grid">
          <div className="info-item">
            <label>ID:</label>
            <span>{university.id}</span>
          </div>
          <div className="info-item">
            <label>Tên trường:</label>
            <span>{university.name}</span>
          </div>
          <div className="info-item">
            <label>Tên viết tắt:</label>
            <span>{university.shortName}</span>
          </div>
          <div className="info-item">
            <label>Loại trường:</label>
            <span>{university.category?.name}</span>
          </div>
          <div className="info-item">
            <label>Tỉnh/Thành:</label>
            <span>{university.province?.name}</span>
          </div>
          <div className="info-item">
            <label>Vùng miền:</label>
            <span>{university.province?.region}</span>
          </div>
          <div className="info-item">
            <label>Năm thành lập:</label>
            <span>{university.foundingYear}</span>
          </div>
          <div className="info-item">
            <label>Trạng thái:</label>
            <span className={`status ${university.status}`}>
              {university.status === "active" ? "Hoạt động" : "Đã xóa"}
            </span>
          </div>
        </div>
      </div>

      <div className="detail-section">
        <h3>Thông tin liên hệ</h3>
        <div className="info-grid">
          <div className="info-item">
            <label>Địa chỉ:</label>
            <span>{university.address}</span>
          </div>
          <div className="info-item">
            <label>Email:</label>
            <span>{university.email}</span>
          </div>
          <div className="info-item">
            <label>Điện thoại:</label>
            <span>{university.phone}</span>
          </div>
          <div className="info-item">
            <label>Website:</label>
            <a
              href={university.website}
              target="_blank"
              rel="noopener noreferrer"
            >
              {university.website}
            </a>
          </div>
        </div>
      </div>

      <div className="detail-section">
        <h3>Mô tả</h3>
        <p>{university.description}</p>
      </div>
    </div>
  );
};

export default UniversityDetail;
```

### 🎯 **3. Best Practices**

#### **A. Caching Dropdown Data**

```javascript
// Cache dropdown data để tránh gọi API nhiều lần
class DropdownCache {
  constructor() {
    this.cache = new Map();
    this.cacheExpiry = 5 * 60 * 1000; // 5 phút
  }

  set(key, data) {
    this.cache.set(key, {
      data,
      timestamp: Date.now(),
    });
  }

  get(key) {
    const cached = this.cache.get(key);
    if (!cached) return null;

    if (Date.now() - cached.timestamp > this.cacheExpiry) {
      this.cache.delete(key);
      return null;
    }

    return cached.data;
  }
}
```

#### **B. Error Handling**

```javascript
// Xử lý lỗi khi load dropdown data
const loadDropdownDataWithRetry = async (apiCall, retries = 3) => {
  for (let i = 0; i < retries; i++) {
    try {
      return await apiCall();
    } catch (error) {
      if (i === retries - 1) throw error;
      await new Promise((resolve) => setTimeout(resolve, 1000 * (i + 1)));
    }
  }
};
```

#### **C. Loading States**

```jsx
// Hiển thị loading state cho dropdown
const DropdownWithLoading = ({ data, loading, error, ...props }) => {
  if (loading) {
    return (
      <select disabled>
        <option>Đang tải...</option>
      </select>
    );
  }

  if (error) {
    return (
      <select disabled>
        <option>Lỗi tải dữ liệu</option>
      </select>
    );
  }

  return (
    <select {...props}>
      <option value="">Chọn...</option>
      {data.map((item) => (
        <option key={item.id} value={item.id}>
          {item.name}
        </option>
      ))}
    </select>
  );
};
```

### 📝 **4. Tóm tắt**

1. **Load dropdown data** khi khởi tạo app
2. **Cache data** để tránh gọi API nhiều lần
3. **Chuyển đổi ID → Name** bằng helper methods
4. **Hiển thị loading states** khi đang tải
5. **Xử lý lỗi** khi load data thất bại
6. **Sử dụng dropdown** cho form input
7. **Hiển thị tên** thay vì ID trong tables/lists

Với cách implement này, Frontend sẽ hiển thị tên người dùng thân thiện thay vì các ID khó hiểu.
