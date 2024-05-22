package org.tron.justlend.justlendapiserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "strx_account_record")
public class StrxAccountRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_address")
    private String userAddress;

    /**
     * 1: DEPOSIT 2: WITHDRAW 4: CLAIM 5: TRANSFER_OUT 6: TRANSFER_IN
     */
    @Column(name = "op_type")
    private Integer opType;

    private String amount;

    private String usd;

    @Column(name = "tx_id")
    private String txId;

    @Column(name = "log_index")
    private Integer logIndex;

    @Column(name = "event_time")
    private Instant eventTime;
}