package com.ansh.entity;

import com.ansh.enums.IssueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueFilter {
    private String issueId;
    private String customerEmail;
    private IssueType issueType;
}
