package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.entity.User;
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

@Controller
@RequestMapping("/edit")
@SessionAttributes(EditController.ATTRIBUTE_NAME)
public class EditController {
	public static final String ATTRIBUTE_NAME = "user";
	public static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

	@Autowired
	private UserService userService;


	@GetMapping("/profile")
	public String showEditProfile(Model model) {

		if(!model.containsAttribute(BINDING_RESULT_NAME)) {
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

		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, bindingResult);
			return "redirect:/edit/profile";
		}

		this.userService.updateUser(user);
		sessionStatus.setComplete();

		redirectAttributes.addFlashAttribute("successMessage", "Ваш профиль обновлён успешно!");
		return "redirect:/";
	}
}
