package com.example.vote.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.google.gson.Gson;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize annotation
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Configure Spring Security to handle authentication and authorization based on
	// user roles.

//	@Autowired
//	private CustomUserDetailsService customUserDetailsService;
//
//	@Autowired
//	private UserDetailsService userDetailsService;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
	    return new JsonAuthenticationSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler failureHandler() {
	    return new JsonAuthenticationFailureHandler();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		// Allow sending cookies
//        config.setAllowCredentials(true);
		// Allow requests from any origin
		config.addAllowedOrigin("*");
		// Allow all headers
		config.addAllowedHeader("*");
		// Allow specified methods
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		System.out.println("WebSecurityConfig::authenticationProvider::authProvider " + authProvider);
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		System.out.println("WebSecurityConfig::authenticationProvider::auth " + auth);
	}
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//	    http.cors().and().authorizeRequests()
//	        .antMatchers("/votingApp/register").permitAll()
//	        .antMatchers("/votingApp/login").permitAll()
//	        .antMatchers("/votingApp/vote").hasRole("USER")
//	        .antMatchers("/votingApp/voteCount").hasRole("ADMIN")
//	        .antMatchers("/votingApp/disableVoting").hasRole("ADMIN")
//	        .antMatchers("/votingApp/checkVotingStatus").hasRole("ADMIN")
//	        .antMatchers("/votingApp/enableVoting").hasRole("ADMIN")
//	        .anyRequest().authenticated()
//	        .and()
//	        .formLogin()
//	        .loginProcessingUrl("/votingApp/login")
//	        .successHandler(successHandler())
//	        .failureHandler(failureHandler())
//	        .permitAll()
//	        .and()
//	        .logout()
//	            .permitAll();
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .cors().and()
	        .csrf().disable()
	        .authorizeRequests()
	            .antMatchers("/votingApp/register").permitAll()
	            .antMatchers("/votingApp/login").permitAll()
	            .antMatchers("/votingApp/vote").hasRole("USER")
	            .antMatchers("votingApp/checkVotingStatus").permitAll()
//	            .antMatchers("/votingApp/voteCount").hasRole("ADMIN")
	            .antMatchers("/votingApp/voteCount").permitAll()
//	            .antMatchers("/votingApp/voteCount").hasAnyRole("USER", "ADMIN")
	            .antMatchers("/votingApp/disableVoting", "/votingApp/checkVotingStatus", "/votingApp/enableVoting")
	            .permitAll()
	            .anyRequest().authenticated()
	        .and()
	        .formLogin()
	            .disable() // Disable default form login
	        .logout()
	            .permitAll()
	        .and()
	        .exceptionHandling()
	            .authenticationEntryPoint((request, response, authException) -> {
	                // Return JSON response for authentication failure
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write("{\"error\": \"Authentication failed\"}");
	            });
	}


