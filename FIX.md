# FIX.md - Troubleshooting Docker Container Conflicts

## Lỗi thường gặp: Container Name Conflict

### Mô tả lỗi

```
Error response from daemon: Conflict. The container name "/minio" is already in use by container "fc28203f355e346b449af25c1828b796d263dc253bf3e76eae800e79ea4e7fe2". You have to remove (or rename) that container to be able to reuse that name.
```

### Nguyên nhân

- Container với tên đã tồn tại từ lần chạy trước
- Docker không thể tạo container mới với tên trùng lặp
- `docker-compose down` có thể không xóa hết containers cũ
- Container được tạo từ docker-compose khác

## Giải pháp

### ✅ Cách 1: Xóa container cụ thể (Khuyến nghị)

```bash
# Xóa container bị conflict
docker rm -f minio

# Xóa các container khác có thể conflict
docker rm -f kafka elasticsearch kibana

# Chạy lại docker-compose
docker-compose up -d
```

### ✅ Cách 2: Dọn dẹp tất cả containers

```bash
# Dừng tất cả containers
docker stop $(docker ps -q)

# Xóa tất cả containers
docker rm $(docker ps -aq)

# Chạy lại
docker-compose up -d
```

### ✅ Cách 3: Reset hoàn toàn (Khi các cách trên không work)

```bash
# Dừng docker-compose hiện tại
docker-compose down

# Xóa tất cả containers
docker rm -f $(docker ps -aq)

# Dọn dẹp networks
docker network prune -f

# Chạy lại
docker-compose up -d
```

### ⚠️ Cách 4: Reset với volumes (Cẩn thận - sẽ mất data)

```bash
# Dừng và xóa containers + volumes
docker-compose down -v

# Xóa tất cả containers
docker rm -f $(docker ps -aq)

# Xóa volumes không sử dụng
docker volume prune -f

# Chạy lại
docker-compose up -d
```

## Lệnh kiểm tra và debug

### Xem danh sách containers

```bash
# Containers đang chạy
docker ps

# Tất cả containers (kể cả đã dừng)
docker ps -a

# Chỉ containers của docker-compose hiện tại
docker-compose ps
```

### Xem thông tin chi tiết container

```bash
# Xem container cụ thể
docker inspect minio

# Xem logs
docker logs minio
```

### Kiểm tra port conflicts

```bash
# Windows
netstat -ano | findstr :9000
netstat -ano | findstr :9200
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :9000
lsof -i :9200
lsof -i :8080
```

## Các lỗi khác thường gặp

### 1. Port đã được sử dụng

**Lỗi:**

```
Error starting userland proxy: listen tcp4 0.0.0.0:8080: bind: address already in use
```

**Fix:**

```bash
# Tìm process đang sử dụng port
netstat -ano | findstr :8080  # Windows
lsof -i :8080                # Linux/Mac

# Kill process
taskkill /PID <PID> /F       # Windows
kill -9 <PID>                # Linux/Mac
```

### 2. Network conflicts

**Lỗi:**

```
Error response from daemon: network with name backend-net already exists
```

**Fix:**

```bash
# Xóa network
docker network rm backend-net

# Hoặc dọn dẹp tất cả networks không sử dụng
docker network prune -f
```

### 3. Volume conflicts

**Lỗi:**

```
Error response from daemon: volume with name es-data already exists
```

**Fix:**

```bash
# Xóa volume cụ thể
docker volume rm sba_m_es-data

# Xóa tất cả volumes không sử dụng
docker volume prune -f
```

### 4. Image build fails

**Lỗi:**

```
COPY failed: file not found in build context
```

**Fix:**

```bash
# Đảm bảo file JAR tồn tại
ls target/backend-service.jar

# Build lại JAR
mvn clean package -DskipTests

# Build lại Docker image
docker-compose build --no-cache
```

## Best Practices để tránh conflicts

### 1. Luôn dọn dẹp trước khi chạy

```bash
# Workflow an toàn
docker-compose down
docker-compose up -d
```

### 2. Sử dụng container names unique

Trong docker-compose.yml, thêm project name:

```yaml
services:
  backend-service:
    container_name: sba_backend
  kafka:
    container_name: sba_kafka
  # ...
```

### 3. Sử dụng các lệnh cleanup định kỳ

```bash
# Weekly cleanup
docker system prune -f
docker volume prune -f
docker network prune -f
```

### 4. Check trước khi chạy

```bash
# Script kiểm tra
echo "Checking for existing containers..."
docker ps -a --format "table {{.Names}}\t{{.Status}}"

echo "Checking ports..."
netstat -ano | findstr ":8080\|:9200\|:9000\|:9094"
```

## Emergency Commands

### Kill tất cả Docker processes

```bash
# Windows
taskkill /f /im docker.exe
taskkill /f /im dockerd.exe

# Linux/Mac
sudo killall docker
sudo killall dockerd
```

### Restart Docker Desktop

