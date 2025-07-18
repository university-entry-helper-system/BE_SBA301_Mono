# SETUP.md - Hướng dẫn chạy ứng dụng SpringBoot với Docker

## Yêu cầu hệ thống

- Docker Desktop (Windows/Mac) hoặc Docker Engine (Linux)
- Docker Compose
- Git (để clone project)

## Cấu trúc dự án cần thiết

```
SBA_M/
├── docker-compose.yml
├── Dockerfile
├── application.properties
├── application.yml
├── university-settings.json
├── target/
│   └── backend-service.jar
└── .env (tùy chọn)
```

## Các bước chạy ứng dụng

### Bước 1: Build ứng dụng

```bash
# Di chuyển vào thư mục dự án
cd D:\FPT\Ky_7\SBA301\University\BE_SBA301_Mono\SBA_M

# Build ứng dụng với Maven
mvn clean package -DskipTests

# Hoặc với Gradle
./gradlew build -x test
```

**Lưu ý:** Đảm bảo file `backend-service.jar` được tạo trong thư mục `target/`

### Bước 2: Chạy Docker Compose

```bash
# Khởi động tất cả services
docker-compose up -d
```

### Bước 3: Kiểm tra trạng thái containers

```bash
# Xem danh sách containers đang chạy
docker-compose ps

# Xem logs của tất cả services
docker-compose logs

# Xem logs của service cụ thể
docker-compose logs backend-service
docker-compose logs kafka
docker-compose logs elasticsearch
```

## Các services và ports

| Service       | Port | URL                   | Mô tả                     |
| ------------- | ---- | --------------------- | ------------------------- |
| Backend API   | 8080 | http://localhost:8080 | Ứng dụng SpringBoot chính |
| Elasticsearch | 9200 | http://localhost:9200 | Search engine             |
| Kibana        | 5601 | http://localhost:5601 | Elasticsearch UI          |
| Kafka         | 9094 | localhost:9094        | Message broker            |
| MinIO Console | 9001 | http://localhost:9001 | Object storage UI         |
| MinIO API     | 9000 | http://localhost:9000 | Object storage API        |

## Thông tin đăng nhập

### MinIO Console (http://localhost:9001)

- **Username:** robin
- **Password:** admin123456

### Elasticsearch (http://localhost:9200)

- **Username:** robin
- **Password:** 12345

## Kiểm tra ứng dụng hoạt động

### 1. Kiểm tra Backend API

```bash
curl http://localhost:8080/actuator/health
```

### 2. Kiểm tra Elasticsearch

```bash
curl http://localhost:9200/_cluster/health
```

### 3. Kiểm tra MinIO

```bash
curl http://localhost:9000/minio/health/live
```

## Cấu hình Elasticsearch Index

Sau khi Elasticsearch khởi động, tạo index với settings:

```bash
curl -X PUT "localhost:9200/university" \
  -H "Content-Type: application/json" \
  -d @university-settings.json
```

## Tạo MinIO Bucket

### Cách 1: Qua MinIO Console

1. Truy cập http://localhost:9001
2. Đăng nhập với robin/admin123456
3. Tạo bucket tên "mybucket"

### Cách 2: Qua API

```bash
# Sử dụng MinIO Client (mc)
mc alias set myminio http://localhost:9000 robin admin123456
mc mb myminio/mybucket
```

## Dừng ứng dụng

```bash
# Dừng tất cả containers
docker-compose down

# Dừng và xóa volumes (sẽ mất data)
docker-compose down -v
```

## Restart ứng dụng

```bash
# Restart tất cả services
docker-compose restart

# Restart service cụ thể
docker-compose restart backend-service
```

## Xem logs chi tiết

```bash
# Theo dõi logs real-time
docker-compose logs -f

# Logs của service cụ thể
docker-compose logs -f backend-service

# Logs với timestamp
docker-compose logs -t backend-service
```

## Environment Variables

Ứng dụng sử dụng các biến môi trường được định nghĩa trong:

- `application.properties`
- `docker-compose.yml` (environment section)

### Database connections:

- **PostgreSQL:** Cloud database qua Neon (đã cấu hình)
- **MongoDB:** Cloud database qua Atlas (đã cấu hình)

### External services:

- **Gmail SMTP:** Configured for email sending
- **JWT:** For authentication
- **Kafka:** For message processing
- **Elasticsearch:** For search functionality
- **MinIO:** For file storage

## Warning về version

Có thể thấy warning:

```
the attribute `version` is obsolete, it will be ignored
```

Đây chỉ là warning, không ảnh hưởng đến việc chạy ứng dụng. Có thể bỏ qua hoặc xóa dòng `version: '3.8'` trong docker-compose.yml.

## Lưu ý quan trọng

1. **Ports:** Đảm bảo các ports 8080, 9200, 5601, 9094, 9000, 9001 không bị chiếm dụng
2. **Memory:** Elasticsearch cần ít nhất 1GB RAM
3. **Disk space:** Docker images sẽ chiếm khoảng 2-3GB
4. **Network:** Cần kết nối internet để truy cập cloud databases
