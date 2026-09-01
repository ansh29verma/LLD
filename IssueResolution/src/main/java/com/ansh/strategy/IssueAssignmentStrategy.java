package com.ansh.strategy;
import com.ansh.entity.Agent;
import com.ansh.entity.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueAssignmentStrategy {
    Optional<Agent> selectAgent(Issue issue, List<Agent> eligibleAgents);
}
