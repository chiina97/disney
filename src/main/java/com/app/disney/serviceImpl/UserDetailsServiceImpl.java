package com.app.disney.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.disney.dto.MainUser;
import com.app.disney.models.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserServiceImpl usuarioService;
    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usuario = usuarioService.findByUsername(username).get();
        return MainUser.build(usuario);
    }
}
