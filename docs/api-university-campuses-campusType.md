# API: University (Updated with Campus Support)

## 🆕 **Các thay đổi quan trọng (2025-07)**

### **Database Schema Changes:**

- **University Entity**: Thêm `universityCode` và `nameEn`
- **Campus Entity**: Entity mới để quản lý nhiều cơ sở của một trường
- **Relationships**: University có nhiều Campus, Campus thuộc về một Province

### **API Changes:**

- **University**: Thêm field `universityCode`, `nameEn`, và relationship `campuses`
- **Campus**: APIs mới để CRUD cơ sở trường đại học
- **Search**: Hỗ trợ tìm kiếm trường theo tỉnh thông qua các cơ sở

### **Logo Upload:**

- FE **không gửi URL** logo, chỉ upload file ảnh qua trường `logoFile` (multipart/form-data)
- BE sẽ upload lên Minio và trả về trường `logoUrl` là URL public ảnh logo

### **Fanpage:**

- Đã bổ sung field `fanpage` (link Facebook) cho trường đại học

---

## 🏫 **University Entity Updates**

### **Các field mới trong University:**

```java
@Column(name = "university_code", unique = true, length = 20)
private String universityCode; // Mã trường (VD: VNU_HN, HUST, NEU)

@Column(name = "name_en")
private String nameEn; // Tên tiếng Anh của trường

@OneToMany(mappedBy = "university", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Campus> campuses = new ArrayList<>(); // Danh sách cơ sở
```

### **Các field deprecated (đã chuyển sang Campus):**

- `address`: deprecated - thông tin địa chỉ đã chuyển sang Campus
- `province`: deprecated - thông tin tỉnh đã chuyển sang Campus
- `region`: deprecated - thông tin vùng đã chuyển sang Campus
- `provinceId`: deprecated - ID tỉnh đã chuyển sang Campus

**Note**: Các field này không còn xuất hiện trong API response và form input.

---

## 🏢 **Campus Entity (New)**

### **Base fields:**

```java
private Long id;
private LocalDateTime createdAt;
private String createdBy;
private String status; // ACTIVE, INACTIVE, DELETED
private LocalDateTime updatedAt;
private String updatedBy;
```

### **Business fields:**

```java
@Column(name = "campus_name", nullable = false)
private String campusName; // Tên cơ sở

@Column(name = "campus_code", length = 50)
private String campusCode; // Mã cơ sở trong trường (VD: "MAIN", "CS1")

@Column(name = "address", nullable = false, columnDefinition = "TEXT")
private String address; // Địa chỉ chi tiết

@Column(name = "phone", length = 20)
private String phone;

@Column(name = "email")
private String email;

@Column(name = "website")
private String website;

@Column(name = "is_main_campus")
private Boolean isMainCampus = false; // Cơ sở chính

@Enumerated(EnumType.STRING)
@Column(name = "campus_type")
private CampusType campusType = CampusType.BRANCH;

@Column(name = "description", columnDefinition = "TEXT")
private String description;

@Column(name = "established_year")
private Integer establishedYear;

@Column(name = "area_hectares", precision = 10, scale = 2)
private BigDecimal areaHectares;
```