//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.cors().and().csrf().disable().authorizeRequests() 
////				.antMatchers("/votingApp/**").permitAll()
//				.antMatchers("/votingApp/register/**").permitAll()
//				.antMatchers("/votingApp/login/**").permitAll()
////				.antMatchers("/votingApp/login/**").hasAnyAuthority("USER")
////				.antMatchers("/votingApp/login/**").hasAnyAuthority("ADMIN")
////				.antMatchers("/votingApp/vote").hasRole("USER")
////				.antMatchers("/votingApp/voteCount").hasRole("ADMIN")
////				.antMatchers("/votingApp/disableVoting").hasRole("ADMIN")
////				.antMatchers("/votingApp/checkVotingStatus").hasRole("ADMIN")
////				.antMatchers("/votingApp/enableVoting").hasRole("ADMIN")
//				.antMatchers("/votingApp/vote/**").permitAll()
//				.antMatchers("/votingApp/voteCount/**").permitAll()
//				.antMatchers("/votingApp/disableVoting/**").permitAll()
//				.antMatchers("/votingApp/checkVotingStatus/**").permitAll()
//				.antMatchers("/votingApp/enableVoting/**").permitAll()
//				.anyRequest().authenticated().and().formLogin().permitAll()
//				.and().logout().permitAll().and()
//	            .exceptionHandling().accessDeniedPage("/403");
//	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//	    http.cors() // Enable CORS
//        .and().authorizeRequests()
//	        .antMatchers("/votingApp/register").permitAll()
//	        .antMatchers("/votingApp/login").permitAll()
//	        .antMatchers("/votingApp/vote").hasRole("USER")
//	        .antMatchers("/votingApp/voteCount").hasRole("ADMIN")
//	        .antMatchers("/votingApp/disableVoting").hasRole("ADMIN")
//	        .antMatchers("/votingApp/checkVotingStatus").hasRole("ADMIN")
//	        .antMatchers("/votingApp/enableVoting").hasRole("ADMIN")
//	        .anyRequest().authenticated()
//	        .and()
//	        .formLogin()
//	        .permitAll()
//	        .and()
//	        .logout()
//	            .permitAll();
//	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().authorizeRequests()
//	        .antMatchers("/votingApp/register").permitAll()
//	        .antMatchers("/votingApp/login").permitAll()
//	        .antMatchers("/votingApp/vote").hasRole("USER")
//	        .antMatchers("/votingApp/voteCount").hasRole("ADMIN")
//	        .antMatchers("/votingApp/disableVoting").hasRole("ADMIN")
//	        .antMatchers("/votingApp/checkVotingStatus").hasRole("ADMIN")
//	        .antMatchers("/votingApp/enableVoting").hasRole("ADMIN")
//	        .anyRequest().authenticated()
//	        .and()
//	        .formLogin()
//	            .loginProcessingUrl("/votingApp/login") // Specify the URL for the login form submission
//	            .successHandler((request, response, authentication) -> {
//	                // Handle successful login here, if needed
//	            })
//	            .failureHandler((request, response, exception) -> {
//	                // Handle failed login here, if needed
//	            })
//	            .permitAll()
//	        .and()
//	        .logout()
//	            .permitAll();
//	}
	
	
	
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//                .antMatchers("/votingApp/register").permitAll() // Allow registration endpoint without authentication
//                .antMatchers("/votingApp/login").permitAll()
//                .antMatchers("/votingApp/vote").hasAnyRole("USER")
//                .antMatchers("/votingApp/checkVotingStatus").hasAnyRole("USER", "ADMIN") // Allow access to voting status for both user and admin
//                .antMatchers("/votingApp/enableVoting", "/votingApp/disableVoting").hasRole("ADMIN") // Allow only admins to enable/disable voting
//                .anyRequest().authenticated()
//            .and()
//            .formLogin()
//                .loginPage("/login") // Configure custom login page URL if needed
//                .permitAll()
//            .and()
//            .logout()
//                .permitAll();
//    }
	
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//	    http
//	    .csrf().disable().
//	    authorizeRequests()
//	        .antMatchers("/votingApp/register").permitAll()
//	        .antMatchers("/votingApp/login").permitAll()
//	        .antMatchers("/votingApp/vote").hasAnyAuthority("USER")
//	        .antMatchers("/votingApp/voteCount").hasAnyAuthority("ADMIN")
//	        .antMatchers("/votingApp/disableVoting").hasAnyAuthority("ADMIN")
//	        .antMatchers("/votingApp/checkVotingStatus").hasAnyAuthority("ADMIN")
//	        .antMatchers("/votingApp/enableVoting").hasAnyAuthority("ADMIN")
//	        .anyRequest().authenticated()
//	        .and()
//	        .formLogin()
//	            .loginPage("/votingApp/login") // Assuming you have a custom login page mapped to "/login"
//	            .permitAll()
//	        .and()
//	        .logout()
//	            .permitAll();
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Configure CORS globally
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3001", "http://localhost:8085"));// Specify allowed origins
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true); // Enable credentials
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
