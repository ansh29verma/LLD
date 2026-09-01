package com.ansh.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ISSUE_NOT_FOUND("ERR_001", "Issue not found"),
    AGENT_NOT_FOUND("ERR_002", "Agent not found"),
    AGENT_ALREADY_EXISTS("ERR_003", "Agent email already exists"),
    NO_ELIGIBLE_AGENT("ERR_004", "No active agent with expertise found for this issue type"),
    INVALID_INPUT("ERR_005", "Provided input data is invalid"),
    INVALID_STATUS_TRANSITION("ERR_006", "Invalid issue status transition");

    private final String code;
    private final String description;
}
