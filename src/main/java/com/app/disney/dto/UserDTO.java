package com.app.disney.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	@NotNull(message = "Debe ingresar un usuario")
	@NotBlank(message = "El usuario no puede tener espacios en blanco")
	private String username;
	@NotNull(message = "Debe ingresar un correo")
	@NotBlank(message = "El correo no puede tener espacios en blanco")
	@Email(message = "el correo debe tener el formato texto@texto.dominio")
	private String mail;
	@NotNull(message = "Debe ingresar una clave")
	@Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
	private String password;
	@NotNull(message = "Debe ingresar un nombre")
	@NotBlank(message = "El nombre no puede tener espacios en blanco")
	@Pattern(regexp="^[A-Za-z\s]+$", message = "el nombre solo puede contener letras")
	private String name;
	@NotNull(message = "Debe ingresar un apellido")
	@NotBlank(message = "El apellido no puede tener espacios en blanco")
	@Pattern(regexp="^[A-Za-z\s]+$", message = "el apellido solo puede contener letras")
	private String surname;
	private boolean enable;
	
	private Set<String> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
