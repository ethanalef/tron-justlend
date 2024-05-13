package org.tron.justlend.justlendapiserver.chainchaser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static org.web3j.abi.EventEncoder.buildEventSignature;

@Getter
@RequiredArgsConstructor
public enum EventSignature {
  UNKNOWN("UNKNOWN"),
  TRANSFER("Transfer(address,address,uint256)"),
  DEPOSIT("Deposit(address,uint256)"),

  // JUSTLEND
  // JTOKEN
  JTOKEN_BALANCE("JTokenBalance(address,uint256)"),
  JTOKEN_MINT("Mint(address,uint256,uint256)"),
  JTOKEN_REDEEM("Redeem(address,uint256,uint256)"),
  JTOKEN_BORROW("Borrow(address,uint256,uint256,uint256,uint256)"),
  JTOKEN_REPAYBORROW("RepayBorrow(address,address,uint256,uint256,uint256,uint256)"),
  JTOKEN_LIQUI("LiquidateBorrow(address,address,uint256,address,uint256)"),
  TOKEN_APPROVE("Approval(address,address,uint256)"),
  JTOKEN_ENTER("MarketEntered(address,address)"),
  JTOKEN_EXIT("MarketExited(address,address)"),
  JTOKEN_NEWCF("NewCollateralFactor(address,uint256,uint256)"),
  JTOKEN_STATUS("JTokenStatus(uint256,uint256,uint256,uint256,uint256,uint256,uint256)"),

  // Governance
  JUSTLENDGOV_PROPOSALSNAPSHOT("ProposalSnapshot(uint256,uint256,uint256)"),
  JUSTLENDGOV_BRAVO_PROPOSALSNAPSHOT("ProposalSnapshotBravo(uint256,uint256,uint256,uint256)"),
  JUSTLENDGOV_USER_VOTE("VoteAndLock(address,uint256,bool,uint256)"),
  JUSTLENDGOV_USER_VOTECAST("VoteCast(address,uint256,bool,uint256)"),
  JUSTLENDGOV_BRAVO_USER_VOTE("VoteAndLock(address,uint256,uint8,uint256)"),
  JUSTLENDGOV_BRAVO_USER_VOTECAST("VoteCast(address,uint256,uint8,uint256,string)"),
  JUSTLENDGOV_USER_WITHDRAW("WithdrawVote(address,uint256,uint256)"),
  JUSTLENDGOV_PROPOSALCREATE("ProposalCreated(uint256,address,address[],uint256[],string[],bytes[],uint256,uint256,string)"),
  JUSTLENDGOV_CANCELED("ProposalCanceled(uint256)"),
  JUSTLENDGOV_QUEUED("ProposalQueued(uint256,uint256)"),
  JUSTLENDGOV_EXECUTED("ProposalExecuted(uint256)"),
  JUSTLENDGOV_WITHDRAW("Withdrawal(address,uint256)"),

  //JUSTLEND FEED
  JUSTLENDPRICE_POST("PricePosted(address,uint256,uint256,uint256)"),
  JUSTLENDCAPPEDPRICE_POST("CappedPricePosted(address,uint256,uint256,uint256)"),

  //SSP
  SSP_TOKEN_EXCHANGE("TokenExchange(address,int128,uint256,int128,uint256)"),
  SSPU_TOKEN_EXCHANGE_UNDER("TokenExchangeUnderlying(address,int128,uint256,int128,uint256)"),
  SSP_ADD_LIQUIDITY("AddLiquidity(address,uint256[3],uint256[3],uint256,uint256)"),
  SSPU_ADD_LIQUIDITY("AddLiquidity(address,uint256[2],uint256[2],uint256,uint256)"),
  SSP_REMOVE_LIQUIDITY("RemoveLiquidity(address,uint256[3],uint256[3],uint256)"),
  SSPU_REMOVE_LIQUIDITY("RemoveLiquidity(address,uint256[2],uint256[2],uint256)"),
  SSP_REMOVE_LIQUIDITY_ONE("RemoveLiquidityOne(address,uint256,uint256)"),
  SSP_REMOVE_LIQUIDITY_IMBALANCE("RemoveLiquidityImbalance(address,uint256[3],uint256[3],uint256,uint256)"),
  SSPU_REMOVE_LIQUIDITY_IMBALANCE("RemoveLiquidityImbalance(address,uint256[2],uint256[2],uint256,uint256)"),
  SSP_VOTELOG("VoteForGauge(uint256,address,address,uint256)"),

  //farm airdrop claimed
  AIRDROP_CLAIMED("Claimed(uint256,uint256,address,uint256)"),

