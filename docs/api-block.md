# Hướng dẫn sử dụng API Block (Khối)

**Base URL:** `/api/v1/blocks`

---

## 1. Lấy danh sách khối (có tìm kiếm, phân trang, sắp xếp)

- **Endpoint:** `GET /`
- **Query params:**
  - `search`: Tìm kiếm theo tên khối (tùy chọn)
  - `page`: Số trang (mặc định 0)
  - `size`: Số lượng mỗi trang (mặc định 10)
  - `sort`: Sắp xếp, ví dụ: `name,asc` hoặc `id,desc` (tùy chọn)
- **Response:**

```json
{
  "code": 1000,
  "message": "List of blocks fetched successfully",
  "result": {
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1,
    "items": [
      {
        "id": 1,
        "name": "Khối A",
        "description": "Khối A - Toán, Lý, Hóa",
        "subjectCombinations": [
          {
            "id": 1,
            "name": "A00",
            "description": "Toán, Lý, Hóa",
            "status": "ACTIVE"
          },
          {
            "id": 2,
            "name": "A01",
            "description": "Toán, Lý, Anh",
            "status": "ACTIVE"
          }
        ],
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "name": "Khối D",
        "description": "Khối D - Toán, Văn, Anh",
        "subjectCombinations": [
          {
            "id": 3,
            "name": "D01",
            "description": "Toán, Văn, Anh",
            "status": "ACTIVE"
          },
          {
            "id": 4,
            "name": "D02",
            "description": "Toán, Văn, Nga",
            "status": "ACTIVE"
          }
        ],
        "status": "ACTIVE"
      }
    ]
  }
}
```

---

## 2. Lấy chi tiết khối theo ID

- **Endpoint:** `GET /{id}`
- **Response:**

```json
{
  "code": 1000,
  "message": "Block fetched successfully",
  "result": {
    "id": 1,
    "name": "Khối A",
    "description": "Khối A - Toán, Lý, Hóa",
    "subjectCombinations": [
      {
        "id": 1,
        "name": "A00",
        "description": "Toán, Lý, Hóa",
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "name": "A01",
        "description": "Toán, Lý, Anh",
        "status": "ACTIVE"
      }
    ],
    "status": "ACTIVE"
  }
}
```

---

## 3. Tạo khối mới (ADMIN)

- **Endpoint:** `POST /`
- **Body (JSON):**

```json
{
  "name": "Khối A",
  "description": "Khối A - Toán, Lý, Hóa",
  "subjectCombinationIds": [1, 2, 3]
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "Block created successfully",
  "result": {
    "id": 1,
    "name": "Khối A",
    "description": "Khối A - Toán, Lý, Hóa",
    "subjectCombinations": [
      {
        "id": 1,
        "name": "A00",
        "description": "Toán, Lý, Hóa",
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "name": "A01",
        "description": "Toán, Lý, Anh",
        "status": "ACTIVE"
      },
      {
        "id": 3,
        "name": "A02",
        "description": "Toán, Lý, Sinh",
        "status": "ACTIVE"
      }
    ],
    "status": "ACTIVE"
  }
}
```

---

## 4. Cập nhật khối (ADMIN)

- **Endpoint:** `PUT /{id}`
- **Body (JSON):**
  - Giống như tạo mới

```json
{
  "name": "Khối A",
  "description": "Khối A - Toán, Lý, Hóa (Cập nhật)",
  "subjectCombinationIds": [1, 2, 4]
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "Block updated successfully",
  "result": {
    "id": 1,
    "name": "Khối A",
    "description": "Khối A - Toán, Lý, Hóa (Cập nhật)",
    "subjectCombinations": [
      {
        "id": 1,
        "name": "A00",
        "description": "Toán, Lý, Hóa",
        "status": "ACTIVE"
      },
      {
        "id": 2,
        "name": "A01",
        "description": "Toán, Lý, Anh",
        "status": "ACTIVE"
      },
      {
        "id": 4,
        "name": "A03",
        "description": "Toán, Lý, Địa",
        "status": "ACTIVE"
      }
    ],
    "status": "ACTIVE"
  }
}
```

---

## 5. Xóa khối (ADMIN)

- **Endpoint:** `DELETE /{id}`
- **Response:**

```json
{
  "code": 1003,
  "message": "Block deleted successfully"
}
```

