package com.sanchoo.property.management.validator;


import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

	@Autowired
	private UserServiceImpl userService;


	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;

		if (!user.getPassword().equals(user.getMatchingPassword())) {
			errors.rejectValue("matchingPassword", "error.user.matchingPassword", "Пароли не совпадают");
		}

		if (this.userService.findUserByUserName(user.getUserName()) != null) {
			errors.rejectValue("userName", "error.user.userExists", "Такой пользователь уже существует");
		}
	}
}
