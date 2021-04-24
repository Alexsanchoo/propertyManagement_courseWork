package com.sanchoo.property.management.controller;


import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.service.UserService;
import com.sanchoo.property.management.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;

	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		return modelAndView;
	}

	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView("login");
		return modelAndView;
	}

	@GetMapping("/registration")
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView("registration");
		modelAndView.addObject("user", new User());
		return modelAndView;
	}

	@PostMapping("/registration")
	public ModelAndView createNewUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("registration");

		this.userValidator.validate(user, bindingResult);

		if (bindingResult.hasErrors()) {
			return modelAndView;
		}

		userService.saveUser(user);
		modelAndView.addObject("successMessage", "Пользователь зарегистрирован успешно!");
		modelAndView.addObject("user", new User());

		return modelAndView;
	}
}
