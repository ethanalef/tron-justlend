package org.tron.justlend.justlendapiserver.chainchaser.tron.chaser;

import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.chainchaser.Event;
import org.tron.justlend.justlendapiserver.chainchaser.EventSignature;
import org.tron.justlend.justlendapiserver.chainchaser.dto.EventLog;
import org.tron.justlend.justlendapiserver.chainchaser.tron.TronEventChaser;
import org.tron.justlend.justlendapiserver.config.Contracts;
import org.tron.justlend.justlendapiserver.config.Tokens;
import org.tron.justlend.justlendapiserver.core.ServiceException;
import org.tron.justlend.justlendapiserver.enums.VoteRecord;
import org.tron.justlend.justlendapiserver.model.LendVoteRecord;
import org.tron.justlend.justlendapiserver.service.ChaserProgressService;
import org.tron.justlend.justlendapiserver.service.LendVoteRecordService;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronAddressUtils;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronJsonRpc;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.tron.justlend.justlendapiserver.chainchaser.EventSignature.*;

@Slf4j
@Component
public class GovernanceUserEventChaser extends TronEventChaser {
  private final Contracts contracts;
  private final Tokens tokens;
  private final LendVoteRecordService lendVoteRecordService;
  protected Logger logger = LoggerFactory.getLogger(this.getClass());



  protected GovernanceUserEventChaser(TronJsonRpc tronJsonRpc, ChaserProgressService chaserProgressService,
                                      Contracts contracts,
                                      Tokens tokens,
                                      LendVoteRecordService lendVoteRecordService
  ) {
    super(tronJsonRpc, chaserProgressService);
    this.contracts = contracts;
    this.tokens = tokens;
    this.lendVoteRecordService = lendVoteRecordService;
  }

  @PostConstruct
  void postConstruct() {
    topic = Lists.newArrayList(
      JUSTLENDGOV_BRAVO_USER_VOTE.getSignatureHash(),       // 单次投票方向及数量
      JUSTLENDGOV_USER_WITHDRAW.getSignatureHash(),  // 回收 WJSTOLD事件
      DEPOSIT.getSignatureHash(),  // 兑换选票 WJSTOLD事件 JST -> WJST
      JUSTLENDGOV_WITHDRAW.getSignatureHash() // 赎回失效选票 WJSTOLD事件 WJSTOLD -> JST or 赎回JST WJST事件 WJST -> JST
    );
    contract = Lists.newArrayList(
      contracts.getTron().get("GovernorBravoDelegator"),
      tokens.getTronTokenBySymbol("WJST").address(),
      tokens.getTronTokenBySymbol("WJSTOLD").address()
    );
    event = Event.GOVERNANCE_USER;
    range = BigInteger.valueOf(10000);
    startHeight = BigInteger.valueOf(10000000);
  }

  @Override
  protected void processEvent(EventLog eventLog) {
    try {
      String signatureHash = eventLog.topics().getFirst();
      EventSignature eventSignature = EventSignature.fromSignature(signatureHash);
      switch (eventSignature) {
        case JUSTLENDGOV_BRAVO_USER_VOTE:
          processVote(eventLog);
          break;
        case JUSTLENDGOV_USER_WITHDRAW:
          processUserWithdraw(eventLog);
          break;
        case DEPOSIT:
          processDeposit(eventLog);
          break;
        case JUSTLENDGOV_WITHDRAW:
          processWithraw(eventLog);
          break;
        default:
          // skip irrelevant events
          break;
      }
    } catch (Exception e) {
      logger.error("Error processing TRON event", e);
      throw new ServiceException(event + " failed for " + eventLog.getUniqueId(), e);
    }
  }

  private void processVote(EventLog eventLog) {
    // 操作紀錄 - 投票
    // e.g. online 0b031b2ea2b46ae1cda25485e2679342ace31d3deb3072f98030e5af9e739c41
    String userAddress = TronAddressUtils.hexToBase58(eventLog.topics().get(1));
    Integer proposalId = Integer.parseInt(eventLog.topics().get(2), 16);
    BigInteger voteCnt = new BigInteger(eventLog.getData(1), 16);
    String amount = new BigDecimal(voteCnt).movePointLeft(tokens.getTronTokenBySymbol("WJST").decimals()).toPlainString();
    int supportType = Integer.parseInt(eventLog.getData(0), 16);
    Integer opType = null;
    if (supportType == 1)
      opType = VoteRecord.VOTE_FOR.getType();
    else if (supportType == 0)
      opType = VoteRecord.VOTE_AGAINST.getType();
    upsertLendVoteRecord(eventLog, opType, userAddress, proposalId, amount);
  }

