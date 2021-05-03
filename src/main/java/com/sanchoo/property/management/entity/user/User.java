package com.sanchoo.property.management.entity.user;

import com.sanchoo.property.management.validator.PasswordMatches;
import com.sanchoo.property.management.validator.group.BasicInfo;
import com.sanchoo.property.management.validator.group.RegistrationInfo;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@PasswordMatches(groups = RegistrationInfo.class)
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@Column(name = "user_name")
	@Length(min = 5, max = 30, message = "*Ваше имя пользователя должно содержать от 5 до 30 символов", groups = RegistrationInfo.class)
	@NotBlank(message = "*Пожалуйста, укажите имя пользователя", groups = RegistrationInfo.class)
	@Pattern(regexp = "^[A-Za-z0-9\\_\\.]+$", message = "*Имя пользователя может содержать только \"A-Z\", \"a-z\", \"0-9\", \"_\" и \".\"", groups = RegistrationInfo.class)
	private String userName;

	@Column(name = "password")
	@Length(min = 5, message = "*Ваш пароль должен содержить минимум 5 символов", groups = RegistrationInfo.class)
	@NotBlank(message = "*Пожалуйста, укажите пароль", groups = RegistrationInfo.class)
	private String password;

	@Transient
	private String matchingPassword;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "email")
	@Email(message = "*Пожалуйста, укажите корректную почту", groups = BasicInfo.class)
	@NotBlank(message = "*Пожалуйста, укажите почту", groups = BasicInfo.class)
	private String email;

	@Column(name = "phone")
	@NotBlank(message = "*Пожалуйста, введите свой номер", groups = BasicInfo.class)
	@Pattern(regexp = "^\\+375\\((25|29|33|44)\\)\\d{3}\\-\\d{2}\\-\\d{2}$", message = "*Пожалуйста, используйте данный шаблон +375(XX)XXX-XX-XX", groups = BasicInfo.class)
	private String phone;

	@Column(name = "first_name")
	@NotBlank(message = "*Пожалуйста, укажите своё имя", groups = BasicInfo.class)
	@Pattern(regexp = "^[ЁёA-Za-zА-Яа-я-]+$", message = "*Пожалуйста, введите корректно своё имя", groups = BasicInfo.class)
	private String firstName;

	@Column(name = "last_name")
	@NotBlank(message = "*Пожалуйста, укажите свою фамилию", groups = BasicInfo.class)
	@Pattern(regexp = "^[ЁёA-Za-zА-Яа-я-]+$", message = "*Пожалуйста, введите корректно свою фамилию", groups = BasicInfo.class)
	private String lastName;

	@Column(name = "active")
	private boolean active;

	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
}