- Windows/Mac: Restart Docker Desktop application
- Linux: `sudo systemctl restart docker`

### Complete Docker reset (Last resort)

```bash
# Dừng Docker
# Windows: Stop Docker Desktop

# Xóa tất cả
docker system prune -a --volumes -f

# Restart Docker
# Windows: Start Docker Desktop
```

## Validation sau khi fix

```bash
# 1. Kiểm tra containers
docker-compose ps

# 2. Kiểm tra logs
docker-compose logs --tail=50

# 3. Test endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:9200/_cluster/health

# 4. Kiểm tra ports
netstat -ano | findstr ":8080\|:9200\|:9000"
```

## Lỗi Environment Variables Missing

### Mô tả lỗi

```
Caused by: org.springframework.util.PlaceholderResolutionException: Could not resolve placeholder 'SPRING_MAIL_HOST' in value "${SPRING_MAIL_HOST}"
```

### Nguyên nhân

- Docker-compose.yml thiếu environment variables
- Application.properties tham chiếu đến biến môi trường không tồn tại
- Spring Boot không thể khởi động vì missing required properties

### Triệu chứng

- Container start nhưng exit ngay với code 1
- Trong `docker ps -a` thấy status "Exited (1)"
- Logs hiển thị PlaceholderResolutionException

### ✅ Fix Environment Variables

#### Cách 1: Cập nhật docker-compose.yml đầy đủ

```yaml
services:
  backend-service:
    environment:
/// .env
```

#### Cách 2: Sử dụng .env file

Tạo file `.env` trong thư mục gốc:

```bash
# Copy tất cả environment variables vào đây
SPRING_APPLICATION_NAME=SBA_M
SPRING_MAIL_HOST=smtp.gmail.com
# ... các biến khác
```

Sau đó trong docker-compose.yml:

```yaml
services:
  backend-service:
    env_file:
      - .env
```

#### Cách 3: Override environment cho testing

```bash
# Chạy với environment variables cụ thể
docker-compose run --rm \
  -e SPRING_MAIL_HOST=smtp.gmail.com \
  -e SPRING_MAIL_PORT=587 \
  -e "SPRING_MAIL_PASSWORD=cbky fiqp xyjc wxie" \
  -p 8080:8080 \
  backend-service
```

### Debug Environment Variables

#### Kiểm tra biến môi trường trong container

```bash
# Vào trong container
docker exec -it backend-service bash

# Xem tất cả environment variables
env | grep SPRING

# Hoặc kiểm tra biến cụ thể
echo $SPRING_MAIL_HOST
```

#### Kiểm tra trước khi chạy

```bash
# Validate docker-compose.yml
docker-compose config

# Xem environment variables sẽ được set
docker-compose config | grep -A 50 environment
```

### Lỗi Docker Network trong Environment Variables

#### ❌ Sai: Sử dụng localhost

```yaml
environment:
  SPRING_KAFKA_BOOTSTRAP_SERVERS: localhost:9094
  ELASTICSEARCH_URIS: http://localhost:9200
  MINIO_ENDPOINT: http://localhost:9000
```

#### ✅ Đúng: Sử dụng container names

```yaml
environment:
  SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
  ELASTICSEARCH_URIS: http://elasticsearch:9200
  MINIO_ENDPOINT: http://minio:9000
```

### Quick Fix Steps

```bash
# 1. Dừng containers
docker-compose down

# 2. Cập nhật docker-compose.yml với đầy đủ environment variables

# 3. Xóa dòng version để tránh warning
# Xóa: version: '3.8'

# 4. Chạy lại
docker-compose up -d

# 5. Kiểm tra logs
docker-compose logs -f backend-service

# 6. Test endpoint
curl http://localhost:8080/actuator/health
```

### Các Environment Variables hay thiếu

**Mail Configuration:**

- `SPRING_MAIL_HOST`
- `SPRING_MAIL_PORT`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`

**Security:**

- `JWT_SECRET`
- `JWT_EXPIRATION`

**Database:**

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

**Services URLs:**

- `SPRING_KAFKA_BOOTSTRAP_SERVERS`
- `ELASTICSEARCH_URIS`
- `MINIO_ENDPOINT`

#### Minio

cd .\Downloads\

# 1. Thiết lập kết nối tới MinIO

.\mc.exe alias set myminio http://localhost:9000 username password

# 2. Kiểm tra danh sách bucket

.\mc.exe ls myminio

# 3. Cấp quyền public (anonymous download) cho bucket đúng tên

.\mc.exe anonymous set download myminio/mybucket

## Khi nào cần hỗ trợ thêm

Nếu các cách trên không work, cung cấp thông tin:

1. Output của `docker ps -a`
2. Output của `docker-compose logs backend-service --tail=50`
3. Output của `docker-compose config` (để kiểm tra cấu hình)
4. Nội dung file application.properties
5. Hệ điều hành và phiên bản Docker
6. Các bước đã thực hiện
