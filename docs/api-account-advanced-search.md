# API Advanced Search - Account Management

## Tổng quan

API `advanced-search` sử dụng **Criteria API** để tìm kiếm và lọc tài khoản với nhiều điều kiện phức tạp. API này hỗ trợ phân trang, sắp xếp và filter theo nhiều tiêu chí khác nhau.

---

## Endpoint

```
GET /api/v1/accounts/advanced-search
```

**Base URL:** `http://localhost:8080`

---

## Tham số (Query Parameters)

### Tham số tìm kiếm

| Parameter   | Type    | Required | Default | Description                                          |
| ----------- | ------- | -------- | ------- | ---------------------------------------------------- |
| `search`    | String  | ❌       | -       | Tìm kiếm theo: fullName, email, username, phone      |
| `status`    | String  | ❌       | -       | Trạng thái tài khoản: `ACTIVE`, `INACTIVE`, `BANNED` |
| `gender`    | String  | ❌       | -       | Giới tính: `MALE`, `FEMALE`, `OTHER`                 |
| `isDeleted` | Boolean | ❌       | -       | Tài khoản đã xóa: `true`, `false`                    |

### Tham số ngày tháng

| Parameter     | Type   | Required | Default | Description                                     |
| ------------- | ------ | -------- | ------- | ----------------------------------------------- |
| `createdFrom` | String | ❌       | -       | Ngày tạo từ (ISO-8601): `2024-01-01T00:00:00Z`  |
| `createdTo`   | String | ❌       | -       | Ngày tạo đến (ISO-8601): `2024-12-31T23:59:59Z` |
| `loginFrom`   | String | ❌       | -       | Ngày đăng nhập cuối từ (ISO-8601)               |
| `loginTo`     | String | ❌       | -       | Ngày đăng nhập cuối đến (ISO-8601)              |

### Tham số phân trang & sắp xếp

| Parameter | Type    | Required | Default     | Description                  |
| --------- | ------- | -------- | ----------- | ---------------------------- |
| `page`    | Integer | ❌       | `0`         | Số trang (zero-based)        |
| `size`    | Integer | ❌       | `10`        | Số lượng mỗi trang           |
| `sortBy`  | String  | ❌       | `createdAt` | Trường sắp xếp               |
| `sortDir` | String  | ❌       | `desc`      | Hướng sắp xếp: `asc`, `desc` |

---

## Headers

```http
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

**Quyền truy cập:** `ADMIN`, `CONSULTANT`

---

## Ví dụ Request

### 1. Tìm kiếm cơ bản

```bash
curl -X GET "http://localhost:8080/api/v1/accounts/advanced-search?search=admin&page=0&size=10" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 2. Filter theo trạng thái và giới tính

```bash
curl -X GET "http://localhost:8080/api/v1/accounts/advanced-search?status=ACTIVE&gender=MALE&page=0&size=20" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 3. Filter theo khoảng thời gian

```bash
curl -X GET "http://localhost:8080/api/v1/accounts/advanced-search?createdFrom=2024-01-01T00:00:00Z&createdTo=2024-12-31T23:59:59Z&page=0&size=15" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### 4. Tìm kiếm phức tạp

```bash
curl -X GET "http://localhost:8080/api/v1/accounts/advanced-search?search=john&status=ACTIVE&gender=MALE&isDeleted=false&createdFrom=2024-01-01T00:00:00Z&page=0&size=10&sortBy=lastLoginAt&sortDir=desc" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

---

## Response Format

### Success Response (200 OK)

```json
{
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "items": [
    {
      "id": "550e29b4-1a71-64-4665-000000000001",
      "fullName": "Nguyễn Văn A",
      "username": "nguyenvana",
      "email": "a@example.com",
      "phone": "0912345678",
      "gender": "MALE",
      "roles": ["ROLE_USER", "ROLE_ADMIN"],
      "status": "ACTIVE",
      "loginCount": 12,
      "lastLoginAt": "2024-06-20T10:30:00Z",
      "createdAt": "2024-01-18T00:00:00Z",
      "isDeleted": false
    }
  ]
}
```

### Error Response (500 Internal Server Error)

```json
{
  "timestamp": "2024-07-20T16:30:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error in advanced search"
}
```

---

## Các trường sắp xếp có sẵn

| Trường        | Mô tả               |
| ------------- | ------------------- |
| `createdAt`   | Ngày tạo tài khoản  |
| `lastLoginAt` | Ngày đăng nhập cuối |
| `fullName`    | Họ tên              |
| `username`    | Tên đăng nhập       |
| `email`       | Email               |
| `loginCount`  | Số lần đăng nhập    |

---

## Frontend Implementation

### JavaScript/TypeScript

```typescript
interface AdvancedSearchParams {
  search?: string;
  status?: "ACTIVE" | "INACTIVE" | "BANNED";
  gender?: "MALE" | "FEMALE" | "OTHER";
  isDeleted?: boolean;
  createdFrom?: string;
  createdTo?: string;
  loginFrom?: string;
  loginTo?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: "asc" | "desc";
}

