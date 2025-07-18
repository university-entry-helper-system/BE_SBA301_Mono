# Hướng dẫn sử dụng API Auth (Frontend)

**Base URL:** `/api/v1/auth`

---

## 1. Đăng ký tài khoản (Register)

- **Endpoint:** `POST /register`
- **Body (JSON):**

```json
{
  "username": "string (4-100 ký tự)",
  "email": "string (email hợp lệ)",
  "password": "string (>=8 ký tự, gồm chữ hoa, thường, số, ký tự đặc biệt)",
  "fullName": "string (tùy chọn)",
  "phone": "string (bắt đầu 0 hoặc +84, 10 số)",
  "dob": "string (MM-DD-YYYY, năm 1900-nay)",
  "gender": "MALE | FEMALE | OTHER (tùy chọn)"
}
```

- **Response:**

```json
{
  "code": 1001,
  "message": "User registered successfully. Please check your email for account activation."
}
```

---

## 2. Kích hoạt tài khoản (Activate)

- **Endpoint:** `GET /activate?email=...&code=...`
- **Response:**

```json
{
  "code": 1002,
  "message": "Account activated successfully."
}
```

---

## 3. Đăng nhập (Login)

- **Endpoint:** `POST /login`
- **Body (JSON):**

```json
{
  "username": "string",
  "password": "string"
}
```

- **Response:**

```json
{
  "code": 1000,
  "message": "Login successful",
  "result": {
    "accessToken": "string",
    "refreshToken": "string",
    "expiresIn": 123456789
  }
}
```

---

## 4. Làm mới token (Refresh Token)

- **Endpoint:** `POST /refresh-token`
- **Body (JSON):**

```json
{
  "refreshToken": "string"
}
```

- **Response:**

```json
{
  "code": 1000,
  "message": "Token refreshed successfully",
  "result": {
    "accessToken": "string",
    "refreshToken": "string",
    "expiresIn": 123456789
  }
}
```

---

## 5. Quên mật khẩu (Forgot Password)

- **Endpoint:** `POST /forgot-password?email=...`
- **Response:**

```json
{
  "code": 1000,
  "message": "If the email is registered, a password reset link has been sent to your inbox."
}
```

---

## 6. Đặt lại mật khẩu (Reset Password)

- **Endpoint:** `POST /reset-password?email=...&token=...`
- **Body (JSON):**

```json
{
  "password": "string (>=8 ký tự, gồm chữ hoa, thường, số, ký tự đặc biệt)"
}
```

- **Response:**

```json
{
  "code": 1002,
  "message": "Password reset successfully. You can now log in with your new password."
}
```

---

## 7. Đăng xuất (Logout)

- **Endpoint:** `POST /logout`
- **Body (JSON):**

```json
{
  "refreshToken": "string"
}
```

- **Response:**

```json
{
  "code": 1000,
  "message": "Logged out successfully."
}
```

---

## 8. Kiểm tra username/email đã tồn tại

- **Endpoint:** `GET /check-username?username=...`
- **Response:**

```json
{
  "code": 1000,
  "message": "Username check complete",
  "result": true
}
```

- **Endpoint:** `GET /check-email?email=...`
- **Response:**

```json
{
  "code": 1000,
  "message": "Email check complete",
  "result": false
}
```

---

## 9. Gửi lại email kích hoạt

- **Endpoint:** `POST /resend-activation?email=...`
- **Response:**

```json
{
  "code": 1000,
  "message": "Activation email resent successfully."
}
```

---

## 10. Xác thực access token

- **Endpoint:** `GET /verify-token?token=...`
- **Response:**

```json
{
  "code": 1000,
  "message": "Access token verification completed",
  "result": true
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

- Các lỗi sẽ trả về code khác và message tương ứng.
- Các API đều trả về theo format trên.

---

## Payload (decode) của accessToken và refreshToken

### 1. accessToken

- Khi decode (ví dụ dùng https://jwt.io), payload sẽ có dạng:

```json
{
  "accountId": "<UUID>",
  "roleId": <number>,
  "roleName": "ROLE_USER | ROLE_CONSULTANT | ROLE_ADMIN",
  "sub": "<username>",
  "iat": <issued at, epoch>,
  "exp": <expiration, epoch>
}
```

- **Giải thích các trường:**
  - `accountId`: ID người dùng (UUID)
  - `roleId`: ID vai trò chính của người dùng
  - `roleName`: Tên vai trò chính ("ROLE_USER", "ROLE_CONSULTANT", "ROLE_ADMIN")
  - `sub`: username
  - `iat`: thời điểm phát hành token (epoch seconds)
  - `exp`: thời điểm hết hạn token (epoch seconds)

### 2. refreshToken

- Khi decode, payload sẽ có dạng:

```json
{
  "accountId": "<UUID>",
  "sub": "<username>",
  "iat": <issued at, epoch>,
  "exp": <expiration, epoch>
}
```

- **Giải thích các trường:**
  - `accountId`: ID người dùng (UUID)
  - `sub`: username
  - `iat`: thời điểm phát hành token (epoch seconds)
  - `exp`: thời điểm hết hạn token (epoch seconds)

**Lưu ý:**

- accessToken dùng để xác thực các API cần đăng nhập.
- refreshToken dùng để lấy accessToken mới khi hết hạn.
- FE có thể decode token để lấy thông tin user, role, thời gian hết hạn nếu cần.
