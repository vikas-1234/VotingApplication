package com.example.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserVoteCountDTO {
    private Long id;
    private String userName;
    private int voteCount;
    private String userRole;
    private String selectedCandidate;
}
