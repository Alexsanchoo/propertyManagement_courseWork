package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.service.property.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

	@Autowired
	private PropertyTypeService propertyTypeService;

	@GetMapping("/")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("home");

		List<PropertyType> propertyTypes = this.propertyTypeService.findAll();
		modelAndView.addObject("propertyTypes", propertyTypes);
		modelAndView.addObject("propertyDto", new PropertyDto());

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

	@GetMapping("/expired")
	public ModelAndView expiredError() {
		ModelAndView modelAndView = new ModelAndView("expired-error");
		return modelAndView;
	}
}
