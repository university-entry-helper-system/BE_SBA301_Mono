# Hướng dẫn deploy lại sau khi update code

## Cách 1: Full rebuild (Khuyến nghị cho thay đổi lớn)

```bash
# Bước 1: Build lại JAR file
mvn clean package -DskipTests

# Bước 2: Dừng containers
docker-compose down

# Bước 3: Rebuild Docker image và chạy lại
docker-compose up -d --build

# Bước 4: Xem logs
docker-compose logs -f backend-service
```

## Cách 2: Rebuild chỉ backend service (Nhanh hơn)

```bash
# Bước 1: Build JAR
mvn clean package -DskipTests

# Bước 2: Rebuild chỉ backend service
docker-compose build --no-cache backend-service

# Bước 3: Restart chỉ backend service
docker-compose up -d backend-service

# Bước 4: Kiểm tra
docker-compose logs -f backend-service
```

## Cách 3: Hot reload (Nhanh nhất - chỉ cho thay đổi nhỏ)

```bash
# Build JAR mới
mvn clean package -DskipTests

# Restart container với JAR mới
docker-compose restart backend-service

# Xem logs
docker-compose logs -f backend-service
```

## Cách 4: Force recreate container

```bash
# Build JAR
mvn clean package -DskipTests

# Force recreate backend container
docker-compose up -d --force-recreate backend-service
```

## Workflow Development (Khuyến nghị)

### Cho development thường xuyên:

```bash
#!/bin/bash
# Tạo script deploy.sh

echo "Building JAR..."
mvn clean package -DskipTests

echo "Rebuilding Docker image..."
docker-compose build --no-cache backend-service

echo "Restarting backend service..."
docker-compose up -d backend-service

echo "Showing logs..."
docker-compose logs -f backend-service
```

Sau đó:

```bash
# Làm executable
chmod +x deploy.sh

# Chạy mỗi lần update code
./deploy.sh
```

## Cách 5: Development với volume mount (Advanced)

Để tránh rebuild mỗi lần, có thể mount JAR file:

### Cập nhật docker-compose.yml:

```yaml
services:
  backend-service:
    # ... other configs
    volumes:
      - ./target/backend-service.jar:/app/backend-service.jar
    # ... rest of config
```

### Workflow với volume mount:

```bash
# Chỉ cần build JAR và restart
mvn clean package -DskipTests
docker-compose restart backend-service
```

## Kiểm tra sau khi deploy

### 1. Verify container status

```bash
docker-compose ps
```

### 2. Check logs for errors

```bash
# Xem 50 dòng logs gần nhất
docker-compose logs --tail=50 backend-service

# Theo dõi logs real-time
docker-compose logs -f backend-service
```

### 3. Test endpoints

```bash
# Health check
curl http://localhost:8080/actuator/health

# Test API endpoint cụ thể
curl http://localhost:8080/api/your-endpoint
```

### 4. Verify all services still running

```bash
# Kiểm tra tất cả services
docker-compose ps

# Test các external services
curl http://localhost:9200/_cluster/health  # Elasticsearch
curl http://localhost:9000/minio/health/live # MinIO
```

## Troubleshooting sau khi deploy

### Container không start

```bash
# Xem lỗi chi tiết
docker logs backend-service

# Kiểm tra JAR file
ls -la target/backend-service.jar

# Rebuild từ đầu
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### Port conflicts

```bash
# Kiểm tra port 8080
netstat -ano | findstr :8080

# Kill process nếu cần
taskkill /PID <PID> /F
```

### Memory issues

```bash
# Xem resource usage
docker stats

# Restart với memory limit
docker-compose up -d --memory=2g backend-service
```

## Best Practices

### 1. Always build JAR first

```bash
# Đảm bảo JAR được build mới nhất
mvn clean package -DskipTests
```

### 2. Check for breaking changes

```bash
# Nếu có database migration
mvn flyway:migrate

# Nếu có schema changes
docker-compose logs backend-service | grep -i "schema"
```

### 3. Backup before major changes

```bash
# Backup database nếu cần
docker exec postgres pg_dump -U user database > backup.sql
```

### 4. Test in isolation

```bash
# Chạy chỉ backend để test
docker-compose up backend-service postgres

# Test riêng biệt từng component
docker-compose up -d elasticsearch
docker-compose up -d kafka
docker-compose up -d backend-service
```

## Quick Commands Reference

```bash
# Build và deploy nhanh
mvn clean package -DskipTests && docker-compose up -d --build backend-service

# Deploy và xem logs ngay
mvn clean package -DskipTests && docker-compose restart backend-service && docker-compose logs -f backend-service

# Reset hoàn toàn
docker-compose down && mvn clean package -DskipTests && docker-compose up -d --build

# Kiểm tra status nhanh
docker-compose ps && curl -s http://localhost:8080/actuator/health
```

## Environment-specific deployment

### Development

```bash
# Với hot reload và debug logs
export SPRING_PROFILES_ACTIVE=dev
mvn clean package -DskipTests
docker-compose -f docker-compose.dev.yml up -d --build
```

### Production

```bash
# Với optimized settings
export SPRING_PROFILES_ACTIVE=prod
mvn clean package
docker-compose -f docker-compose.prod.yml up -d --build
```

## Performance Tips

- **Sử dụng Maven daemon** để build nhanh hơn: `mvnd clean package -DskipTests`
- **Skip tests** trong development: `-DskipTests`
- **Use Docker BuildKit** để build image nhanh hơn: `export DOCKER_BUILDKIT=1`
- **Layer caching**: Đặt dependencies trước source code trong Dockerfile

## Monitoring deployment

```bash
# Theo dõi CPU/Memory usage
docker stats backend-service

# Xem startup time
docker-compose logs backend-service | grep "Started"

# Monitor health endpoint
watch curl -s http://localhost:8080/actuator/health
```
