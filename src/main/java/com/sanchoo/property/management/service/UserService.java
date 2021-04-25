package com.sanchoo.property.management.service;

import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.exception.UserAlreadyExistsException;

public interface UserService {
	User findUserByUserName(String userName);

	User saveUser(User user) throws UserAlreadyExistsException;

	User updateUser(User user);

	User getById(int id);
}
