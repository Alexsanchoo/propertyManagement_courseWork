package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyPhoto;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.repository.PropertyRepository;
import com.sanchoo.property.management.repository.PropertyTypeRepository;
import com.sanchoo.property.management.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/user")
public class UserController {
	private static String ATTRIBUTE_NAME = "property";
	private static String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	private PropertyTypeRepository propertyTypeRepository;

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Autowired
	private PropertyRepository propertyRepository;

	@GetMapping("/ad/add")
	public String addAd(Model model) {
		List<ServiceType> serviceTypes = this.serviceTypeRepository.findAll();
		List<PropertyType> propertyTypes = this.propertyTypeRepository.findAll();

		model.addAttribute("serviceTypes", serviceTypes);
		model.addAttribute("propertyTypes", propertyTypes);

		if(!model.containsAttribute(BINDING_RESULT_NAME)) {
			Property property = new Property();
			model.addAttribute("serviceTypeId", 1);
			model.addAttribute("propertyTypeId", 1);
			model.addAttribute("priceDouble", 0.0);
			model.addAttribute(ATTRIBUTE_NAME, property);
		}
		return "user/ad/add";
	}

	@PostMapping("/ad/add")
	public String addAd(@RequestParam("files") List<MultipartFile> files,
						@RequestParam("serviceTypeId") int serviceTypeId,
						@RequestParam("propertyTypeId") int propertyTypeId,
						@RequestParam("priceDouble") double priceDouble,
						@ModelAttribute @Valid Property property,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes) throws IOException {

		if(files.size() > 10) {
			bindingResult.rejectValue("propertyPhotos", "error.ad.propertyPhotos", "*Максимум можно загрузить 10 фотографий");
		}

		if(property.getFloor() > property.getFloorsNumber()) {
			bindingResult.rejectValue("floor", "error.ad.floor", "*Номер этажа не может быть меньше этажности");
		}

		int price = (int) (priceDouble * 100.0);
		if(price < 0) {
			bindingResult.rejectValue("price", "error.ad.price", "*Цена не может быть отрицательной");
		}

		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, property);
			redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, bindingResult);
			redirectAttributes.addFlashAttribute("serviceTypeId", serviceTypeId);
			redirectAttributes.addFlashAttribute("propertyTypeId", propertyTypeId);
			redirectAttributes.addFlashAttribute("priceDouble", priceDouble);
			return "redirect:/user/ad/add";
		}

		List<PropertyPhoto> propertyPhotoList = savePhotos(files);
		Optional<ServiceType> serviceTypeOptional = this.serviceTypeRepository.findById(serviceTypeId);
		Optional<PropertyType> propertyTypeOptional = this.propertyTypeRepository.findById(propertyTypeId);

		property.setPropertyPhotos(propertyPhotoList);
		serviceTypeOptional.ifPresent(property::setServiceType);
		propertyTypeOptional.ifPresent(property::setPropertyType);
		property.setStatus(PropertyStatus.IN_WAITING);
		property.setPrice(price);

		this.propertyRepository.save(property);

		redirectAttributes.addFlashAttribute("successMessage", "Ваше объявление подано на проверку!");

		return "redirect:/";
	}

	private List<PropertyPhoto> savePhotos(List<MultipartFile> photos) throws IOException {
		List<PropertyPhoto> propertyPhotoList = new ArrayList<>();

		for (MultipartFile photo : photos) {
			if (photo.getContentType().equals("image/png") || photo.getContentType().equals("image/jpeg")) {
				File uploadDir = new File(uploadPath + "/property");

				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}

				String uuidFile = UUID.randomUUID().toString();
				String resultFileName = uuidFile + "." + photo.getOriginalFilename();
				photo.transferTo(new File(uploadPath + "/property/" + resultFileName));

				PropertyPhoto propertyPhoto = new PropertyPhoto();
				propertyPhoto.setPhotoName(resultFileName);
				propertyPhotoList.add(propertyPhoto);
			}
		}
		return propertyPhotoList;
	}
}
