package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.photo.Photo;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.mapper.PropertyMapper;
import com.sanchoo.property.management.repository.PropertyTypeRepository;
import com.sanchoo.property.management.repository.ServiceTypeRepository;
import com.sanchoo.property.management.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	private PropertyService propertyService;

	@Autowired
	private PropertyMapper propertyMapper;

	@GetMapping("/active-ads")
	public String showActiveAds(Model model,
								@RequestParam("page") Optional<Integer> page,
								@RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Property> propertyPage = this.propertyService.findPaginatedActiveAds(PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("propertyPage", propertyPage);

		int totalPages = propertyPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "user/active-ads";
	}

	@GetMapping("/in-review-ads")
	public String showInReviewAds(Model model,
								  @RequestParam("page") Optional<Integer> page,
								  @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Property> propertyPage = this.propertyService.findPaginatedInWaitingAds(PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("propertyPage", propertyPage);

		int totalPages = propertyPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "user/in-review-ads";
	}

	@GetMapping("/not-active-ads")
	public String showNotActiveAds(Model model,
								   @RequestParam("page") Optional<Integer> page,
								   @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Property> propertyPage = this.propertyService.findPaginatedNotActiveAds(PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("propertyPage", propertyPage);

		int totalPages = propertyPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "user/not-active-ads";
	}

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

		Property property = this.propertyMapper.propertyDtoToProperty(propertyDto);
		this.propertyService.save(property);

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
