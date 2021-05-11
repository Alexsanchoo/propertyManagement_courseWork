package com.sanchoo.property.management.service.property.impl;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.notification.Notification;
import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.entity.user.Role;
import com.sanchoo.property.management.entity.user.User;
import com.sanchoo.property.management.repository.PropertyRepository;
import com.sanchoo.property.management.repository.RoleRepository;
import com.sanchoo.property.management.service.property.PropertyService;
import com.sanchoo.property.management.service.property.ServiceTypeService;
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
import java.time.LocalDateTime;
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

	@Autowired
	private ServiceTypeService serviceTypeService;

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
	public Page<Property> findPaginatedSaleAds(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		Optional<ServiceType> serviceTypeOptional = this.serviceTypeService.findById(1);

		List<Property> properties = this.propertyRepository.findByStatusAndServiceType(PropertyStatus.APPROVED, serviceTypeOptional.orElse(null));
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
	public Page<Property> findPaginatedSaleAds(Pageable pageable, PropertyDto propertyDto) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<Property> properties = this.propertyRepository.findAdsByPropertyDto(propertyDto, 1);
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
	public Page<Property> findPaginatedRentAds(Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		Optional<ServiceType> serviceTypeOptional = this.serviceTypeService.findById(2);

		List<Property> properties = this.propertyRepository.findByStatusAndServiceType(PropertyStatus.APPROVED, serviceTypeOptional.orElse(null));
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
	public Page<Property> findPaginatedRentAds(Pageable pageable, PropertyDto propertyDto) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;

		List<Property> properties = this.propertyRepository.findAdsByPropertyDto(propertyDto, 2);
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
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());

		Optional<Property> propertyOptional = this.propertyRepository.findById(id);
		propertyOptional.ifPresent(property -> {
			property.setStatus(PropertyStatus.APPROVED);
			Notification notification = new Notification();
			notification.setActive(true);
			notification.setUserFrom(user);
			StringBuilder sb = new StringBuilder("Ваше объявление: ")
					.append(property.getPropertyType().getName()).append(", ")
					.append("г. ").append(property.getCity()).append(", ")
					.append("р-н ").append(property.getDistrict()).append(", ")
					.append("ул. ").append(property.getStreet());
			if(!property.getHouseNumber().equals("0")) {
				sb.append(", ").append(property.getHouseNumber());
			}
			sb.append("\nбыло одобрено!");
			notification.setMessage(sb.toString());
			notification.setDateTime(LocalDateTime.now());

			User propertyUser = property.getUser();
			propertyUser.getNotifications().add(notification);
		});
	}

	@Override
	public void denyProperty(int id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.findUserByUserName(auth.getName());

		Optional<Property> propertyOptional = this.propertyRepository.findById(id);
		propertyOptional.ifPresent(property -> {
			property.setStatus(PropertyStatus.DENIED);
			Notification notification = new Notification();
			notification.setActive(true);
			notification.setUserFrom(user);
			StringBuilder sb = new StringBuilder("Ваше объявление: ")
					.append(property.getPropertyType().getName()).append(", ")
					.append("г. ").append(property.getCity()).append(", ")
					.append("р-н ").append(property.getDistrict()).append(", ")
					.append("ул. ").append(property.getStreet());
			if(!property.getHouseNumber().equals("0")) {
				sb.append(", ").append(property.getHouseNumber());
			}
			sb.append("\nбыло отклонено!");
			notification.setMessage(sb.toString());
			notification.setDateTime(LocalDateTime.now());

			User propertyUser = property.getUser();
			propertyUser.getNotifications().add(notification);
		});
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
