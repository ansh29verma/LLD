package com.ansh.enums;



import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IssueType {
    PAYMENT_RELATED("Payment Related"),
    MUTUAL_FUND_RELATED("Mutual Fund Related"),
    GOLD_RELATED("Gold Related"),
    INSURANCE_RELATED("Insurance Related");

    private final String displayName;

    public static IssueType fromString(String text) {
        for (IssueType type : IssueType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown IssueType: " + text);
    }
}
