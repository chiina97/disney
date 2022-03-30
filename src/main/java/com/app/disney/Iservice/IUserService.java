package com.app.disney.Iservice;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.disney.models.User;




public interface IUserService {
	
	public Iterable<User> findAll();
	
	public Page<User> findAll(Pageable pageable);
	
	public Optional<User> findById(Long id);
	
	public User save(User user);
	
	public User update(User user,Long id);
	
	public void deleteById(Long id);




}
