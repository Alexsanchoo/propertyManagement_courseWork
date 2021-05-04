package com.sanchoo.property.management.validator.password;

import com.sanchoo.property.management.entity.user.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, User> {
	@Override
	public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
		return user.getPassword().equals(user.getMatchingPassword());
	}
}
