package com.example.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotingStatus {
	private boolean enabled;
    private boolean votingWindowsDisabled;
}
