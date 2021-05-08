package com.sanchoo.property.management.repository;

import com.sanchoo.property.management.entity.property.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyType, Integer> {
}
