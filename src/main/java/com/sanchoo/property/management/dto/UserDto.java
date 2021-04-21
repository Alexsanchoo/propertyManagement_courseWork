package com.sanchoo.property.management.dto;

import com.sanchoo.property.management.validator.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class UserDto {
	@Length(min = 5, message = "*Your user name must have at least 5 characters")
	@NotEmpty(message = "*Pleas	e provide a user name")
	private String userName;

	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;
	private String matchingPassword;

	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	private String email;

	@NotEmpty(message = "*Please provide your first name")
	private String firstName;

	@NotEmpty(message = "*Please provide your last name")
	private String lastName;
}
