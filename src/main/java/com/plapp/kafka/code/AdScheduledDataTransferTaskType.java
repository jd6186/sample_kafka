package com.plapp.kafka.code;

import lombok.Getter;

@Getter
public enum AdScheduledDataTransferTaskType {
    AD_SCHEDULED("ad-scheduled"),
    AD_DATA_TRANSFER("ad-data-transfer");

    private final String code;

    AdScheduledDataTransferTaskType(String code) {
        this.code = code;
    }

    public static AdScheduledDataTransferTaskType fromCode(String code) {
        for (AdScheduledDataTransferTaskType taskType : values()) {
            if (taskType.code.equals(code)) {
                return taskType;
            }
        }
        throw new IllegalArgumentException("Unknown task type code: " + code);
    }
}