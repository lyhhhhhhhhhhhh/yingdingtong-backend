

-- 创建库
create database if not exists yingdingtong;

-- 切换库
use yingdingtong;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    editTime     datetime     default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
    ) comment '用户' collate = utf8mb4_unicode_ci;

-- 电影表
create table if not exists movie
(
    id bigint auto_increment comment 'id' primary key,
    movieTitle  varchar(256)                           not null comment '电影名称',
    movieType  	varchar(256)                           not null comment '电影类型',
    movieDuration  int                                 not null comment '电影时长',
    moviePicture varchar(2048)                         not null comment '电影图片',
    userId      bigint                                 not null comment '创建用户 id',
    movieRating      decimal(3,1)                                 not null comment '电影评分',
    movieRegion      varchar(256)                                  not null comment '电影地区',
    editTime    datetime default CURRENT_TIMESTAMP     not null comment '编辑时间',
    createTime  datetime default CURRENT_TIMESTAMP     not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_movieTitle (movieTitle)
    ) comment '电影' collate = utf8mb4_unicode_ci;

-- 影院表
create table if not exists cinema
(
    id bigint auto_increment comment 'id' primary key,
    cinemaTitle   varchar(256)                           not null comment '影院名称',
    cinemaAddress varchar(256)                           not null comment '影院地址',
    cinemaTags    varchar(1024)                          not null comment '影院标签',
    userId      bigint                                   not null comment '创建用户 id',
    editTime    datetime default CURRENT_TIMESTAMP       not null comment '编辑时间',
    createTime  datetime default CURRENT_TIMESTAMP       not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP       not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_cinemaTitle (cinemaTitle)
    ) comment '影院' collate = utf8mb4_unicode_ci;

-- 影院电影表（硬删除）
create table if not exists cinema_movie
(
    id bigint auto_increment comment 'id' primary key,
    cinemaId bigint                             	not null comment '影院id',
    movieId  bigint                                 not null comment '电影 id',
    userId   bigint                                not null comment '创建用户 id',
    createTime     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    UNIQUE (cinemaId, movieId)
) comment '影院电影' collate = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS CinemaMovieSchedule
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    cinemaId        BIGINT NOT NULL COMMENT '影院 id',
    movieId         BIGINT NOT NULL COMMENT '电影 id',
    userId          BIGINT NOT NULL COMMENT '创建用户 id',
    showTime        DATETIME NOT NULL COMMENT '放映时间',
    movieLanguage   VARCHAR(256) NOT NULL COMMENT '语言(如中文、英文)',
    cinemaHallName  VARCHAR(256) NOT NULL COMMENT '放映厅名称',
    moviePrice      DECIMAL(10, 2) NOT NULL COMMENT '电影价格',
    editTime        DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
    createTime      DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDeleted       TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除',

    INDEX idx_cinemaId (cinemaId),
    INDEX idx_movieId (movieId)
) COMMENT '影院电影排期表' COLLATE = utf8mb4_unicode_ci;