### **Relationships:**

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "university_id", nullable = false)
private University university;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "province_id", nullable = false)
private Province province;
```

### **CampusType Enum:**

```java
public enum CampusType {
    MAIN,           // Cơ sở chính
    BRANCH,         // Cơ sở phân hiệu
    TRAINING_CENTER, // Trung tâm đào tạo
    RESEARCH_CENTER  // Trung tâm nghiên cứu
}
```

---

## ⚠️ **HƯỚNG DẪN FE VỀ UPLOAD ẢNH LOGO**

- Khi tạo/cập nhật trường, **FE phải gửi file ảnh logo qua trường `logoFile`** (kiểu `multipart/form-data`)
- Không gửi trường `logoUrl` kiểu string trong request
- Sau khi upload thành công, **BE sẽ trả về trường `logoUrl` là URL public ảnh logo**
- **Ví dụ gửi form-data:**
  - Key: `logoFile` (type: File)
  - Các key khác: `universityCode`, `nameEn`, `categoryId`, `name`, ... (type: Text)

---

## 🛡️ Quy tắc validate dữ liệu

### **University validation:**

| Field          | Bắt buộc | Kiểu    | Độ dài tối đa | Ghi chú                      |
| -------------- | -------- | ------- | ------------- | ---------------------------- |
| universityCode |          | String  | 20            | Mã trường, unique (optional) |
| nameEn         |          | String  | 255           | Tên tiếng Anh                |
| name           | ✔️       | String  | 255           | Tên trường                   |
| shortName      |          | String  | 50            | Tên viết tắt                 |
| logoFile       |          | File    | 5MB           | Ảnh logo, chỉ nhận file ảnh  |
| fanpage        |          | String  | 255           | Link Facebook                |
| foundingYear   |          | Integer | 4             | Năm thành lập (optional)     |
| email          |          | String  | 255           | Email liên hệ (optional)     |
| phone          |          | String  | 20            | Số điện thoại (optional)     |
| website        |          | String  | 255           | Website (optional)           |
| description    |          | String  | TEXT          | Mô tả (optional)             |
| categoryId     | ✔️       | Integer |               | ID loại trường               |

### **Campus validation:**

| Field        | Bắt buộc | Kiểu    | Độ dài tối đa | Ghi chú                           |
| ------------ | -------- | ------- | ------------- | --------------------------------- |
| campusName   | ✔️       | String  | 255           | Tên cơ sở                         |
| campusCode   |          | String  | 50            | Mã cơ sở, unique trong university |
| address      | ✔️       | String  | TEXT          | Địa chỉ chi tiết                  |
| phone        |          | String  | 20            | Số điện thoại                     |
| email        |          | String  | 255           | Email liên hệ                     |
| website      |          | String  | 255           | Website                           |
| isMainCampus |          | Boolean |               | Cơ sở chính (default: false)      |
| campusType   |          | Enum    |               | Loại cơ sở (default: BRANCH)      |
| universityId | ✔️       | Integer |               | ID trường sở hữu                  |
| provinceId   | ✔️       | Integer |               | ID tỉnh/thành của cơ sở           |

---

## 📋 **UNIVERSITY APIs**

## 1. Lấy danh sách trường (với campus info)

- **Endpoint:** `GET /api/v1/universities`
- **Query params:**

  - `search`: Tìm kiếm theo tên trường, tên tiếng Anh, mã trường
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp (ví dụ: `name,asc`, `universityCode,desc`)
  - `categoryId`: Lọc theo ID loại trường
  - `provinceId`: Lọc theo ID tỉnh/thành (tìm trường có cơ sở trong tỉnh - thông qua Campus)
  - `includeCampuses`: Include campus info (default: false)

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
        "universityCode": "VNU_HN",
        "nameEn": "Vietnam National University, Hanoi",
        "categoryId": 2,
        "admissionMethodIds": [1, 2, 3],
        "name": "Đại học Quốc gia Hà Nội",
        "shortName": "ĐHQGHN",
        "logoUrl": "logo-vnu-hn.png",
        "fanpage": "https://facebook.com/vnu.edu.vn",
        "foundingYear": 1993,
        "email": "info@vnu.edu.vn",
        "phone": "024-37547460",
        "website": "https://vnu.edu.vn",
        "description": "Đại học Quốc gia Hà Nội...",
        "status": "active",
        "campusCount": 3,
        "campuses": [
          // Chỉ có khi includeCampuses=true
          {
            "id": 1,
            "campusName": "Cơ sở chính Xuân Thủy",
            "campusCode": "MAIN",
            "isMainCampus": true,
            "campusType": "MAIN",
            "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
            "province": {
              "id": 1,
              "name": "Hà Nội"
            }
          }
        ]
      }
    ]
  }
}
```

## 2. Lấy chi tiết trường theo ID

