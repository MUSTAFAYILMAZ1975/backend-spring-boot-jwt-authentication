package com.javabackend.northwind.core.security.controllers;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.javabackend.northwind.core.security.models.ERole;
import com.javabackend.northwind.core.security.models.RefreshToken;
import com.javabackend.northwind.core.security.models.Role;
import com.javabackend.northwind.core.security.models.User;
import com.javabackend.northwind.core.security.payload.request.LoginRequest;
import com.javabackend.northwind.core.security.payload.request.SignupRequest;
import com.javabackend.northwind.core.security.payload.request.TokenRefreshRequest;
import com.javabackend.northwind.core.security.payload.response.JwtResponse;
import com.javabackend.northwind.core.security.payload.response.MessageResponse;
import com.javabackend.northwind.core.security.payload.response.TokenRefreshResponse;
import com.javabackend.northwind.core.security.repository.RoleRepository;
import com.javabackend.northwind.core.security.repository.UserRepository;
import com.javabackend.northwind.core.security.security.jwt.JwtUtils;
import com.javabackend.northwind.core.security.security.services.RefreshTokenService;
import com.javabackend.northwind.core.security.security.services.UserDetailsImpl;
import com.javabackend.northwind.core.utilities.exception.TokenRefreshException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	  @Autowired
	  RefreshTokenService refreshTokenService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
	    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		return ResponseEntity.ok(new JwtResponse(jwt, 
				                                 refreshToken.getToken(),
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	 @PostMapping("/refreshtoken")
	  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
	    String requestRefreshToken = request.getRefreshToken();

	    return refreshTokenService.findByToken(requestRefreshToken)
	        .map(refreshTokenService::verifyExpiration)
	        .map(RefreshToken::getUser)
	        .map(user -> {
	          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
	          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
	        })
	        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
	            "Refresh token is not in database!"));
	  }

	 @PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
	    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
	      return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Username is already taken!"));
	    }

	    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
	      return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Email is already in use!"));
	    }

	    // Create new user's account
	    User user = new User(null, signUpRequest.getUsername(), 
	               signUpRequest.getEmail(),
	               encoder.encode(signUpRequest.getPassword()), null);
		
		  Set<String> strRoles = signUpRequest.getRole(); 
		  Set<Role> roles = new HashSet<>();
		 

			/*
			 * if (strRoles == null) { Role userRole =
			 * roleRepository.findByName(ERole.ROLE_USER) .orElseThrow(() -> new
			 * RuntimeException("Error: Role is not found.")); roles.add(userRole); } else {
			 * strRoles.forEach(role -> { switch (role) { case "admin": Role adminRole =
			 * roleRepository.findByName(ERole.ROLE_ADMIN) .orElseThrow(() -> new
			 * RuntimeException("Error: Role is not found.")); roles.add(adminRole);
			 * 
			 * break; case "mod": Role modRole =
			 * roleRepository.findByName(ERole.ROLE_MODERATOR) .orElseThrow(() -> new
			 * RuntimeException("Error: Role is not found.")); roles.add(modRole);
			 * 
			 * break; default: Role userRole = roleRepository.findByName(ERole.ROLE_USER)
			 * .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			 * roles.add(userRole);
			 * 
			 * 
			 * } }); }
			 * 
			 */
	    user.setRoles(roles);
	    userRepository.save(user);

	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	  }
	}