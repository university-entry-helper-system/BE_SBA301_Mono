# Hướng dẫn sử dụng API Account (Frontend)

**Base URL:** `/api/v1accounts`

---

## Tổng quan tính năng

### Cột hiển thị chính trong bảng

- **Thông tin cơ bản:** Full Name, Username, Email, Phone, Gender (badge)
- **Vai trò & Trạng thái:** Roles (badges), Status (badge màu)
- **Thống kê hoạt động:** Login Count, Last Login, Created At
- **Quick Actions:** View, Edit, Toggle Status, Ban/Unban, Reset Password, Soft Delete

### Filters & Search

- Search theo: `full_name`, `email`, `username`, `phone`
- Filter theo: `role`, `status`, `gender`, `is_deleted`, `created_date_range`, `last_login_range`
- Sort theo: `full_name`, `created_at`, `last_login_at`, `login_count`

### Bulk Actions

- Bulk Activate/Deactivate/Ban
- Export Excel
- Send Notification hàng loạt

---

## 1 Lấy danh sách tài khoản (Cơ bản)

- **Endpoint:** `GET /`
- **Query params:**
  - `page`: Số trang (mặc định0- `size`: Số lượng mỗi trang (mặc định 10)
- **Response:**

```json
[object Object]
 code: 100message": "Users fetched successfully",
 result:[object Object]  page:0,
   size": 10,
    totalElements": 2
   totalPages:1items":
     [object Object]        id:550e29b41a-7164-4665000
        fullName": "Nguyễn Văn A",
       username": "nguyenvana",
  email":a@example.com",
      phone": "0912345678,
        gender:MALE",
       roles: [USERADMIN"],
       status":ACTIVE",
        loginCount":12      lastLoginAt: 20246020,
        createdAt: 2024180:00Z",
        isDeleted": false
      }
    ]
  }
}
```

---

## 2. Tìm kiếm tài khoản theo tên

- **Endpoint:** `GET /search`
- **Query params:**
  - `name`: Tên cần tìm kiếm
  - `page`: Số trang (mặc định0- `size`: Số lượng mỗi trang (mặc định 10- **Response:** Giống API1

---

## 3. Tìm kiếm nâng cao với filters

- **Endpoint:** `GET /advanced-search`
- **Query params:**
  - `search`: Từ khóa tìm kiếm (tùy chọn)
  - `searchBy`: Trường tìm kiếm (`full_name`, `email`, `username`, `phone`)
  - `role`: Lọc theo vai trò (`USER`, `CONSULTANT`, `ADMIN`)
  - `status`: Lọc theo trạng thái (`ACTIVE`, `INACTIVE`, `BANNED`)
  - `gender`: Lọc theo giới tính (`MALE`, `FEMALE`, `OTHER`)
  - `isDeleted`: Lọc theo trạng thái xóa mềm (true/false)
  - `createdDateFrom`, `createdDateTo`: Lọc theo ngày tạo (ISO 861 - `lastLoginFrom`, `lastLoginTo`: Lọc theo lần đăng nhập cuối (ISO8601)
  - `sortBy`: Trường sắp xếp (`full_name`, `created_at`, `last_login_at`, `login_count`)
  - `sortOrder`: Thứ tự sắp xếp (`ASC`, `DESC`)
  - `page`: Số trang (mặc định0- `size`: Số lượng mỗi trang (mặc định 10- **Response:** Giống API1---

##4. Lấy chi tiết tài khoản

- **Endpoint:** `GET /{accountId}`
- **Response:**

```json
[object Object]
 code: 100message": "User fetched successfully",
 result":[object Object]
    id:550e29b41a-7164-466554400000,
    fullName": "Nguyễn Văn A,
   username":nguyenvana,
    "email":a@example.com",
  phone":912345678,
    gender": MALE,  roles: [USER, MIN"],
   status: CTIVE,
    loginCount": 12,
  lastLoginAt: 202461T10:20:0Z",
    createdAt: 202411T08Z",
    isDeleted": false
  }
}
```

---

## 5 Tạo tài khoản mới (ADMIN)

- **Endpoint:** `POST /create-user` (tạo user thường)
- **Endpoint:** `POST /create-admin` (tạo admin)
- **Body (JSON):**

```json[object Object]
 username:nguyenvana",
  "email": a@example.com,
  password:StrongPassword123,
  fullName":Nguyễn Văn A,phone:0901234567,
  dob:01-302000
 gender:MALE
```

- **Response:**

```json
[object Object]
 code: 101message": "General user created successfully",
 result":[object Object]
    id:550e29b41a-7164-466554400000,
    fullName": "Nguyễn Văn A,
   username":nguyenvana,
    "email":a@example.com",
  phone":901234567,
    gender": MALE,roles: [SER"],
   status: IVE",
    createdAt: 2024-11T08:00  }
}
```

---

##6Cập nhật thông tin tài khoản (ADMIN)

- **Endpoint:** `PUT /{accountId}`
- **Body (JSON):**

```json[object Object]
  fullName":Nguyễn Văn A",
  "email": a@example.com,phone: 912345678
```

- **Response:**

```json
[object Object]
 code: 102message": "User updated successfully",
 result":[object Object]
    id:550e29b41a-7164-466554400000,
    fullName": "Nguyễn Văn A,
   username":nguyenvana,
    "email":a@example.com",
  phone":912345678,
    gender": MALE,  roles: [USER, MIN"],
   status: CTIVE  }
}
```

---

## 7. Chuyển đổi trạng thái (ACTIVE/INACTIVE)

- **Endpoint:** `PATCH /{accountId}/toggle-status`
- **Response:**

```json
[object Object]
 code: 103message:User status toggled successfully",
 result":[object Object]
    id:550e29b41a-7164-46655440000000,
  status:INACTIVE  }
}
```

---

## 8. Ban/Unban tài khoản (ADMIN)

- **Endpoint:** `PATCH /{accountId}/ban`
- **Body (JSON):**

```json
[object Object]
 banned": true
}
```

- **Response:**

```json
[object Object]
 code: 104message": "User banned successfully",
 result":[object Object]
    id:550e29b41a-7164-46655440000000,
   status: ANNED"
  }
}
```

- **Unban:** Gửi `{ banned": false }` để bỏ cấm.

---

##9. Reset mật khẩu (ADMIN)

- **Endpoint:** `POST /{accountId}/reset-password`
- **Body (JSON):**

```json
[object Object]newPassword": NewStrongPassword123!"
}
```

- **Response:**

```json
[object Object]
 code: 105essage: ssword reset email sent successfully"
}
```

---

## 10n vai trò cho user (ADMIN)

- **Endpoint:** `POST /{accountId}/set-roles`
- **Body (JSON):**

```json
[object Object]
  roleIds:1,2
```

- **Response:**

```json
[object Object]
 code: 102message": "Roles set successfully",
 result":[object Object]
    id:550e29b41a-7164-4665544000000,  roles:USER, DMIN"]
  }
}
```

---

## 11y vai trò của user

- **Endpoint:** `GET /{accountId}/roles`
- **Response:**

```json
[object Object]
 code: 100message": "Roles fetched successfully",
  result:["USER", "ADMIN]
}
```

---

## 12 Cập nhật trạng thái tài khoản (ADMIN)

- **Endpoint:** `PATCH /{accountId}/status`
- **Body (JSON):**

```json
[object Object]status":ACTIVE"
}
```

- **Response:**

```json
[object Object]
 code: 102message": "User status updated successfully",
 result":[object Object]
    id:550e29b41a-7164-46655440000000,   status: CTIVE  }
}
```

---

## 13Bulk Actions (Hành động hàng loạt)

- **Endpoint:** `POST /bulk-action`
- **Body (JSON):**

```json
{
  "action:ACTIVATE,
  ids": ["550e29b41a-7164-466544000000,550e29b41a-7164-4665-544000000001],
  notification": [object Object]  title": Thôngbáo",
 content": Tài khoản của bạn đã được kích hoạt"
  }
}
```

- **Response:**

```json
[object Object]
 code: 1007message": "Bulk action executed successfully",
 result: [object Object]
    affected: 2
```

**Các action hỗ trợ:**

- `ACTIVATE`: Kích hoạt tài khoản
- `DEACTIVATE`: Vô hiệu hóa tài khoản
- `BAN`: Cấm tài khoản
- `EXPORT`: Xuất Excel (trả về file)
- `SEND_NOTIFICATION`: Gửi thông báo

---

## 14. Lịch sử hoạt động (Activity Log)

- **Endpoint:** `GET /{accountId}/activity-log`
- **Query params:** `page`, `size`
- **Response:**

```json
[object Object]
 code: 109essage": "Activity log fetched successfully",
 result:[object Object]  page:0,
   size": 10,
    totalElements": 2
   totalPages:1items":
     [object Object]
      action": LOGIN        timestamp: 20246020:00,        ip": "192168.10.1
      },
     [object Object]
       action:UPDATE_PROFILE",
        timestamp: 2024550:00,        ip": "1920.168.1.1      }
    ]
  }
}
```

---

## 15 Lịch sử đăng nhập (Login History)

- **Endpoint:** `GET /{accountId}/login-history`
- **Query params:** `page`, `size`
- **Response:**

```json
[object Object]
 code: 1010message": "Login history fetched successfully",
 result:[object Object]  page:0,
   size": 10,
    totalElements": 2
   totalPages:1items":[object Object]        timestamp: 20246020:00,        ip: 0.168.1,
        device:Chrome/Windows"
      },[object Object]        timestamp: 2024580:00,        ip: 0.168.1,
       device": Safari/iOS"
      }
    ]
  }
}
```

---

## 16. Kiểm tra độ mạnh mật khẩu

- **Endpoint:** `POST /check-password-strength`
- **Body (JSON):**

```json
{
  "password:StrongPassword123!"
}
```

- **Response:**

```json
[object Object]
 code: 1011essage": Password strength checked successfully",
 result: [object Object]  score:4
  level: "STRONG",
   suggestions": [Increase password length"]
  }
}
```

---

##17óa tài khoản (Soft Delete)

- **Endpoint:** `DELETE /{accountId}`
- **Query params:**
  - `hard`: true/false (mặc định false - soft delete)
- **Response:**

```json
[object Object]
 code: 103message": "User deleted successfully"
}
```

---

##18 Khôi phục tài khoản đã xóa

- **Endpoint:** `POST /{accountId}/restore`
- **Response:**

```json
[object Object]
 code: 102message": "User restored successfully"
}
```

---

## 19 Thông tin cá nhân (Current User)

- **Endpoint:** `GET /my-info`
- **Response:**

```json
[object Object]
 code: 100message": Current user info fetched",
 result":[object Object]
    id:550e29b41a-7164-466554400000,
    fullName": "Nguyễn Văn A,
   username":nguyenvana,
    "email":a@example.com",
  phone":912345678,
    gender": MALE,  roles: [USER, MIN"],
   status: CTIVE,
    loginCount": 12,
  lastLoginAt: 202461T10:20:0Z",
    createdAt: 2024-11T08:00  }
}
```

---

## 20. Cập nhật mật khẩu cá nhân

- **Endpoint:** `POST /my-info/update-password`
- **Body (JSON):**

```json
[object Object]oldPassword: "OldPassword123!",
newPassword": NewStrongPassword123confirmNewPassword": NewStrongPassword123!"
}
```

- **Response:**

```json
[object Object]
 code: 102essage":Password updated successfully"
}
```

---

## 21. Cập nhật profile cá nhân

- **Endpoint:** `PUT /my-info/update-profile`
- **Body (JSON):**

```json[object Object]
  fullName":Nguyễn Văn A",
  "email": a@example.com,phone: 912345678
```

- **Response:**

```json
[object Object]
 code: 102message": "Profile updated successfully",
 result":[object Object]
    id:550e29b41a-7164-466554400000,
    fullName": "Nguyễn Văn A,
   username":nguyenvana,
    "email":a@example.com",
  phone":912345678,
    gender": MALE,  roles: [USER, MIN"],
   status: CTIVE  }
}
```

---

## Format chung của response

```json
[object Object] code": number,
  message": "string,result: ... // tuỳ API
}
```

---

## Giải thích các trường

### Thông tin cơ bản

- `id`: UUID của tài khoản
- `fullName`: Họ tên đầy đủ
- `username`: Tên đăng nhập (unique)
- `email`: Email
- `phone`: Số điện thoại
- `gender`: Giới tính (`MALE`, `FEMALE`, `OTHER`)

### Vai trò & Trạng thái

- `roles`: Danh sách vai trò (`USER`, `CONSULTANT`, `ADMIN`)
- `status`: Trạng thái (`ACTIVE`, `INACTIVE`, `BANNED`)

### Thống kê hoạt động

- `loginCount`: Số lần đăng nhập
- `lastLoginAt`: Lần đăng nhập cuối (ISO 861
- `createdAt`: Ngày tạo tài khoản (ISO 861)
- `isDeleted`: Đánh dấu xóa mềm

### Activity Log & Login History

- `action`: Hành động (`LOGIN`, `UPDATE_PROFILE`, `PASSWORD_CHANGE`, etc.)
- `timestamp`: Thời gian (ISO8601- `ip`: Địa chỉ IP
- `device`: Thiết bị (tùy chọn)

### Password Strength

- `score`: Điểm độ mạnh (0-5
- `level`: Mức độ (`WEAK`, `MEDIUM`, `STRONG`)
- `suggestions`: Gợi ý cải thiện

---

## Lưu ý quan trọng

### Quyền truy cập

- **ADMIN:** Tất cả API
- **CONSULTANT:** Xem danh sách, chi tiết, activity log, login history
- **USER:** Chỉ thông tin cá nhân và cập nhật profile

### Validation

- **Password:** Tối thiểu 8 ký tự, có chữ hoa, chữ thường, số, ký tự đặc biệt
- **Email:** Định dạng email hợp lệ
- **Phone:** Định dạng số điện thoại Việt Nam
- **Username:** Tối thiểu4ký tự, unique

### Error Codes

- `100 Thành công
- `1001`: Tạo thành công
- `1002ập nhật thành công
- `1003`: Xóa thành công
- `1004`: Ban/Unban thành công
- `1005set password thành công
- `1007 Bulk action thành công
- `19Activity log thành công
- `1010ogin history thành công
- `1011`: Password strength check thành công

### Features nâng cao

- **Soft Delete:** Mặc định xóa mềm, có thể hard delete
- **Audit Trail:** Lưu người tạo, người sửa, thời gian
- **Email Integration:** Gửi mail xác thực khi reset password
- **Bulk Operations:** Hành động hàng loạt hiệu quả
- **Advanced Search:** Tìm kiếm và lọc nâng cao
- **Activity Tracking:** Theo dõi hoạt động user
- **Password Policy:** Kiểm tra độ mạnh mật khẩu

---

## Ví dụ sử dụng Frontend

### React/TypeScript Example

```typescript
// Lấy danh sách user với filter
const getUsers = async (filters: UserFilters) => [object Object]const params = new URLSearchParams({
    page: filters.page.toString(),
    size: filters.size.toString(),
    ...(filters.search && { search: filters.search }),
    ...(filters.role && { role: filters.role }),
    ...(filters.status && { status: filters.status })
  });

  const response = await fetch(`/api/v1/accounts/advanced-search?${params}`);
  return response.json();
};

// Bulk action
const bulkAction = async (action: string, ids: string[]) => [object Object]  const response = await fetch('/api/v1/accounts/bulk-action, {
    method: 'POST',
    headers:[object Object]Content-Type': application/json' },
    body: JSON.stringify({ action, ids })
  });
  return response.json();
};
```

### Vue.js Example

```javascript
// Toggle user status
async toggleUserStatus(userId) {
  try {
    const response = await this.$http.patch(`/api/v1/accounts/${userId}/toggle-status`);
    this.$toast.success('User status updated successfully');
    this.loadUsers(); // Refresh list
  } catch (error)[object Object] this.$toast.error('Failed to update user status');
  }
}
```

---

Nếu cần bổ sung chi tiết về bất kỳ API nào, hãy yêu cầu thêm!
