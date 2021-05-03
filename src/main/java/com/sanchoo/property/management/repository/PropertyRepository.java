package com.sanchoo.property.management.repository;

import com.sanchoo.property.management.entity.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Integer> {
}
