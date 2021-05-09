package com.sanchoo.property.management.service.property;

import com.sanchoo.property.management.entity.property.ServiceType;

import java.util.List;
import java.util.Optional;

public interface ServiceTypeService {
	List<ServiceType> findAll();
	Optional<ServiceType> findById(int id);
}
