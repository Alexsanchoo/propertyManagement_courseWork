package com.sanchoo.property.management.repository;

import com.sanchoo.property.management.dto.property.PropertyDto;
import com.sanchoo.property.management.entity.property.Property;

import java.util.List;

public interface PropertySearchRepository {
	List<Property> findSalesAdsByPropertyDto(PropertyDto propertyDto, int servicetTypeId);
}
