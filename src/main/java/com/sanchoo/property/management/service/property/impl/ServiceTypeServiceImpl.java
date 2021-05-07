package com.sanchoo.property.management.service.property.impl;

import com.sanchoo.property.management.entity.property.ServiceType;
import com.sanchoo.property.management.repository.ServiceTypeRepository;
import com.sanchoo.property.management.service.property.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServiceTypeServiceImpl implements ServiceTypeService {

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Override
	public List<ServiceType> findAll() {
		return this.serviceTypeRepository.findAll();
	}
}
