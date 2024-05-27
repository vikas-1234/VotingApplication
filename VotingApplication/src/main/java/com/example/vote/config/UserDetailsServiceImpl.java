package com.example.vote.config;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import com.example.vote.domain.Role;
import com.example.vote.repository.UserRepository;
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	//Implement a custom UserDetailsService to load user details including roles from the database.

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		com.example.vote.domain.User user = userRepository.getUserByUsername(userName);
		System.out.println("UserDetailsServiceImpl::loadUserByUsername::user " + user);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		MyUserDetails customUserDetails=new MyUserDetails(user);
		return customUserDetails;
//		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
//				getAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getUserRole())).collect(Collectors.toList());
	}

}