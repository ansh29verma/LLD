package com.ansh.service;
import com.ansh.entity.Issue;
import com.ansh.entity.IssueFilter;

import java.util.List;
import java.util.Map;

public interface ResolutionService {
    String createIssue(String transactionId, String issueTypeStr, String subject, String description, String email);
    void addAgent(String agentEmail, String agentName, List<String> issueTypeStrs);
    void assignIssue(String issueId);
    List<Issue> getIssues(IssueFilter filter);
    void updateIssue(String issueId, String statusStr, String resolution);
    void resolveIssue(String issueId, String resolution);
    Map<String, List<String>> viewAgentsWorkHistory();
}
