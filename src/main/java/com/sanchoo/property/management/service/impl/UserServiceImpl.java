package com.sanchoo.property.management.service.impl;


import com.sanchoo.property.management.entity.Role;
import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.repository.RoleRepository;
import com.sanchoo.property.management.repository.UserRepository;
import com.sanchoo.property.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User findUserByUserName(String userName) {
		return this.userRepository.findByUserName(userName);
	}

	@Override
	public User saveUser(User user) {
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(true);
		Role role = this.roleRepository.findByRole("ROLE_USER");
		user.setRoles(new HashSet<>(List.of(role)));
		return this.userRepository.save(user);
	}
}
