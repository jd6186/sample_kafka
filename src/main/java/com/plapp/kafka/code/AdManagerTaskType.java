package com.plapp.kafka.code;

import lombok.Getter;

@Getter

public enum AdManagerTaskType {
    AD_VIEW("ad-view"),
    AD_CLICK("ad-click"),
    CALL_SIMILAR_ADS_LIST("call-similar-ads-list"),
    RECOMMENDED_AD("recommended-ad");

    private final String code;

    AdManagerTaskType(String code) {
        this.code = code;
    }

    public static AdManagerTaskType fromCode(String code) {
        for (AdManagerTaskType taskType : values()) {
            if (taskType.code.equals(code)) {
                return taskType;
            }
        }
        throw new IllegalArgumentException("Unknown task type code: " + code);
    }
}
