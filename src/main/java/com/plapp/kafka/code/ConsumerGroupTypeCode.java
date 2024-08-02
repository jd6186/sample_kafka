package com.plapp.kafka.code;

import lombok.Getter;

@Getter

public enum ConsumerGroupTypeCode {
    AD_MANAGER_GROUP("ad-manager-group"),
    AD_TRANSFER_GROUP("ad-transfer-group");

    private final String code;

    ConsumerGroupTypeCode(String code) {
        this.code = code;
    }
}
