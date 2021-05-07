package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Configuration
@RequestMapping("/ad")
public class AdController {

	@Autowired
	private PropertyService propertyService;

	@GetMapping("/{id}")
	public String showAd(@PathVariable int id,
						 Model model) {

		Optional<Property> propertyOptional = this.propertyService.findById(id);

		if(propertyOptional.isPresent()) {
			model.addAttribute("property", propertyOptional.get());
			return "ad/ad";
		} else {
			return "error";
		}
	}
}
