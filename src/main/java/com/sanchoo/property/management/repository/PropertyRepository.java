package com.sanchoo.property.management.repository;

import com.sanchoo.property.management.entity.property.Property;
import com.sanchoo.property.management.entity.property.PropertyStatus;
import com.sanchoo.property.management.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
	List<Property> findByStatusAndUser(PropertyStatus propertyStatus, User user);
	List<Property> findByStatus(PropertyStatus propertyStatus);
}