  private void processUserWithdraw(EventLog eventLog) {
    // 操作紀錄 - 回收
    // e.g. online be1f0ea87d894580a1fbd69eb282a448439615485ba3f3c9118636dfa95768e8
    BigInteger amountRaw = new BigInteger(eventLog.getData(0), 16);
    String amount = new BigDecimal(amountRaw).movePointLeft(tokens.getTronTokenBySymbol("WJST").decimals()).toPlainString();
    if ("0".equals(amount)) {
      return;
    }
    String userAddress = TronAddressUtils.hexToBase58(eventLog.topics().get(1));
    Integer proposalId = Integer.parseInt(eventLog.topics().get(2), 16);
    Integer opType =  VoteRecord.RECYCLE_VOTE.getType();
    upsertLendVoteRecord(eventLog, opType, userAddress, proposalId, amount);
  }

  private void processDeposit(EventLog eventLog) {
    // 操作紀錄 - 兑换选票 deposit: get votes (WJST) from JST
    // e.g. nile 473b208f80b4da35cfad8e394cf718bc847e00a37ba61b68db10d3acec8805ac
    String wjst = tokens.getTronTokenBySymbol("WJST").address();
    // only accept deposit via WJST contract to filter out deposit event in proposalCreate;
    // prevent from missing record, ignore non-target contract check if getTransactionInfoById failed
    if (!wjst.equals(eventLog.to())) {
      return;
    }

    if (wjst.equals(eventLog.contractAddress())) {  // WJST token
      String userAddress = eventLog.from(); // origin txn.getResult().getFrom()
      Integer opType = VoteRecord.GET_VOTE.getType();
      BigInteger amountRaw = new BigInteger(eventLog.getData(0), 16);
      String amount = new BigDecimal(amountRaw).movePointLeft(tokens.getTronTokenBySymbol("WJST").decimals()).toPlainString();
      upsertLendVoteRecord(eventLog, opType, userAddress, null, amount);
    }
  }

  private void processWithraw(EventLog eventLog) {
    // 操作紀錄 - 赎回失效选票 WJSTOLD事件 WJSTOLD -> JST 或 赎回JST WJST事件 WJST -> JST
    // e.g. online 66a16b31bbc97cf15cce316965009f894fa1b8d4dd9b10f7fd8c8d45d4025910
    String wjst = tokens.getTronTokenBySymbol("WJST").address();
    String oldWjst = tokens.getTronTokenBySymbol("WJSTOLD").address();
    // only accept withdrawal via WJST or old WJST contract to filter out unwanted method like withdrawToken, withdrawTokenCFO
    // e.g. unwanted: https://nile.tronscan.io/#/transaction/ac0e422903faa0940a2b3f6c63e6a7f1d588f341306193cfbec6ba0946939429.
    // prevent from missing record, ignore non-target contract check if getTransactionInfoById failed
    if (!wjst.equals(eventLog.to()) && !oldWjst.equals(eventLog.to())) {
      return;
    }

    int opType;
    if (oldWjst.equals(eventLog.contractAddress())) {
      opType = VoteRecord.CONVERT_OLD_VOTE.getType();
    } else if (wjst.equals(eventLog.contractAddress())) {
      opType = VoteRecord.CONVERT_VOTE.getType();
    } else {
      return;
    }
    String userAddress = eventLog.from();
    BigInteger amountRaw = new BigInteger(eventLog.getData(0), 16);
    String amount = new BigDecimal(amountRaw).movePointLeft(tokens.getTronTokenBySymbol("JST").decimals()).toPlainString();
    upsertLendVoteRecord(eventLog, opType, userAddress, null, amount);
  }

  private void upsertLendVoteRecord(EventLog eventLog, Integer opType, String userAddress, Integer proposalId, String amount) {
    // skip irrelevant opType
    if (opType == null) {
      return;
    }

    String txId = eventLog.transactionId();
    int logIndex = eventLog.logIndex();
    long blockTimestamp = eventLog.timestamp();

    LendVoteRecord lendVoteRecord = LendVoteRecord.builder()
                                      .userAddress(userAddress)
                                      .opType(opType)
                                      .proposalId(proposalId)
                                      .amount(amount)
                                      .txId(txId)
                                      .logIndex(logIndex)
                                      .blockTimestamp(blockTimestamp)
                                      .build();
    lendVoteRecordService.upsert(lendVoteRecord);
  }
}
