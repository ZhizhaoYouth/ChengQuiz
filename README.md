# 橙小测 AI 问卷测评平台

### 功能模块+需求分析

---

#### 用户模块

- 用户注册（P0）
- 用户登录（P0）
- 编辑个人资料（P2）
- 管理用户（仅管理员可用）（p1）

#### 应用模块

- 创建应用（P0）
- 编辑应用（P1）
- 删除应用（P1）
- 查看应用列表（P0）
- 查看应用详情（P0）
- 查看自己的应用（P1）
- 管理、审核应用（管理员可用）（P1）
- 分享应用（生成二维码）（p2）

#### 题目模块

- 添加题目（P0）
- 删除题目（P1）
- 修改题目（P1）
- 使用AI生成题目（p1）
- 管理题目（仅管理员可用）（p1）

#### 评分模块

- 创建评分结果（P0）
- 修改评分结果（P1）
- 删除评分结果（P2）
- 评分算法：
	- 测评类应用自定义评分（P0）
	- 得分类应用自定义评分（P0）
	- 测评类应用AI评分（P1）


- 管理评分结果（仅管理员可用）（p1）

#### 回答记录模块

- 创建回答记录（P0）
- 查看回答记录（p0）
- 查看个人回答记录列表（P1）
- 管理回答记录（仅管理员可用）（p1）

#### 统计分析模块

- 根据应用id，分析此应用回答结果分布（P2）
- 查看应用热门top10（P2）
- 其他统计分析功能（p2）

### 流程图

![image-20240802153128149](https://chengquiz-1301835275.cos.ap-beijing.myqcloud.com/README%2F%E6%B5%81%E7%A8%8B%E5%9B%BE.png)

### 架构图

![image-20240802153153729](https://chengquiz-1301835275.cos.ap-beijing.myqcloud.com/README%2F%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

### 技术选型

---

#### 后端

- 框架：Spring Boot框架
- 存储层：MySQL数据库+Redis缓存实现分布式锁+腾讯COS对象存储
- 数据访问：Mybatis-Plus
- 缓存：Caffeine本地缓存+Redisson分布式锁
- AI服务：智谱AI的ChatGLM模型
- 生成内容推送：RxJava+SSE流式推送
- 性能优化：Sharding JDBC分库分表

#### 前端

- 框架：Vue3，Vue-CLI脚手架
- 组件库：Arco Design
- 图表渲染：ECharts
- 请求库：Axios
- 状态管理：Pinia
- API接口代码：umijs
- 二维码生成：QRCode.js
- 前端工程化：ESLint+Prettier+TypeScript

