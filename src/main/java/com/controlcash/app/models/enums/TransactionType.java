package com.controlcash.app.models.enums;

public enum TransactionType {
    PAYMENT(1),
    ENTRANCE(2);

    private final int code;

    TransactionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static TransactionType valueOf(int code) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.getCode() == code) {
                return transactionType;
            }
        }

        throw new IllegalArgumentException("Invalid TransactionType code. Used: " + code);
    }
}
