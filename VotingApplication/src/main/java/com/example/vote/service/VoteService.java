package com.example.vote.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.vote.domain.Role;
import com.example.vote.domain.User;
import com.example.vote.domain.UserDTO;
import com.example.vote.dto.UserVoteCountDTO;
import com.example.vote.repository.RoleRepository;
import com.example.vote.repository.UserRepository;

@Service
public class VoteService {

	private UserRepository userRepo;

	private RoleRepository roleRepository;

	public VoteService(UserRepository userRepo, RoleRepository roleRepository) {
		this.userRepo = userRepo;
		this.roleRepository = roleRepository;
	}

	public User findFirstByEmailId(String emailId) {
		return userRepo.findFirstByEmailId(emailId);
	}

	public User save(User user) {
		return userRepo.save(user);
	}

	public Set<Role> getUserRoles(Long userId) {
		// Fetch user from the database
		User user = userRepo.findById(userId).orElse(null);

		// Check if the user exists
		if (user != null) {
			// Retrieve the roles assigned to the user
			return user.getRoles();
		} else {
			// Handle case where user is not found
			return Collections.emptySet();
		}

	}

	public User getUsersVoted(String emailId) {
		System.out.println("VoteService::hasVoted::emailId " + emailId);

		// Check if the user has already voted
		User userVotedData = userRepo.findFirstByEmailId(emailId);
		System.out.println("VoteService::hasVoted::userVotedData " + userVotedData);
		return userVotedData;
	}

	public void recordVote(String emailId, String selectedCandidate) {
		System.out.println("VoteService::recordVote::emailId " + emailId + " selectedCandidate  " + selectedCandidate);
		// Record the user's vote
		User user = userRepo.findByEmailId(emailId);
		System.out.println("VoteService::recordVote::user " + user);
		if (user == null) {
			throw new RuntimeException("User not found with email: " + emailId);
		}
//		user.setSelectedCandidate(selectedCandidate);
		user.setSelectedCandidate(selectedCandidate);
		user.setVoted(true);
		userRepo.save(user);
	}

	@Transactional
	public void incrementVoteCountByEmailId(String emailId) {
		System.out.println("VoteService::incrementVoteCountByEmailId::emailId " + emailId);
		userRepo.incrementVoteCountByEmailId(emailId);
	}

	public List<UserVoteCountDTO> getAllUsersWithVoteCount() {
		List<UserVoteCountDTO> countVotedData = userRepo.findAllWithVoteCount();

		System.out.println("VoteService::getVoteCount::countVotedData " + countVotedData);
		return countVotedData;
	}

	@Transactional
	public void registerUser(UserDTO userDto) {
		System.out.println("VoteService::registerUser::UserDto " + userDto);
		// Create a new User entity
		User user = User.builder().userName(userDto.getUserName()).password(userDto.getPassword())
				.emailId(userDto.getEmailId()).phoneNumber(userDto.getPhoneNumber()).enabled(true) // Assuming all
																									// registered users
																									// are enabled by
																									// default
				.build();
		System.out.println("VoteService::registerUser::user " + user);
		// Save the user to the database
		User savedUser = userRepo.save(user);
		System.out.println("VoteService::registerUser::savedUser " + savedUser);
		// Create a new Role entity for the user
		Role userRole = new Role();
		userRole.setUserName(user.getUserName());
		userRole.setUserRole("USER");

		// Save the role to the database
		Role savedRole = roleRepository.save(userRole);
		System.out.println("VoteService::registerUser::savedRole " + savedRole);

//		// Associate the user with the role and save the association in the user_roles table
//	    savedUser.getRoles().add(savedRole);
//	    savedRole.getUsers().add(savedUser);
//	    
//	    Set<Role> savedGetRole = savedUser.getRoles();
//	    System.out.println("VoteService::registerUser::savedGetRole " + savedGetRole);
//	    boolean saveRoleInUserRole = savedGetRole.add(savedRole);
//	    System.out.println("VoteService::registerUser::saveRoleInUserRole " + saveRoleInUserRole);
//	    
//	    
//	     savedGetRole = savedRole.getUsers();
//
//	    // Update the changes to the database
//	    userRepo.save(savedUser);
//	    roleRepository.save(savedRole);

	}

//	public boolean areVotingWindowsDisabled() {
//		// Check if voting windows are disabled for all users
//		boolean votingWindowData = userRepo.findAll().stream().allMatch(user -> !user.isEnabled());
//		System.out.println("VoteService::areVotingWindowsDisabled::votingWindowData " + votingWindowData);
//		return votingWindowData;
//	}

//	public boolean isVotingEnabled() {
//		// Check if voting windows are disabled for all users
//		boolean enabledVotingWindowData = userRepo.findAll().stream().allMatch(user -> !user.isEnabled());
//		System.out.println("VoteService::areVotingWindowsDisabled::enabledVotingWindowData " + enabledVotingWindowData);
//		return enabledVotingWindowData;
//	}

//	public boolean isVotingEnabled() {
//        List<User> users = userRepo.findAll();
//        return users.stream().anyMatch(User::isEnabled);
//    }