---

## 6. Cập nhật trạng thái khối (ADMIN)

- **Endpoint:** `PATCH /{id}/status`
- **Query params:**
  - `status`: Trạng thái mới (ACTIVE, INACTIVE, DELETED)
- **Response:**

```json
{
  "code": 1004,
  "message": "Block status updated successfully",
  "result": {
    "id": 1,
    "name": "Khối A",
    "description": "Khối A - Toán, Lý, Hóa",
    "subjectCombinations": [
      {
        "id": 1,
        "name": "A00",
        "description": "Toán, Lý, Hóa",
        "status": "ACTIVE"
      }
    ],
    "status": "INACTIVE"
  }
}
```

---

## Lưu ý cho Frontend

### Lấy danh sách tổ hợp môn để chọn cho khối

Để lấy danh sách tổ hợp môn khi tạo/cập nhật khối, gọi API:

**GET** `/api/v1/subject-combinations`

```javascript
// Ví dụ sử dụng trong frontend
const fetchSubjectCombinations = async () => {
  try {
    const response = await fetch("/api/v1/subject-combinations?size=100");
    const data = await response.json();

    if (data.code === 1000) {
      const combinations = data.result.items;

      // Tạo dropdown cho việc chọn tổ hợp môn
      const selectElement = document.getElementById("subjectCombinations");
      combinations.forEach((combo) => {
        const option = document.createElement("option");
        option.value = combo.id;
        option.textContent = `${combo.name} - ${combo.description}`;
        selectElement.appendChild(option);
      });
    }
  } catch (error) {
    console.error("Error fetching subject combinations:", error);
  }
};
```

### Cấu trúc dữ liệu

- **Block**: Đại diện cho một khối thi (ví dụ: Khối A, Khối D)
- **SubjectCombination**: Các tổ hợp môn thuộc khối đó (ví dụ: A00, A01, D01, D02)
- Một khối có thể có nhiều tổ hợp môn
- Tên tổ hợp môn thường bắt đầu bằng chữ cái của khối (A00, A01 thuộc Khối A)

### Validation

- Tên khối không được trùng lặp
- Tên khối tối đa 50 ký tự
- Mô tả khối không bắt buộc
- Danh sách tổ hợp môn không bắt buộc (có thể thêm sau)

---

### Lấy danh sách môn thi (ExamSubject) để chọn cho tổ hợp môn

Khi tạo/cập nhật tổ hợp môn (SubjectCombination), frontend cần lấy danh sách các môn thi để hiển thị dropdown cho người dùng chọn.

**GET** `/api/v1/exam-subjects`

Ví dụ response:

```json
{
  "code": 1000,
  "message": "List of exam subjects fetched successfully",
  "result": {
    "page": 0,
    "size": 100,
    "totalElements": 5,
    "totalPages": 1,
    "items": [
      { "id": 1, "name": "Toán học", "shortName": "Toán", "status": "ACTIVE" },
      { "id": 2, "name": "Vật lý", "shortName": "Lý", "status": "ACTIVE" },
      { "id": 3, "name": "Hóa học", "shortName": "Hóa", "status": "ACTIVE" },
      { "id": 4, "name": "Tiếng Anh", "shortName": "Anh", "status": "ACTIVE" },
      { "id": 5, "name": "Ngữ văn", "shortName": "Văn", "status": "ACTIVE" }
    ]
  }
}
```

Ví dụ sử dụng trong frontend:

```javascript
const fetchExamSubjects = async () => {
  try {
    const response = await fetch("/api/v1/exam-subjects?size=100");
    const data = await response.json();
    if (data.code === 1000) {
      const subjects = data.result.items;
      const selectElement = document.getElementById("examSubjects");
      subjects.forEach((subject) => {
        const option = document.createElement("option");
        option.value = subject.id;
        option.textContent = `${subject.name} (${subject.shortName})`;
        selectElement.appendChild(option);
      });
    }
  } catch (error) {
    console.error("Error fetching exam subjects:", error);
  }
};
```

**Lưu ý:**

- Chỉ lấy các môn có `status` là `ACTIVE` để cho phép chọn.
- Khi gửi dữ liệu tạo/cập nhật tổ hợp môn, chỉ cần gửi mảng `examSubjectIds` (danh sách id các môn thi đã chọn).
