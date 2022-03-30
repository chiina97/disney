package com.app.disney.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.app.disney.enums.RoleName;
import com.app.disney.models.Role;
import com.app.disney.serviceImpl.RoleService;

@Component
public class CreateData implements CommandLineRunner{
	@Autowired
	RoleService roleService;
	
	@Override
	public void run(String... args) throws Exception {
		// creo los roles:

	    if (roleService.findAll().isEmpty()) {
	      Role roleUser = new Role(RoleName.ROLE_USER);
	      roleService.save(roleUser);
	    }

}
}
