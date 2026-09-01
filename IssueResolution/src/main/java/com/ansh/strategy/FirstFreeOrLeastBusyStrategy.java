package com.ansh.strategy;

import com.ansh.entity.Agent;
import com.ansh.entity.Issue;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FirstFreeOrLeastBusyStrategy implements IssueAssignmentStrategy {

    @Override
    public Optional<Agent> selectAgent(Issue issue, List<Agent> eligibleAgents) {
        if (eligibleAgents == null || eligibleAgents.isEmpty()) {
            return Optional.empty();
        }

        // 1. Try to find an immediate free agent
        Optional<Agent> freeAgent = eligibleAgents.stream()
                .filter(Agent::isFree)
                .findFirst();

        if (freeAgent.isPresent()) {
            return freeAgent;
        }

        // 2. If all are busy, assign/waitlist to the agent with smallest waitlist size
        return eligibleAgents.stream()
                .min(Comparator.comparingInt(agent -> agent.getWaitlistedIssueIds().size()));
    }
}