package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.photo.Photo;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.mapper.PropertyMapper;
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
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
	private static String ATTRIBUTE_NAME = "propertyDto";
	private static String BINDING_RESULT_NAME = "org.springframework.validation.BindingResult." + ATTRIBUTE_NAME;

	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	private PropertyTypeRepository propertyTypeRepository;

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private PropertyMapper propertyMapper;

	@GetMapping("/ad/add")
	public String addAd(Model model) {
		List<ServiceType> serviceTypes = this.serviceTypeRepository.findAll();
		List<PropertyType> propertyTypes = this.propertyTypeRepository.findAll();

		model.addAttribute("serviceTypes", serviceTypes);
		model.addAttribute("propertyTypes", propertyTypes);

		if(!model.containsAttribute(BINDING_RESULT_NAME)) {
			PropertyDto propertyDto = new PropertyDto();
			model.addAttribute(ATTRIBUTE_NAME, propertyDto);
		}
		return "user/ad/add";
	}

	@PostMapping("/ad/add")
	public String addAd(@RequestParam("files") List<MultipartFile> files,
						@ModelAttribute @Valid PropertyDto propertyDto,
						BindingResult bindingResult,
						RedirectAttributes redirectAttributes) throws IOException {

		if((files.size() <= 1 && files.get(0).getOriginalFilename().isEmpty()) || files.size() > 10) {
			bindingResult.rejectValue("photos", "error.ad.propertyPhotos", "*Фотографий недвижимости должно быть от 1 до 10 включительно");
		}

		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME, propertyDto);
			redirectAttributes.addFlashAttribute(BINDING_RESULT_NAME, bindingResult);
			return "redirect:/user/ad/add";
		}

		List<Photo> photoList = savePhotos(files);
		propertyDto.setPhotos(photoList);
		propertyDto.setStatus(PropertyStatus.IN_WAITING);

		Property property = this.propertyMapper.propertyDtoToProperty(propertyDto);

		System.out.println(property);
		this.propertyRepository.save(property);

		redirectAttributes.addFlashAttribute("successMessage", "Ваше объявление подано на проверку!");

		return "redirect:/";
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
