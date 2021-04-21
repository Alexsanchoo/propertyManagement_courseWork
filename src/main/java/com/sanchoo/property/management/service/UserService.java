package com.sanchoo.property.management.service;


import com.sanchoo.property.management.dto.UserDto;
import com.sanchoo.property.management.entity.Role;
import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.exception.UserAlreadyExistException;
import com.sanchoo.property.management.repository.RoleRepository;
import com.sanchoo.property.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User findUserByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public User saveUser(UserDto userDto) throws UserAlreadyExistException {
		User userExists = findUserByUserName(userDto.getUserName());
		if(userExists != null) {
			throw new UserAlreadyExistException(String.format("There is an account with that user name: %s",
					userDto.getUserName()));
		}

		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		user.setEmail(userDto.getEmail());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setActive(true);
		Role role = roleRepository.findByRole("ROLE_USER");
		user.setRoles(new HashSet<>(List.of(role)));
		return userRepository.save(user);
	}
}
