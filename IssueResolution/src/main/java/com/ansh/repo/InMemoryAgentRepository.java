package com.ansh.repo;

import com.ansh.entity.Agent;
import com.ansh.enums.IssueType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryAgentRepository implements AgentRepository {
    private final Map<String, Agent> storage = new ConcurrentHashMap<>();

    @Override
    public Agent save(Agent agent) {
        storage.put(agent.getEmail(), agent);
        return agent;
    }

    @Override
    public Optional<Agent> findByEmail(String email) {
        return Optional.ofNullable(storage.get(email));
    }

    @Override
    public List<Agent> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public List<Agent> findByExpertise(IssueType issueType) {
        return storage.values().stream()
                .filter(agent -> agent.getExpertise().contains(issueType))
                .collect(Collectors.toList());
    }
}