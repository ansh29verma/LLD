package com.ansh;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import com.ansh.entity.Issue;
import com.ansh.entity.IssueFilter;
import com.ansh.enums.IssueType;
import com.ansh.repo.AgentRepository;
import com.ansh.repo.InMemoryAgentRepository;
import com.ansh.repo.InMemoryIssueRepository;
import com.ansh.repo.IssueRepository;
import com.ansh.service.ResolutionService;
import com.ansh.service.ResolutionServiceImpl;
import com.ansh.strategy.FirstFreeOrLeastBusyStrategy;
import com.ansh.strategy.IssueAssignmentStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Initialize Core Components
        IssueRepository issueRepo = new InMemoryIssueRepository();
        AgentRepository agentRepo = new InMemoryAgentRepository();
        IssueAssignmentStrategy strategy = new FirstFreeOrLeastBusyStrategy();

        ResolutionService service = new ResolutionServiceImpl(issueRepo, agentRepo, strategy);

        // 1. Create Issues
        String i1 = service.createIssue("T1", "Payment Related", "Payment Failed", "My payment failed but money is debited", "testUser1@test.com");
        String i2 = service.createIssue("T2", "Mutual Fund Related", "Purchase Failed", "Unable to purchase Mutual Fund", "testUser2@test.com");
        String i3 = service.createIssue("T3", "Payment Related", "Payment Failed", "My payment failed but money is debited", "testUser2@test.com");

        // 2. Onboard Agents
        service.addAgent("agent1@test.com", "Agent 1", Arrays.asList("Payment Related", "Gold Related"));
        service.addAgent("agent2@test.com", "Agent 2", Arrays.asList("Payment Related"));
        service.addAgent("agent3@test.com", "Agent 3", Arrays.asList("Mutual Fund Related"));

        // 3. Assign Issues
        service.assignIssue("I1");
        service.assignIssue("I2"); // Note: Agent 2 doesn't support MF, so I2 will route to agent supporting MF if onboarded, or handle via strategy
        service.assignIssue("I3");

        // 4. Query Issues with Filters
        System.out.println("\n--- Query Issues by Customer Email ---");
        List<Issue> user2Issues = service.getIssues(IssueFilter.builder().customerEmail("testUser2@test.com").build());
        user2Issues.forEach(issue -> System.out.println(issue.getIssueId() + " -> " + issue.getSubject() + " [" + issue.getStatus() + "]"));

        System.out.println("\n--- Query Issues by Type ---");
        List<Issue> paymentIssues = service.getIssues(IssueFilter.builder().issueType(IssueType.PAYMENT_RELATED).build());
        paymentIssues.forEach(issue -> System.out.println(issue.getIssueId() + " -> " + issue.getSubject() + " [" + issue.getStatus() + "]"));

        // 5. Update & Resolve Issues
        System.out.println("\n--- Lifecycle Updates ---");
        service.updateIssue("I3", "In Progress", "Waiting for payment confirmation");
        service.resolveIssue("I3", "PaymentFailed debited amount will get reversed");

        // 6. View Work History
        System.out.println("\n--- Agents Work History ---");
        Map<String, List<String>> history = service.viewAgentsWorkHistory();
        history.forEach((agent, workedIssues) -> System.out.println(agent + " -> " + workedIssues));
    }
}