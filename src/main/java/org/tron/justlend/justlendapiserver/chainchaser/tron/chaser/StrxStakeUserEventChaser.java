package org.tron.justlend.justlendapiserver.chainchaser.tron.chaser;

import com.google.common.collect.Lists;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tron.justlend.justlendapiserver.chainchaser.EventSignature;
import org.tron.justlend.justlendapiserver.chainchaser.dto.EventLog;
import org.tron.justlend.justlendapiserver.chainchaser.tron.TronEventChaser;
import org.tron.justlend.justlendapiserver.config.Tokens;
import org.tron.justlend.justlendapiserver.core.ServiceException;
import org.tron.justlend.justlendapiserver.enums.StrxStakeRecord;
import org.tron.justlend.justlendapiserver.model.StrxAccountRecord;
import org.tron.justlend.justlendapiserver.model.Token;
import org.tron.justlend.justlendapiserver.service.ChaserProgressService;
import org.tron.justlend.justlendapiserver.service.StrxAccountRecordService;
import org.tron.justlend.justlendapiserver.service.TokenService;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronAddressUtils;
import org.tron.justlend.justlendapiserver.utils.blockchain.tron.TronJsonRpc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

import static org.tron.justlend.justlendapiserver.chainchaser.Event.STRX_USER;
import static org.tron.justlend.justlendapiserver.chainchaser.EventSignature.*;

@Slf4j
@Component
public class StrxStakeUserEventChaser extends TronEventChaser {
  private final Tokens tokens;
  private final TokenService tokenService;
  private final StrxAccountRecordService strxAccountRecordService;



  protected StrxStakeUserEventChaser(TronJsonRpc tronJsonRpc, ChaserProgressService chaserProgressService,
                                     Tokens tokens,
                                     TokenService tokenService,
                                     StrxAccountRecordService strxAccountRecordService
  ) {
    super(tronJsonRpc, chaserProgressService);
    this.tokens = tokens;
    this.tokenService = tokenService;
    this.strxAccountRecordService = strxAccountRecordService;
  }

  @PostConstruct
  void postConstruct() {
    topic = Lists.newArrayList(
      STRX_DEPOSIT.getSignatureHash(),
      STRX_WITHDRAW.getSignatureHash(),
      STRX_CLAIM.getSignatureHash(),
      TRANSFER.getSignatureHash()
    );
    contract = Lists.newArrayList(
      tokens.getTokenBySymbol("sTRX").getAddress()
    );
    event = STRX_USER;
    range = BigInteger.valueOf(10000);
    startHeight = BigInteger.valueOf(50200000);
  }

  @Override
  protected void processEvent(EventLog eventLog) {
    try {
      String signatureHash = eventLog.topics().getFirst();
      EventSignature eventSignature = EventSignature.fromSignature(signatureHash);
      switch (eventSignature) {
        case STRX_DEPOSIT, STRX_WITHDRAW:
          processDepositWithdraw(eventLog, eventSignature);
          break;
        case STRX_CLAIM:
          processClaim(eventLog);
          break;
        case TRANSFER:
          processTransfer(eventLog);
          break;
        default:
          // skip irrelevant events
          break;
      }
    } catch (Exception e) {
      logger.error(event, e);
      throw new ServiceException(event + " failed for " + eventLog.getUniqueId(), e);
    }
  }

  private void processDepositWithdraw(EventLog eventLog, EventSignature eventSignature) {
    String userAddress = TronAddressUtils.hexToBase58(eventLog.topics().get(1));
    Token sTrx = tokens.getTokenBySymbol("sTRX");
    if (STRX_DEPOSIT.equals(eventSignature)) {
      BigInteger rawAmount = new BigInteger(eventLog.getData(2), 16);
      BigDecimal amount = tokenService.getAmount(sTrx, rawAmount);
      BigDecimal usd = tokenService.getUsd(sTrx, amount);
      upsertStrxAccountRecord(eventLog, StrxStakeRecord.DEPOSIT.getType(), userAddress, amount, usd);
    } else {
      BigInteger rawAmount = new BigInteger(eventLog.getData(1), 16);
      BigDecimal amount = tokenService.getAmount(sTrx, rawAmount);
      BigDecimal usd = tokenService.getUsd(sTrx, amount);
      upsertStrxAccountRecord(eventLog, StrxStakeRecord.WITHDRAW.getType(), userAddress, amount, usd);
    }
  }

  private void processClaim(EventLog eventLog) {
    String userAddress = TronAddressUtils.hexToBase58(eventLog.topics().get(1));
    Token trx = tokens.getTokenBySymbol("TRX");
    BigInteger rawAmount = new BigInteger(eventLog.getData(0), 16);
    BigDecimal amount = tokenService.getAmount(trx, rawAmount);
    BigDecimal usd = tokenService.getUsd(trx, amount);
    upsertStrxAccountRecord(eventLog, StrxStakeRecord.CLAIM.getType(), userAddress, amount, usd);
  }

  private void processTransfer(EventLog eventLog) {
    String fromAddr = TronAddressUtils.hexToBase58(eventLog.topics().get(1));
    String toAddr = TronAddressUtils.hexToBase58(eventLog.topics().get(2));
    BigInteger rawAmount = new BigInteger(eventLog.getData(0), 16);

    String trxAddress = tokens.getTokenBySymbol("TRX").getAddress();
    if (contract.contains(fromAddr) || contract.contains(toAddr)
          || fromAddr.equals(trxAddress)
          || toAddr.equals(trxAddress)) {
      return;
    }

    Token sTrx = tokens.getTokenBySymbol("sTRX");
    BigDecimal amount = tokenService.getAmount(sTrx, rawAmount);
    BigDecimal usd = tokenService.getUsd(sTrx, amount);
    upsertStrxAccountRecord(eventLog, StrxStakeRecord.TRANSFER_OUT.getType(), fromAddr, amount, usd);
    upsertStrxAccountRecord(eventLog, StrxStakeRecord.TRANSFER_IN.getType(), toAddr, amount, usd);
  }

  private void upsertStrxAccountRecord(EventLog eventLog, Integer opType, String userAddress, BigDecimal amount, BigDecimal usd) {
    // skip irrelevant opType
    if (opType == null) {
      return;
    }

    String txId = eventLog.transactionId();
    int logIndex = eventLog.logIndex();
    Instant eventTime = eventLog.timestamp();

    StrxAccountRecord strxAccountRecord = StrxAccountRecord.builder()
                                      .userAddress(userAddress)
                                      .opType(opType)
                                      .amount(amount.toPlainString())
                                      .usd(usd.toPlainString())
                                      .txId(txId)
                                      .logIndex(logIndex)
                                      .eventTime(eventTime)
                                      .build();
    strxAccountRecordService.upsert(strxAccountRecord);
  }
}
