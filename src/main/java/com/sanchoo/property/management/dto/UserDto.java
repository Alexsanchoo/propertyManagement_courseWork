package com.sanchoo.property.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private int id;

	@Email(message = "*Пожалуйста, укажите корректную почту")
	@NotBlank(message = "*Пожалуйста, укажите почту")
	private String email;

	@NotBlank(message = "*Пожалуйста, введите свой номер")
	@Pattern(regexp = "^\\+375\\((25|29|33|44)\\)\\d{3}\\-\\d{2}\\-\\d{2}$", message = "*Пожалуйста, используйте данный шаблон +375(XX)XXX-XX-XX")
	private String phone;

	@NotBlank(message = "*Пожалуйста, укажите своё имя")
	@Pattern(regexp = "^[A-Za-zА-Яа-я-]+$", message = "*Пожалуйста, введите корректно своё имя")
	private String firstName;

	@NotBlank(message = "*Пожалуйста, укажите свою фамилию")
	@Pattern(regexp = "^[A-Za-zА-Яа-я-]+$", message = "*Пожалуйста, введите корректно свою фамилию")
	private String lastName;
}
