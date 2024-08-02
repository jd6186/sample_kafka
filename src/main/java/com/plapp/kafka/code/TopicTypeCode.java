package com.plapp.kafka.code;

import lombok.Getter;

@Getter

public enum TopicTypeCode {
    AD_MANAGER("ad-manager"),
    AD_SCHEDULED_DATA_TRANSFER("ad-scheduled-data-transfer");

    private final String code;

    TopicTypeCode(String code) {
        this.code = code;
    }
}
