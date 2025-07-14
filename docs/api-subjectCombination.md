# API: Subject Combination

## Response Structure

```json
{
  "id": 1,
  "name": "A00",
  "description": "Toán, Lý, Hóa",
  "examSubjects": [
    { "id": 1, "name": "Toán", "shortName": "TO" },
    { "id": 2, "name": "Lý", "shortName": "LY" },
    { "id": 3, "name": "Hóa", "shortName": "HO" }
  ],
  "status": "ACTIVE",
  "block": {
    "id": 1,
    "name": "Khối A"
  }
}
```

### Trường `block`

- Có thể là `null` nếu tổ hợp không thuộc block nào.
- Nếu có, trả về object `{ id, name }`.

#### Ví dụ block null

```json
{
  "id": 2,
  "name": "D01",
  "description": "Toán, Văn, Anh",
  "examSubjects": [
    { "id": 1, "name": "Toán", "shortName": "TO" },
    { "id": 4, "name": "Văn", "shortName": "VA" },
    { "id": 5, "name": "Anh", "shortName": "AN" }
  ],
  "status": "ACTIVE",
  "block": null
}
```

### Lưu ý

- Khi tạo/cập nhật subject combination, trường block có thể bỏ qua (null) hoặc truyền id block hợp lệ.
- Khi filter theo block, chỉ trả về các tổ hợp có block tương ứng (nếu block null sẽ không lọc được theo tên block).
