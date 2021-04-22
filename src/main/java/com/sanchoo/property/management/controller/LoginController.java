package com.sanchoo.property.management.controller;


import com.sanchoo.property.management.dto.UserDto;
import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.exception.UserAlreadyExistException;
import com.sanchoo.property.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping(value = {"/", "/home"})
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		return modelAndView;
	}

	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView("login");
		return modelAndView;
	}

	@GetMapping(value = "/registration")
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView("registration");
		UserDto userDto = new UserDto();
		modelAndView.addObject("user", userDto);
		return modelAndView;
	}

	@PostMapping(value = "/registration")
	public ModelAndView createNewUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("registration");

		if (bindingResult.hasErrors()) {
			return modelAndView;
		}

		try {
			userService.saveUser(userDto);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new UserDto());
		} catch (UserAlreadyExistException e) {
			bindingResult.rejectValue("userName", "error.user", "There is already a user registered with the user name provided");
		}

		return modelAndView;
	}

	@GetMapping(value = "/user/home")
	public ModelAndView signInHome() {
		ModelAndView modelAndView = new ModelAndView("user/home");
		return modelAndView;
	}
}
