package com.example.vote.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.vote.domain.Role;
import com.example.vote.repository.RoleRepository;
@Service
public class VoteCountService {
	
	private RoleRepository roleRepository;


	public VoteCountService(RoleRepository roleRepository) {
		super();
		this.roleRepository = roleRepository;
	}
	
	
	public List<Role> getVoteCounts() {
		List<Role> roleData= roleRepository.findAll();
		System.out.println("voteCountService::getVoteCounts::roleData " + roleData);
		return roleData;
	}

}
