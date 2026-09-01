package com.ansh.repo;
import com.ansh.entity.Issue;
import com.ansh.entity.IssueFilter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryIssueRepository implements IssueRepository {
    private final Map<String, Issue> storage = new ConcurrentHashMap<>();

    @Override
    public Issue save(Issue issue) {
        storage.put(issue.getIssueId(), issue);
        return issue;
    }

    @Override
    public Optional<Issue> findById(String issueId) {
        return Optional.ofNullable(storage.get(issueId));
    }

    @Override
    public List<Issue> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public List<Issue> findByFilter(IssueFilter filter) {
        return storage.values().stream()
                .filter(issue -> filter.getIssueId() == null || filter.getIssueId().equalsIgnoreCase(issue.getIssueId()))
                .filter(issue -> filter.getCustomerEmail() == null || filter.getCustomerEmail().equalsIgnoreCase(issue.getCustomerEmail()))
                .filter(issue -> filter.getIssueType() == null || filter.getIssueType() == issue.getIssueType())
                .collect(Collectors.toList());
    }
}
