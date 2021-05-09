package com.sanchoo.property.management.service.property.impl;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.user.Role;
import com.sanchoo.property.management.entity.user.User;
import com.sanchoo.property.management.repository.PropertyRepository;
import com.sanchoo.property.management.repository.RoleRepository;
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
import java.util.Optional;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

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

	@Override
	public Page<Property> findPaginatedAdsToCheck(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<Property> properties = this.propertyRepository.findByStatus(PropertyStatus.IN_WAITING);
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
	public Optional<Property> findById(int id) {
		return this.propertyRepository.findById(id);
	}

	@Override
	public List<Property> findByStatus(PropertyStatus propertyStatus) {
		return this.propertyRepository.findByStatus(propertyStatus);
	}

	@Override
	public Property update(Property property) {
		property.setStatus(PropertyStatus.IN_WAITING);
		return this.propertyRepository.save(property);
	}

	@Override
	public void deactivateProperty(int id) {
		Optional<Property> propertyOptional = this.propertyRepository.findById(id);
		propertyOptional.ifPresent(property -> property.setStatus(PropertyStatus.DENIED));
	}

	@Override
	public void activateProperty(int id) {
		Optional<Property> propertyOptional = this.propertyRepository.findById(id);
		propertyOptional.ifPresent(property -> property.setStatus(PropertyStatus.IN_WAITING));
	}

	@Override
	public void approveProperty(int id) {
		Optional<Property> propertyOptional = this.propertyRepository.findById(id);
		propertyOptional.ifPresent(property -> property.setStatus(PropertyStatus.APPROVED));
	}

	@Override
	public void denyProperty(int id) {
		Optional<Property> propertyOptional = this.propertyRepository.findById(id);
		propertyOptional.ifPresent(property -> property.setStatus(PropertyStatus.DENIED));
	}

	@Override
	public boolean hasAccessAuthorizedUser(Property property) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());

		Role moderatorRole = this.roleRepository.findByRole("ROLE_MODERATOR");

		switch (property.getStatus()) {

			case IN_WAITING: {
				if(property.getUser().equals(user) || user.getRoles().contains(moderatorRole)) {
					return true;
				}
			}
				break;

			case APPROVED:
				return true;

			case DENIED: {
				if(property.getUser().equals(user)) {
					return true;
				}
			}
				break;
		}

		return false;
	}
}
