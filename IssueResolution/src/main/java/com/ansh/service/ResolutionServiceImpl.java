package com.ansh.service;



import com.ansh.entity.Agent;
import com.ansh.entity.Issue;
import com.ansh.entity.IssueFilter;
import com.ansh.enums.ErrorCode;
import com.ansh.enums.IssueStatus;
import com.ansh.enums.IssueType;
import com.ansh.exceptions.ResolutionSystemException;
import com.ansh.repo.AgentRepository;
import com.ansh.repo.IssueRepository;
import com.ansh.strategy.IssueAssignmentStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ResolutionServiceImpl implements ResolutionService {

    private final IssueRepository issueRepository;
    private final AgentRepository agentRepository;
    private final IssueAssignmentStrategy assignmentStrategy;
    private final AtomicInteger issueCounter = new AtomicInteger(1);

    public ResolutionServiceImpl(IssueRepository issueRepository,
                                 AgentRepository agentRepository,
                                 IssueAssignmentStrategy assignmentStrategy) {
        this.issueRepository = issueRepository;
        this.agentRepository = agentRepository;
        this.assignmentStrategy = assignmentStrategy;
    }

    @Override
    public String createIssue(String transactionId, String issueTypeStr, String subject, String description, String email) {
        if (transactionId == null || subject == null || email == null) {
            throw new ResolutionSystemException(ErrorCode.INVALID_INPUT, "Required fields missing.");
        }

        IssueType type = IssueType.fromString(issueTypeStr);
        String id = "I" + issueCounter.getAndIncrement();

        Issue newIssue = Issue.builder()
                .issueId(id)
                .transactionId(transactionId)
                .issueType(type)
                .subject(subject)
                .description(description)
                .customerEmail(email)
                .status(IssueStatus.OPEN)
                .build();

        issueRepository.save(newIssue);
        System.out.println(">>> Issue " + id + " created against transaction \"" + transactionId + "\"");
        return id;
    }

    @Override
    public void addAgent(String agentEmail, String agentName, List<String> issueTypeStrs) {
        if (agentRepository.findByEmail(agentEmail).isPresent()) {
            throw new ResolutionSystemException(ErrorCode.AGENT_ALREADY_EXISTS);
        }

        Set<IssueType> expertise = issueTypeStrs.stream()
                .map(IssueType::fromString)
                .collect(Collectors.toSet());

        Agent agent = Agent.builder()
                .email(agentEmail)
                .name(agentName)
                .expertise(expertise)
                .build();

        agentRepository.save(agent);
        System.out.println(">>> Agent " + agentName + " created");
    }

    @Override
    public void assignIssue(String issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResolutionSystemException(ErrorCode.ISSUE_NOT_FOUND));

        List<Agent> eligibleAgents = agentRepository.findByExpertise(issue.getIssueType());
        if (eligibleAgents.isEmpty()) {
            throw new ResolutionSystemException(ErrorCode.NO_ELIGIBLE_AGENT);
        }

        Optional<Agent> selectedAgentOpt = assignmentStrategy.selectAgent(issue, eligibleAgents);
        if (selectedAgentOpt.isEmpty()) {
            throw new ResolutionSystemException(ErrorCode.NO_ELIGIBLE_AGENT);
        }

        Agent agent = selectedAgentOpt.get();

        if (agent.isFree()) {
            agent.setCurrentAssignedIssueId(issue.getIssueId());
            if (!agent.getWorkedHistoryIssueIds().contains(issue.getIssueId())) {
                agent.getWorkedHistoryIssueIds().add(issue.getIssueId());
            }

            issue.setAssignedAgentEmail(agent.getEmail());
            issue.setStatus(IssueStatus.IN_PROGRESS);

            agentRepository.save(agent);
            issueRepository.save(issue);

            System.out.println(">>> Issue " + issueId + " assigned to agent " + agent.getName());
        } else {
            agent.getWaitlistedIssueIds().offer(issue.getIssueId());
            if (!agent.getWorkedHistoryIssueIds().contains(issue.getIssueId())) {
                agent.getWorkedHistoryIssueIds().add(issue.getIssueId());
            }

            issue.setAssignedAgentEmail(agent.getEmail());
            issue.setStatus(IssueStatus.WAITLISTED);

            agentRepository.save(agent);
            issueRepository.save(issue);

            System.out.println(">>> Issue " + issueId + " added to waitlist of Agent " + agent.getName());
        }
    }

    @Override
    public List<Issue> getIssues(IssueFilter filter) {
        return issueRepository.findByFilter(filter);
    }

    @Override
    public void updateIssue(String issueId, String statusStr, String resolution) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResolutionSystemException(ErrorCode.ISSUE_NOT_FOUND));

        IssueStatus newStatus = IssueStatus.valueOf(statusStr.replace(" ", "_").toUpperCase());
        issue.setStatus(newStatus);
        if (resolution != null && !resolution.isBlank()) {
            issue.setResolutionSummary(resolution);
        }

        issueRepository.save(issue);
        System.out.println(">>> " + issueId + " status updated to " + statusStr);
    }

    @Override
    public void resolveIssue(String issueId, String resolution) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResolutionSystemException(ErrorCode.ISSUE_NOT_FOUND));

        issue.setStatus(IssueStatus.RESOLVED);
        issue.setResolutionSummary(resolution);
        issueRepository.save(issue);

        System.out.println(">>> " + issueId + " issue marked resolved");

        // Free up agent and assign next waitlisted issue
        if (issue.getAssignedAgentEmail() != null) {
            Agent agent = agentRepository.findByEmail(issue.getAssignedAgentEmail()).orElse(null);
            if (agent != null && issueId.equals(agent.getCurrentAssignedIssueId())) {
                agent.setCurrentAssignedIssueId(null);

                String nextWaitlistedIssueId = agent.getWaitlistedIssueIds().poll();
                if (nextWaitlistedIssueId != null) {
                    agent.setCurrentAssignedIssueId(nextWaitlistedIssueId);
                    Issue nextIssue = issueRepository.findById(nextWaitlistedIssueId).orElse(null);
                    if (nextIssue != null) {
                        nextIssue.setStatus(IssueStatus.IN_PROGRESS);
                        issueRepository.save(nextIssue);
                    }
                }
                agentRepository.save(agent);
            }
        }
    }

    @Override
    public Map<String, List<String>> viewAgentsWorkHistory() {
        Map<String, List<String>> historyMap = new HashMap<>();
        List<Agent> agents = agentRepository.findAll();

        for (Agent agent : agents) {
            historyMap.put(agent.getName(), List.copyOf(agent.getWorkedHistoryIssueIds()));
        }

        return historyMap;
    }
}
