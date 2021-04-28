package com.sanchoo.property.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");
		return modelAndView;
	}

	@GetMapping("/about")
	public ModelAndView about() {
		ModelAndView modelAndView = new ModelAndView("about");
		return modelAndView;
	}

	@GetMapping("/services")
	public ModelAndView services() {
		ModelAndView modelAndView = new ModelAndView("services");
		return modelAndView;
	}
}
