package com.sanchoo.property.management.service.property.impl;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.user.User;
import com.sanchoo.property.management.repository.PropertyRepository;
import com.sanchoo.property.management.service.property.PropertyService;
import com.sanchoo.property.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private UserService userService;

	@Override
	public Property save(Property property) {
		property.setStatus(PropertyStatus.IN_WAITING);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());
		property.setUser(user);

		return propertyRepository.save(property);
	}

	@Override
	public Page<Property> findPaginatedActiveAds(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());

		List<Property> properties = this.propertyRepository.findByStatusAndUser(PropertyStatus.APPROVED, user);
		List<Property> resultList;

		if(properties.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, properties.size());
			resultList = properties.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());
	}

	@Override
	public Page<Property> findPaginatedInWaitingAds(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());

		List<Property> properties = this.propertyRepository.findByStatusAndUser(PropertyStatus.IN_WAITING, user);
		List<Property> resultList;

		if(properties.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, properties.size());
			resultList = properties.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());
	}

	@Override
	public Page<Property> findPaginatedNotActiveAds(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());

		List<Property> properties = this.propertyRepository.findByStatusAndUser(PropertyStatus.DENIED, user);
		List<Property> resultList;

		if(properties.size() < startItem) {
			resultList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, properties.size());
			resultList = properties.subList(startItem, toIndex);
		}

		return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), properties.size());
	}
}
