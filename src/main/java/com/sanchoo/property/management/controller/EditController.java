package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.PasswordDto;
import com.sanchoo.property.management.entity.User;
import com.sanchoo.property.management.exception.IncorrectPasswordException;
import com.sanchoo.property.management.exception.PasswordsNotMatchException;
import com.sanchoo.property.management.service.UserService;
import com.sanchoo.property.management.validator.group.BasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/edit")
@SessionAttributes(EditController.ATTRIBUTE_USER_NAME)
public class EditController {
	public static final String ATTRIBUTE_USER_NAME = "user";
	public static final String BINDING_RESULT_USER_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_USER_NAME;

	public static final String ATTRIBUTE_PASSWORD_NAME = "passwordDto";
	public static final String BINDING_RESULT_PASSWORD_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_PASSWORD_NAME;


	@Autowired
	private UserService userService;


	@GetMapping("/profile")
	public String showEditProfile(Model model) {

		if (!model.containsAttribute(BINDING_RESULT_USER_NAME)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = this.userService.findUserByUserName(auth.getName());
			model.addAttribute("user", user);
		}

		return "edit/profile";
	}

	@PostMapping("/profile")
	public String editProfile(@ModelAttribute @Validated(BasicInfo.class) User user,
							  BindingResult bindingResult,
							  RedirectAttributes redirectAttributes,
							  SessionStatus sessionStatus) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(BINDING_RESULT_USER_NAME, bindingResult);
			return "redirect:/edit/profile";
		}

		this.userService.updateUser(user);
		sessionStatus.setComplete();

		redirectAttributes.addFlashAttribute("successMessage", "Ваш профиль обновлён успешно!");
		return "redirect:/";
	}

	@GetMapping("/password")
	public String editPassword(Model model) {
		if (!model.containsAttribute(BINDING_RESULT_PASSWORD_NAME)) {
			model.addAttribute(ATTRIBUTE_PASSWORD_NAME, new PasswordDto());
		}

		return "edit/password";
	}

	@PostMapping("/password")
	public String editPassword(@ModelAttribute @Valid PasswordDto passwordDto,
							   BindingResult bindingResult,
							   RedirectAttributes redirectAttributes) {

		if(!bindingResult.hasErrors()) {
			try {
				this.userService.setNewPassword(passwordDto);
			} catch (PasswordsNotMatchException e) {
				bindingResult.rejectValue("matchingPassword", "error.password.matching", e.getMessage());
			} catch (IncorrectPasswordException e) {
				bindingResult.rejectValue("oldPassword", "error.password.old", e.getMessage());
			}
		}

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(BINDING_RESULT_PASSWORD_NAME, bindingResult);
			redirectAttributes.addFlashAttribute(ATTRIBUTE_PASSWORD_NAME, passwordDto);
			return "redirect:/edit/password";
		}

		redirectAttributes.addFlashAttribute("successMessage", "Ваш пароль изменён успешно!");
		return "redirect:/";
	}
}