  //VESUN
  SUNLOCK_DEPOSIT("Deposit(address,uint256,uint256,int128,uint256)"),
  SUNLOCK_WITHDRAW("Withdraw(address,uint256,uint256)"),
  GAUGE_SNAPSHOT("Check(address,uint256)"),
  DAOSTAKER_DEPOSIT("Deposit(address,uint256,address,uint256)"),
  DAOSTAKER_WITHDRAW("Withdraw(address,uint256,address,uint256)"),
  DAOSTAKER_CLAIM("Claimed(address,uint256,address,uint256)"),
  DAOSTAKER_GETFUND("GetFund(address,uint256,address,uint256)"),

  //STRX
  STRX_UPDATE_TOTAL_UNDERLYING("UpdateTotalUnderlying(uint256,uint256)"),
  STRX_ROUND_UNFREEZE("RoundUnfreeze(uint256,uint256,uint256)"),
  STRX_DEPOSIT("Deposit(address,uint256,uint256,uint256)"),
  STRX_WITHDRAW("Withdraw(address,uint256,uint256,uint256)"),
  STRX_ROUNDWITHDRAW("RoundWithdraw(address,uint256,uint256)"),
  STRX_UNFREEZE_BALANCE("UnfreezeBalance(address,uint256,uint256)"),
  STRX_ROUNDCLAIM("RoundClaim(address,uint256,uint256)"),
  STRX_CLAIM("Claim(address,uint256)"),
  RENT_INDEX("RentIndex(uint256,uint256,uint256,uint256,uint256,uint256)"),
  ORDER_INDEX("OrderIndex(address,address,uint256,uint256,uint256,uint256,uint256,uint256)"),
  RENT_RESOURCE("RentResource(address,address,uint256,uint256,uint256,uint256,uint256,uint256)"),
  RETURN_RESOURCE("ReturnResource(address,address,uint256,uint256,uint256,uint256,uint256,uint256,uint256)"),
  RENT_LIQUIDATE("Liquidate(address,address,address,uint256,uint256,uint256,uint256,uint256)"),
  STRX_RENT_FACTOR_UPDATED("RentFactorUpdated(uint256,uint256)"),
  STRX_REWARD_FACTOR_UPDATED("RewardFactorUpdated(uint256,uint256)"),
  STRX_NEW_INTEREST_PARAMS("NewInterestParams(uint256,uint256,uint256,uint256)"),
  STRX_MARKET_MINFEE_UPDATED("MinFeeUpdated(uint256,uint256)"),
  STRX_MARKET_FEE_RATIO_UPDATED("FeeRatioUpdated(uint256,uint256)"),
  STRX_MARKET_LIQTHRESHOLD_UPDATED("ThresholdUpdated(uint256,uint256)"),
  STRX_MAX_ENERGY_AMOUNT_UPDATED("MaxAmountForEnergyUpdated(uint256,uint256)"),

  /*
  stUSDT
   */
  STUSDT_MINT("Mint(address,address,uint256,uint256,uint256,uint256,uint256)"),
  STUSDT_SHARES_BURNT("SharesBurnt(address,address,uint256,uint256,uint256,uint256,uint256)"),
  STUSDT_TRANSFER_SHARES("TransferShares(address,address,uint256)"),
  STUSDT_INCREASE_BASE("IncreaseBase(uint256,uint256,uint256)"),
  STUSDT_DECREASE_BASE("DecreaseBase(uint256,uint256,uint256)"),
  STUSDT_WITHDRAWAL_REQUESTED("WithdrawalRequested(uint256,address,uint256,uint256,uint256)"),
  STUSDT_WITHDRAWALS_FINALIZED("WithdrawalsFinalized(uint256,uint256,uint256,uint256,uint256,uint256,uint256)"),
  STUSDT_WITHDRAWAL_CLAIMED("WithdrawalClaimed(uint256,address,uint256,uint256,uint256)"),
  STUSDT_BLACKLIST_ADDED("BlackListAdded(address,address[])"),
  STUSDT_BLACKLIST_REMOVED("BlackListRemoved(address[])"),
  VAULT_BURNED("Burned(address,uint256)"),
  ;

  private final String signature;

  public static EventSignature fromSignature(String signature) {
    for (EventSignature eventSignature : EventSignature.values()) {
      if (eventSignature.getSignatureHash().equals(signature)) {
        return eventSignature;
      }
    }
    return UNKNOWN;
  }

  public String getSignatureHash() {
    return buildEventSignature(signature).substring(2);   // trim 0x for TRON
  }
}
