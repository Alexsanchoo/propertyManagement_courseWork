package com.sanchoo.property.management.service;

import com.sanchoo.property.management.entity.User;

public interface UserService {
	User findUserByUserName(String userName);

	User saveUser(User user);

	User updateUser(User user);

	User getById(int id);
}
