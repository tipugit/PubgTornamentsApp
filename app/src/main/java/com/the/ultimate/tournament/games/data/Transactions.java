package com.the.ultimate.tournament.games.data;


public class Transactions {

    private String txnAmount;
    private String txnDate;
    private String txnRemark;
    private String txnType;

    public Transactions() {
    }

    public Transactions(String txnAmount, String txnDate, String txnRemark, String txnType) {
        this.txnAmount = txnAmount;
        this.txnDate = txnDate;
        this.txnRemark = txnRemark;
        this.txnType = txnType;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnRemark() {
        return txnRemark;
    }

    public void setTxnRemark(String txnRemark) {
        this.txnRemark = txnRemark;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

}
