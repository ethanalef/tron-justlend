package org.tron.justlend.justlendapiserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作記錄 - 投票
 */
@Getter
@AllArgsConstructor
public enum VoteRecord {
    GET_VOTE(1),            // 兑换选票 ------------ xxx 选票
    VOTE_FOR(2),            // 支持 #{} 提案 ------------ xxx 选票
    VOTE_AGAINST(3),        // 反對 #{} 提案 ------------ xxx 选票
    RECYCLE_VOTE(4),        // 回收 #{} 提案 ------------ xxx 选票
    CONVERT_OLD_VOTE(5),    // 赎回选票 ------------ xxx 失效选票
    CONVERT_VOTE(6);        // "赎回 ------------ xxx JST

    private final int type;
}
