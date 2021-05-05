package com.sanchoo.property.management.service.property.impl;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.user.User;
import com.sanchoo.property.management.repository.PropertyRepository;
import com.sanchoo.property.management.service.property.PropertyService;
import com.sanchoo.property.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
