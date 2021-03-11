DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	`file_group_id`	Bigint(20)  NULL	COMMENT 'file group index',
	`origin_name`	VARCHAR(255)	NOT NULL	COMMENT '실제 파일명',
	`save_name`	VARCHAR(255)	NOT NULL	COMMENT '저장된 파일명',
	`file_size`	INT(11)	NOT NULL	DEFAULT 0	COMMENT '파일 사이즈',
	`extension`	VARCHAR(50)	NOT NULL	COMMENT '파일 확장자',
	`save_path`	VARCHAR(255)	NOT NULL	COMMENT '저장 경로',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `post_comment`;

CREATE TABLE `post_comment` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	`post_id`	Bigint(20)	NOT NULL	COMMENT 'post index',
	`member_id`	Bigint(20)	NOT NULL	COMMENT 'member index',
	`parent_id`	Bigint(20)	NULL	COMMENT '부모 index',
	`seq`	Int	NOT NULL	DEFAULT 0	COMMENT '댓글 순서',
	`content`	TEXT	NOT NULL	COMMENT '댓글 내용',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	`room_id`	Bigint(20)	NOT NULL	COMMENT 'room index',
	`member_id`	Bigint(20)	NOT NULL	COMMENT 'member index',
	`file_group_id` bigint NULL	COMMENT 'file_index',
	`title`	VARCHAR(255)	NOT NULL	COMMENT '게시글 제목',
	`content`	TEXT	NOT NULL	COMMENT '게시글 내용',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `attendance`;

CREATE TABLE `attendance` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	`room_id`	Bigint(20)	NOT NULL	COMMENT 'room index',
	`member_id`	Bigint(20)	NOT NULL	COMMENT 'member index',
	`memo`	TEXT	NOT NULL	COMMENT '출석 메모',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	`account_id`	Bigint(20)	NOT NULL	COMMENT 'user index',
	`room_id`	Bigint(20)	NOT NULL	COMMENT 'room index',
	`role`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '0 : 일반 / 1 : 매니저',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
	`id`	Bigint(20)	NOT NULL AUTO_INCREMENT	COMMENT 'index',
	`email` varchar(100) NOT NULL COMMENT '이메일',
	`password`	VARCHAR(100) NULL	COMMENT '비밀번호',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '이름',
	`profile_img`	Blob	NULL	COMMENT '프로필 사진',
	`role`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '0 : User / 99 : Admin',
	`provider`	CHAR(1)	NOT NULL	DEFAULT 'L'	COMMENT 'L : Local / G : Google  / N : Naver',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `room`;

CREATE TABLE `room` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	`category_id`	Bigint(20)	NOT NULL	COMMENT 'category index',
	`cover_id`	Bigint(20)	NULL	COMMENT 'cover image  file index',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '방 이름',
	`description`	VARCHAR(255)	NULL	COMMENT '방 설명',
	`max_count`	Int	NOT NULL	DEFAULT 0	COMMENT '최대 가입 인원수',
	`join_count`	Int	NOT NULL	DEFAULT 0	COMMENT '가입 인원',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	`name`	VARCHAR(50)	NOT NULL	COMMENT '카테고리 이름',
	`ins_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP	COMMENT '입력 일시',
	`upd_date`	TIMESTAMP	NULL	COMMENT '수정 일시',
	`del_date`	TIMESTAMP	NULL	COMMENT '삭제 일시',
	`del_flag`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '삭제 여부 (0 : false, 1 : true)',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `file_group`;

CREATE TABLE `file_group` (
	`id`	Bigint(20)	NOT NULL    AUTO_INCREMENT	COMMENT 'index',
	PRIMARY KEY (`id`)
);

ALTER TABLE `room` ADD CONSTRAINT `FK_category_TO_room_1` FOREIGN KEY (
	`category_id`
)
REFERENCES `category` (
	`id`
);

ALTER TABLE `room` ADD CONSTRAINT `FK_file_group_TO_room_1` FOREIGN KEY (
	`cover_id`
)
REFERENCES `file_group` (
	`id`
);

ALTER TABLE `member` ADD CONSTRAINT `FK_account_TO_member_1` FOREIGN KEY (
	`account_id`
)
REFERENCES `account` (
	`id`
);

ALTER TABLE `member` ADD CONSTRAINT `FK_room_TO_member_1` FOREIGN KEY (
	`room_id`
)
REFERENCES `room` (
	`id`
);

ALTER TABLE `post` ADD CONSTRAINT `FK_room_TO_post_1` FOREIGN KEY (
	`room_id`
)
REFERENCES `room` (
	`id`
);

ALTER TABLE `post` ADD CONSTRAINT `FK_member_TO_post_1` FOREIGN KEY (
	`member_id`
)
REFERENCES `member` (
	`id`
);

ALTER TABLE `post` ADD CONSTRAINT `FK_file_group_TO_post_1` FOREIGN KEY (
	`file_group_id`
)
REFERENCES `file_group` (
	`id`
);

ALTER TABLE `attendance` ADD CONSTRAINT `FK_room_TO_attendance_1` FOREIGN KEY (
	`room_id`
)
REFERENCES `room` (
	`id`
);

ALTER TABLE `attendance` ADD CONSTRAINT `FK_member_TO_attendance_1` FOREIGN KEY (
	`member_id`
)
REFERENCES `member` (
	`id`
);

ALTER TABLE `post_comment` ADD CONSTRAINT `FK_post_TO_post_comment_1` FOREIGN KEY (
	`post_id`
)
REFERENCES `post` (
	`id`
);

ALTER TABLE `post_comment` ADD CONSTRAINT `FK_member_TO_post_comment_1` FOREIGN KEY (
	`member_id`
)
REFERENCES `member` (
	`id`
);

ALTER TABLE `post_comment` ADD CONSTRAINT `FK_post_comment_TO_post_comment_1` FOREIGN KEY (
	`parent_id`
)
REFERENCES `post_comment` (
	`id`
);

ALTER TABLE `file` ADD CONSTRAINT `FK_file_group_TO_file_1` FOREIGN KEY (
	`file_group_id`
)
REFERENCES `file_group` (
	`id`
);
