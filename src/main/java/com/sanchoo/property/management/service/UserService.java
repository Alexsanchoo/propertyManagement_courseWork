package com.sanchoo.property.management.service;

import com.sanchoo.property.management.dto.PasswordDto;
import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.exception.IncorrectPasswordException;
import com.sanchoo.property.management.exception.PasswordsNotMatchException;
import com.sanchoo.property.management.exception.UserAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
	User findUserByUserName(String userName);

	User saveUser(User user) throws UserAlreadyExistsException;

	User updateUser(User user);

	User getById(int id);

	void setNewPassword(PasswordDto passwordDto) throws PasswordsNotMatchException, IncorrectPasswordException;

	Page<User> findPaginatedAllUsers(Pageable pageable);

	Page<User> findPaginatedModerators(Pageable pageable);

	Page<User> findPaginatedBlockedUsers(Pageable pageable);
}
