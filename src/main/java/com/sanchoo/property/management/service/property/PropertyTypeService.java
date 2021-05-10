package com.sanchoo.property.management.service.property;

import com.sanchoo.property.management.entity.property.PropertyType;

import java.util.List;
import java.util.Optional;

public interface PropertyTypeService {
	List<PropertyType> findAll();

	Optional<PropertyType> findById(int id);
}
