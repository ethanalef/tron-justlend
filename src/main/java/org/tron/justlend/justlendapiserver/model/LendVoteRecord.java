package org.tron.justlend.justlendapiserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lend_vote_record")
public class LendVoteRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_address")
    private String userAddress;

    /**
     * 1: 兑换选票 2: 支持 3: 反對 4: 回收 5: 赎回选票 6: 赎回 JST
     */
    @Column(name = "op_type")
    private Integer opType;

    @Column(name = "proposal_id")
    private Integer proposalId;

    private String amount;

    @Column(name = "tx_id")
    private String txId;

    @Column(name = "log_index")
    private Integer logIndex;

    @Column(name = "block_timestamp")
    private Long blockTimestamp;
}