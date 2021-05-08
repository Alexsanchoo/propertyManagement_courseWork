package com.sanchoo.property.management.service.property;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PropertyService {
	Property save(Property property);

	Page<Property> findPaginatedActiveAds(Pageable pageable);

	Page<Property> findPaginatedInWaitingAds(Pageable pageable);

	Page<Property> findPaginatedNotActiveAds(Pageable pageable);

	Optional<Property> findById(int id);

	List<Property> findByStatus(PropertyStatus propertyStatus);
}
