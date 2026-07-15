#!/bin/bash

# ===== 使用说明 =====
# 1. 本地改完代码: git add -A && git commit -m "xxx" && git push
# 2. SSH 连上服务器: ssh root@118.178.139.197
# 3. 执行部署:      /root/deploy.sh

set -e

echo "===== 1. 拉取最新代码 ====="
cd /root/demo-backend
git pull

echo "===== 2. 构建后端 ====="
cd /root/demo-backend/demo-backend
mvn clean package -DskipTests

echo "===== 3. 停掉旧后端 ====="
kill -9 $(pgrep -f "backend-0.0.1-SNAPSHOT.jar") 2>/dev/null || true
sleep 3

echo "===== 4. 启动后端 ====="
nohup java -jar target/backend-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > app.log 2>&1 &
BACKEND_PID=$!
disown "$BACKEND_PID"

echo "等待后端启动（最多 60 秒）..."
for i in $(seq 1 30); do
    if grep -q "Started DemoBackendApplication" app.log 2>/dev/null; then
        echo "后端启动成功"
        break
    fi
    sleep 2
done

if ! grep -q "Started DemoBackendApplication" app.log 2>/dev/null; then
    echo "后端启动失败，日志:"
    tail -30 app.log
    exit 1
fi

echo "===== 5. 构建前端 ====="
cd /root/demo-backend/demo-frontend
npm install
npm run build

echo "===== 6. 复制前端文件 ====="
cp -r dist/* /var/www/demo-backend/

echo ""
echo "===== 部署完成 ====="
echo "访问 http://118.178.139.197:8080"