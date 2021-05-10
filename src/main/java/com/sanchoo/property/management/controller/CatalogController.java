package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.service.property.PropertyService;
import com.sanchoo.property.management.service.property.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private PropertyTypeService propertyTypeService;

	@GetMapping("/sale")
	public String showSaleCatalog(Model model,
								  @RequestParam("page") Optional<Integer> page,
								  @RequestParam("size") Optional<Integer> size,
								  @ModelAttribute PropertyDto propertyDto) {

		List<PropertyType> propertyTypes = this.propertyTypeService.findAll();
		model.addAttribute("propertyTypes", propertyTypes);
		model.addAttribute("propertyDto", new PropertyDto());

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Property> propertyPage;
		if(propertyDto.getPropertyTypeId() != 0) {
			propertyPage = this.propertyService.findPaginatedSaleAds(
					PageRequest.of(currentPage - 1, pageSize),
					propertyDto);
			model.addAttribute("propertyDto", propertyDto);
		} else  {
			propertyPage = this.propertyService.findPaginatedSaleAds(PageRequest.of(currentPage - 1, pageSize));
		}

		model.addAttribute("propertyPage", propertyPage);

		int totalPages = propertyPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "catalog/sale";
	}

	@GetMapping("/rent")
	public String showRentCatalog(Model model,
								  @RequestParam("page") Optional<Integer> page,
								  @RequestParam("size") Optional<Integer> size,
								  @ModelAttribute PropertyDto propertyDto) {

		List<PropertyType> propertyTypes = this.propertyTypeService.findAll();
		model.addAttribute("propertyTypes", propertyTypes);
		model.addAttribute("propertyDto", new PropertyDto());

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Property> propertyPage;
		if(propertyDto.getPropertyTypeId() != 0) {
			propertyPage = this.propertyService.findPaginatedRentAds(
					PageRequest.of(currentPage - 1, pageSize),
					propertyDto);
			model.addAttribute("propertyDto", propertyDto);
		} else  {
			propertyPage = this.propertyService.findPaginatedRentAds(PageRequest.of(currentPage - 1, pageSize));
		}

		model.addAttribute("propertyPage", propertyPage);

		int totalPages = propertyPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "catalog/rent";
	}
}
