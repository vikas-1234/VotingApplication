package com.example.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.vote.domain.Role;
import com.example.vote.domain.User;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	User findByUserName(String userName);

	User save(User user1);

	Role findByUserRole(String userRole);
}