- **Endpoint:** `GET /api/v1/universities/{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "University fetched successfully",
  "result": {
    "id": 1,
    "universityCode": "VNU_HN",
    "nameEn": "Vietnam National University, Hanoi",
    "categoryId": 2,
    "category": {
      "id": 2,
      "name": "Công lập",
      "description": "Trường công lập..."
    },
    "admissionMethodIds": [1, 2, 3],
    "name": "Đại học Quốc gia Hà Nội",
    "shortName": "ĐHQGHN",
    "logoUrl": "logo-vnu-hn.png",
    "fanpage": "https://facebook.com/vnu.edu.vn",
    "foundingYear": 1993,
    "email": "info@vnu.edu.vn",
    "phone": "024-37547460",
    "website": "https://vnu.edu.vn",
    "description": "Đại học Quốc gia Hà Nội...",
    "status": "active",
    "campuses": [
      {
        "id": 1,
        "campusName": "Cơ sở chính Xuân Thủy",
        "campusCode": "MAIN",
        "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
        "phone": "024-37547460",
        "email": "info@vnu.edu.vn",
        "website": "https://vnu.edu.vn",
        "isMainCampus": true,
        "campusType": "MAIN",
        "description": "Cơ sở chính của ĐHQGHN",
        "establishedYear": 1993,
        "areaHectares": 50.5,
        "province": {
          "id": 1,
          "name": "Hà Nội",
          "region": "BAC"
        },
        "status": "active"
      }
    ]
  }
}
```

## 3. Tạo trường mới (ADMIN)

- **Endpoint:** `POST /api/v1/universities`
- **Content-Type:** `multipart/form-data`
- **Body:**

```
universityCode: VNU_HN
nameEn: Vietnam National University, Hanoi
categoryId: 2
name: Đại học Quốc gia Hà Nội
shortName: ĐHQGHN
logoFile: [FILE] // File ảnh logo
fanpage: https://facebook.com/vnu.edu.vn
foundingYear: 1993
email: info@vnu.edu.vn
phone: 024-37547460
website: https://vnu.edu.vn
description: Đại học Quốc gia Hà Nội...
admissionMethodIds: 1,2,3
```

- **Response:**

```json
{
  "code": 1001,
  "message": "University created successfully",
  "result": {
    "id": 1,
    "universityCode": "VNU_HN",
    "nameEn": "Vietnam National University, Hanoi"
    // ... các trường khác như API detail ...
  }
}
```

## 4. Cập nhật trường (ADMIN)

- **Endpoint:** `PUT /api/v1/universities/{id}`
- **Content-Type:** `multipart/form-data`
- **Body:** Giống như tạo mới (logoFile optional nếu không đổi ảnh)

## 5. Xóa trường (ADMIN)

- **Endpoint:** `DELETE /api/v1/universities/{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "University deleted successfully"
}
```

## 6. Tìm trường theo mã trường

- **Endpoint:** `GET /api/v1/universities/by-code/{universityCode}`
- **Response:** Giống như API detail

---

## 🏢 **CAMPUS TYPES APIs**

## 1. Lấy danh sách loại cơ sở (cho dropdown)

- **Endpoint:** `GET /api/v1/campus-types`
- **Query params:**

  - `page`, `size`, `sort`: Pagination (optional)
  - `status`: Lọc theo trạng thái (default: active)

- **Response:**

```json
{
  "code": 1000,
  "message": "Campus types fetched successfully",
  "result": [
    {
      "id": 1,
      "name": "Cơ sở chính",
      "code": "MAIN",
      "description": "Cơ sở chính của trường",
      "isMainType": true,
      "sortOrder": 1,
      "status": "active",
      "createdAt": "2024-01-01T00:00:00Z",
      "createdBy": "admin"
    },
    {
      "id": 2,
      "name": "Cơ sở phân hiệu",
      "code": "BRANCH",
      "description": "Cơ sở phân hiệu của trường",
      "isMainType": false,
      "sortOrder": 2,
      "status": "active",
      "createdAt": "2024-01-01T00:00:00Z",
      "createdBy": "admin"
    },
    {
      "id": 3,
      "name": "Trung tâm đào tạo",
      "code": "TRAINING_CENTER",
      "description": "Trung tâm đào tạo nghề nghiệp",
      "isMainType": false,
      "sortOrder": 3,
      "status": "active",
      "createdAt": "2024-01-01T00:00:00Z",
      "createdBy": "admin"
    }
  ]
}
```

## 2. Lấy chi tiết loại cơ sở theo ID

- **Endpoint:** `GET /api/v1/campus-types/{id}`

## 3. Tạo loại cơ sở mới (ADMIN)

