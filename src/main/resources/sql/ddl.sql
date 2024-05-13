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

DROP TABLE IF EXISTS lend_proposal;
CREATE TABLE lend_proposal (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `proposal_id` bigint NOT NULL,
    `end_block` bigint NOT NULL,
    `proposer` varchar(64) DEFAULT NULL,
    `propose_txid` varchar(64) DEFAULT NULL,
    `queued_txid` varchar(64) DEFAULT NULL,
    `executed_txid` varchar(64) DEFAULT NULL,
    `state` int NOT NULL DEFAULT '-1' COMMENT '-1:hide,0:Pending,1:Active,2:Cancelled,3:Defeated,4:Succeeded,5:Queued,6:Expired,7:Executed',
    `for_votes` varchar(64) DEFAULT '0',
    `against_votes` varchar(64) DEFAULT '0',
    `abstain_votes` varchar(64) DEFAULT '0',
    `active_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `cancel_time` timestamp NULL DEFAULT NULL,
    `queued_time` timestamp NULL DEFAULT NULL,
    `executed_time` timestamp NULL DEFAULT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `proposal_id` (`proposal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS lend_proposal_vote;
CREATE TABLE lend_proposal_vote (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `user_address` varchar(64) NOT NULL,
    `proposal_id` bigint NOT NULL,
    `for_votes` varchar(64) DEFAULT '0',
    `against_votes` varchar(64) DEFAULT '0',
    `abstain_votes` varchar(64) DEFAULT '0',
    `state` int NOT NULL DEFAULT '1' COMMENT '1:voted in,2:already withdraw',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_address` (`user_address`,`proposal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS lend_vote_record;
CREATE TABLE `lend_vote_record` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `user_address` varchar(64) NOT NULL,
    `op_type` int NOT NULL COMMENT '1: 兑换选票 2: 支持 3: 反對 4: 回收 5: 赎回选票 6: 赎回 JST',
    `proposal_id` int DEFAULT NULL,
    `amount` varchar(64) NOT NULL DEFAULT '',
    `tx_id` varchar(64) NOT NULL,
    `log_index` int NOT NULL,
    `block_timestamp` bigint NOT NULL,
    `status` int NOT NULL DEFAULT '1' COMMENT '1:not yet confirmed,2:confirmed',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `txid_logindex` (`tx_id`,`log_index`),
    KEY `user_address` (`user_address`),
    KEY `blocktimestamp_status` (`block_timestamp`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;