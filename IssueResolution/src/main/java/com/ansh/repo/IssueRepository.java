package com.ansh.repo;

import com.ansh.entity.Issue;
import com.ansh.entity.IssueFilter;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    Issue save(Issue issue);
    Optional<Issue> findById(String issueId);
    List<Issue> findAll();
    List<Issue> findByFilter(IssueFilter filter);
}