- **Endpoint:** `POST /api/v1/campus-types`
- **Body (JSON):**

```json
{
  "name": "Trung tâm thực hành",
  "code": "PRACTICE_CENTER",
  "description": "Trung tâm thực hành nghề nghiệp",
  "isMainType": false,
  "sortOrder": 5
}
```

## 4. Cập nhật loại cơ sở (ADMIN)

- **Endpoint:** `PUT /api/v1/campus-types/{id}`
- **Body:** Giống như tạo mới

## 5. Xóa loại cơ sở (ADMIN)

- **Endpoint:** `DELETE /api/v1/campus-types/{id}`
- **Note:** Không cho phép xóa nếu có Campus đang sử dụng loại này

---

## 🏢 **CAMPUS APIs**

## 1. Lấy danh sách cơ sở

- **Endpoint:** `GET /api/v1/campuses`
- **Query params:**

  - `search`: Tìm kiếm theo tên cơ sở
  - `page`, `size`, `sort`: Pagination và sorting
  - `universityId`: Lọc theo ID trường
  - `provinceId`: Lọc theo ID tỉnh/thành
  - `campusType`: Lọc theo loại cơ sở (MAIN, BRANCH, TRAINING_CENTER, RESEARCH_CENTER)
  - `isMainCampus`: Lọc cơ sở chính (true/false)

- **Response:**

