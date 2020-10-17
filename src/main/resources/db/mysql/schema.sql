DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`email`	VARCHAR(100)	NOT NULL	COMMENT '이메일',
	`password`	VARCHAR(100)	NOT NULL	COMMENT '비밀번호',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '이름',
	`profile_img`	Blob	NULL	COMMENT '프로필 사진',
	`role`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '0 : User / 99 : Admin',
	`provider`	CHAR(1)	NOT NULL	DEFAULT L	COMMENT 'L : Local / G : Google  / N : Naver',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);


DROP TABLE IF EXISTS `room`;

CREATE TABLE `room` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`category_id`	Bigint(20)	NOT NULL	COMMENT 'category index',
	`file_id`	Bigint(20)	NOT NULL	COMMENT 'cover image  file index',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '방 이름',
	`description`	VARCHAR(255)	NULL	COMMENT '방 설명',
	`max_count`	Int	NOT NULL	DEFAULT 0	COMMENT '최대 가입 인원수',
	`join_count`	Int	NOT NULL	DEFAULT 0	COMMENT '가입 인원',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`account_id`	Bigint(20)	NOT NULL	COMMENT 'user index',
	`room_id`	Bigint(20)	NOT NULL	COMMENT 'room index',
	`role`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '0 : 일반 / 1 : 매니저',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
	`Key`	Bigint(20)	NOT NULL	COMMENT 'index',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '카테고리 이름',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`room_id`	Bigint(20)	NOT NULL	COMMENT 'room index',
	`member_id`	Bigint(20)	NOT NULL	COMMENT 'member index',
	`file_id`	Bigint(20)	NOT NULL	COMMENT 'file_index',
	`title`	VARCHAR(255)	NOT NULL	COMMENT '게시글 제목',
	`content`	TEXT	NOT NULL	COMMENT '게시글 내용',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `attendance`;

CREATE TABLE `attendance` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`room_id`	Bigint(20)	NOT NULL	COMMENT 'room index',
	`member_id`	Bigint(20)	NOT NULL	COMMENT 'member index',
	`memo`	TEXT	NOT NULL	COMMENT '출석 메모',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `post_comment`;

CREATE TABLE `post_comment` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`post_id`	Bigint(20)	NOT NULL	COMMENT 'post index',
	`member_id`	Bigint(20)	NOT NULL	COMMENT 'member index',
	`parent_id`	Bigint(20)	NOT NULL	COMMENT '부모 index',
	`seq`	Int	NOT NULL	DEFAULT 0	COMMENT '댓글 순서',
	`content`	TEXT	NOT NULL	COMMENT '댓글 내용',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`file_group_id`	Bigint(20)	NOT NULL	COMMENT 'file group index',
	`origin_name`	VARCHAR(255)	NOT NULL	COMMENT '실제 파일명',
	`save_name`	VARCHAR(255)	NOT NULL	COMMENT '저장된 파일명',
	`file_size`	INT(11)	NOT NULL	DEFAULT 0	COMMENT '파일 사이즈',
	`extension`	VARCHAR(50)	NOT NULL	COMMENT '파일 확장자',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `smtp_send_template`;

CREATE TABLE `smtp_send_template` (
	`id`	Bigint(20)	NOT NULL	COMMENT 'index',
	`template_type`	CHAR(1)	NOT NULL	COMMENT '메일 유형 (R : Register, F : Forgot Password)',
	`title`	varchar(50)	NOT NULL	COMMENT '메일 제목',
	`content`	TEXT	NOT NULL	COMMENT '메일 내용',
	`valid_time`	Int(11)	NOT NULL	COMMENT '유효 시간 (millisecond)',
	`ins_date`	TIMESTAMP	NOT NULL	DEFAULT ON CURRENT_TIMESTAMP	COMMENT '등록 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)'
);

DROP TABLE IF EXISTS `file_group`;

CREATE TABLE `file_group` (
	`id`	Bigint(20)	NOT NULL	DEFAULT AUTO_INCREMENT	COMMENT 'index',
	`save_path`	VARCHAR(255)	NOT NULL	COMMENT '저장 경로'
);

ALTER TABLE `account` ADD CONSTRAINT `PK_ACCOUNT` PRIMARY KEY (
	`id`
);

ALTER TABLE `Untitled2` ADD CONSTRAINT `PK_UNTITLED2` PRIMARY KEY (
	`Key`
);

ALTER TABLE `room` ADD CONSTRAINT `PK_ROOM` PRIMARY KEY (
	`id`
);

ALTER TABLE `member` ADD CONSTRAINT `PK_MEMBER` PRIMARY KEY (
	`id`
);

ALTER TABLE `category` ADD CONSTRAINT `PK_CATEGORY` PRIMARY KEY (
	`Key`
);

ALTER TABLE `post` ADD CONSTRAINT `PK_POST` PRIMARY KEY (
	`id`
);

ALTER TABLE `attendance` ADD CONSTRAINT `PK_ATTENDANCE` PRIMARY KEY (
	`id`
);

ALTER TABLE `post_comment` ADD CONSTRAINT `PK_POST_COMMENT` PRIMARY KEY (
	`id`
);

ALTER TABLE `file` ADD CONSTRAINT `PK_FILE` PRIMARY KEY (
	`id`
);

ALTER TABLE `smtp_send_template` ADD CONSTRAINT `PK_SMTP_SEND_TEMPLATE` PRIMARY KEY (
	`id`
);

ALTER TABLE `file_group` ADD CONSTRAINT `PK_FILE_GROUP` PRIMARY KEY (
	`id`
);
