package com.app.disney.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.disney.Iservice.IUserService;
import com.app.disney.models.User;
import com.app.disney.repository.UserRepository;





@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepo; // inyectamos

	@Override
	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return userRepo.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(Long id) {
		return userRepo.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	@Transactional
	public User save(User user) {
		return userRepo.save(user);
	}

	@Override
	@Transactional
	public User update(User userRequest, Long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent()) {
			user.get().setName(userRequest.getName());
			user.get().setSurname(userRequest.getSurname());
			user.get().setUsername(userRequest.getUsername());
			user.get().setMail(userRequest.getMail());
			user.get().setEnable(userRequest.isEnable());
			return userRepo.save(user.get());
		} else
			return null;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		userRepo.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public List<User> findByUserAndPassword(String usuario, String clave) {
		return userRepo.findByUserAndPassword(usuario, clave);
	}
	

	@Transactional(readOnly = true)
	public boolean existsByMail(String mail) {
		return userRepo.existsByMail(mail);
	}
	
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}

}
