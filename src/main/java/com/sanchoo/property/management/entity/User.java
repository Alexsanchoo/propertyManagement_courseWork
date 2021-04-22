package com.sanchoo.property.management.entity;

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
@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@Column(name = "user_name")
	@Length(min = 5, max = 30, message = "*Ваше имя пользователя должно содержать от 5 до 30 символов")
	@NotBlank(message = "*Пожалуйста, укажите имя пользователя")
	@Pattern(regexp = "^[A-Za-z0-9\\_\\.]+$", message = "*Имя пользователя может содержать только \"A-Z\", \"a-z\", \"0-9\", \"_\" и \".\"")
	private String userName;

	@Column(name = "password")
	@Length(min = 5, message = "*Ваш пароль должен содержить минимум 5 символов")
	@NotBlank(message = "*Пожалуйста, укажите пароль")
	private String password;

	@Transient
	private String matchingPassword;

	@Column(name = "email")
	@Email(message = "*Пожалуйста, укажите корректную почту")
	@NotBlank(message = "*Пожалуйста, укажите почту")
	private String email;

	@Column(name = "first_name")
	@NotBlank(message = "*Пожалуйста, укажите своё имя")
	@Pattern(regexp = "^[A-Za-zА-Яа-я-]+$", message = "*Пожалуйста, введите корректно своё имя")
	private String firstName;

	@Column(name = "last_name")
	@NotBlank(message = "*Пожалуйста, укажите свою фамилию")
	@Pattern(regexp = "^[A-Za-zА-Яа-я-]+$", message = "*Пожалуйста, введите корректно свою фамилию")
	private String lastName;

	@Column(name = "active")
	private boolean active;

	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
}
