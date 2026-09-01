package com.ansh.dto;


import com.ansh.enums.Difficulty;
import com.ansh.enums.ProblemSortField;
import com.ansh.enums.SortOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProblemFilterCriteria {
    private Difficulty difficulty;
    private String tag;
    private ProblemSortField sortBy;
    @Builder.Default
    private SortOrder sortOrder = SortOrder.DESC;
}
