CREATE TABLE `tb_ad`
(
    `ad_id`             BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '광고 ID',
    `ad_name`           VARCHAR(255)                      NOT NULL COMMENT '광고 이름',
    `ad_description`    LONGTEXT                          NULL COMMENT '광고 설명',
    `ad_url`            VARCHAR(255)                      NOT NULL COMMENT '광고 URL',
    `start_date`        DATE                              NOT NULL COMMENT '광고 시작일',
    `end_date`          DATE                              NULL COMMENT '광고 종료일',
    `ad_type`           ENUM('MAN_FOOD', 'WOMAN_FOOD', 'KIDS_FOOD')     NOT NULL COMMENT '광고 타입',
    `created_at`        DATETIME                          NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '생성일',
    `updated_at`        DATETIME                          NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '수정일'
) COMMENT '광고 테이블' ENGINE = InnoDB DEFAULT CHARSET = utf8;


CREATE TABLE `tb_user_ad_recommendation`
(
    `user_ad_recommendation_id` BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '유저별 추천 광고 ID',
    `user_id`             BIGINT                             NOT NULL COMMENT '유저 ID',
    `ad_id`               BIGINT                             NOT NULL COMMENT '광고 ID',
    `created_at`          DATETIME                           NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '생성일',
    `updated_at`          DATETIME                           NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '수정일',
    FOREIGN KEY (`ad_id`) REFERENCES `tb_ad` (`ad_id`)
) COMMENT '유저별 추천 광고 테이블' ENGINE = InnoDB DEFAULT CHARSET = utf8;


CREATE TABLE `tb_daily_ad_statistics`
(
    `daily_ad_statistics_id` BIGINT     PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '일별 광고 통계 ID',
    `ad_id`         BIGINT     NOT NULL COMMENT '광고 ID',
    `stat_date`     DATE       NOT NULL COMMENT '통계 날짜',
    `views`         INT        NOT NULL DEFAULT 0 COMMENT '조회수',
    `clicks`        INT        NOT NULL DEFAULT 0 COMMENT '클릭수',
    `created_at`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '생성일',
    `updated_at`    DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '수정일',
    FOREIGN KEY (`ad_id`) REFERENCES `tb_ad` (`ad_id`)
) COMMENT '일별 광고 통계 테이블' ENGINE = InnoDB DEFAULT CHARSET = utf8;
