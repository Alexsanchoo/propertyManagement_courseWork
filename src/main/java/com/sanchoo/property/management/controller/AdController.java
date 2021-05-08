package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.photo.Photo;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.mapper.PropertyMapper;
import com.sanchoo.property.management.service.property.PropertyService;
import com.sanchoo.property.management.service.property.PropertyTypeService;
import com.sanchoo.property.management.service.property.ServiceTypeService;
import com.sanchoo.property.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@SessionAttributes(AdController.ATTRIBUTE_NAME)
public class AdController {
	public static final String ATTRIBUTE_NAME = "propertyDto";
	public static final String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

	@Value("${upload.path}")
	private String uploadPath;

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

		return "user/ad/edit";
	}

	@PostMapping("/user/ad/edit")
	public String editAd(@RequestParam("files") List<MultipartFile> files,
						 @ModelAttribute @Valid PropertyDto propertyDto,
						 BindingResult bindingResult,
						 RedirectAttributes redirectAttributes,
						 SessionStatus sessionStatus) throws IOException {

		long size = files.stream().filter(file -> !file.getOriginalFilename().isEmpty()).count();
		if(size + propertyDto.getPhotos().size() > 10) {
			bindingResult.rejectValue("photos", "error.property.photos", "*Всего фотографий должно быть от 1 до 10 включительно");
		}

		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, bindingResult);
			return "redirect:/user/ad/edit/" + propertyDto.getId();
		}

		List<Photo> photos = savePhotos(files);
		propertyDto.getPhotos().addAll(photos);
		Property property = this.propertyMapper.propertyDtoToProperty(propertyDto);
		this.propertyService.update(property);
		sessionStatus.setComplete();

		redirectAttributes.addFlashAttribute("successMessage", "Информация о недвижимости успешно обновлена!");

		return "redirect:/ad/" + property.getId();
	}

	private List<Photo> savePhotos(List<MultipartFile> photos) throws IOException {
		List<Photo> photoList = new ArrayList<>();

		for (MultipartFile photo : photos) {
			if (photo.getContentType().equals("image/png") || photo.getContentType().equals("image/jpeg")) {
				File uploadDir = new File(uploadPath + "/property");

				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}

				String uuidFile = UUID.randomUUID().toString();
				String resultFileName = uuidFile + "." + photo.getOriginalFilename();
				photo.transferTo(new File(uploadPath + "/property/" + resultFileName));

				Photo propertyPhoto = new Photo();
				propertyPhoto.setName(resultFileName);
				photoList.add(propertyPhoto);
			}
		}
		return photoList;
	}
}
