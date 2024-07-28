package com.Optimart.enums;



public enum PaymentType {
    CREATE("SETTING.PAYMENT_TYPE.CREATE"),
    UPDATE("SETTING.PAYMENT_TYPE.UPDATE"),
    DELETE("SETTING.PAYMENT_TYPE.DELETE");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
