# Mamba Search Backend

聚合搜索后端服务，支持文章、图片、用户等多数据源的全网聚合搜索，并提供文章管理、用户管理、ES 全文检索等能力。

## 技术栈

| 技术 | 说明 |
|------|------|
| **Spring Boot 3.5** | 应用框架，Java 17 |
| **MyBatis-Plus** | ORM 框架 |
| **MySQL** | 关系型数据库 |
| **Redis + Spring Session** | 缓存 & 分布式 Session |
| **Elasticsearch** | 全文搜索引擎（IK 分词） |
| **SpringDoc OpenAPI** | 接口文档（Swagger UI） |
| **AOP** | 登录鉴权切面 |
| **腾讯云 COS** | 对象存储 |
| **Hutool** | 工具类库 |
| **JSoup** | HTML 解析（图片抓取） |

## 项目结构

```
src/main/java/com/rc/mambasaerchbackend/
├── annotation/           # 自定义注解（@AuthCheck）
├── aop/                  # AOP 切面（鉴权拦截）
├── common/               # 通用响应体、异常、错误码、全局异常处理
├── config/               # 配置类（CORS、Jackson、MyBatis-Plus、Redis、Session、Swagger）
├── controller/           # 接口层
│   ├── ArticleController   # 文章增删改查
│   ├── PictureController   # 图片搜索
│   ├── SearchController    # 聚合搜索入口
│   └── UserController      # 用户注册/登录/查询
├── datasource/           # 多数据源搜索架构
│   ├── DataSource          # 数据源接口
│   ├── DataSourceRegistry  # 数据源注册中心
│   ├── ArticleDataSource   # 文章数据源
│   ├── PictureDataSource   # 图片数据源
│   └── UserDataSource      # 用户数据源
├── enums/                # 搜索类型枚举
├── manager/              # 业务门面（SearchFacade）
├── mapper/               # MyBatis-Plus Mapper
├── model/
│   ├── dto/                # 请求参数 DTO
│   ├── entity/             # 数据库实体
│   └── vo/                 # 视图对象
├── search/               # ES 搜索模块
│   ├── ElasticsearchService  # ES 搜索服务
│   ├── SearchDoc             # ES 文档实体
│   ├── SearchDocRepository   # ES Repository
│   ├── SearchItemVO          # 搜索结果条目
│   ├── SearchResultVO        # 搜索结果封装
│   └── TypeCountVO           # 类型统计聚合
├── service/              # 业务逻辑层
└── utils/                # 工具类
```

## 快速启动

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6+
- Elasticsearch 8.x（可选，启动时需要连接）

### 配置修改

编辑 `application-dev.yml`，配置以下依赖服务地址：

```yaml
spring:
  datasource:
    url: jdbc:mysql://<your-host>:3306/mamba_search?...
    username: root
    password: 123456
  data:
    redis:
      host: localhost
      port: 6379
  elasticsearch:
    uris: http://<your-host>:9200    # 如未安装 ES 请注释掉
```

> 若未安装 Elasticsearch，注释掉 `spring.elasticsearch` 相关配置。

### 启动

```bash
mvn spring-boot:run
```

应用默认运行在 `http://localhost:8080`。

API 文档（Swagger UI）：`http://localhost:8080/swagger-ui/index.html`

## 核心设计

### 聚合搜索

采用策略模式实现多数据源聚合搜索：

- `DataSource<T>` 接口定义统一的 `doSearch()` 方法
- 各数据源（文章、图片、用户）分别实现该接口
- `DataSourceRegistry` 在初始化时自动注册所有数据源
- `SearchFacade` 根据请求类型路由到对应数据源或并发查询全部

### ES 全文搜索

- 使用 Spring Data Elasticsearch（ELC 客户端）
- IK 中文分词，标题权重 3 倍
- 支持类型过滤、分类过滤、多字段匹配、高亮展示
- 聚合统计：按文档类型分组计数

### 鉴权体系

- `@AuthCheck` 注解 + AOP 切面实现登录校验
- Redis + Spring Session 管理分布式登录态

## API 概览

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/user/register` | 用户注册 |
| POST | `/api/user/login` | 用户登录 |
| POST | `/api/user/logout` | 用户登出 |
| GET | `/api/user/current` | 获取当前登录用户 |
| GET | `/api/user/search` | 用户搜索 |
| POST | `/api/article` | 创建文章 |
| DELETE | `/api/article/{id}` | 删除文章 |
| PUT | `/api/article/{id}` | 更新文章 |
| GET | `/api/article/{id}` | 获取文章详情 |
| GET | `/api/article/search` | 条件检索文章 |
| GET | `/api/picture/search` | 图片搜索 |
| POST | `/api/search/all` | 聚合搜索（全部/按类型） |
