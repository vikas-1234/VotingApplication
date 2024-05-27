package com.example.vote.domain;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "username")
	private String userName;

	@Column(name = "password")
	private String password;

	@NotEmpty(message = "Email cannot be empty")
	@Email(message = "Invalid email format")
	@Column(name = "email_id")
	private String emailId;

	@NotEmpty(message = "Phone number cannot be empty")
	@NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits long")
	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "voted")
	private boolean voted;

	private boolean enabled;

	@Column(name = "vote_count")
	private int voteCount;

	@Column(name = "selected_candidate")
	private String selectedCandidate;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	

	@Override
	public int hashCode() {
		return Objects.hash(id, userName, password, emailId, phoneNumber, voted, enabled);
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", userName='" + userName + '\'' + ", password='" + password + '\''
				+ ", emailId='" + emailId + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", voted=" + voted
				+ ", enabled=" + enabled + ", selectedCandidate='" + selectedCandidate + '\'' + ", roles=" + roles
				+ '}';
	}
}