```json
{
  "code": 1000,
  "message": "Campuses fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5,
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
        "campusType": "MAIN",
        "university": {
          "id": 1,
          "universityCode": "VNU_HN",
          "name": "Đại học Quốc gia Hà Nội",
          "nameEn": "Vietnam National University, Hanoi"
        },
        "province": {
          "id": 1,
          "name": "Hà Nội",
          "region": "BAC"
        },
        "status": "active"
      }
    ]
  }
}
```

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
    "campusType": "MAIN",
    "description": "Cơ sở chính của ĐHQGHN",
    "establishedYear": 1993,
    "areaHectares": 50.5,
    "university": {
      "id": 1,
      "universityCode": "VNU_HN",
      "name": "Đại học Quốc gia Hà Nội",
      "nameEn": "Vietnam National University, Hanoi",
      "logoUrl": "logo-vnu-hn.png"
    },
    "province": {
      "id": 1,
      "name": "Hà Nội",
      "description": "Thủ đô Hà Nội",
      "region": "BAC"
    },
    "status": "active",
    "createdAt": "2024-05-01T10:00:00Z",
    "createdBy": "admin",
    "updatedAt": "2024-05-01T11:00:00Z",
    "updatedBy": "admin"
  }
}
```

## 3. Tạo cơ sở mới (ADMIN)

- **Endpoint:** `POST /api/v1/campuses`
- **Body (JSON):**

```json
{
  "universityId": 1,
  "provinceId": 1,
  "campusName": "Cơ sở Nguyễn Trãi",
  "campusCode": "NT",
  "address": "25 Nguyễn Trãi, Thanh Xuân, Hà Nội",
  "phone": "024-38584943",
  "email": "nt@vnu.edu.vn",
  "website": "https://nt.vnu.edu.vn",
  "isMainCampus": false,
  "campusType": "BRANCH",
  "description": "Cơ sở Nguyễn Trãi của ĐHQGHN",
  "establishedYear": 1995,
  "areaHectares": 15.2
}
```

## 4. Cập nhật cơ sở (ADMIN)

- **Endpoint:** `PUT /api/v1/campuses/{id}`
- **Body:** Giống như tạo mới

## 5. Xóa cơ sở (ADMIN)

- **Endpoint:** `DELETE /api/v1/campuses/{id}`

## 6. Lấy cơ sở theo trường

- **Endpoint:** `GET /api/v1/campuses/by-university/{universityId}`
- **Query params:** `includeInactive` (default: false)

## 7. Lấy cơ sở theo tỉnh

- **Endpoint:** `GET /api/v1/campuses/by-province/{provinceId}`
- **Query params:** `page`, `size`, `sort`

---

## 🔍 **SEARCH APIs**

## 1. Tìm trường có cơ sở trong tỉnh

- **Endpoint:** `GET /api/v1/universities/by-province/{provinceId}`
- **Query params:**

  - `includeMainCampusOnly`: Chỉ tính cơ sở chính (default: false)
  - `page`, `size`, `sort`

- **Response:**

```json
{
  "code": 1000,
  "message": "Universities in province fetched successfully",
  "result": {
    "province": {
      "id": 1,
      "name": "Hà Nội",
      "region": "BAC"
    },
    "page": 0,
    "size": 10,
    "totalElements": 25,
    "totalPages": 3,
    "items": [
      {
        "id": 1,
        "universityCode": "VNU_HN",
        "name": "Đại học Quốc gia Hà Nội",
        "nameEn": "Vietnam National University, Hanoi",
        "logoUrl": "logo-vnu-hn.png",
        "campusesInProvince": [
          {
            "id": 1,
            "campusName": "Cơ sở chính Xuân Thủy",
            "campusCode": "MAIN",
            "address": "144 Xuân Thủy, Cầu Giấy, Hà Nội",
            "isMainCampus": true,
            "campusType": "MAIN"
          },
          {
            "id": 2,
            "campusName": "Cơ sở Nguyễn Trãi",
            "campusCode": "NT",
            "address": "25 Nguyễn Trãi, Thanh Xuân, Hà Nội",
            "isMainCampus": false,
            "campusType": "BRANCH"
          }
        ]
      }
    ]
  }
}
```

## 2. Thống kê cơ sở theo tỉnh

- **Endpoint:** `GET /api/v1/statistics/campuses-by-province`
- **Response:**

```json
{
  "code": 1000,
  "message": "Campus statistics by province fetched successfully",
  "result": [
    {
      "province": {
        "id": 1,
        "name": "Hà Nội",
        "region": "BAC"
      },
      "totalCampuses": 45,
      "totalUniversities": 25,
      "mainCampuses": 25,
      "branchCampuses": 15,
      "trainingCenters": 3,
      "researchCenters": 2
    }
  ]
}
```

---

## 📝 **Migration Notes**

### **Dữ liệu hiện tại:**

1. **University** có address, province, region → Tạo Campus chính tương ứng
2. **Mỗi University** sẽ có ít nhất 1 Campus với `isMainCampus = true`

### **Business Rules:**

1. **universityCode** phải unique across toàn hệ thống
2. **campusCode** phải unique trong phạm vi 1 university
3. Mỗi university phải có ít nhất 1 campus với `isMainCampus = true`
4. Khi tạo university mới, tự động tạo campus chính
5. Khi xóa university, tự động xóa tất cả campus của university đó

### **API Backwards Compatibility:**

- Các API University cũ vẫn hoạt động
- Thêm field `campusCount` trong University list
- Thêm array `campuses` trong University detail
- **Field deprecated trong University**: `address`, `province`, `region`, `provinceId` không còn trong response và form input
- **Migration**: Dữ liệu cũ từ University đã được chuyển sang Campus tương ứng
- **Search by Province**: Tìm kiếm University theo provinceId sẽ tìm thông qua Campus relationship

---

## 📋 **Frontend Integration**

### **Dropdown APIs (unchanged):**

- `GET /api/v1/university-categories/paginated` - Loại trường
- `GET /api/v1/provinces` - Tỉnh/thành
- `GET /api/v1/admission-methods` - Phương thức tuyển sinh

### **New Campus Types for dropdown:**

```javascript
const CAMPUS_TYPES = [
  { value: "MAIN", label: "Cơ sở chính" },
  { value: "BRANCH", label: "Cơ sở phân hiệu" },
  { value: "TRAINING_CENTER", label: "Trung tâm đào tạo" },
  { value: "RESEARCH_CENTER", label: "Trung tâm nghiên cứu" },
];
```

### **Search Enhancement:**

- Tìm kiếm University theo province sẽ tìm qua Campus
- Hiển thị số lượng cơ sở của mỗi trường
- Filter Campus theo University và Province

### **Form Updates:**

- University Form: Thêm `universityCode`, `nameEn`
- Campus Form: Form mới để tạo/sửa cơ sở
- University Detail: Hiển thị danh sách cơ sở

### **Logo Display (unchanged):**

```javascript
const logoUrl = `${minioBaseUrl}/${bucketName}/${university.logoUrl}`;
```
