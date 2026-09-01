package com.ansh.entity;

import com.ansh.enums.IssueStatus;
import com.ansh.enums.IssueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Issue {
    private String issueId;
    private String transactionId;
    private IssueType issueType;
    private String subject;
    private String description;
    private String customerEmail;
    private IssueStatus status;
    private String assignedAgentEmail;
    private String resolutionSummary;
}