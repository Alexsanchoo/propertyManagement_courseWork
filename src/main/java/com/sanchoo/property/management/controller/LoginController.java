package com.sanchoo.property.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	@GetMapping("/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView("login");
		return modelAndView;
	}
}
