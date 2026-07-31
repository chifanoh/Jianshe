# 简社（社区/论坛）

一个基于 Vue 3 + Spring Boot 的现代化内容管理平台。

## 技术栈

### 前端

- **框架**: Vue 3 + TypeScript
- **构建工具**: Vite 7.x
- **状态管理**: Pinia
- **路由**: Vue Router 4.x
- **UI 组件**: Naive UI
- **富文本编辑器**: WangEditor
- **图标**: @vicons/ionicons5, @sicons/carbon

### 后端

- **框架**: Spring Boot 3.x
- **数据库**: MySQL 8.x + Redis 7.x + Manticore Search
- **ORM**: MyBatis Plus
- **认证**: JWT
- **验证码**: Cloudflare Turnstile

## 功能特性

- ✅ 用户注册与登录
- ✅ 文章发布与管理
- ✅ 评论系统
- ✅ 文章搜索功能
- ✅ 新闻轮播展示
- ✅ 点赞与关注
- ✅ 响应式设计

## 快速开始

### 环境要求

- Node.js >= 22.x
- JDK >= 21
- MySQL >= 8.0
- Redis >= 7.0
- Manticore Search >= 6.0

### 前端启动

```bash
cd Vue/miku-app-vue
npm install
npm run dev
```

### 后端启动

```bash
cd spring/miku-spring-boot
mvn spring-boot:run
```

### 配置说明

后端配置文件位于 `spring/miku-spring-boot/miku-services/src/main/resources/application.yml`

主要配置项：

- 数据库连接信息
- Redis 配置
- JWT 密钥
- Turnstile 验证码密钥
- NewsAPI 密钥

## 项目结构

```
├── Vue/                              # 前端项目
│   └── miku-app-vue/
│       ├── src/
│       │   ├── api/                  # API 请求
│       │   ├── components/           # 组件
│       │   ├── composables/          # 组合式函数
│       │   ├── Layouts/              # 布局组件
│       │   ├── pages/                # 页面
│       │   ├── router/               # 路由配置
│       │   ├── store/                # 状态管理
│       │   └── utils/                # 工具函数
│       └── package.json
├── spring/                           # 后端项目
│   └── miku-spring-boot/
│       ├── miku-common/              # 公共模块
│       ├── miku-pojo/                # 数据模型
│       └── miku-services/            # 服务模块
│           └── src/main/java/com/miku/
│               ├── controller/        # 控制器
│               ├── service/           # 服务层
│               ├── interceptor/       # 拦截器
│               └── config/            # 配置类
└── README.md
```

## API 接口

### 用户接口

- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `GET /api/user/profile` - 获取用户信息

### 文章接口

- `GET /api/articles` - 获取文章列表
- `POST /api/articles` - 创建文章
- `GET /api/articles/{id}` - 获取文章详情
- `PUT /api/articles/{id}` - 更新文章
- `DELETE /api/articles/{id}` - 删除文章

### 评论接口

- `GET /api/comments/{articleId}` - 获取评论列表
- `POST /api/comments` - 发布评论

### 搜索接口

- `GET /api/search` - 全文搜索
