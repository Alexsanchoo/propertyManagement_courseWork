package com.sanchoo.property.management.service.property.impl;

import com.sanchoo.property.management.entity.property.PropertyType;
import com.sanchoo.property.management.repository.PropertyTypeRepository;
import com.sanchoo.property.management.service.property.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PropertyTypeServiceImpl implements PropertyTypeService {

	@Autowired
	private PropertyTypeRepository propertyTypeRepository;

	@Override
	public List<PropertyType> findAll() {
		return this.propertyTypeRepository.findAll();
	}
}
