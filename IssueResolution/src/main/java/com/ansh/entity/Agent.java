package com.ansh.entity;

import com.ansh.enums.IssueType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agent {
    private String email;
    private String name;
    private Set<IssueType> expertise;

    @Builder.Default
    private String currentAssignedIssueId = null;

    @Builder.Default
    private Queue<String> waitlistedIssueIds = new LinkedList<>();

    @Builder.Default
    private List<String> workedHistoryIssueIds = new ArrayList<>();

    public boolean isFree() {
        return currentAssignedIssueId == null;
    }
}
