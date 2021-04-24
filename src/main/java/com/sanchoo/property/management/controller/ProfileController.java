package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.UserDto;
import com.sanchoo.property.management.entity.User;
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
public class ProfileController {

	@Autowired
	private UserService userService;


	@GetMapping("/edit/profile")
	public ModelAndView editProfile() {
		ModelAndView modelAndView = new ModelAndView("edit/profile");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());

		UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getPhone(), user.getFirstName(), user.getLastName());

		modelAndView.addObject("user", userDto);

		return modelAndView;
	}

	@PostMapping("/edit/profile")
	public ModelAndView editProfile(@ModelAttribute("user") @Valid UserDto userDto,
									@ModelAttribute("password") String password,
									@ModelAttribute("userName") String userName,
									BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("edit/profile");
		modelAndView.addObject("user", userDto);

		if(bindingResult.hasErrors()) {
			return modelAndView;
		}

		User user = this.userService.getById(userDto.getId());
		user.setEmail(userDto.getEmail());
		user.setPhone(userDto.getPhone());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());

		this.userService.updateUser(user);
		modelAndView.addObject("successMessage", "Пользователь обновлён успешно!");

		return modelAndView;
	}
}
