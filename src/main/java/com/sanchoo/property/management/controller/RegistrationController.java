package com.sanchoo.property.management.controller;


import com.sanchoo.property.management.entity.user.User;
import com.sanchoo.property.management.exception.UserAlreadyExistsException;
import com.sanchoo.property.management.service.user.UserService;
import com.sanchoo.property.management.validator.group.BasicInfo;
import com.sanchoo.property.management.validator.group.RegistrationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
	public static final String ATTRIBUTE_NAME = "user";
	public static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

	@Autowired
	private UserService userService;

	@GetMapping("/registration")
	public String registration(Model model) {
		if(!model.containsAttribute(BINDING_RESULT_NAME)) {
			model.addAttribute(ATTRIBUTE_NAME, new User());
		}
		return "registration";
	}

	@PostMapping("/registration")
	public String createNewUser(@ModelAttribute @Validated({RegistrationInfo.class, BasicInfo.class}) User user,
								BindingResult bindingResult,
								RedirectAttributes redirectAttributes) {

		if(!bindingResult.hasErrors()) {
			try {
				this.userService.saveUser(user);
			} catch (UserAlreadyExistsException e) {
				bindingResult.rejectValue("userName", "error.user.Username", e.getMessage());
			}
		}


		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, user);
			redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, bindingResult);
			return "redirect:/registration";
		}

		redirectAttributes.addFlashAttribute("successMessage", "Пользователь зарегистрирован успешно!");
		return "redirect:/";
	}
}