interface AccountResponse {
  id: string;
  fullName: string;
  username: string;
  email: string;
  phone: string;
  gender: string;
  roles: string[];
  status: string;
  loginCount: number;
  lastLoginAt: string;
  createdAt: string;
  isDeleted: boolean;
}

interface PageResponse<T> {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  items: T[];
}

// Hàm gọi API
async function advancedSearch(
  params: AdvancedSearchParams
): Promise<PageResponse<AccountResponse>> {
  const queryParams = new URLSearchParams();

  // Thêm các tham số không null/undefined
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined) {
      queryParams.append(key, value.toString());
    }
  });

  const response = await fetch(
    `/api/v1/accounts/advanced-search?${queryParams}`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${getToken()}`,
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }

  return response.json();
}

// Sử dụng
const searchParams: AdvancedSearchParams = {
  search: "admin",
  status: "ACTIVE",
  gender: "MALE",
  page: 0,
  size: 10,
  sortBy: "createdAt",
  sortDir: "desc",
};

try {
  const result = await advancedSearch(searchParams);
  console.log("Total accounts:", result.totalElements);
  console.log("Accounts:", result.items);
} catch (error) {
  console.error("Search failed:", error);
}
```

### React Hook

```typescript
import { useState, useEffect } from "react";

function useAdvancedSearch() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<PageResponse<AccountResponse> | null>(null);

  const search = async (params: AdvancedSearchParams) => {
    setLoading(true);
    setError(null);

    try {
      const result = await advancedSearch(params);
      setData(result);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Search failed");
    } finally {
      setLoading(false);
    }
  };

  return { loading, error, data, search };
}

// Sử dụng trong component
function AccountList() {
  const { loading, error, data, search } = useAdvancedSearch();
  const [searchParams, setSearchParams] = useState<AdvancedSearchParams>({
    page: 0,
    size: 10,
  });

  useEffect(() => {
    search(searchParams);
  }, [searchParams]);

  const handleSearch = (newParams: Partial<AdvancedSearchParams>) => {
    setSearchParams((prev) => ({ ...prev, ...newParams, page: 0 }));
  };

  const handlePageChange = (newPage: number) => {
    setSearchParams((prev) => ({ ...prev, page: newPage }));
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      {/* Search form */}
      <SearchForm onSearch={handleSearch} />

      {/* Results */}
      {data && (
        <div>
          <p>Total: {data.totalElements} accounts</p>
          <AccountTable accounts={data.items} />
          <Pagination
            currentPage={data.page}
            totalPages={data.totalPages}
            onPageChange={handlePageChange}
          />
        </div>
      )}
    </div>
  );
}
```

---

## Lưu ý quan trọng

1. **Phân trang:** Sử dụng `page` (zero-based) và `size` để phân trang
2. **Ngày tháng:** Sử dụng định dạng ISO-8601: `YYYY-MM-DDTHH:mm:ssZ`
3. **Sắp xếp:** Mặc định sắp xếp theo `createdAt` giảm dần
4. **Tìm kiếm:** Tìm kiếm không phân biệt hoa thường trong: fullName, email, username, phone
5. **Filter:** Có thể kết hợp nhiều filter cùng lúc
6. **Quyền:** Cần token JWT hợp lệ với quyền ADMIN hoặc CONSULTANT

---

## Troubleshooting

### Lỗi thường gặp

1. **401 Unauthorized:** Token không hợp lệ hoặc hết hạn
2. **403 Forbidden:** Không có quyền truy cập
3. **500 Internal Server Error:** Lỗi server, kiểm tra log backend

### Debug

```javascript
// Log request URL
console.log("Request URL:", `/api/v1/accounts/advanced-search?${queryParams}`);

// Log response
console.log("Response:", response);
console.log("Response headers:", response.headers);
```
