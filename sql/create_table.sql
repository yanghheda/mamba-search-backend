-- =============================================
-- 用户表 (user)
-- =============================================
CREATE TABLE IF NOT EXISTS `user` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`        VARCHAR(50)  NOT NULL              COMMENT '用户名',
    `password`        VARCHAR(255) NOT NULL              COMMENT '密码（加密存储）',
    `nickname`        VARCHAR(50)  DEFAULT NULL          COMMENT '昵称',
    `email`           VARCHAR(100) DEFAULT NULL          COMMENT '邮箱',
    `phone`           VARCHAR(20)  DEFAULT NULL          COMMENT '手机号',
    `avatar`          VARCHAR(500) DEFAULT NULL          COMMENT '头像URL',
    `role`            VARCHAR(20)  NOT NULL DEFAULT 'user' COMMENT '角色: admin / user',
    `status`          TINYINT      NOT NULL DEFAULT 1    COMMENT '状态: 0-禁用, 1-启用',
    `last_login_time` DATETIME     DEFAULT NULL          COMMENT '最后登录时间',
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT      NOT NULL DEFAULT 0    COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 文章表 (article)
-- =============================================
CREATE TABLE IF NOT EXISTS `article` (
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`      VARCHAR(200) NOT NULL              COMMENT '标题',
    `content`    LONGTEXT     NOT NULL              COMMENT '内容',
    `tags`       VARCHAR(500) DEFAULT NULL          COMMENT '标签（逗号分隔）',
    `user_id`    BIGINT       NOT NULL              COMMENT '作者ID',
    `created_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    TINYINT      NOT NULL DEFAULT 0    COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';
