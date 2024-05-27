package com.example.vote.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.vote.domain.User;
import com.example.vote.domain.VotingStatus;
import com.example.vote.dto.UserVoteCountDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//	@Query("SELECT u FROM User u WHERE u.emailId = :emailId")
//	public User findFirstByEmailId(@Param("emailId") String emailId);

//	Optional<User> findFirstByEmailId(String emailId);
	
	User findFirstByEmailId(String emailId);

	@Transactional
	@Modifying
	@Query("update User u set u.voteCount = u.voteCount + 1 where u.emailId = :emailId")
	void incrementVoteCountByEmailId(@Param("emailId") String emailId);

	@Query("SELECT new com.example.vote.dto.UserVoteCountDTO(u.id, u.userName, u.voteCount, r.userRole, u.selectedCandidate) FROM User u JOIN u.roles r")
	List<UserVoteCountDTO> findAllWithVoteCount();

	boolean existsByEmailIdAndVoted(String emailId, boolean voted);

	User findFirstById(Long id);

	Optional<User> findById(String emailId);

	@Query("SELECT u FROM User u WHERE u.emailId = :emailId")
	public User getUserByUsername(@Param("emailId") String emailId);

	User findByUserName(String username);

	User findByEmailId(String emailId);

	int countByVoted(boolean voted);

//	@Query("SELECT new com.example.vote.dto.VotingStatus(v.enabled, v.votingWindowsDisabled) FROM VotingStatus v")
//	VotingStatus getVotingStatus();
//
//	@Modifying
//	@Query("UPDATE VotingStatus v SET v.enabled = :enabled, v.votingWindowsDisabled = :votingWindowsDisabled")
//	void updateVotingStatus(@Param("enabled") boolean enabled, @Param("votingWindowsDisabled") boolean votingWindowsDisabled);

}
