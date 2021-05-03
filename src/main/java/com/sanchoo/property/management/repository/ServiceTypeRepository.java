package com.sanchoo.property.management.repository;

import com.sanchoo.property.management.entity.property.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
}
