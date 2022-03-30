package com.app.disney.controllers;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.disney.dto.JwtDTO;
import com.app.disney.dto.LoginDTO;
import com.app.disney.dto.Message;
import com.app.disney.dto.UserDTO;
import com.app.disney.enums.RoleName;
import com.app.disney.jwt.JwtProvider;
import com.app.disney.models.Role;
import com.app.disney.models.User;
import com.app.disney.serviceImpl.RoleService;
import com.app.disney.serviceImpl.UserServiceImpl;

@RestController
// @CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RoleService roleService;

	@Autowired
	JwtProvider jwtProvider;

	// Create a new user
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO, BindingResult result) {

		// validaciones:
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message(result.getFieldError().getDefaultMessage()),
					HttpStatus.BAD_REQUEST);
		}
		if (userService.existsByUsername(userDTO.getUsername())) {
			return new ResponseEntity<Message>(new Message("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
		}
		if (userService.existsByMail(userDTO.getMail())) {
			return new ResponseEntity<Message>(new Message("Ya existe una cuenta asociada a ese mail"),
					HttpStatus.BAD_REQUEST);
		}

		// convert DTO to entity
		User userRequest = modelMapper.map(userDTO, User.class);

		// creo un user con la pass hasheada
		User user = new User(userRequest.getUsername(), passwordEncoder.encode(userRequest.getPassword()),
				userRequest.getMail(), userRequest.getName(), userRequest.getSurname());

		// agrego rol y guardo el user
		Set<Role> roles = new HashSet<>();
		roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
		user.setRoles(roles);

		userService.save(user);

		return new ResponseEntity<Message>(new Message("Usuario registrado!"), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginDto, BindingResult result) {

		// validaciones:
		if (result.hasErrors()) {
			return new ResponseEntity<Message>(new Message("usuario/contrasaña incorrecta"), HttpStatus.BAD_REQUEST);
		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		JwtDTO jwtDto = new JwtDTO(jwt);

		return new ResponseEntity<JwtDTO>(jwtDto, HttpStatus.OK);

	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refresh(@RequestBody JwtDTO jwtDto) throws ParseException {
		String token = jwtProvider.refreshToken(jwtDto);
		JwtDTO jwt = new JwtDTO(token);
		return new ResponseEntity<JwtDTO>(jwt, HttpStatus.OK);

	}

	// Read an user
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable(value = "id") Long userId) {
		Optional<User> user = userService.findById(userId);

		// convert entity to DTO
		UserDTO userResponse = modelMapper.map(user.get(), UserDTO.class);
		if (user.isPresent()) {
			return ResponseEntity.ok(userResponse);
		} else {
			return ResponseEntity.notFound().build();
		}

	}
}