	public boolean isVotingEnabled() {
		// Check if voting is enabled for all users
		boolean enabledVotingWindowData = userRepo.findAll().stream().allMatch(User::isEnabled);
		System.out.println("VoteService::isVotingEnabled::enabledVotingWindowData " + enabledVotingWindowData);
		return enabledVotingWindowData;
	}

	public boolean areVotingWindowsDisabled() {
		// Check if voting windows are disabled for all users
		boolean votingWindowData = userRepo.findAll().stream().allMatch(User::isEnabled);
		System.out.println("VoteService::areVotingWindowsDisabled::votingWindowData " + votingWindowData);
		return !votingWindowData;
	}

	public void disableVotingForAllUsers() {
		try {
			// Update enabled flag for all users
			List<User> users = userRepo.findAll();
			System.out.println("VoteService::disableVotingForAllUsers::users " + users);
			for (User user : users) {
				// setter method for 'enabled' flag
				user.setEnabled(false);
			}
			List<User> listOfUser = userRepo.saveAll(users);
			System.out.println("VoteService::disableVotingForAllUsers::listOfUser " + listOfUser);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception Caught!!! ");
		}
	}

	// Service method to enable voting for all users
	public void enableVotingForAllUsers() {
		try {
			List<User> users = userRepo.findAll();
			System.out.println("VoteService::enableVotingForAllUsers::users " + users);
			for (User user : users) {
				// Set enabled to true for all users
				user.setEnabled(true);
			}
			List<User> listOfEnabledUser = userRepo.saveAll(users);
			System.out.println("VoteService::enableVotingForAllUsers::listOfEnabledUser " + listOfEnabledUser);
//			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception Caught!!!");
//			return false;
		}

	}

//	   public UserDTO determineWinner() {
//	        try {
//	            // Retrieve all users from the database
//	            List<User> users = userRepo.findAll();
//
//	            // Find the user with the maximum vote count
//	            Optional<User> winner = users.stream()
//	                    .max(Comparator.comparingInt(User::getVoteCount));
//
//	            // Check if a winner is present
//	            if (winner.isPresent()) {
//	                // Convert the winner User entity to a UserDTO and return
//	                User user = winner.get();
//	                return new UserDTO(
//	                        "Winner",
//	                        user.getUserName(),
//	                        user.getVoteCount(),
//	                        // Assuming you have a method to get user role
//	                        user.getRoles().stream().findFirst().map(Role::getUserRole).orElse(""),
//	                        user.getSelectedCandidate()
//	                );
//	            } else {
//	                // No winner found, return null
//	                return null;
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            // Handle exception
//	            return null;
//	        }
//	    }

}
