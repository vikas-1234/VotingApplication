//package com.example.vote.config;
//
//import java.util.HashSet;
//import java.util.Set;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import com.example.vote.domain.User;
//import com.example.vote.repository.UserRepository;
//
//
//public class CustomUserDetailsServiceImpl implements UserDetailsService {
//
//	@Autowired
//	private  UserRepository userRepository;
//
////	public CustomUserDetailsService(UserRepository userRepository) {
////		this.userRepository = userRepository;
////	}
//
//	@Override
//	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
////        User user = userRepository.findFirstByEmailId(emailId);
//		User user = userRepository.findFirstByEmailId(emailId);
//		System.out.println("CustomUserDetailsService::loadUserByUsername::user " + user);
//		if (user == null) {
//			throw new UsernameNotFoundException("User not found with email: " + emailId);
//		}
//
//		Set<GrantedAuthority> authorities = new HashSet<>();
//		user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getUserName())));
////
//		return org.springframework.security.core.userdetails.User.withUsername(emailId).password(user.getPassword())
//				.authorities(authorities).accountExpired(false).accountLocked(false).credentialsExpired(false)
//				.disabled(false).build();
//
////		return new MyUserDetails(user);
//	}
//	
//	
////	@Override
////    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
////        User user = userRepository.findFirstByEmailId(emailId);
////        System.out.println("CustomUserDetailsService::loadUserByUsername::user " + user);
////        if (user == null) {
////            throw new UsernameNotFoundException("User not found with email: " + emailId);
////        }
////
////        System.out.println("CustomUserDetailsService::loadUserByUsername::user " + user);
////
////        Set<Role> roles = user.getRoles();
////        System.out.println("CustomUserDetailsService::loadUserByUsername::roles " + roles);
////
////        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
////        for (Role role : roles) {
////            authorities.add(new SimpleGrantedAuthority(role.getUserName()));
////        }
////
////        System.out.println("CustomUserDetailsService::loadUserByUsername::authorities " + authorities);
////
////        return new MyUserDetails(user);
////    }
//	
//	
////	@Override
////	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
////	    User user = userRepository.findFirstByEmailId(emailId);
////	    System.out.println("CustomUserDetailsService::loadUserByUsername::user " + user);
////	    if (user == null) {
////	        throw new UsernameNotFoundException("User not found with email: " + emailId);
////	    }
////
////	    // Check if roles are null or empty
////	    Set<Role> roles = user.getRoles();
////	    if (roles == null || roles.isEmpty()) {
////	        throw new UsernameNotFoundException("User has no roles assigned: " + emailId);
////	    }
////
////	    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
////	    for (Role role : roles) {
////	        authorities.add(new SimpleGrantedAuthority(role.getUserName()));
////	    }
////
////	    System.out.println("CustomUserDetailsService::loadUserByUsername::authorities " + authorities);
////
////	    return new MyUserDetails(user);
////	}
//
//}
