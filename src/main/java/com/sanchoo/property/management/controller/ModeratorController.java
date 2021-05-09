package com.sanchoo.property.management.controller;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.service.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

	@Autowired
	private PropertyService propertyService;

	@GetMapping("/check-ads")
	public String checkAds(Model model,
						   @RequestParam("page") Optional<Integer> page,
						   @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Property> propertyPage = this.propertyService.findPaginatedAdsToCheck(PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("propertyPage", propertyPage);

		int totalPages = propertyPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "moderator/check-ads";
	}

	@PostMapping("/ad/approve")
	public String approveAd(@RequestParam int id,
							@RequestParam String url) {

		this.propertyService.approveProperty(id);

		return "redirect:" + url;
	}

	@PostMapping("/ad/deny")
	public String denyAd(@RequestParam int id,
						 @RequestParam String url) {

		this.propertyService.denyProperty(id);

		return "redirect:" + url;
	}
}
