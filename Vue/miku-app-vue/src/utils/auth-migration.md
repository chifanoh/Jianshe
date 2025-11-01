# 登录状态管理迁移方案：IndexedDB → HttpOnly Cookie

## 迁移概述

本次迁移将现有的 IndexedDB 登录状态管理机制升级为更安全的 HttpOnly Cookie 认证机制，解决了以下问题：

1. **安全性提升**：HttpOnly Cookie 无法通过 JavaScript 直接访问，有效防止 XSS 攻击
2. **数据持久性**：删除数据库表后无需重新登录，认证信息存储在 Cookie 中
3. **自动认证**：浏览器自动携带 Cookie，无需手动管理 token

## 主要变更

### 1. 请求配置更新 (`src/request.ts`)
- 保持 `withCredentials: true` 配置，支持跨域 Cookie 传输
- 增强响应拦截器，处理 401/403 认证错误

### 2. 状态管理升级 (`src/store/useLoginUserStore.ts`)
- **缓存加载逻辑**：优先检查 HttpOnly Cookie 认证状态
- **登出流程**：优先清除 HttpOnly Cookie，再清理本地缓存
- **错误处理**：增强认证失败的容错处理

### 3. 新增认证工具类 (`src/utils/auth.ts`)
- `isAuthenticated()`：通过后端接口验证 Cookie 有效性
- `getCurrentUserByCookie()`：获取当前用户信息
- `clearAuthCredentials()`：清除 Cookie 认证信息
- `AuthStatusChecker`：定期检查认证状态

### 4. 应用初始化 (`src/main.ts`)
- 启动认证状态检查器，定期检查用户登录状态
- 自动处理认证失效场景

## 后端要求

### 接口规范
1. **登录接口** (`POST /api/user/login`)
   - 设置 HttpOnly Cookie：`Set-Cookie: auth-token=xxx; HttpOnly; Secure; SameSite=Strict`
   - 返回用户基本信息

2. **当前用户接口** (`GET /api/user/current`)
   - 验证 Cookie 中的认证信息
   - 返回完整用户信息

3. **登出接口** (`POST /api/user/logout`)
   - 清除 HttpOnly Cookie
   - 返回操作结果

### Cookie 配置建议
```http
Set-Cookie: auth-token=xxx; 
  HttpOnly; 
  Secure; 
  SameSite=Strict; 
  Max-Age=604800; 
  Path=/
```

### 跨域配置
如果前后端分离部署，需要配置 CORS：
```java
// Spring Boot 示例
@CrossOrigin(
  origins = "http://localhost:5173", 
  allowCredentials = "true",
  allowedHeaders = "*",
  methods = {GET, POST, PUT, DELETE, OPTIONS}
)
```

## 前端使用指南

### 1. 检查登录状态
```typescript
import { isAuthenticated } from '@/utils/auth'

const isLogin = await isAuthenticated()
if (!isLogin) {
  // 跳转到登录页
}
```

### 2. 获取用户信息
```typescript
import { getCurrentUserByCookie } from '@/utils/auth'

const result = await getCurrentUserByCookie()
if (result.success) {
  console.log('用户信息:', result.data)
}
```

### 3. 登出操作
```typescript
import { useLoginUserStore } from '@/store/useLoginUserStore'

const loginUserStore = useLoginUserStore()
await loginUserStore.logout()
```

### 4. 监听认证状态
```typescript
import { authChecker } from '@/utils/auth'

// 启动状态检查
authChecker.startChecking((isAuth) => {
  if (!isAuth) {
    // 认证失效处理
    console.log('需要重新登录')
  }
})

// 停止检查
authChecker.stopChecking()
```

## 安全优势

1. **XSS 防护**：HttpOnly Cookie 无法通过 JavaScript 访问
2. **CSRF 防护**：配合 SameSite 属性防止跨站请求伪造
3. **自动过期**：Cookie 过期时间由后端控制
4. **传输安全**：配合 HTTPS 使用 Secure 属性

## 迁移验证

### 测试场景
1. **正常登录**：验证 Cookie 是否正确设置
2. **页面刷新**：验证无需重新登录
3. **数据库清空**：验证 Cookie 认证仍然有效
4. **Cookie 清除**：验证需要重新登录
5. **跨域访问**：验证 CORS 配置正确

### 调试建议
1. 打开浏览器开发者工具
2. 查看 Network 面板中的 Cookie 传输
3. 检查 Application 面板中的 Cookie 存储
4. 监控控制台中的认证状态日志

## 回滚方案

如遇到问题，可以快速回滚到 IndexedDB 方案：
1. 恢复 `useLoginUserStore.ts` 的原始缓存逻辑
2. 禁用 `authChecker` 状态检查器
3. 重新启用 IndexedDB 相关功能

## 后续优化

1. **记住登录**：支持长时效 Cookie
2. **多端同步**：处理多设备登录场景
3. **安全增强**：添加设备指纹识别
4. **性能优化**：优化认证状态检查频率