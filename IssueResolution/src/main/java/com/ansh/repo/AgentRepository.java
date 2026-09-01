package com.ansh.repo;

import com.ansh.entity.Agent;
import com.ansh.enums.IssueType;

import java.util.List;
import java.util.Optional;

public interface AgentRepository {
    Agent save(Agent agent);
    Optional<Agent> findByEmail(String email);
    List<Agent> findAll();
    List<Agent> findByExpertise(IssueType issueType);
}