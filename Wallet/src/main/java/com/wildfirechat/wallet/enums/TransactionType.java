package com.wildfirechat.wallet.enums;

public enum TransactionType {
	 
	TOPUP("TOPUP"), CASHOUT("CASHOUT");


    private final String txnType;

    TransactionType(String txnType) {
        this.txnType = txnType;
    }

    @Override
    public String toString() {
        return this.txnType;
    }
}
