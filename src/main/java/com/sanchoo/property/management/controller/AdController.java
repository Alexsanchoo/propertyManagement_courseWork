package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.mapper.PropertyMapper;
import com.sanchoo.property.management.service.property.PropertyService;
import com.sanchoo.property.management.service.property.PropertyTypeService;
import com.sanchoo.property.management.service.property.ServiceTypeService;
import com.sanchoo.property.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes(AdController.ATTRIBUTE_NAME)
public class AdController {
	public static final String ATTRIBUTE_NAME = "propertyDto";
	public static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private UserService userService;

	@Autowired
	private PropertyMapper propertyMapper;

	@Autowired
	private ServiceTypeService serviceTypeService;

	@Autowired
	private PropertyTypeService propertyTypeService;

	@GetMapping("/ad/{id}")
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

	@PostMapping("/user/add/favorite")
	public String addFavoriteProperty(@RequestParam int id,
									  @RequestParam String url) {

		this.userService.addFavoriteProperty(id);
		return "redirect:" + url;
	}

	@PostMapping("/user/delete/favorite")
	public String deleteFavoriteProperty(@RequestParam int id,
										 @RequestParam String url) {

		this.userService.deleteFavoriteProperty(id);
		return "redirect:" + url;
	}

	@GetMapping("/user/ad/edit/{id}")
	public String editAd(@PathVariable int id,
						 Model model) {

		List<ServiceType> serviceTypes = this.serviceTypeService.findAll();
		List<PropertyType> propertyTypes = this.propertyTypeService.findAll();
		model.addAttribute("serviceTypes", serviceTypes);
		model.addAttribute("propertyTypes", propertyTypes);

		Optional<Property> propertyOptional = this.propertyService.findById(id);

		if(!model.containsAttribute(BINDING_RESULT_NAME)) {
			if(propertyOptional.isEmpty()) {
				return "error";
			}

			PropertyDto propertyDto = propertyMapper.propertyToPropertyDto(propertyOptional.get());
			model.addAttribute(ATTRIBUTE_NAME, propertyDto);
		}

		return "/user/ad/edit";
	}

	@PostMapping("/user/ad/edit")
	public String editAd(@RequestParam("files") List<MultipartFile> files,
						 @ModelAttribute @Valid PropertyDto propertyDto,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 SessionStatus sessionStatus) {
		return "";
	}
}
