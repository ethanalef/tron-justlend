DROP TABLE IF EXISTS chaser_progress;
CREATE TABLE chaser_progress (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `event` varchar(32) NOT NULL,
    `processed_height` bigint NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `event` (`event`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS lend_vote_record;
CREATE TABLE `lend_vote_record` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `user_address` varchar(64) NOT NULL,
    `op_type` int NOT NULL COMMENT '1: 兑换选票 2: 支持 3: 反對 4: 回收 5: 赎回选票 6: 赎回 JST',
    `proposal_id` int DEFAULT NULL,
    `amount` varchar(64) NOT NULL,
    `tx_id` varchar(64) NOT NULL,
    `log_index` int unsigned NOT NULL,
    `event_time` timestamp NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` DESC),
    UNIQUE KEY `txid_logindex` (`tx_id`,`log_index`),
    KEY `user_address` (`user_address`),
    KEY `event_time` (`event_time` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS strx_account_record;
CREATE TABLE strx_account_record (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `user_address` varchar(64) NOT NULL DEFAULT '',
    `op_type` int NOT NULL COMMENT '1: DEPOSIT 2: WITHDRAW 4: CLAIM 5: TRANSFER_OUT 6: TRANSFER_IN',
    `amount` varchar(64) NOT NULL,
    `usd` varchar(64) DEFAULT '',
    `tx_id` varchar(64) NOT NULL,
    `log_index` int unsigned NOT NULL,
    `event_time` timestamp NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id` DESC),
    KEY `address_event_time` (`user_address`, `event_time` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS price_centre_feed;
CREATE TABLE price_centre_feed (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `symbol` varchar(16) NOT NULL,
    `usd` decimal(64,18) NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `symbol` (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;