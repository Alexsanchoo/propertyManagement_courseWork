package com.sanchoo.property.management.service.property;

import com.sanchoo.property.management.entity.property.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyService {
	Property save(Property property);

	Page<Property> findPaginatedActiveAds(Pageable pageable);

	Page<Property> findPaginatedInWaitingAds(Pageable pageable);

	Page<Property> findPaginatedNotActiveAds(Pageable pageable);
}
