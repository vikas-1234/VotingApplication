package com.example.vote.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.vote.domain.Role;
import com.example.vote.domain.User;
import com.example.vote.domain.UserDTO;
import com.example.vote.dto.UserVoteCountDTO;
import com.example.vote.repository.UserRepository;
import com.example.vote.service.VoteCountService;
import com.example.vote.service.VoteService;

@RestController
@RequestMapping("/votingApp")
public class LoginController {

	private VoteService userService;

	private UserRepository userRepository;

	private VoteCountService voteCountService;

	public LoginController(VoteService userService, VoteCountService voteCountService, UserRepository userRepository) {
		this.userService = userService;
		this.voteCountService = voteCountService;
		this.userRepository = userRepository;

	}

	@GetMapping("/checkVotingStatus")
	public ResponseEntity<Boolean> checkVotingStatus() {
		try {
			// Call the service method to check voting status
			boolean votingEnabled = userService.isVotingEnabled();
			boolean votingWindowsDisabled = userService.areVotingWindowsDisabled();
			System.out.println("LoginController::checkVotingStatus::votingEnabled " + votingEnabled
					+ " votingWindowsDisabled " + votingWindowsDisabled);
			// Combine the voting status and voting windows disabled status into one boolean

			boolean combinedStatus = votingEnabled && !votingWindowsDisabled;
			return ResponseEntity.ok(combinedStatus);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}
//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/enableVoting")
	public ResponseEntity<String> enableVoting() {
		try {
			userService.enableVotingForAllUsers();
			return ResponseEntity.ok("Voting windows have been enabled for all users");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to enable voting");
		}
	}
//	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/disableVoting")
	public ResponseEntity<String> disableVoting() {
		try {
			userService.disableVotingForAllUsers();
			return ResponseEntity.ok("Voting windows have been disabled for all users");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to disable voting");
		}
	}

//	@GetMapping("/login")
//	public String redirectToLogin() {
//	     return "redirect:/login";
//	}
//	@PreAuthorize("hasRole('ADMIN')")
//	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpSession session) {
		System.out.println("LoginController::login ::user " + user.toString());
		String email = user.getEmailId();
		String password = user.getPassword();
		System.out.println("LoginController::login ::email " + email + " password " + password);
		User dbUser = userService.findFirstByEmailId(email);
		System.out.println("LoginController::login ::dbUser " + dbUser);
		if (dbUser != null && dbUser.getPassword().equals(password)) {
			System.out.println(" Login successful");

			// Store email ID and password in session
			session.setAttribute("email", email);
			session.setAttribute("password", password);

			// Get user roles
			Set<String> roles = dbUser.getRoles().stream().map(Role::getUserRole).collect(Collectors.toSet());
			System.out.println("LoginController::login::roles " + roles);
			// Prepare response with user roles
			Map<String, Object> responseData = new HashMap<>();
			responseData.put("message", "Login successful");
//	        responseData.put("roles", String.join(",", roles));
			responseData.put("roles", roles);
			System.out.println("LoginController::login::responseData " + responseData);
			return ResponseEntity.ok(responseData);
		} else {
			System.out.println(" Invalid email or password");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("error", "Invalid email or password"));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserDTO userDto) {
		try {
			userService.registerUser(userDto);
			System.out.println("User registered successfully");
			return ResponseEntity.ok("User registered successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
		}
	}

	@PostMapping("/vote")
	public ResponseEntity<String> vote(@RequestBody Map<String, String> voteData) {
		try {
			String emailId = voteData.get("emailId");
			String password = voteData.get("password");
			String selectedCandidate = voteData.get("selectedCandidate");
			System.out.println("LoginController::vote::emailId " + emailId + " password " + password
					+ " selectedCandidate  " + selectedCandidate);

			// Check if the user has already voted
			User votedUser = userService.getUsersVoted(emailId);
			System.out.println("LoginController::vote::votedUser " + votedUser.toString());
			if (votedUser != null && votedUser.isVoted()) {
				return new ResponseEntity<>("You already voted", HttpStatus.BAD_REQUEST);
			}

			// Record the user's vote
			User user = userRepository.findFirstByEmailId(emailId);
			System.out.println("LoginController::vote::Record the user's vote::user " + user.toString());
			if (user == null) {
				throw new RuntimeException("User not found with email: " + emailId);
			}
			user.setSelectedCandidate(selectedCandidate);
			user.setVoted(true);
			userRepository.save(votedUser);

			// Increment vote count
			userService.incrementVoteCountByEmailId(emailId);

			return new ResponseEntity<>("Vote submitted successfully", HttpStatus.OK);
		} catch (Exception e) {
			// Log the exception for debugging purposes
			e.printStackTrace();
			return new ResponseEntity<>("Failed to submit vote", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/voteCount")
	public ResponseEntity<?> getVoteCount() {
		try {
			// Call the service method to get the list of users with their information
			List<UserVoteCountDTO> users = userService.getAllUsersWithVoteCount();
			System.out.println("LoginController::getVoteCount::users " + users);
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get vote count");
		}
	}

	@GetMapping("/logout")
	public ResponseEntity<String> logout() {
		return ResponseEntity.ok("Logout successful");
	}

	@GetMapping("/user/{userId}/roles")
	public ResponseEntity<?> getUserRoles(@PathVariable("userId") Long userId) {
		Set<Role> roles = userService.getUserRoles(userId);
		if (!roles.isEmpty()) {
			return ResponseEntity.ok(roles);
		} else {
			return ResponseEntity.notFound().build();
		}

	}
}
